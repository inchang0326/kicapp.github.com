package com.example.me.thinkbig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelActivity extends Activity {

    private ImageButton basicVButton;
    public static ImageButton advancedVButton, sentenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        basicVButton = (ImageButton) findViewById(R.id.basicvbtn);
        advancedVButton = (ImageButton) findViewById(R.id.advancedvbtn);
        sentenceButton = (ImageButton) findViewById(R.id.sentencebtn);

        //advancedVButton.setEnabled(false);
        //sentenceButton.setEnabled(false);

        basicVButton.setOnClickListener(onClickListener);
        advancedVButton.setOnClickListener(onClickListener);
        sentenceButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.basicvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 3;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.advancedvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 4;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
                case R.id.sentencebtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 5;
                    i.putExtra("mapInfo", mapInfo);
                    startActivity(i);
                    break;
                }
            }
        }
    };
}