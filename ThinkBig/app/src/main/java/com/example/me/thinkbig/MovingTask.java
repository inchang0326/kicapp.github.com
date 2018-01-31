package com.example.me.thinkbig;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MovingTask extends AsyncTask<MovingTaskParams, Integer, Boolean> {

    private ViewActivity m_va;
    public MovingTask(ViewActivity va) {
        m_va = va;
    }

    private ImageView m_dingco;
    private RelativeLayout m_relativeLayout;
    private RelativeLayout.LayoutParams m_rlp;

    private int m_firstMarginLeft, m_firstMarginTop,
            m_moveToMarginLeft, m_moveToMarginTop,
            m_startMarginLeft, m_startMarginTop,
            m_destMarginLeft, m_destMarginTop;

    protected void onPreExecute() {
        m_dingco = m_va.getDingco();
        m_relativeLayout = m_va.getRelativeLayout();
        m_rlp = m_va.getLayoutParams();

        m_firstMarginLeft = m_va.getFirstMarginLeft();
        m_firstMarginTop = m_va.getFirstMarginTop();
        m_moveToMarginLeft = m_va.getMoveToMarginLeft();
        m_moveToMarginTop = m_va.getMoveToMarginTop();

        m_startMarginLeft = m_va.getLayoutParams().leftMargin;
        m_startMarginTop = m_va.getLayoutParams().topMargin;
    }

    protected Boolean doInBackground(MovingTaskParams... params) {

        int currMarginLeft, currMarginTop;
        // repeat 버튼의 반복 수 만큼
        for (int i = 0; i < params[0].getSize(); i++) {
            // 캐릭터가 가야하는 방향에 따라
            Arrow arrow = params[0].getArrow();
            switch (arrow) {
                case up: {
                    switch (params[0].getDir()) {
                        case 1: {
                            m_destMarginTop = m_startMarginTop - m_moveToMarginTop;
                            for (currMarginTop = m_startMarginTop; currMarginTop > m_destMarginTop; currMarginTop-=5) {
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
                            for (currMarginTop = m_startMarginTop; currMarginTop < m_destMarginTop; currMarginTop+=5) {
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
                            for (currMarginTop = m_startMarginTop; currMarginTop < m_destMarginTop; currMarginTop+=5) {
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
                            for (currMarginTop = m_startMarginTop; currMarginTop > m_destMarginTop; currMarginTop-=5) {
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
                            for (currMarginLeft = m_startMarginLeft; currMarginLeft > m_destMarginLeft; currMarginLeft-=5) {
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
                            for (currMarginLeft = m_startMarginLeft; currMarginLeft < m_destMarginLeft; currMarginLeft+=5) {
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
                            for (currMarginLeft = m_startMarginLeft; currMarginLeft < m_destMarginLeft; currMarginLeft+=5) {
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
                            for (currMarginLeft = m_startMarginLeft; currMarginLeft > m_destMarginLeft; currMarginLeft-=5) {
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
        m_va.setLastDingcoPos(posInfo[0], posInfo[1], 0, 0);
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }
}