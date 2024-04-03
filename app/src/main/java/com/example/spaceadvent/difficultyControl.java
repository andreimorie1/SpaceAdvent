package com.example.spaceadvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class difficultyControl extends AppCompatActivity {

    EditText minNum, maxNum, roundsNum, timerTime;
    Button changeAnsOn, changeAnsOff, timerOn, timerOff, reset, save;
    int minimumNumber = 1, maximumNumber = 10,numberRounds = 5, timer = 0;
    boolean timerEnabled = false, answerChangable = true, isDefault = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_difficulty_control);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.difficultyControl), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        minNum = findViewById(R.id.minNum);
        maxNum = findViewById(R.id.maxNum);
        roundsNum = findViewById(R.id.roundNum);
        timerTime = findViewById(R.id.timerTime);
        changeAnsOn = findViewById(R.id.changeAnsOn);
        changeAnsOff = findViewById(R.id.changeAnsOff);
        timerOn = findViewById(R.id.timerOn);
        timerOff = findViewById(R.id.timerOff);
        reset = findViewById(R.id.reset);
        save = findViewById(R.id.save);
        Intent intent = new Intent(difficultyControl.this, Choose_Operation.class);

        //Timer trigger
        timerOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerOn.setBackgroundResource(R.drawable.btn_option_correct);
                timerOff.setBackgroundResource(R.drawable.button_option);
                timerTime.setVisibility(View.VISIBLE);
                timerEnabled = true;
            }
        });
        timerOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerOff.setBackgroundResource(R.drawable.btn_option_correct);
                timerOn.setBackgroundResource(R.drawable.button_option);
                timerTime.setVisibility(View.GONE);
                timerEnabled = false;
            }
        });

        //Answer is changable trigger
        changeAnsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAnsOn.setBackgroundResource(R.drawable.btn_option_correct);
                changeAnsOff.setBackgroundResource(R.drawable.button_option);
                answerChangable = true;
            }
        });
        changeAnsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAnsOff.setBackgroundResource(R.drawable.btn_option_correct);
                changeAnsOn.setBackgroundResource(R.drawable.button_option);
                answerChangable = false;
            }
        });

        //Reset to default button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDefault = true;
                minimumNumber = 1;
                maximumNumber = 10;
                numberRounds = 5;
                timer = 0;
                timerEnabled = false;
                answerChangable = true;
                minNum.setText(Integer.toString(minimumNumber));
                maxNum.setText(Integer.toString(maximumNumber));
                roundsNum.setText(Integer.toString(numberRounds));
                timerTime.setVisibility(View.GONE);
                timerOff.setBackgroundResource(R.drawable.btn_option_correct);
                changeAnsOn.setBackgroundResource(R.drawable.btn_option_correct);
                Toast.makeText(difficultyControl.this, "Difficulty reset", Toast.LENGTH_LONG).show();
            }
        });
        //ClickListener Set Button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if inputs has blanks
                if (minNum.getText().toString().isEmpty() || maxNum.getText().toString().isEmpty() || roundsNum.getText().toString().isEmpty()){
                    Toast.makeText(difficultyControl.this, "All input fields must be filled or reset", Toast.LENGTH_LONG).show();
                } else if (timerEnabled && timerTime.getText().toString().isEmpty()){
                    Toast.makeText(difficultyControl.this, "All input fields must be filled or reset", Toast.LENGTH_LONG).show();
                } else {
                    //run if input is good
                    minimumNumber = Integer.parseInt(minNum.getText().toString());
                    maximumNumber = Integer.parseInt(maxNum.getText().toString());
                    numberRounds = Integer.parseInt(roundsNum.getText().toString());
                    if (timerEnabled){
                        timer = Integer.parseInt(timerTime.getText().toString());
                    }
                    if (minimumNumber != 1 && maximumNumber != 10 && numberRounds != 5 && timer != 0 && timerEnabled){
                        isDefault = false;
                    }

                    intent.putExtra("minNum", minimumNumber);
                    intent.putExtra("maxNum", maximumNumber);
                    intent.putExtra("rounds", numberRounds);
                    intent.putExtra("changeAns", answerChangable);
                    intent.putExtra("timerEnabled", timerEnabled);
                    intent.putExtra("timer", timer);
                    intent.putExtra("isDefault", isDefault);

                    Toast.makeText(difficultyControl.this, "Difficulty Set", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });
    }

}