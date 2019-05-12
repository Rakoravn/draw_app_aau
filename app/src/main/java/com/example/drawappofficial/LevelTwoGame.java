package com.example.drawappofficial;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drawappofficial.models.Classification;
import com.example.drawappofficial.models.Classifier;
import com.example.drawappofficial.models.TensorFlowClassifier;
import com.example.drawappofficial.views.DrawModel;
import com.example.drawappofficial.views.DrawView;

import java.io.IOException;
import java.util.ArrayList;

public class LevelTwoGame extends Activity implements View.OnClickListener, View.OnTouchListener{

    private static final int PIXEL_WIDTH = 100;

    // ui elements
    private TextView pointTxt, triesTxt, questTxt, timeTxt;
    private Button clearBtn, classBtn;
    private Classifier mClassifiers;

    final Handler handler = new Handler();

    // views
    private DrawModel drawModel;
    private DrawView drawView;
    private PointF mTmpPoint = new PointF();

    private int tries, points;
    private long timeLeft;
    private String shape;

    private float mLastX;
    private float mLastY;

    private CountDownTimer countDownTimer;

    AnimationDrawable correctAnim;

    private ArrayList<Integer> quizNumsTwo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two_game);

        //get drawing view from XML (where the finger writes the number)
        drawView = (DrawView) findViewById(R.id.levelTwoView);
        //get the model object
        drawModel = new DrawModel(PIXEL_WIDTH, PIXEL_WIDTH);

        //init the view with the model object
        drawView.setModel(drawModel);
        // give it a touch listener to activate when the user taps
        drawView.setOnTouchListener(this);

        //clear button
        //clear the drawing when the user taps
        clearBtn = (Button) findViewById(R.id.clearLevelTwoBtn);
        clearBtn.setOnClickListener(this);

        //class button
        //when tapped, this performs classification on the drawn image
        classBtn = (Button) findViewById(R.id.idLevelTwoBtn);
        classBtn.setOnClickListener(this);
        //classBtn.setBackgroundResource(R.drawable.correct_animation);

        //correctAnim = (AnimationDrawable) classBtn.getBackground();

        triesTxt = (TextView) findViewById(R.id.compLevelTwoTryTxt);
        tries = getIntent().getExtras().getInt("triesVar");
        triesTxt.setText(tries + "");

        pointTxt = (TextView) findViewById(R.id.compLevelTwoPointTxt);
        points = getIntent().getExtras().getInt("pointVar");
        pointTxt.setText("Points: " + points);

        questTxt = (TextView) findViewById(R.id.levelTwoGuideTxt);
        shape = getIntent().getExtras().getString("shapeVar");
        questTxt.setText("TEGN EN " + shape.toUpperCase());

        timeTxt = (TextView) findViewById(R.id.timeTxt2);
        timeLeft = getIntent().getExtras().getLong("timeLeftVar");

        countDownTimer = new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000 >= 60){
                    int minutes = (int) millisUntilFinished / 60000;
                    long seconds = millisUntilFinished - (minutes * 60000);

                    timeTxt.setText(minutes + "m " + seconds / 1000 + "s");
                    timeLeft = millisUntilFinished;
                } else {
                    timeTxt.setText(millisUntilFinished / 1000 + "s");
                    timeLeft = millisUntilFinished;
                }
            }

            public void onFinish() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LevelTwoGame.this, MainActivity.class);
                        intent.putExtra("pointVar", points);
                        startActivity(intent);
                    }
                }, 700);
            }
        }.start();

        quizNumsTwo = getIntent().getExtras().getIntegerArrayList("quizListVar");

        loadModel();
    }

    @Override
    //OnResume() is called when the user resumes his Activity which he left a while ago,
    // //say he presses home button and then comes back to app, onResume() is called.
    protected void onResume() {
        drawView.onResume();
        super.onResume();
    }

    @Override
    //OnPause() is called when the user receives an event like a call or a text message,
    // //when onPause() is called the Activity may be partially or completely hidden.
    protected void onPause() {
        drawView.onPause();
        super.onPause();
    }
    //creates a model object in memory using the saved tensorflow protobuf model file
    //which contains all the learned weights
    private void loadModel() {
        //The Runnable interface is another way in which you can implement multi-threading other than extending the
        // //Thread class due to the fact that Java allows you to extend only one class. Runnable is just an interface,
        // //which provides the method run.
        // //Threads are implementations and use Runnable to call the method run().
        //add 2 classifiers to our classifier arraylist
        //the tensorflow classifier and the keras classifier

        try {
            mClassifiers = TensorFlowClassifier.create(getAssets(), "Chuni",
                    "opt_chuniModel.pb", "labels.txt", PIXEL_WIDTH,
                    "conv2d_1_input", "dense_2/Softmax", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        tries += 1;
        triesTxt.setText(tries + "");

        if (v.getId() == R.id.idLevelTwoBtn){

            float pixels[] = drawView.getPixelData();

            //perform classification on the image
            final Classification res = mClassifiers.recognize(pixels);

            drawModel.clear();
            drawView.reset();
            drawView.invalidate();

            if (res.getLabel().equals(shape.toLowerCase())) {
                //classBtn.setText("");
                //correctAnim.start();

                Toast.makeText(this, "Korrekt!", Toast.LENGTH_SHORT).show();

                points += 50;
                pointTxt.setText("Points: " + points);

                pointTxt.setTextColor(getResources().getColor(R.color.correct));

                clearBtn.setEnabled(false);
                classBtn.setEnabled(false);

                toQuestionActivity();

            } else {
                Toast.makeText(this, "Forkert", Toast.LENGTH_SHORT).show();
                toQuestionActivity();
            }

        } else if (v.getId() == R.id.clearLevelTwoBtn){

            drawModel.clear();
            drawView.reset();
            drawView.invalidate();

        }
    }

    private void toQuestionActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.cancel();
                countDownTimer = null;
                Intent intent = new Intent(LevelTwoGame.this, LevelTwoQuiz.class);
                intent.putExtra("quizListVar2", quizNumsTwo);
                intent.putExtra("pointVar", points);
                intent.putExtra("triesVar", tries);
                intent.putExtra("timeLeftVar", timeLeft);
                startActivity(intent);
            }
        }, 700);
    }

    @Override
    //this method detects which direction a user is moving
    //their finger and draws a line accordingly in that
    //direction
    public boolean onTouch(View v, MotionEvent event) {
        //get the action and store it as an int
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        //actions have predefined ints, lets match
        //to detect, if the user has touched, which direction the users finger is
        //moving, and if they've stopped moving

        //if touched
        if (action == MotionEvent.ACTION_DOWN) {
            //begin drawing line
            processTouchDown(event);
            return true;
            //draw line in every direction the user moves
        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;
            //if finger is lifted, stop drawing
        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp();
            return true;
        }
        return false;
    }

    //draw line down

    private void processTouchDown(MotionEvent event) {
        //calculate the x, y coordinates where the user has touched
        mLastX = event.getX();
        mLastY = event.getY();
        //user them to calcualte the position
        drawView.calcPos(mLastX, mLastY, mTmpPoint);
        //store them in memory to draw a line between the
        //difference in positions
        float lastConvX = mTmpPoint.x;
        float lastConvY = mTmpPoint.y;
        //and begin the line drawing
        drawModel.startLine(lastConvX, lastConvY);
    }

    //the main drawing function
    //it actually stores all the drawing positions
    //into the drawmodel object
    //we actually render the drawing from that object
    //in the drawrenderer class
    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        drawView.calcPos(x, y, mTmpPoint);
        float newConvX = mTmpPoint.x;
        float newConvY = mTmpPoint.y;
        drawModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        drawModel.endLine();
    }
}
