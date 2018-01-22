package com.example.me.codinggame;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;

public class ViewActivity extends AppCompatActivity {

    private ImageView m_up, m_down, m_left, m_right, m_repeat, m_collect;
    private Button m_back, m_clear, m_complete;
    private TextView m_text;
    private Stack<String> m_stackForMakingView = new Stack<>();
    private Queue<Arrow> m_queueForMakingView = new ArrayDeque<>();
    private String m_voca;
    private char m_alphabatsMap[][];
    private int m_mapInfo, m_characterR, m_characterC, m_totalCnt, m_correctAlphaCnt, m_winCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        
        // 캐릭터 좌표 정보
        m_characterR = 1;
        m_characterC = 1;
        // 사용자가 게임을 시도한 횟수
        m_totalCnt = 0;
        // 사용자가 알파벳을 맞춘 횟수
        m_correctAlphaCnt = 0;
        // 사용자가 맵을 클리어한 횟수
        m_winCnt = 0;

        // 1. LevelActivity에서 맵정보(3X3 or 4X4) 받기
        Bundle extras = getIntent().getExtras();
        m_mapInfo = extras.getInt("mapInfo");

        // 2. 맵정보(3X3 or 4X4)에 따라 영단어 랜덤 출력
        Random randomGenerator = new Random();
        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
        Log.d("inchang_Voca", m_voca);

        // 3. 맵정보(3X3 or 4X4)에 따라 큐에 알파벳(Point) 채우기
        Queue<Character> alphabatsQueue = fillOutQueue();

        // 4. 알파벳 맵 만들기
        m_alphabatsMap = fillOutAlphabatsMap(alphabatsQueue);

        // 버튼 모음
        m_up = (ImageView) findViewById(R.id.up);
        m_down = (ImageView) findViewById(R.id.down);
        m_left = (ImageView) findViewById(R.id.left);
        m_right = (ImageView) findViewById(R.id.right);
        m_repeat = (ImageView) findViewById(R.id.repeat);
        m_collect = (ImageView) findViewById(R.id.collect);
        m_back = (Button) findViewById(R.id.back);
        m_clear = (Button) findViewById(R.id.clear);
        m_complete = (Button) findViewById(R.id.complete);
        m_text = (TextView) findViewById(R.id.text);

        // 버튼 이벤트 등록
        m_up.setOnLongClickListener(longClickListener);
        m_down.setOnLongClickListener(longClickListener);
        m_left.setOnLongClickListener(longClickListener);
        m_right.setOnLongClickListener(longClickListener);
        m_repeat.setOnLongClickListener(longClickListener);
        m_collect.setOnLongClickListener(longClickListener);
        m_back.setOnClickListener(clickListener);
        m_clear.setOnClickListener(clickListener);
        m_complete.setOnClickListener(clickListener);
        m_text.setOnDragListener(dragListener);
    }

    private Queue<Character> fillOutQueue() {
        int vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        Random randomGenerator = new Random();
        Queue<Character> queue = new ArrayDeque<>();
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};

        for (int i = 0; i < vocaMaxLength; i++) {
            // (1) 큐에 영단어 알파벳 저장
            if (i < m_voca.length())
                queue.add(m_voca.charAt(i));
            else {
                char rc = alphas[randomGenerator.nextInt(alphas.length)];

                // (2) 영단어가 가지고 있는 알파벳이 포함되지 않을 때 까지 반복
                while (queue.contains(rc)) {
                    rc = alphas[randomGenerator.nextInt(alphas.length)];
                }

                // (3) 포함된 알파벳이 아니라면 큐에 저장
                queue.add(rc);
            }
        }

        return queue;
    }

    private char[][] fillOutAlphabatsMap(Queue<Character> queue) {
        char alphabatsMap[][] = new char[m_mapInfo + 2][m_mapInfo + 2];
        boolean isVisited[][] = new boolean[m_mapInfo + 2][m_mapInfo + 2];
        for (int i = 1; i < m_mapInfo + 1; i++) {
            for (int j = 1; j < m_mapInfo + 1; j++) {
                isVisited[i][j] = true;
            }
        }

        int currR = 1;
        int currC = 1;
        int vocaMaxLength = m_mapInfo * m_mapInfo - 1;
        alphabatsMap[currR][currC] = '@';
        isVisited[currR][currC] = false;
        Arrow arrow;
        boolean flag = false;
        Random randomGenerator = new Random();
        Stack<Point> stack = new Stack<>();

        // (1) 큐에 있는 알파벳이 모두 사라질 때까지 반복
        while (queue.size() > 0) {

            arrow = Arrow.valueOf(randomGenerator.nextInt(4));
//            Log.d("inchang_RandomArrow", arrow.toString());

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
//                Log.d("inchang_NormalAlpha", Character.toString(alphabatsMap[nextR][nextC]));
//                Log.d("inchang_NormalTrueFalse", Boolean.toString(isVisited[nextR][nextC]));
//                Log.d("inchang_NormalRowCol", String.valueOf(currR) + " " + String.valueOf(currC));
            } else {// (4) 방문했던 곳이라면(false) 현재 위치(currR, currC)에서 4방면을 확인
                flag = false;
                for (int i = 0; i < 4; i++) {// 위, 아래, 왼쪽, 오른쪽
                    arrow = Arrow.valueOf(i);
                    nextR = currR + arrow.getRow();
                    nextC = currC + arrow.getCol();
                    if (isVisited[nextR][nextC] == true) {
//                        Log.d("inchang_FourWaysSearch", "? - 1");

                        // (5) 현재 위치에서 주변에 하나라도 갈 곳이 있으면 flag를 true로 설정
                        flag = true;
                    }
                }
                // (6) 한 곳으로라도 갈 곳이 없다면 이전 위치를 스택에서 pop
                if (flag == false) {
//                    Log.d("inchang_NoWayException", "? - 2");
//                    Log.d("inchang_NoWayRowCol", String.valueOf(currR) + " " + String.valueOf(currC));
                    // (7) 주어진 단어의 알파벳이 모두 채워지지 않았는데 길이 막혔을 경우, 맵 재배치
                    if (queue.size() > (vocaMaxLength - m_voca.length())) {
//                        Log.d("inchang_FatalException", "? - 4");
                        stack.clear();
                        queue = fillOutQueue();
                        for (int i = 1; i < m_mapInfo + 1; i++) {
                            for (int j = 1; j < m_mapInfo + 1; j++) {
                                isVisited[i][j] = true;
                            }
                        }
                        currR = 1;
                        currC = 1;
                        alphabatsMap[currR][currC] = '@';
                        isVisited[currR][currC] = false;
                        flag = false;
                    }
                    // (8) 주어진 단어의 알파벳이 모두 채워졌는데 길이 막혔을 경우, 이전 위치로 이동
                    else {
                        Point p = stack.pop();
//                        Log.d("inchang_SafeException", "? - 3");
//                        Log.d("inchang_safeException", Character.toString(p.getAlpha()));

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
                    final View view = (View) event.getLocalState();
                    switch (view.getId()) {
                        case R.id.up:
                            m_queueForMakingView.add(Arrow.valueOf("up"));
                            m_stackForMakingView.push("up");
                            break;
                        case R.id.down:
                            m_queueForMakingView.add(Arrow.valueOf("down"));
                            m_stackForMakingView.push("down");
                            break;
                        case R.id.left:
                            m_queueForMakingView.add(Arrow.valueOf("left"));
                            m_stackForMakingView.push("left");
                            break;
                        case R.id.right:
                            m_queueForMakingView.add(Arrow.valueOf("right"));
                            m_stackForMakingView.push("right");
                            break;
                        case R.id.repeat:
                            m_queueForMakingView.add(Arrow.valueOf("repeat"));
                            m_stackForMakingView.push("repeat");
                            break;
                        case R.id.collect:
                            // 비활성화
                            m_queueForMakingView.add(Arrow.valueOf("collect"));
                            m_stackForMakingView.push("collect");
                            break;
                    }
                    m_text.setText(m_stackForMakingView.toString());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
            }
            return true;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: {
                    if (!m_stackForMakingView.isEmpty()) {
                        m_stackForMakingView.pop();
                        m_queueForMakingView.poll();
                        if (m_stackForMakingView.isEmpty())
                            m_text.setText("데이터를 넣어주세요.");
                        else
                            m_text.setText(m_stackForMakingView.toString());
                    } else
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                case R.id.clear: {
                    m_stackForMakingView.clear();
                    m_queueForMakingView.clear();
                    m_characterR = 1;
                    m_characterC = 1;
                    m_totalCnt = 0;
                    m_correctAlphaCnt = 0;
                    m_queueForMakingView.clear();
                    m_stackForMakingView.clear();
                    m_text.setText("데이터를 넣어주세요.");
                    Toast.makeText(getApplicationContext(), "모든 데이터를 삭제했습니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                case R.id.complete: {
                    if (m_stackForMakingView.isEmpty())
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                    else {
                        while (m_queueForMakingView.size() > 0) {
//                            Log.d("inchang_QueueSize", String.valueOf(m_queueForMakingView.size()));
                            m_totalCnt++;
                            Arrow arrow = m_queueForMakingView.poll();
                            m_characterR += arrow.getRow();
                            m_characterC += arrow.getCol();
//                            Log.d("inchang_QueueElement", arrow.toString());
//                            Log.d("inchang_ChaRowCol", String.valueOf(m_characterR) + " " + String.valueOf(m_characterC));

                            if (m_alphabatsMap[m_characterR][m_characterC] != m_voca.charAt(m_correctAlphaCnt)) {
                                Toast.makeText(getApplicationContext(), "실패했어요. 다시 시도해주세요!", Toast.LENGTH_LONG).show();
//                                Log.d("inchang_QueueSize", String.valueOf(m_queueForMakingView.size()));

                                m_characterR -= arrow.getRow();
                                m_characterC -= arrow.getCol();
                                m_correctAlphaCnt--;
                                while(m_queueForMakingView.size() > 0) {
//                                    Log.d("inchang_QueueSize", String.valueOf(m_queueForMakingView.size()));
//                                    Log.d("inchang_StackSize", String.valueOf(m_stackForMakingView.size()));

                                    m_stackForMakingView.pop();
                                    m_queueForMakingView.poll();
                                }
                                if (!m_stackForMakingView.isEmpty()) {
                                    m_stackForMakingView.pop();
                                    if (m_stackForMakingView.isEmpty())
                                        m_text.setText("데이터를 넣어주세요.");
                                    else
                                        m_text.setText(m_stackForMakingView.toString());
                                }
                            }
                            m_correctAlphaCnt++;
                        }
                    }
                    // 이겼을 경우, 변수 초기화
                    if (m_correctAlphaCnt == m_voca.length()) {
                        Toast.makeText(getApplicationContext(), "축하합니다!", Toast.LENGTH_LONG).show();
                        m_winCnt++;
                        m_characterR = 1;
                        m_characterC = 1;
                        m_totalCnt = 0;
                        m_correctAlphaCnt = 0;
                        m_queueForMakingView.clear();
                        m_stackForMakingView.clear();
                        m_text.setText("데이터를 넣어주세요.");
                        Random randomGenerator = new Random();
                        m_voca = m_mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
                        Log.d("inchang_Voca", m_voca);
                        m_alphabatsMap = fillOutAlphabatsMap(fillOutQueue());


                        // 보류
                        // 이긴 횟수 저장해놔야됨.. 뒤로가기 누르면 m_winCnt 데이터 날라감
                        // 각각 다르게 관리 해야됨.. 초급 영어단어 win cnt / 고급 영어 단어 win cnt / 초급 영어문장 win cnt / 고급 영어문장 win cnt
                        // 로컬에서 하는 거니까 LevelActivity에서 관리하고 ViewActivity로 인텐트로 날려주면 될듯
                        // 이 아이디어 컨펌받으면 실행하기
                        if(m_winCnt > 10) {
                            Toast.makeText(getApplicationContext(), "축하합니다! 다음 level이 열렸어요!", Toast.LENGTH_LONG).show();
                            LevelActivity.advancedVButton.setEnabled(true);
                        }
                        else if(m_winCnt > 20) {
                            Toast.makeText(getApplicationContext(), "축하합니다! 다음 level이 열렸어요!", Toast.LENGTH_LONG).show();
                            LevelActivity.basicSButton.setEnabled(true);
                        }
                        else if(m_winCnt > 30) {
                            Toast.makeText(getApplicationContext(), "축하합니다! 다음 level이 열렸어요!", Toast.LENGTH_LONG).show();
                            LevelActivity.advancedSButton.setEnabled(true);
                        }
                    }
                    break;
                }
            }
        }
    };
}