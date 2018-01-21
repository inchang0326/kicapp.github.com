package com.example.me.myapplication;

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

public class ViewActivity extends AppCompatActivity {

    private ImageView m_up, m_down, m_left, m_right, m_repeat, m_collect;
    private Button m_back, m_clear, m_complete;
    private TextView m_text;
    private Stack<String> m_stackForMakingView = new Stack<>();
    private Stack<Point> m_stackForMakingMap = new Stack<>();
    private Queue<Character> m_queue = new ArrayDeque<>();
    private String m_voca;
    private char m_alphaMap[][];
    private boolean m_isVisited[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setTitle("Coding View");

        // 1. LevelActivity에서 맵정보(3X3 or 4X4) 받기
        Bundle extras = getIntent().getExtras();
        int mapInfo = extras.getInt("mapInfo");

        int i, j, vocaMaxLength = mapInfo * mapInfo;
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        m_alphaMap = new char[mapInfo + 2][mapInfo + 2];
        m_isVisited = new boolean[mapInfo + 2][mapInfo + 2];

        // 2. 맵정보(3X3 or 4X4)에 따라 영단어 랜덤 출력
        Random randomGenerator = new Random();
        m_voca = mapInfo > 3 ? AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.numberOf())).toString()
                : BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.numberOf())).toString();
        Log.d("inchang_voca", m_voca);

        // 3. 맵정보(3X3 or 4X4)에 따라 큐에 알파벳(Point) 채우기
        for (i = 0; i < vocaMaxLength - 1; i++) {
            // (1) 큐에 영단어 알파벳 저장
            if (i < m_voca.length())
                m_queue.add(m_voca.charAt(i));
            else {
                char rc = alphas[randomGenerator.nextInt(alphas.length)];

                // (2) 영단어가 가지고 있는 알파벳이 포함되지 않을 때 까지 반복
                while (m_queue.contains(rc)) {
                    rc = alphas[randomGenerator.nextInt(alphas.length)];
                }

                // (3) 포함된 알파벳이 아니라면 큐에 저장
                m_queue.add(rc);
            }
        }

        for (i = 1; i < mapInfo + 1; i++) {
            for (j = 1; j < mapInfo + 1; j++) {
                m_isVisited[i][j] = true;
            }
        }

        int currR = 1, currC = 1;
        m_alphaMap[currR][currC] = '@';
        m_isVisited[currR][currC] = false;
        Arrow arrow;
        boolean flag = false;

        // 4. 알파벳 맵 만들기
        // (1) 큐에 있는 알파벳이 모두 사라질 때까지 반복
        while (m_queue.size() > 0) {

            arrow = Arrow.valueOf(randomGenerator.nextInt(4));
            // Log.d("inchang_arrow", arrow.toString());

            // (2) 다음 갈 방향 랜덤 지정
            int nextR = currR + arrow.getRow();
            int nextC = currC + arrow.getCol();

            // (3) 방문하지 않았던(true) 곳이라면 방문(false)하고 스택에 저장
            if (m_isVisited[nextR][nextC] == true) {
                m_alphaMap[nextR][nextC] = m_queue.poll();
                m_isVisited[nextR][nextC] = false;
                m_stackForMakingMap.add(new Point(m_alphaMap[nextR][nextC], nextR, nextC));
                currR = nextR;
                currC = nextC;
                // Log.d("inchang_alphaMap", Character.toString(m_alphaMap[nextR][nextC]));
                // Log.d("inchang_alphaMap", Boolean.toString(m_isVisited[nextR][nextC]));
                // Log.d("inchang_arrow", String.valueOf(currR) + " " + String.valueOf(currC));
            } else {// (4) 방문했던 곳이라면(false) 현재 위치(currR, currC)에서 4방면을 확인
                flag = false;
                for (i = 0; i < 4; i++) {// 위, 아래, 왼쪽, 오른쪽
                    arrow = Arrow.valueOf(i);
                    nextR = currR + arrow.getRow();
                    nextC = currC + arrow.getCol();
                    if (m_isVisited[nextR][nextC] == true) {
                        //Log.d("inchang_exception", "? - 1");

                        // (5) 현재 위치에서 주변에 하나라도 갈 곳이 있으면 flag를 true로 설정
                        flag = true;
                    }
                }
                // (6) 한 곳으로라도 갈 곳이 없다면 이전 위치를 스택에서 pop
                if (flag == false) {
                    // Log.d("inchang_exception", "? - 2");
                    // Log.d("inchang_exception", String.valueOf(currR) + " " + String.valueOf(currC));
                    Point p = m_stackForMakingMap.pop();
                    // Log.d("inchang_exception", Character.toString(p.getAlpha()));

                    // (7) 현재의 위치를 이전 포인트의 위치로 변경
                    currR = p.getRow();
                    currC = p.getCol();
                }
            }
        }

        for (i = 1; i < mapInfo + 1; i++) {
            for (j = 1; j < mapInfo + 1; j++) {
                Log.d("inchang_mapCheck", Character.toString(m_alphaMap[i][j]) + " ");
            }
            Log.d("inchang_mapCheck", "enter");
        }

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
                            m_stackForMakingView.push("up");
                            break;
                        case R.id.down:
                            m_stackForMakingView.push("down");
                            break;
                        case R.id.left:
                            m_stackForMakingView.push("left");
                            break;
                        case R.id.right:
                            m_stackForMakingView.push("right");
                            break;
                        case R.id.repeat:
                            m_stackForMakingView.push("repeat");
                            break;
                        case R.id.collect:
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
                    m_text.setText("데이터를 넣어주세요.");
                    Toast.makeText(getApplicationContext(), "모든 데이터를 삭제했습니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                case R.id.complete: {
                    if (m_stackForMakingView.isEmpty())
                        Toast.makeText(getApplicationContext(), "입력된 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                    else {
                        // 애니메이션 추가
                    }
                    break;
                }
            }
        }
    };
}