package com.wjthinkbig.a10010952.btproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.wjthinkbig.a10010952.btproject.service.BTCTemplateService;
import com.wjthinkbig.a10010952.codinggame.Arrow;
import com.wjthinkbig.a10010952.codinggame.DialogActivity;
import com.wjthinkbig.a10010952.codinggame.MovingTaskParams;

public class MovingTask extends AsyncTask<MovingTaskParams, Integer, Boolean> {

    private MainActivity m_ma;
    private BTCTemplateService m_service;

    public MovingTask(MainActivity va) {
        m_ma = va;
    }

    private int m_moveToMarginLeft, m_moveToMarginTop,
            m_startMarginLeft, m_startMarginTop,
            m_destMarginLeft, m_destMarginTop;

    protected void onPreExecute() {
        m_service = m_ma.getService();
        m_moveToMarginLeft = m_ma.getMoveToMarginLeft();
        m_moveToMarginTop = m_ma.getMoveToMarginTop();
        m_startMarginLeft = m_ma.getLayoutParams().leftMargin;
        m_startMarginTop = m_ma.getLayoutParams().topMargin;
    }

    protected Boolean doInBackground(MovingTaskParams... params) {
        int currMarginLeft, currMarginTop;
        for (int i = 0; i < params[0].getSize(); i++) {
            Arrow arrow = params[0].getArrow();
            if(m_service != null) {
                m_service.sendMessageToRemote(arrow.toString() + " ");
            }
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
        m_ma.setDingcoLastPos(posInfo[0], posInfo[1]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(m_ma.getIsFinished()) {
            // 정답 팝업 메시지
            m_ma.setToastMessage("축하해요!!");
            Intent i = new Intent(m_ma, DialogActivity.class);
            String voca = m_ma.getVoca();
            i.putExtra("codinggame", voca);
            m_ma.startActivity(i);
            // 정답을 맞출 경우 게임을 리셋한다.
            m_ma.resetGame();
            // 정답을 맞췄는지 아닌지에 대한 플래그 초기화
            m_ma.setIsFinished(false);
        }
        m_ma.getBackBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_ma.getClearBtn().setBackgroundColor(Color.parseColor("#424242"));
        m_ma.getBackBtn().setEnabled(true);
        m_ma.getClearBtn().setEnabled(true);
    }

    @Override
    protected void onCancelled() {
    }
}