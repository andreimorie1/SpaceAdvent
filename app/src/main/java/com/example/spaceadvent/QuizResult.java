package com.example.spaceadvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizResult extends AppCompatActivity {

    TextView textHighScore, diffcultyNums, difficultyRounds, difficultyChangeAns, difficultytimer, operationText, scoreInfo;
    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playMusic(R.raw.dogsong, true, false);

        Intent intent = new Intent(QuizResult.this, Choose_Operation.class);

        //INSERT HIGHSCORE FROM FIREBASE
        int highscore = 0;

        int minNum = getIntent().getIntExtra("minNum",1);
        int maxNum = getIntent().getIntExtra("maxNum",10);
        int rounds = getIntent().getIntExtra("rounds",5);
        boolean timerEnabled = getIntent().getBooleanExtra("timerEnabled",false);
        int timer = getIntent().getIntExtra("timer",0);
        String operation = getIntent().getStringExtra("operation");
        Boolean changeAns = getIntent().getBooleanExtra("changeAns",true);
        int score = getIntent().getIntExtra("score",0);

        diffcultyNums = findViewById(R.id.difficultyNumber);
        operationText = findViewById(R.id.operation);
        difficultyRounds = findViewById(R.id.difficultyRounds);
        difficultyChangeAns = findViewById(R.id.difficultyFreedom);
        difficultyChangeAns = findViewById(R.id.difficultyFreedom);
        difficultytimer = findViewById(R.id.difficultyTimer);
        textHighScore = findViewById(R.id.TextHighScore);
        scoreInfo = findViewById(R.id.scoreInfo);
        btn_done = findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

//Change highScore Text
        if (score > highscore){
            textHighScore.setText("New High Score!");
            highscore = score;//INSERT HIGHSCORE TO FIREBASE

        } else{
            textHighScore.setText("Finished!");
        }

        //Change score Text
        scoreInfo.setText(Integer.toString(score));

        //Change min and max numbers that came up
        diffcultyNums.setText(minNum + " - " + maxNum);

        //Change number of qestions
        difficultyRounds.setText(rounds + " Questions");

        //Change answer changable
        if (changeAns){
            difficultyChangeAns.setText("Yes");
        } else {
            difficultyChangeAns.setText("Can not change answers");
        }

        //Change timer
        if (timerEnabled){
            difficultytimer.setText("Has timer ("+ timer +")");
        } else {
            difficultytimer.setText("Has no timer ("+ timer +")");
        }

        //set operation
        if (operation.equals("+")){
            operationText.setText("Addition");
        } else if (operation.equals("-")){
            operationText.setText("Subtraction");
        } else {
            operationText.setText("woshit");
        }

    }

    public void playMusic(int resource, boolean isLooping, boolean stopService){
        Intent serviceIntent = new Intent(this, MusicService.class);
        if (stopService){
            stopService(serviceIntent);
        } else {
            serviceIntent.putExtra("musicResource", resource);
            serviceIntent.putExtra("isLooping", isLooping);
            startService(serviceIntent);
        }
    }
}