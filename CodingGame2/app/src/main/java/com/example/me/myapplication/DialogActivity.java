package com.example.me.myapplication;

import android.app.Activity;
import android.content.Intent;
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

        Intent i = getIntent();
        String info[] = i.getStringArrayExtra("info");

        m_voca = findViewById(R.id.voca);
        m_description = findViewById(R.id.description);
        m_check = findViewById(R.id.check);

        m_voca.setText(info[0]);
        switch (Integer.parseInt(info[1])) {
            case 3: {
                m_description.setText(BasicVoca.valueOf(info[0]).meaningOf());
                break;
            }
            case 4: {
                m_description.setText(AdvancedVoca.valueOf(info[0]).meaningOf());
                break;
            }
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