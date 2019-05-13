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
    private TextView pointTxt, triesTxt;
    private int points, tries, triesCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointTxt = (TextView) findViewById(R.id.mainMenuPoint);
        triesTxt = (TextView) findViewById(R.id.mainMenuPoint);

        //Check whether a points variable have been passed from other activities.
        if (getIntent().getExtras() != null){
            points = getIntent().getExtras().getInt("pointVar");
            tries = getIntent().getExtras().getInt("triesVar");
            triesCorrect = getIntent().getExtras().getInt("correctQuizVar");

            pointTxt.setText("Point: " + points);
            triesTxt.setText(triesCorrect + "/" + tries);
        } else {
            points = 0;
            tries = 0;
            triesCorrect = 0;
        }

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
