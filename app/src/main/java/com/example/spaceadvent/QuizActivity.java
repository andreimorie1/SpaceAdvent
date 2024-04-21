package com.example.spaceadvent;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    TextView question, progress, userAnswerPreview, timerText;
    Button btn_option1, btn_option2, btn_option3, btn_option4, btn_next, btn_click;
    int num1, num2, correctAns, score, userAnswer = -100, currentProgress = 1 ;
    boolean isClicked = false, changeAns;
    ConstraintLayout mainCardLayout;
    List<Integer> answers = new ArrayList<>();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quiz), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int[] bgMusic = {R.raw.dummy, R.raw.metal_crusher, R.raw.death_by_glamour, R.raw.spider_dance};

        musicPlayer.start(this, bgMusic[random.nextInt(bgMusic.length)], false);

        //Extracting data from intent
        int minNum = getIntent().getIntExtra("minNum",1);
        int maxNum = getIntent().getIntExtra("maxNum",10);
        int rounds = getIntent().getIntExtra("rounds",5);
        boolean timerEnabled = getIntent().getBooleanExtra("timerEnabled",false);
        int timer = getIntent().getIntExtra("timer",0);
        String operation = getIntent().getStringExtra("operation");
        changeAns = getIntent().getBooleanExtra("changeAns",true);

        //Intent
        Intent intent = new Intent(QuizActivity.this, QuizResult.class);

        //change intent if operation is null
        if (operation == null){
            startActivity(new Intent(QuizActivity.this, Choose_Operation.class));
            Toast.makeText(QuizActivity.this, "Error: Operation is not stated", Toast.LENGTH_SHORT).show();
        }

        mainCardLayout = findViewById(R.id.mainCard);
        question = findViewById(R.id.question);
        progress = findViewById(R.id.progress);
        userAnswerPreview = findViewById(R.id.userAnswerPreview);
        btn_option1 = findViewById(R.id.btn_option1);
        btn_option2 = findViewById(R.id.btn_option2);
        btn_option3 = findViewById(R.id.btn_option3);
        btn_option4 = findViewById(R.id.btn_option4);
        btn_next = findViewById(R.id.btn_next);
        timerText = findViewById(R.id.timerText);

        //Sets progress and Questions on Create
        updateProgress(rounds);
        generateQuestion(maxNum, minNum, operation);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if answer is correct or not
                if (userAnswer == correctAns){
                    btn_click.setBackgroundResource(R.drawable.btn_option_correct);
                    mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card_correct);
                    score += 1;
                }
                else {
                    //Checks if answer is default or not (setting btn resource "btn_option_wrong" without selecting an answer crashes the app for some reason
                    if (userAnswer == -100){
                        mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card_wrong);
                    } else {
                        btn_click.setBackgroundResource(R.drawable.btn_option_wrong);
                        mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card_wrong);
                    }

                }

                new Handler().postDelayed(() -> {
                    if (currentProgress < rounds){
                        currentProgress += 1;
                        isClicked = false;
                        userAnswer = -100;
                        userAnswerPreview.setText("");
                        btn_click.setBackgroundResource(R.drawable.button_option);
                        mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card);
                        updateProgress(rounds);
                        generateQuestion(maxNum, minNum, operation);
                        if (timerEnabled){
                            startTimer(timer);
                        }
                    }
                    else {
                        //Quiz finish
                        intent.putExtra("minNum", minNum);
                        intent.putExtra("maxNum", maxNum);
                        intent.putExtra("rounds", rounds);
                        intent.putExtra("changeAns", changeAns);
                        intent.putExtra("timerEnabled", timerEnabled);
                        intent.putExtra("timer", timer);
                        intent.putExtra("operation", operation);
                        intent.putExtra("score", score);
                        startActivity(new Intent(intent));
                    }
                }, 1000);
            }
        });
        //starts timer
        if (timerEnabled){
            startTimer(timer);
            startTimer(timer);
        }
    }

    public void finishQuiz(){

    }
    public void updateProgress(int rounds){
        progress.setText(currentProgress + " / " + rounds);

    }
    //Generate question and answer
  public void generateQuestion(int maxNum, int minNum, String operation){


      if (operation.equals("+")){
          num1 = random.nextInt(maxNum) + minNum;
          num2 = random.nextInt(maxNum) + minNum;
          correctAns = num1 + num2;
      } else if (operation.equals("-")){
          num1 = random.nextInt(maxNum) + minNum;
          num2 = random.nextInt(num1);
          correctAns = num1 - num2;
      }

      question.setText(num1 + " "+ operation + " " + num2);

      answers = new ArrayList<>();
      answers.add(correctAns);

      while (answers.size() < 4) {//check if addition or subtraction
          if (operation.equals("+")) {
              int randomAns = num1 + (random.nextInt(num1) + 1);
              //To avoid duplication of answers
              if (!answers.contains(randomAns)) {
                  answers.add(randomAns);
              }
          }
          else if (operation.equals("-")) {
              int randomAns = num1 - (random.nextInt(num1) +1);
              //To avoid duplication of answers
              if (!answers.contains(randomAns)) {
                  answers.add(randomAns);
              }
          }
      }

      //shuffle the answers within the ArrayList
      Collections.shuffle(answers);
      btn_option1.setText(Integer.toString(answers.get(0)));
      btn_option2.setText(Integer.toString(answers.get(1)));
      btn_option3.setText(Integer.toString(answers.get(2)));
      btn_option4.setText(Integer.toString(answers.get(3)));
  }


    public void btn_option_click(View view){
        if (changeAns){
            // Set the background of the clicked button
            btn_click = (Button)view;
            btn_click.setBackgroundResource(R.drawable.btn_option_clicked);

            // Update userAnswer and userAnswerPreview
            userAnswer = Integer.parseInt(btn_click.getText().toString());
            userAnswerPreview.setText(String.valueOf(userAnswer));

            // Iterate through all buttons to reset their backgrounds
            for (Button button : new Button[]{btn_option1, btn_option2, btn_option3, btn_option4}) {
                if (button != view) {
                    button.setBackgroundResource(R.drawable.button_option); // Set default background here
                }
            }
        } else {
            if (!isClicked){
                // Set the background of the clicked button
                btn_click = (Button)view;
                btn_click.setBackgroundResource(R.drawable.btn_option_clicked);

                // Update userAnswer and userAnswerPreview
                userAnswer = Integer.parseInt(btn_click.getText().toString());
                userAnswerPreview.setText(String.valueOf(userAnswer));
                isClicked = true;
            }
        }
    }

    public void startTimer(int timer){
        Long toMilliSec = (long) (timer + 1) * 1000;
        CountDownTimer timerTime = new CountDownTimer(toMilliSec, 1000) {
            @Override
            public void onTick(long milliUntilTime) {
                long timeLeft = milliUntilTime / 1000;
                timerText.setText(String.valueOf(timeLeft));
            }

            @Override
            public void onFinish() {
                btn_next.performClick();
            }
        };
            timerTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayer.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        musicPlayer.unpause();
    }
}