package com.example.drawappofficial;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.ColorLong;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drawappofficial.LevelTwoGame;
import com.example.drawappofficial.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Delayed;

public class LevelTwoQuiz extends Activity implements View.OnClickListener{

    private TextView questionTV, triesTV, pointTV, timeTV;
    private Button firstAnswer, secondAnswer, thirdAnswer, fourthAnswer;
    private ImageView quizIm;

    private final Handler handler = new Handler();

    private String shape = "";

    private String rightAnswer;
    private long timeLeft = 240000 ;
    private int points, tries;

    private CountDownTimer countDownTimer;

    private ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    private ArrayList<Button> quizButtons = new ArrayList<>();

    public ArrayList<Integer> quizNums = new ArrayList<>();

    private int[] imagesForQuestions = {
            R.drawable.square_one,
            R.drawable.square_two,
            R.drawable.square_three,
            R.drawable.square_four,

            R.drawable.circle_one,
            R.drawable.circle_two,
            R.drawable.circle_three,
            R.drawable.circle_four,

            R.drawable.triangle_one,
            R.drawable.triangle_two,
            R.drawable.triangle_three,
            R.drawable.triangle_four,
            R.drawable.triangle_five,
            R.drawable.triangle_six,

            R.drawable.trapez_one,
            R.drawable.trapez_two,
            R.drawable.trapez_three,
            R.drawable.trapez_four,
    };

    public String questionData[][] = {
            {"Hvilken form er det her?", "Firkant","Trekant","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Firkant","Trapez","Cirkel", "Trekant"},
            {"Hvilken form er det her?", "Firkant","Trekant","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Firkant","Cirkel","Trekant", "Trapez"},
            {"Hvilken form er det her?", "Cirkel","Firkant","Trapez", "Trekant"},
            {"Hvilken form er det her?", "Cirkel","Firkant","Trekant", "Trapez"},
            {"Hvilken form er det her?", "Cirkel","Firkant","Trekant", "Trapez"},
            {"Hvilken form er det her?", "Cirkel","Firkant","Trekant", "Trapez"},
            {"Hvilken form er det her?", "Trekant","Trapez","Cirkel", "Firkant"},
            {"Hvilken form er det her?", "Trekant","Firkant","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Trekant","Firkant ","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Trekant","Firkant ","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Trekant","Firkant ","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Trekant","Firkant ","Trapez", "Cirkel"},
            {"Hvilken form er det her?", "Trapez","Firkant ","Cirkel", "Trekant"},
            {"Hvilken form er det her?", "Trapez","Firkant ","Trekant", "Cirkel"},
            {"Hvilken form er det her?", "Trapez","Trekant","Firkant", "Cirkel"},
            {"Hvilken form er det her?", "Trapez","Trekant","Firkant", "Cirkel"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two_quiz);

        quizIm = (ImageView) findViewById(R.id.quizImage);
        questionTV = (TextView) findViewById(R.id.questionTxt);

        triesTV = (TextView) findViewById(R.id.triesTxt);
        pointTV = (TextView) findViewById(R.id.pointTxt);
        timeTV = (TextView) findViewById(R.id.timeTxt);

        //Check if a point variable and a tries variable have been passed to the activity
        if (getIntent().getExtras() == null){
            tries = 0;
            points = 0;
        } else {
            tries = getIntent().getExtras().getInt("triesVar");
            points = getIntent().getExtras().getInt("pointVar");
            timeLeft = getIntent().getExtras().getLong("timeLeftVar");
            quizNums = getIntent().getExtras().getIntegerArrayList("quizListVar2");
        }

        triesTV.setText(tries + "");
        pointTV.setText("Point: " + points);

        firstAnswer = (Button) findViewById(R.id.answerA);
        firstAnswer.setOnClickListener(this);

        secondAnswer = (Button) findViewById(R.id.answerB);
        secondAnswer.setOnClickListener(this);

        thirdAnswer = (Button) findViewById(R.id.answerC);
        thirdAnswer.setOnClickListener(this);

        fourthAnswer = (Button) findViewById(R.id.answerD);
        fourthAnswer.setOnClickListener(this);

        countDownTimer = new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000 >= 60){
                    int minutes = (int) millisUntilFinished / 60000;
                    long seconds = millisUntilFinished - (minutes * 60000);

                    timeTV.setText(minutes + "m " + seconds / 1000 + "s");
                    timeLeft = millisUntilFinished;

                } else {
                    timeTV.setText(millisUntilFinished / 1000 + "s");
                    timeLeft = millisUntilFinished;
                }
            }

            public void onFinish() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LevelTwoQuiz.this, MainActivity.class);
                        intent.putExtra("pointVar", points);
                        startActivity(intent);
                    }
                }, 700);
            }
        }.start();

        //Add each question to a temporary array (tempArray)
        // and then add each line (iteration of question to the quizArray
        for (int i = 0; i < questionData.length; i++){
            ArrayList<String> tempArray = new ArrayList<>();
            tempArray.add(questionData[i][0]);
            tempArray.add(questionData[i][1]);
            tempArray.add(questionData[i][2]);
            tempArray.add(questionData[i][3]);
            tempArray.add(questionData[i][4]);

            quizArray.add(tempArray);
        }

        generateQuestion();
    }

    //Here the questions are generated, as well as which image to show
    //which answer is correct and which answer is wrong
    public void generateQuestion(){
        Random r = new Random();
        int randomNum = r.nextInt(quizArray.size());
        System.out.println(quizNums);
        System.out.println(randomNum);

        while (quizNums.contains(randomNum)){
            randomNum = r.nextInt(quizArray.size());
        }

        System.out.println(quizNums);
        System.out.println(randomNum);

        quizNums.add(randomNum);

        //The random number indicates the question to take from the quizArray
        //The images used for each question are placed in the array corresponding to
        //each question in quizarray, respectively so the same random generated number
        //is used to pick a question and to display the appropriate image
        quizIm.setImageResource(imagesForQuestions[randomNum]);

        ArrayList<String> quiz = quizArray.get(randomNum);

        questionTV.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        shape = rightAnswer;

        quiz.remove(0);
        Collections.shuffle(quiz);

        firstAnswer.setText(quiz.get(0));
        secondAnswer.setText(quiz.get(1));
        thirdAnswer.setText(quiz.get(2));
        fourthAnswer.setText(quiz.get(3));

        quizButtons.add(firstAnswer);
        quizButtons.add(secondAnswer);
        quizButtons.add(thirdAnswer);
        quizButtons.add(fourthAnswer);

        quizArray.remove(randomNum);
    }

    public void checkAnswer(View view){
        Button answerBtn = (Button) findViewById(view.getId());
        String btnTxt = answerBtn.getText().toString();

        if (btnTxt.equals(rightAnswer)){
            answerBtn.setBackgroundResource(R.drawable.button_shape_correct);

            for (Button b : quizButtons){
                if (b.getText() != answerBtn.getText()){
                    b.setEnabled(false);
                }
            }

            points += 50;
            pointTV.setText("Point: " + points);
            pointTV.setTextColor(getResources().getColor(R.color.correct));
        } else {
            answerBtn.setBackgroundResource(R.drawable.button_shape_wrong);

            for (Button b : quizButtons){
                if (b.getText() == rightAnswer){
                    b.setBackgroundResource(R.drawable.button_shape_correct);
                }

                if (b.getText() != rightAnswer && b.getText() != answerBtn.getText()){
                    b.setEnabled(false);
                }
            }
        }
    }

    public void toQuestionPage(){
        countDownTimer.cancel();
        countDownTimer = null;
        Intent intent = new Intent(this, LevelTwoGame.class);
        intent.putExtra("quizListVar", quizNums);
        intent.putExtra("pointVar", points);
        intent.putExtra("timeLeftVar", timeLeft);
        intent.putExtra("triesVar", tries);
        intent.putExtra("shapeVar", shape);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        tries += 1;
        triesTV.setText(tries + "");

        if (v.getId() == R.id.answerA){
            checkAnswer(v);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toQuestionPage();
                }
            }, 700);
        } else if (v.getId() == R.id.answerB){
            checkAnswer(v);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toQuestionPage();
                }
            }, 700);
        } else if (v.getId() == R.id.answerC){
            checkAnswer(v);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toQuestionPage();
                }
            }, 700);
        } else if (v.getId() == R.id.answerD){
            checkAnswer(v);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toQuestionPage();
                }
            }, 700);
        }
    }
}
