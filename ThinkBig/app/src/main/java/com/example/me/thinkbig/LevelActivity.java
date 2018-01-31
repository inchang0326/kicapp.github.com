package com.example.me.thinkbig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelActivity extends Activity {

    private ImageButton m_basicVButton, m_advancedVButton, m_sentenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        m_basicVButton = findViewById(R.id.basicvbtn);
        m_advancedVButton = findViewById(R.id.advancedvbtn);
        m_sentenceButton = findViewById(R.id.sentencebtn);

        //m_advancedVButton.setEnabled(false);
        //m_sentenceButton.setEnabled(false);

        m_basicVButton.setOnClickListener(onClickListener);
        m_advancedVButton.setOnClickListener(onClickListener);
        m_sentenceButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.basicvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 3;
                    i.putExtra("mapInfo", mapInfo);
                    startActivityForResult(i, 0);
                    break;
                }
                case R.id.advancedvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 4;
                    i.putExtra("mapInfo", mapInfo);
                    startActivityForResult(i, 0);
                    break;
                }
                case R.id.sentencebtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    int mapInfo = 5;
                    i.putExtra("mapInfo", mapInfo);
                    startActivityForResult(i, 0);
                    break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                m_advancedVButton.setEnabled(true);
                m_advancedVButton.setImageResource(R.drawable.advancedv);
                break;
        }
    }
}