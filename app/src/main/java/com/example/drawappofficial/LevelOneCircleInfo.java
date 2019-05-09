package com.example.drawappofficial;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelOneCircleInfo extends Activity {

    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one_circle_info);

        nextBtn = (Button) findViewById(R.id.circleInfoNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelOneCircleInfo.this,LevelOneCircleGame.class);
                startActivity(intent);
            }
        });
    }
}
