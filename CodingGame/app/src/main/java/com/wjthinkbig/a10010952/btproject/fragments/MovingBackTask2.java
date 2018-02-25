package com.wjthinkbig.a10010952.btproject.fragments;

import android.os.AsyncTask;

import com.wjthinkbig.a10010952.codinggame.Arrow;

public class MovingBackTask2 extends AsyncTask<Arrow, Integer, Boolean> {

    private GameFragment2 m_gf;
    private boolean flag = false;

    public MovingBackTask2(GameFragment2 gf) {
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

    protected Boolean doInBackground(Arrow... params) {

        int currMarginLeft, currMarginTop;
        Arrow arrow = params[0];
        switch (arrow) {
            case up: {
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
            case down: {
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
            case left: {
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
            case right: {
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
        m_gf.setBackState(true);
    }
}