package com.wjthinkbig.a10010952.btproject;

import android.os.AsyncTask;
import android.util.Log;

import com.wjthinkbig.a10010952.btproject.service.BTCTemplateService;
import com.wjthinkbig.a10010952.codinggame.Arrow;

public class MovingBackTask extends AsyncTask<Arrow, Integer, Boolean> {

    private MainActivity m_ma;
    private BTCTemplateService m_service;

    public MovingBackTask(MainActivity va) {
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

    protected Boolean doInBackground(Arrow... params) {

        int currMarginLeft, currMarginTop;
        Arrow arrow = params[0];
        switch (arrow) {
            case up: {
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
                if(m_service != null) {
                    m_service.sendMessageToRemote(Arrow.down.toString() + " ");
                }
                break;
            }
            case down: {
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
                if(m_service != null) {
                    m_service.sendMessageToRemote(Arrow.up.toString() + " ");
                }
                break;
            }
            case left: {
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
                if(m_service != null) {
                    m_service.sendMessageToRemote(Arrow.right.toString() + " ");
                }
                break;
            }
            case right: {
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
                if(m_service != null) {
                    m_service.sendMessageToRemote(Arrow.left.toString() + " ");
                }
                break;
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
        m_ma.setBackState(true);
    }
}