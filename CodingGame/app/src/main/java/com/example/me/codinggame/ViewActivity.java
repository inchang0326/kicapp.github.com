package com.example.me.codinggame;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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

    private ImageView m_up, m_down, m_left, m_right, m_repeat;
    private Button m_back, m_clear, m_complete;
    private EditText m_repeatNum;
    private GridView m_gridView;
    private GridViewAdapter m_gridViewAdapter;
    private TableLayout m_tableLayout;

    private ArrayList<GridViewItem> m_listForMakingView = new ArrayList<>();
    private ArrayDeque<Arrow> m_queueForMakingView = new ArrayDeque<>();
    private String m_voca;
    private char m_alphabatsMap[][];
    private int m_mapInfo, m_characterR = 1, m_characterC = 1, m_correctAlphaCnt = 0, m_winCnt = 0;
    private boolean m_repeatFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // 위젯 모음
        m_up = (ImageView) findViewById(R.id.up);
        m_down = (ImageView) findViewById(R.id.down);
        m_left = (ImageView) findViewById(R.id.left);
        m_right = (ImageView) findViewById(R.id.right);
        m_repeat = (ImageView) findViewById(R.id.repeat);
        m_back = (Button) findViewById(R.id.back);
        m_clear = (Button) findViewById(R.id.clear);
        m_complete = (Button) findViewById(R.id.complete);
        m_gridView = (GridView) findViewById(R.id.codingStation);
        m_gridViewAdapter = new GridViewAdapter(getApplicationContext(), R.layout.gridview_item, m_listForMakingView);
        m_tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        // 위젯 이벤트 등록
        m_up.setOnLongClickListener(longClickListener);
        m_down.setOnLongClickListener(longClickListener);
        m_left.setOnLongClickListener(longClickListener);
        m_right.setOnLongClickListener(longClickListener);
        m_repeat.setOnLongClickListener(longClickListener);
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

        // repeat 버튼 애니메이션 추가
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

    private void displayMap() {
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(m_voca);

        // 알파벳 맵 UI
        for (int i = 1; i < m_mapInfo + 1; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 1; j < m_mapInfo + 1; j++) {
                TextView textView = new TextView(this);
                textView.setTextSize(38);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.parseColor("#424242"));
                textView.setText("  " + String.valueOf(m_alphabatsMap[i][j]) + "  ");
                TableRow.LayoutParams trp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setBackgroundColor(Color.parseColor("#c1cdcd"));
                trp.setMargins(12, 12, 0, 0);
                tableRow.addView(textView, trp);
            }
            TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            if(i != m_mapInfo) {
                tlp.setMargins(0, 0, 12, 0);
                m_tableLayout.addView(tableRow, tlp);
            }
            else {
                tlp.setMargins(0, 0, 12, 12);
                m_tableLayout.addView(tableRow, tlp);
            }
        }
    }

    private void deleteMap() {
        m_tableLayout.removeAllViews();
    }

    // 난이도 높이려면 알파벳 중복으로 나오도록 해도 될 것 같다.
    private Queue<Character> getAlphaQueue() {
        int vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        Random randomGenerator = new Random();
        Queue<Character> queue = new ArrayDeque<>();

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

    private char[][] getAlphaMap(Queue<Character> queue) {
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
            if (isVisited[nextR][nextC] == true) {
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
                    if (isVisited[nextR][nextC] == true) {

                        // (5) 현재 위치에서 주변에 하나라도 갈 곳이 있으면 flag를 true로 설정
                        flag = true;
                    }
                }

                // (6) 한 곳으로라도 갈 곳이 없다면 이전 위치를 스택에서 팝
                if (flag == false) {

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
                        currR = p.getRow();
                        currC = p.getCol();
                    }
                }
            }
        }

        // for debugging
        for (int i = 1; i < m_mapInfo + 1; i++) {
            for (int j = 1; j < m_mapInfo + 1; j++) {
                Log.d("inchang_mapCheck", Character.toString(alphabatsMap[i][j]) + " ");
            }
            Log.d("inchang_mapCheck", "enter");
        }

        return alphabatsMap;
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
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
                            m_queueForMakingView.add(Arrow.valueOf("up"));
                            m_listForMakingView.add(new GridViewItem(getResources().getDrawable(R.drawable.up)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.down:
                            m_queueForMakingView.add(Arrow.valueOf("down"));
                            m_listForMakingView.add(new GridViewItem(getResources().getDrawable(R.drawable.down)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.left:
                            m_queueForMakingView.add(Arrow.valueOf("left"));
                            m_listForMakingView.add(new GridViewItem(getResources().getDrawable(R.drawable.left)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.right:
                            m_queueForMakingView.add(Arrow.valueOf("right"));
                            m_listForMakingView.add(new GridViewItem(getResources().getDrawable(R.drawable.right)));
                            m_gridView.setAdapter(m_gridViewAdapter);
                            m_repeatFlag = true;
                            break;
                        case R.id.repeat:
                            // repeat 블록이 연속으로 입력되는 것을 방지함
                            if (m_repeatFlag) {
                                m_queueForMakingView.add(Arrow.valueOf("repeat"));
                                m_listForMakingView.add(new GridViewItem(getResources().getDrawable(R.drawable.repeat)));
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
                case R.id.back: {
                    if (!m_listForMakingView.isEmpty()) {
                        m_listForMakingView.remove(m_listForMakingView.size() - 1);
                        m_gridView.setAdapter(m_gridViewAdapter);
                        m_queueForMakingView.pollLast();
                    } else {
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case R.id.clear: {                  // 게임 정보 초기화
                    m_characterR = 1;               // 캐릭터의 Row
                    m_characterC = 1;               // 캐릭터의 Col
                    m_correctAlphaCnt = 0;          // 맞춘 알파벳 조합 수
                    m_listForMakingView.clear();    // 리스트 데이터 제거
                    m_gridView.setAdapter(m_gridViewAdapter);
                    m_queueForMakingView.clear();   // 큐 데이터 제거
                    Toast.makeText(getApplicationContext(), "모든 데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.complete: {
                    // 입력된 코딩블록이 없다면
                    if (m_listForMakingView.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else { // 입력된 코딩블록이 있다면
                        int repeatNum = 1, correctRepeatCnt = 0;
                        boolean isRepeat = false, isFailed = false;
                        m_listForMakingView.clear();
                        m_gridView.setAdapter(m_gridViewAdapter);

                        // 큐에 입력된 코딩블록의 수 만큼 반복
                        while (m_queueForMakingView.size() > 0) {
                            Arrow arrow = m_queueForMakingView.poll();
                            Log.d("inchang_Arrow최초확인", arrow.toString());

                            if (m_correctAlphaCnt < m_voca.length()) {
                                // 코딩블록이 repeat일 경우
                                if (arrow.toString() == "repeat") {
                                    Log.d("inchang_Repeat확인", "OK");
                                    isRepeat = true;
                                    // 반복 수
                                    String chk = m_repeatNum.getText().toString();
                                    try {
                                        if(Integer.parseInt(chk) > 1 && Integer.parseInt(chk) < 9)
                                            repeatNum = Integer.parseInt(chk);
                                        else {
                                            Toast.makeText(getApplicationContext(), "반복은 최소 2번 최대 9번까지에요.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(getApplicationContext(), "숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                } else { // 코딩블록이 repeat이 아닐 경우
                                    Log.d("inchang_NoRepeat확인", arrow.toString());
                                    for (int i = 0; i < repeatNum; i++) {
                                        correctRepeatCnt++;
                                        Log.d("inchang_Repeat반복수", String.valueOf(i));
                                        Log.d("inchang_Repeat몇번까지", String.valueOf(repeatNum - 1));

                                        // 캐릭터 좌표 이동
                                        m_characterR += arrow.getRow();
                                        m_characterC += arrow.getCol();
                                        Log.d("inchang_Arrow좌표", String.valueOf(arrow.getRow()) + " " + String.valueOf(arrow.getCol()));
                                        Log.d("inchang_Character좌표", String.valueOf(m_characterR) + " " + String.valueOf(m_characterC));

                                        // 반복문 때문에 m_correctAlphaCnt가 단어 알파벳 수를 넘어가는 예외처리해줘야댐

                                        // 캐릭터가 이동한 자리에 있는 알파벳이 영어단어에 포함되지 않는다면
                                        if (m_alphabatsMap[m_characterR][m_characterC] != m_voca.charAt(m_correctAlphaCnt)) {
                                            isFailed = true;
                                            Toast.makeText(getApplicationContext(), " 다시 시도해보세요.", Toast.LENGTH_SHORT).show();

                                            // 이전 좌표로 캐릭터 이동
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();
                                            m_correctAlphaCnt--;
                                            correctRepeatCnt--;
                                            Log.d("inchang_잘못된Arrow좌표", String.valueOf(arrow.getRow()) + " " + String.valueOf(arrow.getCol()));
                                            Log.d("inchang_바뀐Chracter좌표", String.valueOf(m_characterR) + " " + String.valueOf(m_characterC));

                                            // 정답이 아닌 코딩블록 이후 큐 비우기
                                            if (!m_queueForMakingView.isEmpty()) {
                                                m_queueForMakingView.poll();
                                            }
                                        }
                                        m_correctAlphaCnt++;
                                        Log.d("inchang_알파벳맞은수", String.valueOf(m_correctAlphaCnt));
                                    }
                                    if (isRepeat && isFailed) {
                                        // 반복문 할 때 맞은 개수만큼 스택 팝
                                        Log.d("inchang_반복문맞은수", String.valueOf(m_correctAlphaCnt));
                                        for (int i = 0; i < correctRepeatCnt; i++) {
                                            // 이전 좌표로 캐릭터 이동
                                            m_characterR -= arrow.getRow();
                                            m_characterC -= arrow.getCol();
                                            m_correctAlphaCnt--;
                                        }
                                    }
                                    repeatNum = 1;
                                }
                            }
                        }
                        Log.d("inchang_최종Chracter좌표", String.valueOf(m_characterR) + " " + String.valueOf(m_characterC));
                    }

                    // 영어단어를 맞췄을 경우 재시작
                    if (m_correctAlphaCnt == m_voca.length()) {
                        Toast.makeText(getApplicationContext(), "축하합니다!", Toast.LENGTH_SHORT).show();

                        m_winCnt++;
                        m_characterR = 1;
                        m_characterC = 1;
                        m_correctAlphaCnt = 0;
                        m_queueForMakingView.clear();
                        m_listForMakingView.clear();
                        Random randomGenerator = new Random();
                        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
                        Log.d("inchang_Voca", m_voca);
                        m_alphabatsMap = getAlphaMap(getAlphaQueue());
                        deleteMap();
                        displayMap();

                        // 레벨 업 - 보류
                        // 뒤로가기 시, m_winCnt 데이터가 사라지기 때문에 저장해야됨
                        // 초급영단어, 고급영단어,  초급영문장, 고급영문장의 m_winCnt를 각각 따로 관리 해야됨
                        // 로컬에서 하는 것이므로 LevelActivity에서 각각 관리하고 ViewActivity로 인텐트로 날려주면 될 거 같음
                        /*
                        if(m_winCnt > 10) {
                            Toast.makeText(getApplicationContext(), "축하합니다! 다음 level이 열렸어요!", Toast.LENGTH_LONG).show();
                            LevelActivity.advancedVButton.setEnabled(true);
                            LevelActivity.advancedVButton.setImageResource(R.drawable.advancedv);
                        }
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
}