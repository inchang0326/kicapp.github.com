package com.wjthinkbig.a10010952.btproject.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.wjthinkbig.a10010952.codinggame.Arrow;
import com.wjthinkbig.a10010952.codinggame.MovingTaskParams;

public class MovingTask2 extends AsyncTask<MovingTaskParams, Integer, Boolean> {

    private GameFragment2 m_gf;
    private boolean flag = false;

    public MovingTask2(GameFragment2 gf) {
        m_gf = gf;
    }

    private int m_moveToMarginLeft, m_moveToMarginTop,
            m_startMarginLeft, m_startMarginTop,
            m_destMarginLeft, m_destMarginTop;

    protected void onPreExecute() {
        m_moveToMarginLeft = m_gf.getMoveToMarginLeft();
        m_moveToMarginTop = m_gf.getMoveToMarginTop();
        m_startMarginLeft = m_gf.getLayoutParams().leftMargin;
        m_startMarginTop = m_gf.getLayoutParams().topMargin;
    }

    protected Boolean doInBackground(MovingTaskParams... params) {
        int currMarginLeft, currMarginTop;
        for (int i = 0; i < params[0].getSize(); i++) {
            flag = false;
            Arrow arrow = params[0].getArrow();
            switch (arrow) {
                case up: {
                    m_destMarginTop = m_startMarginTop - m_moveToMarginTop;
                    for (currMarginTop = m_startMarginTop; currMarginTop > m_destMarginTop; currMarginTop--) {
                        if(currMarginTop == m_destMarginTop+1)
                            flag = true;
                        publishProgress(m_startMarginLeft, currMarginTop);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    m_startMarginTop = currMarginTop;
                    break;
                }
                case down: {
                    m_destMarginTop = m_startMarginTop + m_moveToMarginTop;
                    for (currMarginTop = m_startMarginTop; currMarginTop < m_destMarginTop; currMarginTop++) {
                        if(currMarginTop == m_destMarginTop-1)
                            flag = true;
                        publishProgress(m_startMarginLeft, currMarginTop);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    m_startMarginTop = currMarginTop;
                    break;
                }
                case left: {
                    m_destMarginLeft = m_startMarginLeft - m_moveToMarginLeft;
                    for (currMarginLeft = m_startMarginLeft; currMarginLeft > m_destMarginLeft; currMarginLeft--) {
                        if(currMarginLeft == m_destMarginLeft+1)
                            flag = true;
                        publishProgress(currMarginLeft, m_startMarginTop);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    m_startMarginLeft = currMarginLeft;
                    break;
                }
                case right: {
                    m_destMarginLeft = m_startMarginLeft + m_moveToMarginLeft;
                    for (currMarginLeft = m_startMarginLeft; currMarginLeft < m_destMarginLeft; currMarginLeft++) {
                        if(currMarginLeft == m_destMarginLeft-1)
                            flag = true;
                        publishProgress(currMarginLeft, m_startMarginTop);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    m_startMarginLeft = currMarginLeft;
                    break;
                }
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... posInfo) {
        m_gf.setDingcoLastPos(posInfo[0], posInfo[1]);
        try {
            if(flag) {
                m_gf.setAlphabetImage(m_gf.getCurrAlpha());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(m_gf.getIsFinished()) {
            // 정답 팝업 메시지
            m_gf.setToastMessage("축하해요!!");
            startApplication();
            // 정답을 맞출 경우 게임을 리셋한다.
            m_gf.resetGame();
            // 정답을 맞췄는지 아닌지에 대한 플래그 초기화
            m_gf.setIsFinished(false);
            // 아두이노에 정답 메시지 전달
            m_gf.correctMsgToArduino();
        }
        m_gf.getBackBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_gf.getClearBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_gf.getBackBtn().setEnabled(true);
        m_gf.getClearBtn().setEnabled(true);
    }

    @Override
    protected void onCancelled() {
    }

    private void startApplication() {
        ComponentName compoentName = new ComponentName("com.kakao.sdk.newtone.sample","com.kakao.sdk.newtone.sample.TextToSpeechActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(compoentName);
        intent.putExtra("codinggmae", m_gf.getVoca());
        Log.d("inchang", m_gf.getVoca());
        m_gf.startActivity(intent);
    }
}