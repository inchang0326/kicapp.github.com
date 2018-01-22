package com.example.me.codinggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    private Button basicVButton;
    public static Button advancedVButton, basicSButton, advancedSButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        basicVButton = (Button) findViewById(R.id.basicV);
        advancedVButton = (Button) findViewById(R.id.advancedV);
        basicSButton = (Button) findViewById(R.id.basicS);
        advancedSButton = (Button) findViewById(R.id.advancedS);

        basicVButton.setOnClickListener(onClickListener);
        advancedVButton.setOnClickListener(onClickListener);
        basicSButton.setOnClickListener(onClickListener);
        advancedSButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.basicV: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 3;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.advancedV: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 4;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.basicS: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 5;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.advancedS: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 6;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
            }
        }
    };
}
