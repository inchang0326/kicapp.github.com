package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.wjthinkbig.a10010952.R;

import java.util.Random;

public class LevelActivity extends Activity {

    private ImageButton m_easyBasicVButton, m_easyAdvancedVButton, m_hardBasicVButton, m_hardAdvancedVButton, m_sentenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        m_easyBasicVButton = (ImageButton) findViewById(R.id.easybasicvbtn);
        m_easyAdvancedVButton = (ImageButton) findViewById(R.id.easyadvancedvbtn);
        m_hardBasicVButton = (ImageButton) findViewById(R.id.hardbasicvbtn);
        m_hardAdvancedVButton = (ImageButton) findViewById(R.id.hardadvancedvbtn);
        m_sentenceButton = (ImageButton) findViewById(R.id.sentencebtn);

        m_easyBasicVButton.setOnClickListener(onClickListener);
        m_easyAdvancedVButton.setOnClickListener(onClickListener);
        m_hardBasicVButton.setOnClickListener(onClickListener);
        m_hardAdvancedVButton.setOnClickListener(onClickListener);
        m_sentenceButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Random randomGenerator = new Random();
            switch (v.getId()) {
                case R.id.easybasicvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    String voca = BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.sizeOf())).toString();
                    i.putExtra("codinggame", voca);
                    startActivity(i);
                    break;
                }
                case R.id.easyadvancedvbtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    String voca = AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.sizeOf())).toString();
                    i.putExtra("codinggame", voca);
                    startActivity(i);
                    break;
                }
                case R.id.hardbasicvbtn: {
                    Intent i = new Intent(LevelActivity.this, UpgradeActivity.class);
                    String voca = BasicVoca.valueOf(randomGenerator.nextInt(BasicVoca.sizeOf())).toString();
                    i.putExtra("codinggame", voca);
                    startActivity(i);
                    break;
                }
                case R.id.hardadvancedvbtn: {
                    Intent i = new Intent(LevelActivity.this, UpgradeActivity.class);
                    String voca = AdvancedVoca.valueOf(randomGenerator.nextInt(AdvancedVoca.sizeOf())).toString();
                    i.putExtra("codinggame", voca);
                    startActivity(i);
                    break;
                }
                case R.id.sentencebtn: {
                    Intent i = new Intent(LevelActivity.this, ViewActivity.class);
                    String voca = Sentence.valueOf(randomGenerator.nextInt(Sentence.sizeOf())).toString();
                    i.putExtra("codinggame", voca);
                    startActivity(i);
                    break;
                }
            }
        }
    };
}