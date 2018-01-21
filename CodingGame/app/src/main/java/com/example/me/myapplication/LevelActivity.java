package com.example.me.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Random;

/**
 * Created by Me on 2018-01-20.
 */

public class LevelActivity extends AppCompatActivity {

    private Button basicButton, advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Level Selection");
        setContentView(R.layout.activity_level);

        basicButton = (Button) findViewById(R.id.basic);
        advancedButton = (Button) findViewById(R.id.advanced);
        basicButton.setOnClickListener(onClickListener);
        advancedButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.basic: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 3;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.advanced: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 4;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
            }
        }
    };
}
