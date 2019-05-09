package com.example.drawappofficial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Type;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button levelOne, levelTwo;
    private TextView pointTxt;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointTxt = (TextView) findViewById(R.id.mainMenuPoint);

        //Check whether a points variable have been passed from other activity.
        //If not, set points to 0, else set points to value of given variable "pointVar"
        if (getIntent().getExtras() != null){
            points = getIntent().getExtras().getInt("pointVar");
        } else {
            points = 0;
        }

        pointTxt.setText("Points: " + points);

        levelOne = (Button) findViewById(R.id.level1Btn);
        levelOne.setOnClickListener(this);

        levelTwo = (Button) findViewById(R.id.level2Btn);
        levelTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.level1Btn){
            Intent intent = new Intent(this, LevelOne.class);
            startActivity(intent);
        } else if (v.getId() == R.id.level2Btn){
            Intent intent = new Intent(this, LevelTwoQuiz.class);
            startActivity(intent);
        }
    }
}
