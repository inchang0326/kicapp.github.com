package com.wjthinkbig.a10010952.btproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.wjthinkbig.a10010952.R;
import com.wjthinkbig.a10010952.btproject.service.BTCTemplateService;
import com.wjthinkbig.a10010952.btproject.utils.Constants;
import com.wjthinkbig.a10010952.btproject.utils.Logs;
import com.wjthinkbig.a10010952.btproject.utils.RecycleUtils;

public class ControlActivity extends Activity {

    private ImageButton mUpBtn = null, mDownBtn = null, mLeftBtn = null, mRightBtn = null, mStopBtn = null,
            mRedLedBtn = null, mYellowLedBtn = null, mGreenLedBtn = null, mHornBtn = null;

    private BTCTemplateService m_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        m_service = MainActivity.getService();

        mUpBtn = (ImageButton) findViewById(R.id.forward);
        mDownBtn = (ImageButton) findViewById(R.id.back);
        mLeftBtn = (ImageButton) findViewById(R.id.left);
        mRightBtn = (ImageButton) findViewById(R.id.right);
        mStopBtn = (ImageButton) findViewById(R.id.stop);
        mRedLedBtn = (ImageButton) findViewById(R.id.red_led);
        mYellowLedBtn = (ImageButton) findViewById(R.id.yellow_led);
        mGreenLedBtn = (ImageButton) findViewById(R.id.green_led);
        mHornBtn = (ImageButton) findViewById(R.id.horn);

        mUpBtn.setOnTouchListener(touchListener);
        mDownBtn.setOnTouchListener(touchListener);
        mLeftBtn.setOnTouchListener(touchListener);
        mRightBtn.setOnTouchListener(touchListener);
        mStopBtn.setOnTouchListener(touchListener);
        mRedLedBtn.setOnTouchListener(touchListener);
        mYellowLedBtn.setOnTouchListener(touchListener);
        mGreenLedBtn.setOnTouchListener(touchListener);
        mHornBtn.setOnTouchListener(touchListener);
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
                            if(m_service != null) {
                                m_service.sendMessageToRemote("up ");
                            }
                            break;
                        }
                        case R.id.back: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("down ");
                            }
                            break;
                        }
                        case R.id.left: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("left ");
                            }
                            break;
                        }
                        case R.id.right: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("right ");
                            }
                            break;
                        }
                        case R.id.stop: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("stop ");
                            }
                            break;
                        }
                        case R.id.red_led: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("red");
                            }
                            break;
                        }
                        case R.id.yellow_led: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("yellow");
                            }
                            break;
                        }
                        case R.id.green_led: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("green");
                            }
                            break;
                        }
                        case R.id.horn: {
                            if(m_service != null) {
                                m_service.sendMessageToRemote("horn");
                            }
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
}


