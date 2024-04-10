package com.example.spaceadvent;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Choose_Operation extends AppCompatActivity {

    Button difficultyControl, additionQuiz, subtractionQuiz;
    TextView diffcultyNums, difficultyRounds, difficultyChangeAns, difficultytimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_operation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.operation_selection), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playMusic(R.raw.sans, true, false);

        diffcultyNums = findViewById(R.id.difficultyNumber);
        difficultyRounds = findViewById(R.id.difficultyRounds);
        difficultyChangeAns = findViewById(R.id.difficultyFreedom);
        difficultyChangeAns = findViewById(R.id.difficultyFreedom);
        difficultytimer = findViewById(R.id.difficultyTimer);
        difficultyControl = findViewById(R.id.difficultyControl);
        additionQuiz = findViewById(R.id.operationAddition);
        subtractionQuiz = findViewById(R.id.operationSubtraction);
        Intent intent = new Intent(Choose_Operation.this, QuizActivity.class);

        //onclick for difficulty control new intent
        difficultyControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_Operation.this, difficultyControl.class));
            }
        });

        //extracting data from difficulty control
        int minNum = getIntent().getIntExtra("minNum",1);
        int maxNum = getIntent().getIntExtra("maxNum",10);
        int rounds = getIntent().getIntExtra("rounds",5);
        Boolean changeAns = getIntent().getBooleanExtra("changeAns",true);
        Boolean timerEnabled = getIntent().getBooleanExtra("timerEnabled",false);
        int timer = getIntent().getIntExtra("timer",0);
        Boolean isDefault = getIntent().getBooleanExtra("isDefault",false);

        //Store data for quiz
        intent.putExtra("minNum", minNum);
        intent.putExtra("maxNum", maxNum);
        intent.putExtra("rounds", rounds);
        intent.putExtra("changeAns", changeAns);
        intent.putExtra("timerEnabled", timerEnabled);
        intent.putExtra("timer", timer);
        intent.putExtra("isDefault", isDefault);

        //Texts for difficulty
        diffcultyNums.setText("Numbers to come up " + minNum + " - " + maxNum);
        difficultyRounds.setText(rounds + " Questions");
        if (changeAns){
            difficultyChangeAns.setText("Can change answer before submitting");
        } else {
            difficultyChangeAns.setText("Can not change answers");
        }
        if (timerEnabled){
            difficultytimer.setText("Has timer ("+ timer +")");
        } else {
            difficultytimer.setText("Has no timer ("+ timer +")");
        }

        additionQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic(1,true,true);
                intent.putExtra("operation", "+");

                startActivity(intent);
            }
        });

        subtractionQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic(1,true,true);
                intent.putExtra("operation", "-");
                startActivity(intent);
            }
        });
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