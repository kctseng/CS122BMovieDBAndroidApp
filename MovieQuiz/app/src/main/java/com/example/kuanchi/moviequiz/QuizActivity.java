package com.example.kuanchi.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuanchi on 3/3/2015.
 */
public class QuizActivity extends Activity
{
    int correctAnswer = 0;
    int correct = 0;
    int wrong = 0;
    int totalQ = 0;

    int cursorCount = 0;
    ArrayList<Button> choicesList = new ArrayList<Button>();
    Cursor cursor;
    TextView q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        MovieDB db = new MovieDB(this);
        q = (TextView)findViewById(R.id.question);
        Button b1 = (Button)findViewById(R.id.choice1);
        Button b2 = (Button)findViewById(R.id.choice2);
        Button b3 = (Button)findViewById(R.id.choice3);
        Button b4 = (Button)findViewById(R.id.choice4);
        this.choicesList.add(b1);
        this.choicesList.add(b2);
        this.choicesList.add(b3);
        this.choicesList.add(b4);
        this.cursor = db.fetchAll();
        cursor.moveToFirst();
        this.cursorCount = cursor.getCount();
        generateQuestionAndChoices();
    }

    private void generateQuestionAndChoices()
    {
        Random random = new Random();
        int format = random.nextInt(2);
        int position = random.nextInt(cursorCount);
        correctAnswer = random.nextInt(4);
        switch(format)
        {
            case 0:
                q.setText("Who directed the movie " + cursor.getString(2) + "?");
                int originalPos = cursor.getPosition();
                for(int i = 0; i<4; i++)
                {
                    if(i==correctAnswer)
                    {
                        cursor.moveToPosition(originalPos);
                        choicesList.get(i).setText(cursor.getString(4).replace("\"", ""));
                    }
                    else
                    {
                        cursor.moveToPosition(cursor.getPosition()+20);
                        choicesList.get(i).setText(cursor.getString(4).replace("\"",""));
                    }
                }
                cursor.moveToPosition(position);
                break;
            case 1:
                q.setText("When was the movie " + cursor.getString(2) + " released?");
                for(int i = 0; i<4; i++)
                {
                    if(i==correctAnswer)
                    {
                        choicesList.get(i).setText(String.valueOf(cursor.getInt(3)));
                    }
                    else
                    {
                        choicesList.get(i).setText(String.valueOf(cursor.getInt(3)+1+i));
                    }
                }
                cursor.moveToPosition(position);
                break;
            case 2:

                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
        }
    }

    public void checkAnswer(View button)
    {
        if((correctAnswer == 0 && button.getId()==R.id.choice1)
                || (correctAnswer == 1 && button.getId()==R.id.choice2)
                || (correctAnswer == 2 && button.getId()==R.id.choice3)
                || (correctAnswer == 3 && button.getId()==R.id.choice4))
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
