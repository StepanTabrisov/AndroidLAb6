package com.example.lab5;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class InitScene extends Activity {
    int mode = 0;
    Button enter;
    Button reg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_scene);

        enter = findViewById(R.id.enterButton);
        reg = findViewById(R.id.regButton);

        Intent intent = new Intent(this, EnterScene.class);

        Bundle flag = new Bundle();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                flag.putInt("mode", mode);
                intent.putExtras(flag);
                startActivity(intent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 1;
                flag.putInt("mode", mode);
                intent.putExtras(flag);
                startActivity(intent);
            }
        });
    }
}
