package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.Random;

public class LevelActivity extends Activity {

    public final static int ADVANCED_RESULT_OK = 1,
                            SENTENCE_RESULT_OK = 2;

    private ImageButton m_basicVButton, m_advancedVButton, m_sentenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        m_basicVButton = findViewById(R.id.basicvbtn);
        m_advancedVButton = findViewById(R.id.advancedvbtn);
        m_sentenceButton = findViewById(R.id.sentencebtn);

//        m_advancedVButton.setEnabled(false);
//        m_sentenceButton.setEnabled(false);
        m_advancedVButton.setImageResource(R.drawable.advancedv_berry_s);
        m_sentenceButton.setImageResource(R.drawable.sentence_kiwi_s);

        m_basicVButton.setOnClickListener(onClickListener);
        m_advancedVButton.setOnClickListener(onClickListener);
        m_sentenceButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Random randomGenerator = new Random();
            Intent i = new Intent(LevelActivity.this, ViewActivity.class);
            String voca = "";
            switch (v.getId()) {
                case R.id.basicvbtn: {
                    voca = BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.sizeOf())).toString();
                    break;
                }
                case R.id.advancedvbtn: {
                    voca = AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.sizeOf())).toString();
                    break;
                }
                case R.id.sentencebtn: {
                    voca = Sentence.valueOf(randomGenerator.nextInt(Sentence.sizeOf())).toString();
                    break;
                }
            }
            i.putExtra("codinggame", voca);
            startActivityForResult(i, 0);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ADVANCED_RESULT_OK: {
                m_advancedVButton.setEnabled(true);
                m_advancedVButton.setImageResource(R.drawable.advancedv_berry_s);
                break;
            }
            case SENTENCE_RESULT_OK: {
                m_sentenceButton.setEnabled(true);
                m_sentenceButton.setImageResource(R.drawable.sentence_kiwi_s);
                break;
            }
        }
    }
}