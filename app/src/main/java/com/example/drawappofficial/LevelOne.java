package com.example.drawappofficial;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelOne extends Activity implements View.OnClickListener {

    Button squareBtn, triangleBtn, circleBtn, trapezBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        squareBtn = (Button) findViewById(R.id.levelOneSquareBtn);
        squareBtn.setOnClickListener(this);

        triangleBtn = (Button) findViewById(R.id.levelOneTriangleBtn);
        triangleBtn.setOnClickListener(this);

        circleBtn = (Button) findViewById(R.id.levelOneCircleBtn);
        circleBtn.setOnClickListener(this);

        trapezBtn = (Button) findViewById(R.id.levelOneTrapezBtn);
        trapezBtn.setOnClickListener(this);

        backBtn = (Button) findViewById(R.id.levelOneBackBtn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.levelOneSquareBtn){
            Intent intent = new Intent(this,LevelOneSquareInfo.class);
            startActivity(intent);
        } else if (v.getId() == R.id.levelOneTriangleBtn){
            Intent intent = new Intent(this,LevelOneTriangleInfo.class);
            startActivity(intent);
        } else if (v.getId() == R.id.levelOneCircleBtn){
            Intent intent = new Intent(this,LevelOneCircleInfo.class);
            startActivity(intent);
        } else if (v.getId() == R.id.levelOneTrapezBtn){
            Intent intent = new Intent(this,LevelOneTrapezInfo.class);
            startActivity(intent);
        } else if (v.getId() == R.id.levelOneBackBtn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
