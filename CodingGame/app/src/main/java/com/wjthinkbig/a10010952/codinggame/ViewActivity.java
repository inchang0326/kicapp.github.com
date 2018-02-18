package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.media.MediaPlayer;
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

import com.wjthinkbig.a10010952.R;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class ViewActivity extends Activity {

    private final static int CLEAR_BTN_CLICKED = 1,
            CLEAR_BTN_NOT_CLICKED = 0;

    private ImageView m_up, m_down, m_left, m_right, m_repeat, m_dingco;
    private Button m_back, m_clear, m_run;
    private EditText m_repeatNum;
    private Toast m_toast;
    private TextView m_toastTextView;
    private GridView m_codingStation;// 코딩 작업창 뷰
    private GridViewAdapter m_codingStationAdapter;// 코딩 작업창 뷰 어댑터
    private TableLayout m_alphaMapContainer;// 알파벳 맵 컨테이너
    private RelativeLayout m_dingcoContainer;// 캐릭터 컨테이너
    private RelativeLayout.LayoutParams m_rlp;// 캐릭터 컨테이너 파라미터 정보

    /*
       m_queueForCodingBlock
       1) 코딩 블록을 입력 순서대로 저장함
       2) 마지막으로 입력된 코딩블록이 반복인지 아닌지에 대한 플래그
     */
    private ArrayDeque<Arrow> m_queueForCodingBlock = new ArrayDeque<>();
    /*
       m_stackForCodingStation
       1) 코딩 작업창에 코딩 블록을 채움
       2) back 버튼 클릭 시, 코딩 작업창에서 가장 최근에 입력된 코딩 블록을 제거함
     */
    private Stack<GridViewItem> m_stackForCodingStation = new Stack<>();
    /*
       m_queueForMvoingTask
       1) 정답인 코딩 블록들을 저장함
       2) MovingTask 클래스로 정답인 코딩 블록들을 매개함
     */
    private ArrayDeque<Arrow> m_queueForMovingTask = new ArrayDeque<>();
    /* m_stackForAnswers
       1) 정답인 코딩 블록들을 저장함
       2) 코딩 작업창에 코딩 블록이 없을 때, back 버튼 클릭 시, 가장 최근 정답인 코딩 블록을 제거함
     */
    private ArrayDeque<Arrow> m_queueForAnswers = new ArrayDeque<>();
    /*
       m_queueForRepeatNum
       : 코딩 작업창에 있는 반복 블록의 반복 수들을 저장함
     */
    private Queue<Integer> m_queueForRepeatNum = new ArrayDeque<>();
    private String m_voca;// 영단어
    private int m_mapInfo = 0,// 맵 정보
            m_firstR,// 캐릭터 최초 Row 위치
            m_firstC,// 캐릭터 최초 Col 위치
            m_currR,// 캐릭터 현재 Row 위치
            m_currC,// 캐릭터 현재 Col 위치
            m_correctAlphaCnt;// 맞은 알파벳 개수
    private char m_alphabatsMap[][];// 알파벳 맵
    private int m_moveToMarginLeft = 0,// UI x축 이동량
            m_moveToMarginTop = 0,// UI y축 이동량
            m_firstMarginLeft = 0,// UI 캐릭터 x축 첫 위치
            m_firstMarginTop = 0,// UI  캐릭터 y축 첫 위치
            m_basicWinCnt = 0,// 기본 영단어 이긴 횟수
            m_advancedWinCnt = 0;// 심화 영단어 이긴 횟수
    private boolean m_repeatAvailable = true,// 반복 블록이 사용 가능한지 아닌지에 대한 플래그
            m_backAvailable = true,// back 버튼이 사용 가능한지 아닌지에 대한 플래그
            m_isFinished = false;// 게임이 끝났는지 아닌지에 대한 플래그
    private static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // 음악 재생
        mp = MediaPlayer.create(this, R.raw.bgm);
        mp.setLooping(true);
        mp.start();
        mp.setVolume(0.2f, 0.2f);

        // 영단어 받기
        Bundle extras = getIntent().getExtras();
        m_voca = extras.getString("codinggame");

        // 영단어가 어느 보카 클래스에 포함되는지에 따라 맵 정보 초기화
        if(BasicVoca.contain(m_voca)) {
            m_mapInfo = 3;
        } else if(AdvancedVoca.contain(m_voca)) {
            m_mapInfo = 4;
        } else if(Sentence.contain(m_voca)) {
            m_mapInfo = 5;
        }

        // 위젯 모음
        m_up = (ImageView) findViewById(R.id.up);
        m_down =(ImageView) findViewById(R.id.down);
        m_left = (ImageView)findViewById(R.id.left);
        m_right =(ImageView) findViewById(R.id.right);
        m_repeat =(ImageView) findViewById(R.id.repeat);
        m_back = (Button) findViewById(R.id.back);
        m_clear =(Button) findViewById(R.id.clear);
        m_run =(Button) findViewById(R.id.run);
        m_codingStation = (GridView) findViewById(R.id.codingStation);
        m_codingStationAdapter = new GridViewAdapter(getApplicationContext(), R.layout.gridview_item, m_stackForCodingStation);
        m_toast = Toast.makeText(getApplicationContext(), "No message", Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) m_toast.getView();
        m_toastTextView = (TextView) group.getChildAt(0);
        m_toastTextView.setTextColor(Color.WHITE);
        m_toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 38);
        m_dingco = (ImageView) findViewById(R.id.dingco);

        // 레이아웃 모음
        m_alphaMapContainer = (TableLayout) findViewById(R.id.tableLayout);
        m_dingcoContainer = (RelativeLayout) findViewById(R.id.dingcocontainer);
        m_rlp = new RelativeLayout.LayoutParams(150, 150);

        // 위젯 이벤트 등록
        m_up.setOnTouchListener(touchListener);
        m_down.setOnTouchListener(touchListener);
        m_left.setOnTouchListener(touchListener);
        m_right.setOnTouchListener(touchListener);
        m_back.setOnClickListener(clickListener);
        m_clear.setOnClickListener(clickListener);
        m_run.setOnClickListener(clickListener);
        m_codingStation.setOnDragListener(dragListener);

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

        // 게임 정보 초기화
        init(CLEAR_BTN_NOT_CLICKED);
        // 알파벳 맵 생성
        generateMap();
        // 캐릭터 생성
        setDingcoStartPos();
    }

    /*
       게임에 셋팅에 필요한 데이터를 초기화한다.
    */
    private void init(int isClear) {
        switch (isClear) {
            // CLEAR_BTN_NOT_CLICKED일 경우 캐릭터 최초 위치를 랜덤으로 받는다.
            case CLEAR_BTN_NOT_CLICKED: {
                int loc;
                Random randomGenerator = new Random();
                loc = randomGenerator.nextInt(m_mapInfo) + 1; m_currR = loc; m_firstR = m_currR;
                loc = randomGenerator.nextInt(m_mapInfo) + 1; m_currC = loc; m_firstC = m_currC;
                break;
            }
            // CLEAR_BTN_CLICKED일 경우 캐릭터 위치를 처음으로 되돌린다.
            case CLEAR_BTN_CLICKED: {
                m_currR = m_firstR;
                m_currC = m_firstC;
                break;
            }
        }

        // 영단어 정답 알파벳 개수 초기화
        m_correctAlphaCnt = 0;

        // 자료구조 데이터 초기화
        m_stackForCodingStation.clear();
        m_codingStation.setAdapter(m_codingStationAdapter);
        m_queueForCodingBlock.clear();
        m_queueForAnswers.clear();
        m_queueForRepeatNum.clear();
    }

    /*
       알파벳 맵을 생성한다..
    */
    private void generateMap() {
        m_alphabatsMap = getAlphaMap(getAlphaQueue());
        deleteMap();
        displayMap();
    }

    /*
       알파벳들을 저장한 큐를 반환한다.
    */
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

                // 영단어가 가지고 있는 알파벳이 포함되지 않을 때 까지 반복
                while (queue.contains(rc)) {
                    rc = alphas[randomGenerator.nextInt(alphas.length)];
                }

                // 포함된 알파벳이 아니라면 큐에 저장
                queue.add(rc);
            }
        }
        return queue;
    }

    /*
       알파벳들을 저장한 큐 가져와 만든 알파벳 맵을 반환한다.
    */
    private char[][] getAlphaMap(ArrayDeque<Character> queue) {
        int currR = m_currR, currC = m_currC, vocaMaxLength = m_mapInfo * m_mapInfo - 1;
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
        ArrayList<Point> arrayList = new ArrayList<>();

        // (1) 큐에 있는 알파벳이 모두 사라질 때까지 반복
        while (queue.size() > 0) {
            Random randomGenerator = new Random();
            Arrow arrow;
            int nextR, nextC;

            // (2) 현재 위치에서 방문 가능한 곳을 저장
            flag = false;
            for (int i = 0; i < 4; i++) {
                // 위, 아래, 왼쪽, 오른쪽
                arrow = Arrow.valueOf(i);
                nextR = currR + arrow.getRow();
                nextC = currC + arrow.getCol();
                if (isVisited[nextR][nextC]) {
                    flag = true;
                    arrayList.add(new Point(arrow.getRow(), arrow.getCol()));
                }
            }

            if(flag) {
                // (3) 방문 가능한 곳 중 한 곳을 랜덤으로 입력 받음
                Point point = arrayList.get(randomGenerator.nextInt(arrayList.size()));
                nextR = currR + point.getRow();
                nextC = currC + point.getCol();

                alphabatsMap[nextR][nextC] = queue.poll();
                isVisited[nextR][nextC] = false;
                stack.add(new Point(nextR, nextC));
                currR = nextR;
                currC = nextC;

                arrayList.clear();
            }

            // (4) 길이 막혔을 때
            else {
                // (5) 주어진 단어의 알파벳이 모두 채워지지 않았으면, 맵 재배치
                if (queue.size() > (vocaMaxLength - m_voca.length())) {
                    stack.clear();
                    queue = getAlphaQueue();
                    for (int i = 1; i < m_mapInfo + 1; i++) {
                        for (int j = 1; j < m_mapInfo + 1; j++) {
                            isVisited[i][j] = true;
                        }
                    }
                    currR = m_currR;
                    currC = m_currC;
                    alphabatsMap[currR][currC] = ' ';
                    isVisited[currR][currC] = false;
                }
                // (6) 주어진 단어의 알파벳이 모두 채워졌으면, 이전 위치로 이동
                else {
                    Point p = stack.pop();
                    currR = p.getRow();
                    currC = p.getCol();
                }
            }
        }

        return alphabatsMap;
    }

    /*
       알파벳 맵 UI를 생성한다.
    */
    private void displayMap() {
        // 영단어 뷰
        TextView text = (TextView) findViewById(R.id.text);
        if(m_mapInfo == 5) {
            text.setText(Sentence.valueOf(m_voca).expression());
        } else {
            text.setText(m_voca);
        }

        // 알파벳 맵 뷰
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
            m_alphaMapContainer.setStretchAllColumns(true);
            if (i != m_mapInfo) {
                tlp.setMargins(0, 0, 12, 0);
                m_alphaMapContainer.addView(tableRow, tlp);
            } else {
                tlp.setMargins(0, 0, 12, 12);
                m_alphaMapContainer.addView(tableRow, tlp);
            }
        }
    }

    /*
       알파벳 맵 UI를 제거한다.
    */
    private void deleteMap() {
        m_alphaMapContainer.removeAllViews();
    }

    /*
       캐릭터를 시작 위치로 생성한다.
    */
    public void setDingcoStartPos() {
        m_dingcoContainer.removeAllViews();
        switch (m_mapInfo) {
            case 3: {
                m_dingco.setImageResource(R.drawable.genie_change_apple_s);
                m_moveToMarginLeft = 234;
                m_moveToMarginTop = 114;
                m_firstMarginLeft = 117 + (m_moveToMarginLeft * (m_firstC - 1));
                m_firstMarginTop = 138 + (m_moveToMarginTop * (m_firstR - 1));
                break;
            }
            case 4: {
                m_dingco.setImageResource(R.drawable.genie_change_berry_s);
                m_moveToMarginLeft = 177;
                m_moveToMarginTop = 114;
                m_firstMarginLeft = 88 + (m_moveToMarginLeft * (m_firstC - 1));
                m_firstMarginTop = 82 + (m_moveToMarginTop * (m_firstR - 1));
                break;
            }
            case 5: {
                m_dingco.setImageResource(R.drawable.genie_change_bana_s);
                m_moveToMarginLeft = 142;
                m_moveToMarginTop = 114;
                m_firstMarginLeft = 70 + (m_moveToMarginLeft * (m_firstC - 1));
                m_firstMarginTop = 25 + (m_moveToMarginTop * (m_firstR - 1));
                break;
            }
        }
        m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
        m_dingcoContainer.addView(m_dingco, m_rlp);
    }

    /*
       캐릭터의 실시간 위치를 정한다.
    */
    public void setDingcoLastPos(int left, int top) {
        m_dingcoContainer.removeAllViews();
        m_rlp.setMargins(left, top, 0, 0);
        m_dingcoContainer.addView(m_dingco, m_rlp);
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
                            // 뷰 인플레트 예외처리
                            try {
                                m_queueForCodingBlock.add(Arrow.valueOf("up"));
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_up)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_repeatAvailable = true;
                            } catch (Exception e) {
                                setToastMessage("블록을 다시 넣어주세요.");
                            }
                            break;
                        case R.id.down:
                            // 뷰 인플레트 예외처리
                            try {
                                m_queueForCodingBlock.add(Arrow.valueOf("down"));
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_down)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_repeatAvailable = true;
                            } catch (Exception e) {
                                setToastMessage("블록을 다시 넣어주세요.");
                            }
                            break;
                        case R.id.left:
                            // 뷰 인플레트 예외처리
                            try {
                                m_queueForCodingBlock.add(Arrow.valueOf("left"));
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_left)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_repeatAvailable = true;
                            } catch (Exception e) {
                                setToastMessage("블록을 다시 넣어주세요.");
                            }
                            break;
                        case R.id.right:
                            // 뷰 인플레트 예외처리
                            try {
                                m_queueForCodingBlock.add(Arrow.valueOf("right"));
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_right)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_repeatAvailable = true;
                            } catch (Exception e) {
                                setToastMessage("블록을 다시 넣어주세요.");
                            }
                            break;
                        case R.id.repeat:
                            // repeat 블록이 연속으로 입력되는 예외처리
                            if (m_repeatAvailable) {
                                // 반복 수
                                String chk = m_repeatNum.getText().toString();
                                // 숫자가 아닌 값 예외처리
                                try {
                                    int value = Integer.parseInt(chk);
                                    if (value > 1 && value < 9) {
                                        // 뷰 인플레트 예외처리
                                        try {
                                            m_queueForRepeatNum.add(value);
                                            m_queueForCodingBlock.add(Arrow.valueOf("repeat"));
                                            m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.repeat)));
                                            m_codingStation.setAdapter(m_codingStationAdapter);
                                            m_repeatAvailable = false;
                                        } catch (Exception e) {
                                            setToastMessage("블록을 다시 넣어주세요.");
                                        }
                                    } else {
                                        setToastMessage("반복은 2 ~ 9 사이의 숫자를 넣어주세요.");
                                    }
                                } catch (NumberFormatException e) {
                                    setToastMessage("숫자를 넣어주세요.");
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
            m_repeatAvailable = true;
            switch (v.getId()) {
                case R.id.back: {// 이전 블록 지우기
                    if (!m_queueForCodingBlock.isEmpty()) {
                        m_stackForCodingStation.pop();
                        m_codingStation.setAdapter(m_codingStationAdapter);
                        if (m_queueForCodingBlock.pollLast().toString().equals("repeat")) {
                            m_queueForRepeatNum.poll();
                        }
                    } else {
                        if (!m_queueForAnswers.isEmpty()) {
                            if(m_backAvailable) {
                                m_backAvailable = false;
                                Arrow arrow = m_queueForAnswers.pollLast();
                                m_currR -= arrow.getRow();
                                m_currC -= arrow.getCol();
                                m_correctAlphaCnt--;
                                m_stackForCodingStation.pop();
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                new MovingBackTask(ViewActivity.this).execute(arrow);
                            }
                        } else {
                            setToastMessage("되돌릴 코딩 블록이 없어요.");
                        }
                    }
                    break;
                }
                case R.id.clear: {
                    init(CLEAR_BTN_CLICKED);
                    setDingcoStartPos();
                    setToastMessage("모든 블록을 지웠어요.");
                    break;
                }
                case R.id.run: {
                    // 입력된 코딩 블록이 없다면
                    if (m_queueForCodingBlock.isEmpty()) {
                        setToastMessage("블록을 넣어주세요.");

                        // 입력된 코딩블록이 있다면
                    } else {
                        // run 버튼 클릭 시, clear와 back 버튼을 통해 AsyncTask 인스턴스가 겹치는 것을 방지함
                        m_back.setBackgroundColor(Color.parseColor("#707B7C"));
                        m_clear.setBackgroundColor(Color.parseColor("#707B7C"));
                        m_back.setEnabled(false);
                        m_clear.setEnabled(false);
                        // 마지막에 반복 블록이 입력되고 run 버튼을 클릭하여 반복 수가 그대로 남는 것을 방지함
                        int repeatNum = 1;
                        int correctRepeatCnt = 0;
                        boolean isRepeat = false, isFailed = false;

                        // (1) 큐에 입력된 코딩 블록의 수 만큼 반복
                        while (m_queueForCodingBlock.size() > 0) {
                            Arrow arrow = m_queueForCodingBlock.poll();

                            // (2) 코딩블록이 repeat일 경우
                            if (arrow.toString().equals("repeat")) {

                                // 바운드를 넘어서 repeat 블록이 들어와서 맞았을 경우 예외처리
                                if(m_correctAlphaCnt == m_voca.length()) {
                                    setToastMessage("다시 시도해봐요.");
                                    m_correctAlphaCnt--;
                                    Arrow temp = m_queueForAnswers.pollLast();
                                    m_currR -= temp.getRow();
                                    m_currC -= temp.getCol();
                                    m_queueForMovingTask.pollLast();
                                }
                                isRepeat = true;
                                repeatNum = m_queueForRepeatNum.poll();

                                // (3) 코딩블록이 repeat이 아닐 경우
                            } else {
                                for (int i = 0; i < repeatNum; i++) {

                                    // (4) 캐릭터 좌표 이동
                                    m_currR += arrow.getRow();
                                    m_currC += arrow.getCol();

                                    // 아웃오브인덱스 예외처리
                                    try {
                                        // (5-1) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함되지 않는다면
                                        if (m_alphabatsMap[m_currR][m_currC] != m_voca.charAt(m_correctAlphaCnt)) {
                                            setToastMessage("다시 시도해봐요.");
                                            isFailed = true;

                                            // (5-2) 이전 좌표로 캐릭터 이동
                                            m_currR -= arrow.getRow();
                                            m_currC -= arrow.getCol();

                                            // (5-3) 정답이 아닌 코딩 블록 이후 큐(코딩블록) 비우기
                                            while (!m_queueForCodingBlock.isEmpty()) {
                                                m_queueForCodingBlock.poll();
                                            }
                                            // (5-4) 정답이 아닌 코딩 블록 이후 큐(반복수) 비우기
                                            while (!m_queueForRepeatNum.isEmpty()) {
                                                m_queueForRepeatNum.poll();
                                            }
                                            // (6) 캐릭터가 이동한 자리에 있는 알파벳이 영단어에 포함된다면
                                        } else {
                                            m_queueForAnswers.addLast(arrow);
                                            m_correctAlphaCnt++;
                                            correctRepeatCnt++;
                                        }
                                    } catch (Exception e) {
                                        // 반복 시, 아웃오브인덱스 예외처리
                                        for (int j = 0; j < correctRepeatCnt; j++) {
                                            m_currR -= arrow.getRow();
                                            m_currC -= arrow.getCol();
                                            m_correctAlphaCnt--;
                                            m_queueForAnswers.pollLast();
                                        }
                                        // 예외가 발생한 지점을 추가하기 때문에 한번 더 빼줘야 함
                                        m_currR -= arrow.getRow();
                                        m_currC -= arrow.getCol();
                                        setToastMessage("다시 시도해봐요.");
                                        isFailed = true;
                                        correctRepeatCnt = 0;
                                        // 정답이 아닌 코딩 블록 이후 큐(코딩블록) 비우기
                                        while (!m_queueForCodingBlock.isEmpty()) {
                                            m_queueForCodingBlock.poll();
                                        }
                                        // 정답이 아닌 코딩 블록 이후 큐(리핏넘) 비우기
                                        while (!m_queueForRepeatNum.isEmpty()) {
                                            m_queueForRepeatNum.poll();
                                        }
                                        // 반복이 아닌데 바운드를 넘어서 틀린 경우 예외처리
                                        if (!isRepeat) {
                                            setToastMessage("다시 시도해봐요.");
                                            m_correctAlphaCnt--;
                                            Arrow temp = m_queueForAnswers.pollLast();
                                            m_currR -= temp.getRow();
                                            m_currC -= temp.getCol();
                                            m_queueForMovingTask.pollLast();
                                        }
                                        break;
                                    }
                                }
                                // (7) 반복 중에 알파벳이 틀렸을 경우
                                if (isRepeat && isFailed) {
                                    // 반복O, 실패0 반복 중에 맞은 개수 만큼 캐릭터 이동
                                    for (int i = 0; i < correctRepeatCnt; i++) {
                                        m_currR -= arrow.getRow();
                                        m_currC -= arrow.getCol();
                                        m_correctAlphaCnt--;
                                        m_queueForAnswers.pollLast();
                                        setToastMessage("다시 시도해봐요.");
                                    }
                                    // (8) 반복X, 실패X 캐릭터 이동
                                } else if (!isRepeat && !isFailed) {
                                    m_queueForMovingTask.add(arrow);
                                    // (9) 반복O, 실패X 반복 수 만큼 캐릭터 이동
                                } else if (isRepeat && !isFailed) {
                                    for (int i = 0; i < repeatNum; i++) {
                                        m_queueForMovingTask.add(arrow);
                                    }
                                }
                                // (10) 값 초기화
                                repeatNum = 1;
                                isRepeat = false;
                                correctRepeatCnt = 0;
                                isFailed = false;
                            }
                        }
                        // (11) UI 작업
                        new MovingTask(ViewActivity.this).execute(new MovingTaskParams(m_queueForMovingTask, m_queueForMovingTask.size()));
                    }

                    // 영어단어를 맞췄을 경우
                    if (m_correctAlphaCnt == m_voca.length()) {
                        // MovingTask 클래스한테 게임 종료를 알림
                        m_isFinished = true;
                    }

                    // 정답인 코딩 블록만 코딩 작업창에 남기기
                    m_stackForCodingStation.clear();
                    m_codingStation.setAdapter(m_codingStationAdapter);
                    ArrayDeque<Arrow> queue = m_queueForAnswers.clone();
                    while(!queue.isEmpty()) {
                        m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(queue.pollFirst().getDrawable())));
                        m_codingStation.setAdapter(m_codingStationAdapter);
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp.stop();
    }

    public void setToastMessage(String message) {
        m_toastTextView.setText(message);
        m_toast.show();
    }

    public RelativeLayout.LayoutParams getLayoutParams() {
        return m_rlp;
    }

    public int getMoveToMarginLeft() {
        return m_moveToMarginLeft;
    }

    public int getMoveToMarginTop() {
        return m_moveToMarginTop;
    }

    public void setBackState(boolean back) {
        m_backAvailable = back;
    }

    public boolean getIsFinished() {
        return m_isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        m_isFinished = isFinished;
    }

    public Button getBackBtn() {
        return m_back;
    }

    public Button getClearBtn() {
        return m_clear;
    }

    public String getVoca() {
        return m_voca;
    }

    /*
       게임을 리셋한다.
    */
    public void resetGame() {
        // (1) 영단어 랜덤 출력
        Random randomGenerator = new Random();
        switch(m_mapInfo) {
            case 3 : {
                m_voca = BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.sizeOf())).toString();
                break;
            }
            case 4 : {
                m_voca = AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.sizeOf())).toString();
                break;
            }
            case 5 : {
                m_voca = Sentence.valueOf(randomGenerator.nextInt(Sentence.sizeOf())).toString();
                break;
            }
        }
        // (2) 게임 정보 초기화
        init(CLEAR_BTN_NOT_CLICKED);
        // (3) 맵 생성
        generateMap();
        // (4) 캐릭터 생성
        setDingcoStartPos();
    }
}