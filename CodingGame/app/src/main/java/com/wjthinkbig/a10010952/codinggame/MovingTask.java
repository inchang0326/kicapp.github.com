package com.wjthinkbig.a10010952.codinggame;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

public class MovingTask extends AsyncTask<MovingTaskParams, Integer, Boolean> {

    private ViewActivity m_va;

    public MovingTask(ViewActivity va) {
        m_va = va;
    }

    private int m_moveToMarginLeft, m_moveToMarginTop,
            m_startMarginLeft, m_startMarginTop,
            m_destMarginLeft, m_destMarginTop;

    protected void onPreExecute() {
        m_moveToMarginLeft = m_va.getMoveToMarginLeft();
        m_moveToMarginTop = m_va.getMoveToMarginTop();
        m_startMarginLeft = m_va.getLayoutParams().leftMargin;
        m_startMarginTop = m_va.getLayoutParams().topMargin;
    }

    protected Boolean doInBackground(MovingTaskParams... params) {
        int currMarginLeft, currMarginTop;
        for (int i = 0; i < params[0].getSize(); i++) {
            Arrow arrow = params[0].getArrow();
            switch (arrow) {
                case up: {
                    m_destMarginTop = m_startMarginTop - m_moveToMarginTop;
                    for (currMarginTop = m_startMarginTop; currMarginTop > m_destMarginTop; currMarginTop--) {
                        publishProgress(m_startMarginLeft, currMarginTop);
                        try {
                            Thread.sleep(1);
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
                        publishProgress(m_startMarginLeft, currMarginTop);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    m_startMarginTop = currMarginTop;
                    if(isCancelled()) break;
                    break;
                }
                case left: {
                    m_destMarginLeft = m_startMarginLeft - m_moveToMarginLeft;
                    for (currMarginLeft = m_startMarginLeft; currMarginLeft > m_destMarginLeft; currMarginLeft--) {
                        publishProgress(currMarginLeft, m_startMarginTop);
                        try {
                            Thread.sleep(1);
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
                        publishProgress(currMarginLeft, m_startMarginTop);
                        try {
                            Thread.sleep(1);
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
        m_va.setDingcoLastPos(posInfo[0], posInfo[1]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(m_va.getIsFinished()) {
            // 정답 팝업 메시지
            m_va.setToastMessage("축하해요!!");
            startApplication();
            // 정답을 맞출 경우 게임을 리셋한다.
            m_va.resetGame();
            // 정답을 맞췄는지 아닌지에 대한 플래그 초기화
            m_va.setIsFinished(false);
        }
        m_va.getBackBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_va.getClearBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_va.getBackBtn().setEnabled(true);
        m_va.getClearBtn().setEnabled(true);
    }

    @Override
    protected void onCancelled() {
    }

    private void startApplication() {
        ComponentName compoentName = new ComponentName("com.kakao.sdk.newtone.sample","com.kakao.sdk.newtone.sample.TextToSpeechActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(compoentName);
        intent.putExtra("codinggmae", m_va.getVoca());
        Log.d("inchang", m_va.getVoca());
        m_va.startActivity(intent);
    }
}