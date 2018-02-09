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

package com.example.a10010952.btproject.fragments;

import com.example.a10010952.btproject.utils.AppSettings;
import com.hardcopy.btchat.R;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ControlFragment extends Fragment {

    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;

    private ImageButton mUpBtn = null, mDownBtn = null, mLeftBtn = null, mRightBtn = null, mStopBtn = null,
                    mRedLedBtn = null, mYellowLedBtn = null, mGreenLedBtn = null, mHornBtn = null;

    public ControlFragment(Context c, IFragmentListener l) {
        mContext = c;
        mFragmentListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppSettings.initializeAppSettings(mContext);

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mUpBtn = rootView.findViewById(R.id.forward);
        mDownBtn = rootView.findViewById(R.id.back);
        mLeftBtn = rootView.findViewById(R.id.left);
        mRightBtn = rootView.findViewById(R.id.right);
        mStopBtn = rootView.findViewById(R.id.stop);
        mRedLedBtn = rootView.findViewById(R.id.red_led);
        mYellowLedBtn = rootView.findViewById(R.id.yellow_led);
        mGreenLedBtn = rootView.findViewById(R.id.green_led);
        mHornBtn = rootView.findViewById(R.id.horn);

        mUpBtn.setOnTouchListener(touchListener);
        mDownBtn.setOnTouchListener(touchListener);
        mLeftBtn.setOnTouchListener(touchListener);
        mRightBtn.setOnTouchListener(touchListener);
        mStopBtn.setOnTouchListener(touchListener);
        mRedLedBtn.setOnTouchListener(touchListener);
        mYellowLedBtn.setOnTouchListener(touchListener);
        mGreenLedBtn.setOnTouchListener(touchListener);
        mHornBtn.setOnTouchListener(touchListener);

        return rootView;
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE: {
                    switch (v.getId()) {
                        case R.id.forward: {
                            sendMessage("f");
                            break;
                        }
                        case R.id.back: {
                            sendMessage("b");
                            break;
                        }
                        case R.id.left: {
                            sendMessage("l");
                            break;
                        }
                        case R.id.right: {
                            sendMessage("r");
                            break;
                        }
                        case R.id.stop: {
                            sendMessage("s");
                            break;
                        }
                        case R.id.red_led: {
                            sendMessage("red");
                            break;
                        }
                        case R.id.yellow_led: {
                            sendMessage("yellow");
                            break;
                        }
                        case R.id.green_led: {
                            sendMessage("green");
                            break;
                        }
                        case R.id.horn: {
                            sendMessage("horn");
                            break;
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
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
