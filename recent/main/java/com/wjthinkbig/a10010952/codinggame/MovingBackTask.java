package com.wjthinkbig.a10010952.codinggame;

import android.os.AsyncTask;
import android.util.Log;

public class MovingBackTask extends AsyncTask<Arrow, Integer, Boolean> {

    private ViewActivity m_va;

    public MovingBackTask(ViewActivity va) {
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
                break;
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
        m_va.setBackState(true);
    }
}