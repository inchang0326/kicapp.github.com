package com.example.me.myapplication;

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
            m_va.setDingcoStartPos();
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
}