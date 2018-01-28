package com.example.me.codinggame;

import android.os.AsyncTask;
import android.util.Log;

public class MovingTask extends AsyncTask<MovingTaskParams, Integer, Boolean> {

    private int m_firstMarginLeft, m_firstMarginTop,
            m_startMarginLeft, m_startMarginTop,
            m_moveToMarginLeft, m_moveToMarginTop,
            m_destMarginLeft, m_destMarginTop;

    protected void onPreExecute() {
        m_firstMarginLeft = ViewActivity.m_firstMarginLeft;
        m_firstMarginTop = ViewActivity.m_firstMarginTop;
        m_startMarginLeft = ViewActivity.m_rlp.leftMargin;
        m_startMarginTop = ViewActivity.m_rlp.topMargin;
        m_moveToMarginLeft = ViewActivity.m_moveToMarginLeft;
        m_moveToMarginTop = ViewActivity.m_moveToMarginTop;
        m_destMarginLeft = 0;
        m_destMarginTop = 0;
    }

    protected Boolean doInBackground(MovingTaskParams... params) {

        int currMarginLeft, currMarginTop;
        // repeat 버튼의 반복 수 만큼
        for (int i = 0; i < params[0].getRepeatNum(); i++) {
            // 캐릭터가 가야하는 방향에 따라
            switch (params[0].getArrow()) {
                case up: {
                    switch (params[0].getDir()) {
                        case 1: {
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
                        case -1: {
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
                    }
                    break;
                }
                case down: {
                    switch (params[0].getDir()) {
                        case 1: {
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
                        case -1: {
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
                    }
                    break;
                }
                case left: {
                    switch (params[0].getDir()) {
                        case 1: {
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
                        case -1: {
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
                    break;
                }
                case right: {
                    switch (params[0].getDir()) {
                        case 1: {
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
                        case -1: {
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
                    break;
                }
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... posInfo) {
        ViewActivity.m_relativeLayout.removeAllViews();
        ViewActivity.m_rlp.setMargins(posInfo[0], posInfo[1], 0, 0);
        ViewActivity.m_relativeLayout.addView(ViewActivity.m_dingco, ViewActivity.m_rlp);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // 정답이면 처음 자리로 가도록
        if(ViewActivity.m_isFinished && result) {
            ViewActivity.m_relativeLayout.removeAllViews();
            ViewActivity.m_rlp.setMargins(m_firstMarginLeft, m_firstMarginTop, 0, 0);
            ViewActivity.m_relativeLayout.addView(ViewActivity.m_dingco, ViewActivity.m_rlp);
            ViewActivity.m_isFinished = false;
        }
    }
}