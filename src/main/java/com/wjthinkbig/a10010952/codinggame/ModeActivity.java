package com.wjthinkbig.a10010952.codinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class ModeActivity extends Activity {

    private ImageButton controllerButton, bookpadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        controllerButton = findViewById(R.id.controller);
        bookpadButton = findViewById(R.id.bookpad);

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
                    String pacakgeName = "com.example.a10010952.btproject";
                    startApplication(pacakgeName);
                    break;
                }
            }
        }
    };

    private boolean startApplication(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent != null) {
            startActivity(intent);
            return true;
        }
        else return false;
    }
}
