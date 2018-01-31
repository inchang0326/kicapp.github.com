package com.example.me.thinkbig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 2000);
    }

    private class splashHandler implements Runnable {
        public void run() {
            // 로딩이 끝난후 이동할 Activity
            startActivity(new Intent(getApplication(), ModeActivity.class));
            // 로딩페이지 Activity Stack에서 제거
            finish();
        }
    }
}