package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.wjthinkbig.a10010952.R;

public class ModeActivity extends Activity {

    private ImageButton controllerButton, bookpadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        controllerButton = (ImageButton) findViewById(R.id.controller);
        bookpadButton = (ImageButton) findViewById(R.id.bookpad);

        controllerButton.setOnClickListener(onClickListener);
        bookpadButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookpad: {
                    Intent i = new Intent(ModeActivity.this, LevelActivity.class);
                    startActivity(i);
                    break;
                }
                case R.id.controller: {
                    Intent i = new Intent(ModeActivity.this, com.wjthinkbig.a10010952.btproject.MainActivity.class);
                    startActivity(i);
                    break;
                }
            }
        }
    };
}
