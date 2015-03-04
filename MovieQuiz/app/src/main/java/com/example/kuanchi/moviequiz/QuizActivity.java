package com.example.kuanchi.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kuanchi on 3/3/2015.
 */
public class QuizActivity extends Activity
{
    ArrayList<String> questionFormat = new ArrayList<String>();
    int correctAnswer = 0;
    int correct = 0;
    int wrong = 0;
    int totalQ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setupDB();
    }

    private void setupDB()
    {

    }

    private void generateQuestionAndChoices()
    {

    }

    public void checkAnswer(View button)
    {
        if((correctAnswer == 1 && button.getTag().equals("c1"))
                || (correctAnswer == 2 && button.getTag().equals("c2"))
                || (correctAnswer == 3 && button.getTag().equals("c3"))
                || (correctAnswer == 4 && button.getTag().equals("c4")))
        {
            correct++;
            ((TextView)findViewById(R.id.result)).setText("Correct!");
            generateQuestionAndChoices();
        }
        else
        {
            wrong++;
            ((TextView)findViewById(R.id.result)).setText("Incorrect!");
            generateQuestionAndChoices();
        }
        totalQ++;
    }

    public void viewResult(View btn)
    {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("total", totalQ);
        intent.putExtra("correct", correct);
        intent.putExtra("wrong", wrong);
        startActivity(intent);

    }


}
