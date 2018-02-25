/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wjthinkbig.a10010952.btproject.fragments;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wjthinkbig.a10010952.R;
import com.wjthinkbig.a10010952.btproject.MainActivity;
import com.wjthinkbig.a10010952.codinggame.GridViewAdapter;
import com.wjthinkbig.a10010952.codinggame.GridViewItem;

import java.util.ArrayDeque;
import java.util.Random;
import java.util.Stack;

public class ExampleFragment extends Fragment {

    private String[] m_questions = {"오른쪽으로 돌고 앞으로가서 경적을 울리세요.",
                                        "앞으로 두 번 가서 초록 불을 켜세요.",
                                        "뒤로 가서 빨간 불을 켜세요."};
    private String[] m_answerCodings = {"오른쪽\n앞\n경적\n",
                                            "앞\n앞\n초록\n",
                                            "뒤\n빨강\n"};
    private String m_question;
    private String m_answerCoding;
    private ImageView m_forward, m_backward, m_turnL, m_turnR, m_red, m_yellow, m_green, m_blue, m_turnOff, m_horn;
    private Button m_run, m_back, m_clear;
    private ToggleButton m_toggle;
    private TextView m_textView;
    private GridView m_codingStation;
    private GridViewAdapter m_codingStationAdapter;
    /*
       m_stackForCodingStation
       1) 코딩 작업창에 코딩 블록을 채움
       2) back 버튼 클릭 시, 코딩 작업창에서 가장 최근에 입력된 코딩 블록을 제거함
     */
    private Stack<GridViewItem> m_stackForCodingStation = new Stack<>();
    private ArrayDeque<String> m_codingBlocks = new ArrayDeque<>();

    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;
    private Handler mActivityHandler = null;

    private MainActivity m_ma;

    public ExampleFragment(Context c, IFragmentListener l, Handler h) {
        mContext = c;
        mFragmentListener = l;
        mActivityHandler = h;
        m_ma = (MainActivity) mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);

        Random randomGenerator = new Random();
        int temp = randomGenerator.nextInt(3);
        m_question = m_questions[temp];
        m_answerCoding = m_answerCodings[temp];

        m_forward = (ImageView) rootView.findViewById(R.id.forward);
        m_backward = (ImageView) rootView.findViewById(R.id.backward);
        m_turnL = (ImageView) rootView.findViewById(R.id.turn_left);
        m_turnR = (ImageView) rootView.findViewById(R.id.turn_right);
        m_red = (ImageView) rootView.findViewById(R.id.red_led);
        m_yellow = (ImageView) rootView.findViewById(R.id.yellow_led);
        m_green = (ImageView) rootView.findViewById(R.id.green_led);
        m_blue = (ImageView) rootView.findViewById(R.id.blue_led);
        m_blue.setVisibility(View.GONE);
        m_turnOff = (ImageView) rootView.findViewById(R.id.turn_off);
        m_horn = (ImageView) rootView.findViewById(R.id.horn);
        m_run = (Button) rootView.findViewById(R.id.run);
        m_back = (Button) rootView.findViewById(R.id.back);
        m_clear = (Button) rootView.findViewById(R.id.clear);
        m_toggle = (ToggleButton) rootView.findViewById(R.id.question);
        m_textView = (TextView) rootView.findViewById(R.id.text);
        m_textView.setText("");

        m_codingStation = (GridView) rootView.findViewById(R.id.codingStation);
        m_codingStationAdapter = new GridViewAdapter(mContext.getApplicationContext(), R.layout.gridview_item_freecoding, m_stackForCodingStation);

        m_forward.setOnTouchListener(touchListener);
        m_backward.setOnTouchListener(touchListener);
        m_turnL.setOnTouchListener(touchListener);
        m_turnR.setOnTouchListener(touchListener);
        m_red.setOnTouchListener(touchListener);
        m_yellow.setOnTouchListener(touchListener);
        m_green.setOnTouchListener(touchListener);
        m_blue.setOnTouchListener(touchListener);
        m_turnOff.setOnTouchListener(touchListener);
        m_horn.setOnTouchListener(touchListener);
        m_run.setOnClickListener(clickListener);
        m_back.setOnClickListener(clickListener);
        m_clear.setOnClickListener(clickListener);
        m_toggle.setOnClickListener(clickListener);
        m_codingStation.setOnDragListener(dragListener);

        return rootView;
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
                        case R.id.forward:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("앞\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_up)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.backward:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("뒤\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_down)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.turn_left:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("왼쪽\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_left)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.turn_right:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("오른쪽\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.codinggame_right)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.red_led:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("빨강\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.red_led)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.yellow_led:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("노랑\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.yellow_led)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.green_led:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("초록\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.green_led)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.blue_led:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("파랑\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.red_led)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.turn_off:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("끄기\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.stop)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case R.id.horn:
                            // 뷰 인플레트 예외처리
                            try {
                                m_codingBlocks.add("경적\n");
                                m_stackForCodingStation.add(new GridViewItem(getResources().getDrawable(R.drawable.horn)));
                                m_codingStation.setAdapter(m_codingStationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
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
            switch (v.getId()) {
                case R.id.run:
                    if(m_codingBlocks.isEmpty()) {
                        m_ma.setToastMessage("코딩 블록을 넣어주세요.");
                    }
                    else {
                        String message = "";
                        while (m_codingBlocks.size() > 0) {
                            String temp = m_codingBlocks.pollFirst();
                            message += temp;
                        }
                        Log.d("inchang", message);
                        if (m_toggle.isChecked()) {
                            if (message.equals(m_answerCoding)) {
                                if (message != null && message.length() > 0)
                                    sendMessage(message);
                                m_codingBlocks.clear();
                                m_stackForCodingStation.clear();
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                Random randomGenerator = new Random();
                                int temp = randomGenerator.nextInt(3);
                                m_question = m_questions[temp];
                                m_answerCoding = m_answerCodings[temp];
                                m_textView.setText(m_question);
                                m_ma.setToastMessage("축하해요!!");
                            } else {
                                m_codingBlocks.clear();
                                m_stackForCodingStation.clear();
                                m_codingStation.setAdapter(m_codingStationAdapter);
                                m_ma.setToastMessage("다시 시도해봐요.");
                            }
                        } else {
                            if (message != null && message.length() > 0)
                                sendMessage(message);
                            m_codingBlocks.clear();
                            m_stackForCodingStation.clear();
                            m_codingStation.setAdapter(m_codingStationAdapter);
                        }
                    }
                    break;
                case R.id.back:
                    if (!m_codingBlocks.isEmpty()) {
                        m_codingBlocks.pollLast();
                        m_stackForCodingStation.pop();
                        m_codingStation.setAdapter(m_codingStationAdapter);
                    } else{
                        m_ma.setToastMessage("되돌릴 코딩 블록이 없어요.");
                    }
                    break;
                case R.id.clear:
                    if(m_codingBlocks.isEmpty()) {
                        m_ma.setToastMessage("지울 코딩 블록이 없어요.");
                    } else {
                        m_codingBlocks.clear();
                        m_stackForCodingStation.clear();
                        m_codingStation.setAdapter(m_codingStationAdapter);
                    }
                    break;
                case R.id.question:
                    if(m_toggle.isChecked()) {
                        Random randomGenerator = new Random();
                        int temp = randomGenerator.nextInt(3);
                        m_question = m_questions[temp];
                        m_answerCoding = m_answerCodings[temp];
                        m_textView.setText(m_question);
                        m_textView.setText(m_question);
                    }
                    else m_textView.setText("");
            }
        }
    };

    // Sends user message to remote
    private void sendMessage(String message) {
        if (message == null || message.length() < 1)
            return;
        // send to remote
        if (mFragmentListener != null)
            mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null, null);
        else
            return;
    }
}
