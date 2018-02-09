package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends Activity {

    private TextView m_voca, m_description;
    private Button m_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        Bundle extras = getIntent().getExtras();
        String voca = extras.getString("codinggame");

        m_voca = findViewById(R.id.voca);
        m_description = findViewById(R.id.description);
        m_check = findViewById(R.id.check);

        if(BasicVoca.contain(voca)) {
            m_voca.setText(voca);
            m_description.setText(BasicVoca.valueOf(voca).meaningOf());
        } else if(AdvancedVoca.contain(voca)) {
            m_voca.setText(voca);
            m_description.setText(AdvancedVoca.valueOf(voca).meaningOf());
        } else if(Sentence.contain(voca)){
            m_voca.setText(Sentence.valueOf(voca).expression());
            m_description.setText(Sentence.valueOf(voca).meaningOf());
        }

        m_check.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check: {
                    finish();
                    break;
                }
            }
        }
    };
}