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

public class UpgradeActivity extends Activity {

    private final static int GAME_STARTED = 1,
                                GAME_FINISHED = 1,
                                GAME_NOT_FINISHED = 2;

    private ImageView m_up, m_down, m_left, m_right, m_repeat, m_get, m_way, m_dingco;
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
       m_queueForMovingTask
       1) 정답인 코딩 블록들을 저장함
       2) MovingTask 클래스로 정답인 코딩 블록들을 매개함
     */
    private ArrayDeque<Arrow> m_queueForMovingTask = new ArrayDeque<>();
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
            m_firstMarginTop = 0;// UI  캐릭터 y축 첫 위치
    private boolean m_repeatAvailable = true,// 반복 블록이 사용 가능한지 아닌지에 대한 플래그
            m_isFinished = false;// 게임이 끝났는지 아닌지에 대한 플래그
    private static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

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
        m_get =(ImageView) findViewById(R.id.get);
        m_back = (Button) findViewById(R.id.back);
        m_clear =(Button) findViewById(R.id.clear);
        m_clear.setText("비우기");
        m_run =(Button) findViewById(R.id.run);
        //m_way =(ImageView) findViewById(R.id.shortest_way);
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
        m_get.setOnTouchListener(touchListener);
        //m_way.setOnTouchListener(touchListener);
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
        init(GAME_STARTED);
        // 알파벳 맵 생성
        generateMap();
        // 캐릭터 생성
        setDingcoStartPos();
    }

    /*
       게임에 셋팅에 필요한 데이터를 초기화한다.
    */
    private void init(int GAME_STATE) {
        // 게임이 시작하거나 끝났을 경우 영단어 정답 알파벳 개수 초기화
        if(GAME_STATE == 1)
            m_correctAlphaCnt = 0;

        // 자료구조 데이터 초기화
        m_stackForCodingStation.clear();
        m_codingStation.setAdapter(m_codingStationAdapter);
        m_queueForCodingBlock.clear();
        m_queueForRepeatNum.clear();
    }

    /*
       알파벳 맵을 생성한다..
    */
    private void generateMap() {
        m_alphabatsMap = getAlphaMap(getAlphaList());
        deleteMap();
        displayMap();
    }

    /*
        알파벳들을 저장한 큐를 반환한다.
    */
    private ArrayList<Character> getAlphaList() {
        int vocaMaxLength = m_mapInfo * m_mapInfo;
        char alphas[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};
        Random randomGenerator = new Random();
        ArrayList<Character> arrayList = new ArrayList<>();

        for (int i = 0; i < vocaMaxLength - 1; i++) {
            // 큐에 영단어 알파벳 저장
            if (i < m_voca.length())
                arrayList.add(m_voca.charAt(i));
            else {
                char rc = alphas[randomGenerator.nextInt(alphas.length)];
                // 영단어가 가지고 있는 알파벳이 포함되지 않을 때 까지 반복
                while (arrayList.contains(rc)) {
                    rc = alphas[randomGenerator.nextInt(alphas.length)];
                }
                // 포함된 알파벳이 아니라면 큐에 저장
                arrayList.add(rc);
            }
        }
        // 시작 지점
        arrayList.add(' ');
        return arrayList;
    }

    /*
       알파벳들을 저장한 큐 가져와 만든 알파벳 맵을 반환한다.
    */
    private char[][] getAlphaMap(ArrayList<Character> arrayList) {
        int i, j;
        Random randomGenerator = new Random();
        char alphabatsMap[][] = new char[m_mapInfo + 2][m_mapInfo + 2];
        for (i = 1; i < m_mapInfo + 1; i++) {
            for (j = 1; j < m_mapInfo + 1; j++) {
                int index = randomGenerator.nextInt(arrayList.size());
                alphabatsMap[i][j] = arrayList.get(index);
                arrayList.remove(index);
                if (alphabatsMap[i][j] == ' ') {
                    // 캐릭터 시작 지점
                    m_firstR = m_currR = i;
                    m_firstC = m_currC = j;
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
                m_dingco.setImageResource(R.drawable.genie_change_choco_s);
                m_moveToMarginLeft = 234;
                m_moveToMarginTop = 114;
                m_firstMarginLeft = 117 + (m_moveToMarginLeft * (m_firstC - 1));
                m_firstMarginTop = 138 + (m_moveToMarginTop * (m_firstR - 1));
                break;
            }
            case 4: {
                m_dingco.setImageResource(R.drawable.genie_change_kiwi_s);
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
                        case R.id.get:
                            // 뷰 인플레트 예외처리
                            try {
                                m_queueForCodingBlock.add(Arrow.valueOf("get"));
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.get)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_repeatAvailable = true;
                            } catch (Exception e) {
                                setToastMessage("블록을 다시 넣어주세요.");
                            }
                            break;
//                        case R.id.shortest_way :
//                            findShortestWay();
//                            break;
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
                         setToastMessage("되돌릴 코딩 블록이 없어요.");
                    }
                    break;
                }
                case R.id.clear: {
                    init(GAME_NOT_FINISHED);
                    setToastMessage("모든 블록을 지웠어요.");
                    break;
                }
                case R.id.run: {
                    int repeatNum = 1;
                    boolean isRepeat = false;
                    if (m_queueForCodingBlock.isEmpty()) {
                        setToastMessage("블록을 넣어주세요.");
                    } else {
                        while (m_queueForCodingBlock.size() > 0) {
                            Arrow arrow = m_queueForCodingBlock.poll();

                            // 코딩 블록이 "줍기"면
                            if (arrow.toString().equals("get")) {

                                // 정답 알파벳을 다 맞춘 이후에도 줍기를 수행하면 오류가 나는 예외 처리
                                try {
                                    if (m_alphabatsMap[m_currR][m_currC] == m_voca.charAt(m_correctAlphaCnt)) {
                                        m_correctAlphaCnt++;
                                        try {
                                            setToastMessage("정답이에요! 다음 알파벳은 " + m_voca.charAt(m_correctAlphaCnt) + " 에요.");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            setToastMessage("정답이에요! 모든 알파벳을 맞췄어요.");
                                        }
                                    } else {
                                        setToastMessage("틀렸어요! 알파벳 " + m_voca.charAt(m_correctAlphaCnt) + "을 찾아봐요.");
                                    }
                                } catch(Exception e) {
                                    m_correctAlphaCnt--;
                                    setToastMessage("너무 많아요! 알파벳 " + m_voca.charAt(m_correctAlphaCnt) + "만 찾아봐요.");
                                    e.printStackTrace();
                                }
                            }

                            // 코딩 블록이 "줍기"가 아니면
                            else {

                                // 코딩 블록이 "반복"이면
                                if (arrow.toString().equals("repeat")) {
                                    isRepeat = true;
                                    repeatNum = m_queueForRepeatNum.poll();

                                    // 코딩 블록이 repeat이 아닐 경우
                                } else {
                                    for (int i = 0; i < repeatNum; i++) {
                                        // 캐릭터 좌표 이동
                                        int nextR = m_currR + arrow.getRow();
                                        int nextC = m_currC + arrow.getCol();
                                        if(nextR < 1 || nextC < 1 || nextR > m_mapInfo || nextC > m_mapInfo) {
                                            setToastMessage("더 이상 갈 수 없어요.");
                                        } else {
                                            m_currR += arrow.getRow();
                                            m_currC += arrow.getCol();
                                            m_queueForMovingTask.add(arrow);
                                        }
                                    }
                                    repeatNum = 1;
                                }
                            }
                        }
                        new MovingTask2(UpgradeActivity.this).execute(new MovingTaskParams(m_queueForMovingTask, m_queueForMovingTask.size()));
                    }
                    // 알파벳을 모두 맞추었으면
                    if (m_correctAlphaCnt == m_voca.length()) {
                        // MovingTask 클래스한테 게임 종료를 알림
                        m_isFinished = true;
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
        init(GAME_FINISHED);
        // (3) 맵 생성
        generateMap();
        // (4) 캐릭터 생성
        setDingcoStartPos();
    }

    private void findShortestWay() {
        int correctAlphaCnt = m_correctAlphaCnt;
        int currR = m_currR;
        int currC = m_currC;
        m_stackForCodingStation.clear();
        m_queueForCodingBlock.clear();
        m_codingStation.setAdapter(m_codingStationAdapter);

        while(correctAlphaCnt != m_voca.length()) {
            for(int i=1; i<m_mapInfo + 1; i++) {
                for(int j=1; j<m_mapInfo + 1; j++) {
                    try {
                        if (m_alphabatsMap[i][j] == m_voca.charAt(correctAlphaCnt)) {
                            int diff;
                            if (currR == i && currC == j) {
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.get)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_queueForCodingBlock.add(Arrow.valueOf("get"));
                            }
                            // 캐릭터의 위치가 알파벳 보다 아래에 있다.
                            if (currR > i) {
                                // 차이만큼 위로
                                diff = currR - i;
                                for (int z = 0; z < diff; z++) {
                                    m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_up)));
                                    m_codingStation.setAdapter(m_codingStationAdapter);
                                    m_queueForCodingBlock.add(Arrow.valueOf("up"));
                                }
                            }
                            // 캐릭터의 위치가 알파벳 보다 위에 있다.
                            if (currR < i) {
                                // 차이만큼 아래로
                                diff = i - currR;
                                for (int z = 0; z < diff; z++) {
                                    m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_down)));
                                    m_codingStation.setAdapter(m_codingStationAdapter);
                                    m_queueForCodingBlock.add(Arrow.valueOf("down"));
                                }
                            }
                            // 캐릭터의 위치가 알파벳 보다 오른쪽에 있다.
                            if (currC > j) {
                                // 차이만큼 왼쪽으로
                                diff = currC - j;
                                for (int z = 0; z < diff; z++) {
                                    m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_left)));
                                    m_codingStation.setAdapter(m_codingStationAdapter);
                                    m_queueForCodingBlock.add(Arrow.valueOf("left"));
                                }
                            }
                            if (currC < j) {
                                // 차이만큼 왼쪽으로
                                diff = j - currC;
                                for (int z = 0; z < diff; z++) {
                                    m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_right)));
                                    m_codingStation.setAdapter(m_codingStationAdapter);
                                    m_queueForCodingBlock.add(Arrow.valueOf("right"));
                                }
                            }
                            m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.get)));
                            m_queueForCodingBlock.add(Arrow.valueOf("get"));
                            m_codingStation.setAdapter(m_codingStationAdapter);
                            correctAlphaCnt++;
                            currR = i;
                            currC = j;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}