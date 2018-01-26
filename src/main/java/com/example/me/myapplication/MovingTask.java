package com.example.me.myapplication;

import android.os.AsyncTask;

public class MovingTask extends AsyncTask<MovingTaskParams, Void, Boolean> {
    @Override
    protected Boolean doInBackground(MovingTaskParams... parms){

        /*
            1. mapInfo 선별
            - Left, Top Margin 값 지정
            
            2-1. relativelayout 선별
            - 현재의 Margin 값 지정

            2-2. arrow 선별
            - 현재의 Margin 값과 방향(위, 아래, 오른쪽, 왼쪽)에 따라 1에서 선별된 값과 연산하여 목적지 값 지정
            Ex)

            3. 목적지 지점 값까지 - 또는 +로 가기
         */
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {

        }

        return true;
    }
}
