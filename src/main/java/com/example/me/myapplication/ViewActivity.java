package com.example.me.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class ViewActivity extends Activity {

    private ImageView m_up, m_down, m_left, m_right, m_repeat, m_dingco;
    private Button m_back, m_clear, m_complete;
    private EditText m_repeatNum;
    private GridView m_gridView;
    private GridViewAdapter m_gridViewAdapter;
    private TableLayout m_tableLayout;
    private RelativeLayout m_relativeLayout;
    private RelativeLayout.LayoutParams m_rlp;

    private ArrayList<GridViewItem> m_listForCodingStation = new ArrayList<>();// coding station
    private Stack<Arrow> m_stackForAnswers = new Stack<>();// back if no data in coding station
    private ArrayDeque<Arrow> m_queueForCharacterMoving = new ArrayDeque<>();// move character inside
    private Queue<Arrow> m_queueForMovingTask = new ArrayDeque<>();// move character for outside(UI)
    private String m_voca;
    private char m_alphabatsMap[][];
    private int m_mapInfo = 0, m_characterR = 1, m_characterC = 1, m_correctAlphaCnt = 0;
    private int m_moveToMarginLeft = 0, m_moveToMarginTop = 0, m_firstMarginLeft = 0, m_firstMarginTop = 0;
    private boolean m_repeatFlag = true;
    private boolean m_isFinished = false;

    private static int m_winCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // 위젯 모음
        m_up = findViewById(R.id.up);
        m_down = findViewById(R.id.down);
        m_left = findViewById(R.id.left);
        m_right = findViewById(R.id.right);
        m_repeat = findViewById(R.id.repeat);
        m_back = findViewById(R.id.back);
        m_clear = findViewById(R.id.clear);
        m_complete = findViewById(R.id.complete);
        m_gridView = findViewById(R.id.codingStation);
        m_gridViewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.gridview_item, m_listForCodingStation);
        m_dingco = findViewById(R.id.dingco);

        m_tableLayout = findViewById(R.id.tableLayout);
        m_relativeLayout = findViewById(R.id.dingcocontainer);
        m_rlp = new RelativeLayout.LayoutParams(150, 150);

        // 위젯 이벤트 등록
        m_up.setOnTouchListener(touchListener);
        m_down.setOnTouchListener(touchListener);
        m_left.setOnTouchListener(touchListener);
        m_right.setOnTouchListener(touchListener);
        m_back.setOnClickListener(clickListener);
        m_clear.setOnClickListener(clickListener);
        m_complete.setOnClickListener(clickListener);
        m_gridView.setOnDragListener(dragListener);

        // 맵 정보 받기
        Bundle extras = getIntent().getExtras();
        m_mapInfo = extras.getInt("mapInfo");

        // 맵 정보에 따라 영어 랜덤 출력
        Random randomGenerator = new Random();
        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
        Log.d("inchang_Voca", m_voca);

        // 알파벳 맵 만들기
        m_alphabatsMap = getAlphaMap(getAlphaQueue());
        displayMap();

        // 캐릭터 위치 지정
        setDingcoStartPos();

        // repeat 버튼 애니메이션 추가
        m_repeat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, myShadowBuilder, v, 0);
                return true;
            }
        });

        final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.transition);
        m_repeatNum = (EditText) transitionsContainer.findViewById(R.id.repeatnum);
        m_repeat = (ImageView) transitionsContainer.findViewById(R.id.repeat);
        m_repeat.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer);
                visible = !visible;
                m_repeatNum.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private ArrayDeque<Character> getAlphaQueue() {
        int vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        Random randomGenerator = new Random();
        ArrayDeque<Character> queue = new ArrayDeque<>();

        for (int i = 0; i < vocaMaxLength; i++) {
            // (1) 큐에 영단어 알파벳 저장
            if (i < m_voca.length())
                queue.add(m_voca.charAt(i));
            else {
                char rc = alphas[randomGenerator.nextInt(alphas.length)];

                // (2) 영단어가 가지고 있는 알파벳이 포함되지 않을 때 까지 반복
                /*while (queue.contains(rc)) {
                    rc = alphas[randomGenerator.nextInt(alphas.length)];
                }
                */
                // (3) 포함된 알파벳이 아니라면 큐에 저장
                queue.add(rc);
            }
        }

        return queue;
    }

    private char[][] getAlphaMap(ArrayDeque<Character> queue) {
        int currR = 1, currC = 1, vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        char alphabatsMap[][] = new char[m_mapInfo + 2][m_mapInfo + 2];
        boolean isVisited[][] = new boolean[m_mapInfo + 2][m_mapInfo + 2];
        for (int i = 1; i < m_mapInfo + 1; i++) {
            for (int j = 1; j < m_mapInfo + 1; j++) {
                isVisited[i][j] = true;
            }
        }
        alphabatsMap[currR][currC] = ' ';
        isVisited[currR][currC] = false;
        boolean flag = false;
        Stack<Point> stack = new Stack<>();

        // (1) 큐에 있는 알파벳이 모두 사라질 때까지 반복
        while (queue.size() > 0) {
            Random randomGenerator = new Random();
            Arrow arrow = Arrow.valueOf(randomGenerator.nextInt(4));

            // (2) 이전 위치 저장
            int prevR = currR;
            int prevC = currC;
            // (3) 다음 갈 방향 랜덤 지정
            int nextR = currR + arrow.getRow();
            int nextC = currC + arrow.getCol();

            // (4-1) 방문하지 않았던(true) 곳이라면 방문(false)하고 스택에 저장
            if (isVisited[nextR][nextC]) {
                alphabatsMap[nextR][nextC] = queue.poll();
                isVisited[nextR][nextC] = false;
                stack.add(new Point(alphabatsMap[nextR][nextC], nextR, nextC, prevR, prevC));

                // (4-2) 다음 위치를 현재 위치로 바꿈
                currR = nextR;
                currC = nextC;

                // (5-1) 다음 위치가 방문했던 곳이라면(false) 현재 위치(currR, currC)에서 4방면을 확인
            } else {
                flag = false;
                for (int i = 0; i < 4; i++) {

                    // 위, 아래, 왼쪽, 오른쪽
                    arrow = Arrow.valueOf(i);
                    nextR = currR + arrow.getRow();
                    nextC = currC + arrow.getCol();
                    if (isVisited[nextR][nextC]) {

                        // (5-2) 현재 위치에서 주변에 하나라도 갈 곳이 있으면 flag를 true로 설정
                        flag = true;
                    }
                }

                // (6) 한 곳으로라도 갈 곳이 없다면 이전 위치를 스택에서 팝
                if (!flag) {

                    // (7-1) 주어진 단어의 알파벳이 모두 채워지지 않았는데 길이 막혔을 경우
                    if (queue.size() > (vocaMaxLength - m_voca.length())) {

                        // (7-2) 알파벳을 다시 큐의 처음 위치에 넣기
                        Point p = stack.pop();
                        queue.addFirst(p.getAlpha());

                        // (7-3) 현재 위치를 다시 true로 바꿈
                        isVisited[p.getCurrRow()][p.getCurrCol()] = true;

                        // (7-4) 캐릭터의 위치를 이전 위치로 이동
                        currR = p.getPrevRow();
                        currC = p.getPrevCol();
                    }

                    // (8) 주어진 단어의 알파벳이 모두 채워졌는데 길이 막혔을 경우
                    else {
                        Point p = stack.pop();
                        currR = p.getCurrRow();
                        currC = p.getCurrCol();
                    }

                    // 또는 맵 재배치 코드
                    /*
                    if (queue.size() > (vocaMaxLength - m_voca.length())) {
                        stack.clear();
                        queue = getAlphaQueue();
                        for (int i = 1; i < m_mapInfo + 1; i++) {
                            for (int j = 1; j < m_mapInfo + 1; j++) {
                                isVisited[i][j] = true;
                            }
                        }
                        currR = 1;
                        currC = 1;
                        alphabatsMap[currR][currC] = ' ';
                        isVisited[currR][currC] = false;
                        // flag = false;
                    }
                    */
                }
            }
        }
//        for (int i = 1; i < m_mapInfo + 1; i++) {
//            for (int j = 1; j < m_mapInfo + 1; j++) {
//                Log.d("inchang_mapCheck", Character.toString(alphabatsMap[i][j]) + " ");
//            }
//            Log.d("inchang_mapCheck", "enter");
//        }
        return alphabatsMap;
    }

    private void displayMap() {

        // 영어 단어 출력
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(m_voca);

        // 알파벳 맵 UI
        for (int i = 1; i < m_mapInfo + 1; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 1; j < m_mapInfo + 1; j++) {
                TextView textView = new TextView(this);
                textView.setTextSize(38);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.parseColor("#424242"));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setText(String.valueOf(m_alphabatsMap[i][j]));
                TableRow.LayoutParams trp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
                trp.setMargins(12, 12, 0, 0);
                tableRow.setBackgroundColor(Color.parseColor("#c1cdcd"));
                tableRow.addView(textView, trp);
            }
            TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            m_tableLayout.setStretchAllColumns(true);
            if (i != m_mapInfo) {
                tlp.setMargins(0, 0, 12, 0);
                m_tableLayout.addView(tableRow, tlp);
            } else {
                tlp.setMargins(0, 0, 12, 12);
                m_tableLayout.addView(tableRow, tlp);
            }
        }
    }

    private void deleteMap() {
        m_tableLayout.removeAllViews();
    }

    private void setDingcoStartPos() {
        m_relativeLayout.removeAllViews();
        switch (m_mapInfo) {
            case 3: {
                m_firstMarginLeft = 250;
                m_firstMarginTop = 240;
                m_moveToMarginLeft = 272;
                m_moveToMarginTop = 150;
                m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
                m_relativeLayout.addView(m_dingco, m_rlp);
                break;
            }
            case 4: {
                m_firstMarginLeft = 218;
                m_firstMarginTop = 167;
                m_moveToMarginLeft = 205;
                m_moveToMarginTop = 148;
                m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
                m_relativeLayout.addView(m_dingco, m_rlp);
                break;
            }
            case 5: {
                m_firstMarginLeft = 198;
                m_firstMarginTop = 95;
                m_moveToMarginLeft = 163;
                m_moveToMarginTop = 147;
                m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
                m_relativeLayout.addView(m_dingco, m_rlp);
                break;
            }
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View view = (View) event.getLocalState();
                    switch (view.getId()) {
                        case R.id.up:
                            m_queueForCharacterMoving.add(Arrow.valueOf("up"));
                            m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.up)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.down:
                            m_queueForCharacterMoving.add(Arrow.valueOf("down"));
                            m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.down)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.left:
                            m_queueForCharacterMoving.add(Arrow.valueOf("left"));
                            m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.left)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.right:
                            m_queueForCharacterMoving.add(Arrow.valueOf("right"));
                            m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.right)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.repeat:
                            // repeat 블록이 연속으로 입력되는 것을 방지함
                            if (m_repeatFlag) {
                                m_queueForCharacterMoving.add(Arrow.valueOf("repeat"));
                                m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.repeat)));
                                m_gridView.setAdapter(m_gridViewAdapter);
                                m_repeatFlag = false;
                            }
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            m_repeatFlag = true;
            switch (v.getId()) {
                case R.id.back: {// 이전 블록 지우기
                    if (!m_listForCodingStation.isEmpty()) {
                        m_listForCodingStation.remove(m_listForCodingStation.size() - 1);
                        m_gridView.setAdapter(m_gridViewAdapter);
                        m_queueForCharacterMoving.pollLast();
                    } else {
                        if (!m_stackForAnswers.isEmpty()) {
                            Arrow arrow = m_stackForAnswers.pop();
                            m_characterR -= arrow.getRow();
                            m_characterC -= arrow.getCol();
                            m_correctAlphaCnt--;
                            Queue<Arrow> queue = new ArrayDeque<>();
                            queue.add(arrow);
                            new MovingTask(ViewActivity.this).execute(new MovingTaskParams(m_queueForMovingTask, m_queueForMovingTask.size(), -1));
                        } else {
                            Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case R.id.clear: {// 게임 정보 초기화
                    m_characterR = 1;// 캐릭터 위치 초기화
                    m_characterC = 1;
                    m_correctAlphaCnt = 0;// 영단어 정답 알파벳 개수 초기화
                    m_listForCodingStation.clear();// 자료구조 데이터 클리어
                    m_gridView.setAdapter(m_gridViewAdapter);
                    m_queueForCharacterMoving.clear();
                    m_stackForAnswers.clear();
                    m_queueForMovingTask.clear();
                    setDingcoStartPos();// 캐릭터 처음 위치로
                    Toast.makeText(getApplicationContext(), "모든 데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.complete: {

                    // 입력된 코딩 블록이 없다면
                    if (m_listForCodingStation.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();

                        // 입력된 코딩블록이 있다면
                    } else {
                        int repeatNum = 1, correctRepeatCnt = 0;
                        boolean isRepeat = false, isFailed = false;
                        m_listForCodingStation.clear();
                        m_gridView.setAdapter(m_gridViewAdapter);

                        // (1) 큐에 입력된 코딩 블록의 수 만큼 반복
                        while (m_queueForCharacterMoving.size() > 0) {
                            Log.d("inchang_ViewActivity", "while 시작 while 시작 while 시작 while 시작 while 시작 while 시작 while 시작 while 시작");
                            Arrow arrow = m_queueForCharacterMoving.poll();
                            Log.d("inchang_ViewActivity", arrow.toString());
                            if (m_correctAlphaCnt < m_voca.length()) {

                                // (2) 코딩블록이 repeat일 경우
                                if (arrow.toString().equals("repeat")) {
                                    isRepeat = true;

                                    Log.d("inchang_ViewActivity", "Repeat 확인");

                                    // 반복 수
                                    String chk = m_repeatNum.getText().toString();
                                    try {
                                        if (Integer.parseInt(chk) > 1 && Integer.parseInt(chk) < 9)
                                            repeatNum = Integer.parseInt(chk);
                                        else {
                                            Toast.makeText(getApplicationContext(), "반복은 최소 2번 최대 9번까지에요.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getApplicationContext(), "숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    // (3) 코딩블록이 repeat이 아닐 경우
                                } else {
                                    Log.d("inchang_ViewActivity", String.valueOf(repeatNum) + "반복수반복수반복수반복수반복수반복수반복수");
                                    for (int i = 0; i < repeatNum; i++) {
                                        Log.d("inchang_ViewActivity", "내부캐릭터이동 실행 내부캐릭터이동 실행 내부캐릭터이동 실행 내부캐릭터이동 실행 내부캐릭터이동 실행");
                                        // (4) 캐릭터 좌표 이동
                                        m_characterR += arrow.getRow();
                                        m_characterC += arrow.getCol();

                                        Log.d("inchang_ViewActivity", String.valueOf(m_characterR) + "캐릭터위치 캐릭터위치 캐릭터위치 캐릭터위치 캐릭터위치");
                                        Log.d("inchang_ViewActivity", String.valueOf(m_characterC) + "캐릭터위치 캐릭터위치 캐릭터위치 캐릭터위치 캐릭터위치");

                                        // (5-1) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함되지 않는다면
                                        if (m_alphabatsMap[m_characterR][m_characterC] != m_voca.charAt(m_correctAlphaCnt)) {
                                            Log.d("inchang_ViewActivity", "repeat인데 틀림 repeat인데 틀림 repeat인데 틀림 repeat인데 틀림");
                                            isFailed = true;
                                            Toast.makeText(getApplicationContext(), " 다시 시도해보세요.", Toast.LENGTH_SHORT).show();

                                            // (5-2) 이전 좌표로 캐릭터 이동
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();

                                            Log.d("inchang_ViewActivity", String.valueOf(m_characterR) + "위치 수정 위치 수정 위치 수정 위치 수정 위치 수정 위치 수정");
                                            Log.d("inchang_ViewActivity", String.valueOf(m_characterC) + "위치 수정 위치 수정 위치 수정 위치 수정 위치 수정 위치 수정");

                                            // (5-3) 정답이 아닌 코딩 블록 이후 큐 비우기
                                            if (!m_queueForCharacterMoving.isEmpty()) {
                                                m_queueForCharacterMoving.poll();
                                            }
                                            // (6) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함된다면
                                        } else {
                                            m_stackForAnswers.add(arrow);
                                            m_correctAlphaCnt++;
                                            correctRepeatCnt++;
                                            Log.d("inchang_ViewActivity", String.valueOf(m_correctAlphaCnt) + "알파벳 맞은 개수 알파벳 맞은 개수 알파벳 맞은 개수 알파벳 맞은 개수");
                                        }
                                    }
                                    if (isRepeat && isFailed) {
                                        // (5-4) 잘못 반복된 만큼 이전 좌표로 캐릭터 이동
                                        for (int i = 0; i < correctRepeatCnt; i++) {
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();
                                            m_correctAlphaCnt--;
                                            m_stackForAnswers.pop();
                                            Log.d("inchang_ViewActivity", "repeat인데 틀려서 이전 위치로 이동 repeat인데 틀려서 이전 위치로 이동 repeat인데 틀려서 이전 위치로 이동");
                                            Log.d("inchang_ViewActivity", String.valueOf(m_characterR) + "위치 수정 위치 수정 위치 수정 위치 수정 위치 수정 위치 수정");
                                            Log.d("inchang_ViewActivity", String.valueOf(m_characterC) + "위치 수정 위치 수정 위치 수정 위치 수정 위치 수정 위치 수정");
                                        }
                                        // (7) 캐릭터 이동
                                    } else if (!isRepeat && !isFailed) {
                                        Log.d("inchang_ViewActivity", "반복 없이 정상 이동 반복 없이 정상 이동 반복 없이 정상 이동 반복 없이 정상 이동");
                                        m_queueForMovingTask.add(arrow);
                                    } else if (isRepeat && !isFailed) {
                                        for (int i = 0; i < repeatNum; i++) {
                                            Log.d("inchang_ViewActivity", "반복 이동 반복 이동 반복 이동 반복 이동 반복 이동 반복 이동 반복 이동 반복 이동 반복 이동");
                                            m_queueForMovingTask.add(arrow);
                                        }
                                    }
                                    // (8) 반복수 초기화
                                    repeatNum = 1;
                                    isRepeat = false;
                                    correctRepeatCnt = 0;
                                    isFailed = false;
                                }
                            }
                            Log.d("inchang_ViewActivity", "while 종료 while 종료 while 종료 while 종료 while 종료 while 종료 while 종료 while 종료");
                        }
                        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 맞을 때마다 생기는 큐를 통째로 받는 무빙테스크
                        new MovingTask(ViewActivity.this).execute(new MovingTaskParams(m_queueForMovingTask, m_queueForMovingTask.size(), 1));
                    }
                    if (m_correctAlphaCnt == m_voca.length()) {// 영어단어를 맞췄을 경우 재시작, 게임 정보 초기화
                        Toast.makeText(getApplicationContext(), "축하합니다!", Toast.LENGTH_SHORT).show();
                        m_winCnt++;// 승리 수 증가
                        m_characterR = 1;// 캐릭터 위치 초기화
                        m_characterC = 1;
                        m_correctAlphaCnt = 0;// 영단어 맞춘 개수 초기화
                        m_queueForCharacterMoving.clear();// 자료구조 데이터 초기화
                        m_listForCodingStation.clear();
                        m_queueForMovingTask.clear();
                        m_stackForAnswers.clear();
                        Random randomGenerator = new Random();// 영단어 랜덤 팝업
                        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
                        Log.d("inchang_Voca", m_voca);
                        m_alphabatsMap = getAlphaMap(getAlphaQueue());// 맵 다시 그리기
                        deleteMap();
                        displayMap();
                        m_isFinished = true;
                        Intent i = new Intent(ViewActivity.this, DialogActivity.class);
                        String info[] = {m_voca, String.valueOf(m_mapInfo)};
                        i.putExtra("info", info);
                        startActivity(i);

                        // 레벨 업 - 보류
                        // 뒤로가기 시, m_winCnt 데이터가 사라지기 때문에 저장해야됨
                        // 초급영단어, 고급영단어,  초급영문장, 고급영문장의 m_winCnt를 각각 따로 관리 해야됨
                        // 로컬에서 하는 것이므로 LevelActivity에서 각각 관리하고 ViewActivity로 인텐트로 날려주면 될 거 같음
                        if (m_winCnt > 2) {
                            Toast.makeText(getApplicationContext(), "심화 영단어 단계도 풀어보아요!", Toast.LENGTH_LONG).show();
                            LevelActivity.advancedVButton.setEnabled(true);
                            LevelActivity.advancedVButton.setImageResource(R.drawable.advancedv);
                        }
                        /*
                        else if(m_winCnt > 20) {
                            Toast.makeText(getApplicationContext(), "축하합니다! 다음 level이 열렸어요!", Toast.LENGTH_LONG).show();
                            LevelActivity.basicSButton.setEnabled(true);
                            LevelActivity.basicSButton.setImageResource(R.drawable.basics);
                        }
                        */
                    }
                    break;
                }
            }
        }
    };

    public RelativeLayout getRelativeLayout() {
        return m_relativeLayout;
    }

    public RelativeLayout.LayoutParams getLayoutParams() {
        return m_rlp;
    }

    public void setLastDingcoPos(int left, int top, int right, int bottom) {
        m_relativeLayout.removeAllViews();
        m_rlp.setMargins(left, top, right, bottom);
        m_relativeLayout.addView(m_dingco, m_rlp);
    }

    public ImageView getDingco() {
        return m_dingco;
    }

    public int getMoveToMarginLeft() {
        return m_moveToMarginLeft;
    }

    public int getMoveToMarginTop() {
        return m_moveToMarginTop;
    }

    public int getFirstMarginLeft() {
        return m_firstMarginLeft;
    }

    public int getFirstMarginTop() {
        return m_firstMarginTop;
    }

    public boolean getIsFinished() {
        return m_isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.m_isFinished = isFinished;
    }
}