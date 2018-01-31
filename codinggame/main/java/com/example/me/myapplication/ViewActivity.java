package com.example.me.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
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
    private Toast m_toast;
    private TextView m_toastTextView;
    private GridView m_gridView;
    private GridViewAdapter m_gridViewAdapter;
    private TableLayout m_tableLayout;
    private RelativeLayout m_relativeLayout;
    private RelativeLayout.LayoutParams m_rlp;

    private ArrayDeque<Arrow> m_queueForCharacterMoving = new ArrayDeque<>();// 입력된 코딩 블록들을 저장하는 Queue
    private ArrayList<GridViewItem> m_listForCodingStation = new ArrayList<>();// 코딩 작업창을 채우는 ArrayList
    private Stack<Arrow> m_stackForAnswers = new Stack<>();// 코딩 작업창에 블록이 없을 때, back키 클릭 시, 캐릭터를 이전 위치로 되돌리는 Stack
    private Queue<Arrow> m_queueForMovingTask = new ArrayDeque<>();// 정답인 코딩 블록만 저장해서 UI로 보내는 Queue
    private Queue<Integer> m_queueForRepeatNum = new ArrayDeque<>();// 반복 코딩 블록 입력 시, 반복 수들을 뽑아내는 Queue
    private String m_voca;
    private char m_alphabatsMap[][];
    private int m_mapInfo = 0, m_characterR, m_characterC, m_correctAlphaCnt;
    private int m_moveToMarginLeft = 0, m_moveToMarginTop = 0, m_firstMarginLeft = 0, m_firstMarginTop = 0, m_winCnt = 0;
    private boolean m_repeatFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // 맵 정보 받기
        Bundle extras = getIntent().getExtras();
        m_mapInfo = extras.getInt("mapInfo");

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
        m_toast = Toast.makeText(getApplicationContext(), "No message", Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) m_toast.getView();
        m_toastTextView = (TextView) group.getChildAt(0);
        m_toastTextView.setText("hdfjsdhflaskehflsaenfnlseakf");
        m_toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);

        // 레이아웃 모음
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

        init();// 게임 정보 초기화
        generateMap();// 맵 생성
        setDingcoStartPos();// 캐릭터 생성

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

    private void init() {
        // 캐릭터 위치 초기화
        m_characterR = 1;
        m_characterC = 1;
        // 영단어 정답 알파벳 개수 초기화
        m_correctAlphaCnt = 0;
        // 자료구조 데이터 클리어
        m_listForCodingStation.clear();
        m_gridView.setAdapter(m_gridViewAdapter);
        m_queueForCharacterMoving.clear();
        m_stackForAnswers.clear();
        m_queueForRepeatNum.clear();
    }

    private void generateMap() {
        // 맵 정보에 따라 영어 랜덤 출력
        Random randomGenerator = new Random();
        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();

        // 알파벳 맵 만들기
        m_alphabatsMap = getAlphaMap(getAlphaQueue());
        deleteMap();
        displayMap();
    }

    private ArrayDeque<Character> getAlphaQueue() {
        int vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        Random randomGenerator = new Random();
        ArrayDeque<Character> queue = new ArrayDeque<>();

        for (int i = 0; i < vocaMaxLength; i++) {
            // 큐에 영단어 알파벳 저장
            if (i < m_voca.length())
                queue.add(m_voca.charAt(i));
            else {
                char rc = alphas[randomGenerator.nextInt(alphas.length)];
                // 포함된 알파벳이 아니라면 큐에 저장
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

            // (2) 다음 갈 방향 랜덤 지정
            int nextR = currR + arrow.getRow();
            int nextC = currC + arrow.getCol();

            // (3) 방문하지 않았던(true) 곳이라면 방문(false)하고 스택에 저장
            if (isVisited[nextR][nextC]) {
                alphabatsMap[nextR][nextC] = queue.poll();
                isVisited[nextR][nextC] = false;
                stack.add(new Point(alphabatsMap[nextR][nextC], nextR, nextC));
                currR = nextR;
                currC = nextC;

                // (4) 다음 위치가 방문했던 곳이라면(false) 현재 위치(currR, currC)에서 4방면을 확인
            } else {
                flag = false;
                for (int i = 0; i < 4; i++) {

                    // 위, 아래, 왼쪽, 오른쪽
                    arrow = Arrow.valueOf(i);
                    nextR = currR + arrow.getRow();
                    nextC = currC + arrow.getCol();
                    if (isVisited[nextR][nextC]) {

                        // (5) 현재 위치에서 주변에 하나라도 갈 곳이 있으면 flag를 true로 설정
                        flag = true;
                    }
                }

                // (6) 한 곳으로라도 갈 곳이 없다면 이전 위치를 스택에서 팝
                if (!flag) {

                    // (7) 주어진 단어의 알파벳이 모두 채워지지 않았는데 길이 막혔을 경우, 맵 재배치
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

                    // (8) 주어진 단어의 알파벳이 모두 채워졌는데 길이 막혔을 경우, 이전 위치로 이동
                    else {
                        Point p = stack.pop();
                        currR = p.getCurrRow();
                        currC = p.getCurrCol();
                    }
                }
            }
        }
        return alphabatsMap;
    }

    private void displayMap() {

        // 영단어 UI
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(m_voca);

        // 맵 UI
        for (int i = 1; i < m_mapInfo + 1; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 1; j < m_mapInfo + 1; j++) {
                TextView textView = new TextView(this);
                textView.setTextSize(76);
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
                m_firstMarginLeft = 115;
                m_firstMarginTop = 140;
                m_moveToMarginLeft = 240;
                m_moveToMarginTop = 120;
                m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
                m_relativeLayout.addView(m_dingco, m_rlp);
                break;
            }
            case 4: {
                m_firstMarginLeft = 90;
                m_firstMarginTop = 85;
                m_moveToMarginLeft = 180;
                m_moveToMarginTop = 115;
                m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
                m_relativeLayout.addView(m_dingco, m_rlp);
                break;
            }
            case 5: {
                m_firstMarginLeft = 72;
                m_firstMarginTop = 27;
                m_moveToMarginLeft = 140;
                m_moveToMarginTop = 115;
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
                            // 뷰 인플레터 오류 예외처리
                            try {
                                m_queueForCharacterMoving.add(Arrow.valueOf("up"));
                                m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.up)));
                                m_gridView.setAdapter(m_gridViewAdapter);
                                m_repeatFlag = true;
                            } catch (Exception e) {
                                m_toastTextView.setText("블록을 다시 넣어주세요.");
                                m_toast.show();
                            }
                            break;
                        case R.id.down:
                            // 뷰 인플레터 오류 예외처리
                            try {
                                m_queueForCharacterMoving.add(Arrow.valueOf("down"));
                                m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.down)));
                                m_gridView.setAdapter(m_gridViewAdapter);
                                m_repeatFlag = true;
                            } catch (Exception e) {
                                m_toastTextView.setText("블록을 다시 넣어주세요.");
                                m_toast.show();
                            }
                            break;
                        case R.id.left:
                            // 뷰 인플레터 오류 예외처리
                            try {
                                m_queueForCharacterMoving.add(Arrow.valueOf("left"));
                                m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.left)));
                                m_gridView.setAdapter(m_gridViewAdapter);
                                m_repeatFlag = true;
                            } catch (Exception e) {
                                m_toastTextView.setText("블록을 다시 넣어주세요.");
                                m_toast.show();
                            }
                            break;
                        case R.id.right:
                            // 뷰 인플레터 오류 예외처리
                            try {
                                m_queueForCharacterMoving.add(Arrow.valueOf("right"));
                                m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.right)));
                                m_gridView.setAdapter(m_gridViewAdapter);
                                m_repeatFlag = true;
                            } catch (Exception e) {
                                m_toastTextView.setText("블록을 다시 넣어주세요.");
                                m_toast.show();
                            }
                            break;
                        case R.id.repeat:
                            // repeat 블록이 연속으로 입력되는 것 예외처리
                            if (m_repeatFlag) {
                                // 반복 수
                                String chk = m_repeatNum.getText().toString();
                                // 숫자가 아닌 값 예외처리
                                try {
                                    int value = Integer.parseInt(chk);
                                    if (value > 1 && value < 9) {
                                        // 뷰 인플레터 오류 예외처리
                                        try {
                                            m_queueForRepeatNum.add(value);
                                            m_queueForCharacterMoving.add(Arrow.valueOf("repeat"));
                                            m_listForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.repeat)));
                                            m_gridView.setAdapter(m_gridViewAdapter);
                                            m_repeatFlag = false;
                                        } catch (Exception e) {
                                            m_toastTextView.setText("블록을 다시 넣어주세요.");
                                            m_toast.show();
                                        }
                                    } else {
                                        m_toastText = "반복은 2 ~ 9 사이의 숫자를 넣어주세요.";
                                        m_toast.show();
                                    }
                                } catch (NumberFormatException e) {
                                    m_toastText = "숫자를 넣어주세요.";
                                    m_toast.show();
                                }
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
                        if (m_queueForCharacterMoving.pollLast().toString().equals("repeat")) {
                            m_queueForRepeatNum.poll();
                        }
                    } else {
                        if (!m_stackForAnswers.isEmpty()) {
                            Arrow arrow = m_stackForAnswers.pop();
                            m_characterR -= arrow.getRow();
                            m_characterC -= arrow.getCol();
                            m_correctAlphaCnt--;
                            Queue<Arrow> queue = new ArrayDeque<>();
                            queue.add(arrow);
                            new MovingTask(ViewActivity.this).execute(new MovingTaskParams(queue, queue.size(), -1));
                        } else {
                            m_toastText = "블록을 넣어주세요.";
                            m_toast.show();
                        }
                    }
                    break;
                }
                case R.id.clear: {
                    init();// 게임 정보 초기화
                    setDingcoStartPos();// 캐릭터 처음 위치로
                    m_toastText = "모든 블록을 지웠어요.";
                    m_toast.show();
                    break;
                }
                case R.id.complete: {

                    // 입력된 코딩 블록이 없다면
                    if (m_listForCodingStation.isEmpty()) {
                        m_toastText = "블록을 넣어주세요.";
                        m_toast.show();

                        // 입력된 코딩블록이 있다면
                    } else {
                        int repeatNum = 1, correctRepeatCnt = 0;
                        boolean isRepeat = false, isFailed = false;
                        m_listForCodingStation.clear();
                        m_gridView.setAdapter(m_gridViewAdapter);

                        // (1) 큐에 입력된 코딩 블록의 수 만큼 반복
                        while (m_queueForCharacterMoving.size() > 0) {
                            Arrow arrow = m_queueForCharacterMoving.poll();

                            // (2) 코딩블록이 repeat일 경우
                            if (arrow.toString().equals("repeat")) {
                                isRepeat = true;
                                repeatNum = m_queueForRepeatNum.poll();

                                // (3) 코딩블록이 repeat이 아닐 경우
                            } else {
                                for (int i = 0; i < repeatNum; i++) {

                                    // (4) 캐릭터 좌표 이동
                                    m_characterR += arrow.getRow();
                                    m_characterC += arrow.getCol();


                                    // 아웃오브바운드 익셉션 예외 처리
                                    try {
                                        // (5-1) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함되지 않는다면
                                        if (m_alphabatsMap[m_characterR][m_characterC] != m_voca.charAt(m_correctAlphaCnt)) {
                                            m_toastText = "다시 시도해보세요.";
                                            m_toast.show();
                                            isFailed = true;

                                            // (5-2) 이전 좌표로 캐릭터 이동
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();

                                            // (5-3) 정답이 아닌 코딩 블록 이후 큐 비우기
                                            while (!m_queueForCharacterMoving.isEmpty()) {
                                                m_queueForCharacterMoving.poll();
                                            }
                                            // (6) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함된다면
                                        } else {
                                            m_stackForAnswers.add(arrow);
                                            m_correctAlphaCnt++;
                                            correctRepeatCnt++;
                                        }
                                    } catch (Exception e) {
                                        // 반복 시, 아웃오브인덱스 예외처리
                                        for (int j = 0; j < correctRepeatCnt; j++) {
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();
                                            m_correctAlphaCnt--;
                                            m_stackForAnswers.pop();
                                        }
                                        // 예외처리 지점을 추가하기 때문에 한번 더 빼줘야 함
                                        m_characterR -= arrow.getRow();
                                        m_characterC -= arrow.getCol();
                                        m_toastText = "다시 시도해보세요.";
                                        m_toast.show();
                                        isFailed = true;
                                        correctRepeatCnt = 0;
                                        break;
                                    }
                                }
                                // 반복 시, 틀린 블록이 있는 경우
                                if (isRepeat && isFailed) {
                                    // (5-4) 잘못 반복된 만큼 이전 좌표로 캐릭터 이동
                                    for (int i = 0; i < correctRepeatCnt; i++) {
                                        m_characterR -= arrow.getRow();
                                        m_characterC -= arrow.getCol();
                                        m_correctAlphaCnt--;
                                        m_stackForAnswers.pop();
                                        m_toastText = "다시 시도해보세요.";
                                        m_toast.show();
                                    }
                                    // (7) 캐릭터 이동
                                } else if (!isRepeat && !isFailed) {
                                    m_queueForMovingTask.add(arrow);
                                } else if (isRepeat && !isFailed) {
                                    for (int i = 0; i < repeatNum; i++) {
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
                        new MovingTask(ViewActivity.this).execute(new MovingTaskParams(m_queueForMovingTask, m_queueForMovingTask.size(), 1));
                    }
                    // 영어단어를 맞췄을 경우 재시작, 게임 정보 초기화
                    if (m_correctAlphaCnt == m_voca.length()) {
                        m_toastText = "축하해요!!";
                        m_toast.show();

                        // 영단어 팝업 메시지
                        Intent i = new Intent(ViewActivity.this, DialogActivity.class);
                        String info[] = {m_voca, String.valueOf(m_mapInfo)};
                        i.putExtra("info", info);
                        startActivityForResult(i, 0);

                        init();// 게임 정보 초기화
                        generateMap();// 맵 생성
                        m_winCnt++;// 승리 수 증가
                        if (m_winCnt > 0) {
                            m_toastText = "심화 영단어 단계도 풀어보아요!!";
                            m_toast.show();
                            setResult(RESULT_OK);
                        }
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                setDingcoStartPos();
                break;
        }
    }

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
}