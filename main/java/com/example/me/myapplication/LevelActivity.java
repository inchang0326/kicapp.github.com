package com.example.me.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Me on 2018-01-20.
 */

public class LevelActivity extends Activity {

    private ImageButton basicVButton;
    public static ImageButton advancedVButton, basicSButton, advancedSButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        basicVButton = (ImageButton) findViewById(R.id.basicV);
        advancedVButton = (ImageButton) findViewById(R.id.advancedV);
        basicSButton = (ImageButton) findViewById(R.id.basicS);
        advancedSButton = (ImageButton) findViewById(R.id.advancedS);

        //advancedVButton.setEnabled(false);
        //basicSButton.setEnabled(false);
        //advancedSButton.setEnabled(false);

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
