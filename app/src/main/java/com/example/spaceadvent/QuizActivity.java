package com.example.spaceadvent;

import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    TextView question, progress, userAnswerPreview;
    Button btn_option1, btn_option2, btn_option3, btn_option4, btn_next, btn_click;
    private int num1 , num2, correctAns, score, timer, userAnswer = -1, currentProgress = 1 ;
    private boolean isClicked = false, changeAns;
    ConstraintLayout mainCardLayout;
    List<Integer> answers = new ArrayList<>();
    CountDownTimer timerTime;

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

        //Extracting data from intent
        int minNum = getIntent().getIntExtra("minNum",1);
        int maxNum = getIntent().getIntExtra("maxNum",10);
        int rounds = getIntent().getIntExtra("rounds",5);
        Boolean timerEnabled = getIntent().getBooleanExtra("timerEnabled",false);
        timer = getIntent().getIntExtra("timer",0);
        Boolean isDefault = getIntent().getBooleanExtra("isDefault",false);
        String defaultString = "error";
        String operation = getIntent().getStringExtra("operation");
        changeAns = getIntent().getBooleanExtra("changeAns",true);

        mainCardLayout = findViewById(R.id.mainCard);
        question = findViewById(R.id.question);
        progress = findViewById(R.id.progress);
        userAnswerPreview = findViewById(R.id.userAnswerPreview);
        btn_option1 = findViewById(R.id.btn_option1);
        btn_option2 = findViewById(R.id.btn_option2);
        btn_option3 = findViewById(R.id.btn_option3);
        btn_option4 = findViewById(R.id.btn_option4);
        btn_next = findViewById(R.id.btn_next);



        findViewById(R.id.back_button).setOnClickListener(
                a -> finish()
        );

        //Sets progress and Questions on Create
        updateProgress(rounds);
        generateQuestion(maxNum, minNum, operation);

        if (timerEnabled){
            //Starts timer
            startTimer();
        }


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if answer is correct or not
                if (userAnswer == correctAns){
                    Toast.makeText(QuizActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
                    btn_click.setBackgroundResource(R.drawable.btn_option_correct);
                    mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card_correct);
                    score += 1;
                }
                else {
                    Toast.makeText(QuizActivity.this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
                    btn_click.setBackgroundResource(R.drawable.btn_option_wrong);
                    mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card_wrong);
                }
                new Handler().postDelayed(() -> {
                    if (currentProgress < rounds){
                        currentProgress += 1;
                        updateProgress(rounds);
                        generateQuestion(maxNum, minNum, operation);
                        isClicked = false;
                        userAnswer = -100;
                        userAnswerPreview.setText("");
                        btn_option1.setBackgroundResource(R.drawable.button_option);
                        btn_option2.setBackgroundResource(R.drawable.button_option);
                        btn_option3.setBackgroundResource(R.drawable.button_option);
                        btn_option4.setBackgroundResource(R.drawable.button_option);
                        mainCardLayout.setBackgroundResource(R.drawable.quiz_layout_card);
                        if (timerEnabled){
                            startTimer();
                        }
                    }
                    else {
                        Toast.makeText(QuizActivity.this, "Finished Quiz with a score of " + score, Toast.LENGTH_SHORT).show();
                    }
                }, 1500);
            }
        });
    }

    public void updateProgress(int rounds){
        progress.setText((currentProgress) + " / " + rounds);

    }
    //Generate question and answer
  public void generateQuestion(int maxNum, int minNum, String operation){
        Random random = new Random();

      num1 = random.nextInt(maxNum) + minNum;
      num2 = random.nextInt(maxNum) + minNum;

      question.setText(num1 + " "+ operation + " " + num2);

      if (operation.equals("+")){
          correctAns = num1 + num2;
      } else if (operation.equals("-")){
          correctAns = num1 - num2;
      }

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
          if (operation.equals("-")) {
              int randomAns = num1 - (random.nextInt(num1) + 1);
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

    public void startTimer(){
        Long toMilliSec = (long) timer * 1000;
        timerTime = new CountDownTimer(toMilliSec, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
            btn_next.performClick();
            }
        };
    }


}