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
    MovieDB db;
    int cursorCount = 0;
    ArrayList<Button> choicesList = new ArrayList<Button>();
    Cursor cursor;
    TextView q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        db = new MovieDB(this);
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
        int format = random.nextInt(8);
        int position = random.nextInt(cursorCount);
        correctAnswer = random.nextInt(4);
        switch(format)
        {
            case 0:
                question0();
                cursor.moveToPosition(position);
                break;
            case 1:
                question1();
                cursor.moveToPosition(position);
                break;
            case 2:
                question2();
                cursor.moveToPosition(position);
                break;
            case 3:
                question3();
                cursor.moveToPosition(position);
                break;
            case 4:
                question4();
                cursor.moveToPosition(position);
                break;
            case 5:
                question5();
                cursor.moveToPosition(position);
                break;
            case 6:
                question6();
                cursor.moveToPosition(position);
                break;
            case 7:
                question7();
                cursor.moveToPosition(position);
                break;
        }
    }



    private void question0()
    {
        q.setText("Who directed the movie " + cursor.getString(2) + "?");
        int originalPos = cursor.getPosition();
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                cursor.moveToPosition(originalPos);
            }
            else
            {
                if(cursor.getPosition()+20 > cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.moveToPosition(cursor.getPosition()+20);
            }
            choicesList.get(i).setText(cursor.getString(4).replace("\"",""));
        }
    }

    private void question1()
    {
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
    }

    private void question2()
    {
        q.setText("Which star was in the movie " + cursor.getString(2) + "?");
        int originalPos = cursor.getPosition();
        Random r = new Random();
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                cursor.moveToPosition(originalPos);
            }
            else
            {
                int jump = r.nextInt(40)+20;
                if(cursor.getPosition()+jump > cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.moveToPosition(cursor.getPosition()+jump);
            }
            choicesList.get(i).setText(cursor.getString(5).replace("\"", "")+" " + cursor.getString(6).replace("\"", ""));
        }
    }

    private void question3()
    {
        String starA = "";
        String starB = "";
        String movie = cursor.getString(2);
        cursor.moveToNext();
        while(!cursor.getString(2).equals(movie))
        {
            movie = cursor.getString(2);
            cursor.moveToNext();
            if(cursor.isAfterLast())
            {
                cursor.moveToFirst();
            }
        }
        starB = cursor.getString(5) + " " + cursor.getString(6);
        cursor.moveToPrevious();
        starA = cursor.getString(5) + " " + cursor.getString(6);
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                choicesList.get(i).setText(movie.replace("\"", ""));
            }
            else
            {
                if(cursor.getPosition()+20 > cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.moveToPosition(cursor.getPosition()+20);
                choicesList.get(i).setText(cursor.getString(2).replace("\"", ""));
            }
        }
        q.setText("Which movie was " + starA.replace("\"", "") + " and " + starB.replace("\"", "") + " appear together?");
    }

    private void question4()
    {
        Random r = new Random();
        cursor = db.groupByStar();
        cursor.moveToPosition(r.nextInt(cursorCount));
        int originalPos = cursor.getPosition();
        q.setText("Which director directed " + cursor.getString(5).replace("\"", "") + " " + cursor.getString(6).replace("\"", "") + " before?");
        for(int i = 0; i<4; i++)
        {
            if(correctAnswer == i)
            {
                cursor.moveToPosition(originalPos);
            }
            else
            {
                while(cursor.getPosition()+30 > cursorCount)
                {
                    cursor.move(-250);
                }
                cursor.move(20);
            }
            choicesList.get(i).setText(cursor.getString(4).replace("\"", ""));
        }
    }

    private void question5()
    {
        Random r = new Random();
        cursor = db.groupByStar();
        cursor.moveToPosition(r.nextInt(cursorCount));
        String star = cursor.getString(5) + " " + cursor.getString(6);
        cursor.moveToNext();
        while(!(cursor.getString(5) + " "+ cursor.getString(6)).equals(star))
        {
            star = cursor.getString(5) + " " + cursor.getString(6);
            cursor.moveToNext();
            if(cursor.isAfterLast())
            {
                cursor.moveToFirst();
            }
        }

        String movieA = cursor.getString(2).replace("\"", "");
        cursor.moveToPrevious();
        String movieB = cursor.getString(2).replace("\"", "");

        q.setText("Which star appears in both " + movieA + " and " + movieB + "?");

        int originalPos = cursor.getPosition();
        for(int i = 0; i< 4; i++)
        {
            if(correctAnswer == i)
            {
               choicesList.get(i).setText(star.replace("\"", ""));
            }
            else
            {
                if(cursor.getPosition()+15 > cursorCount)
                {
                    cursor.move(-270);
                }
                cursor.move(15);
                choicesList.get(i).setText((cursor.getString(5) + " " + cursor.getString(6)).replace("\"", ""));
            }
        }
    }

    private void question6()
    {
        Random r = new Random();
        cursor = db.groupByMovie();
        cursor.moveToPosition(r.nextInt(cursorCount));
        String movie = cursor.getString(2);
        String star = cursor.getString(5) + " " + cursor.getString(6);
        String[] stars = new String[3];
        boolean notFound = true;
        while(notFound)
        {
            cursor.moveToNext();
            if(cursor.isAfterLast())
            {
                cursor.moveToFirst();
            }
            for(int i = 0; i<3; i++)
            {
                if(cursor.getString(2).equals(movie))
                {
                    stars[i] = cursor.getString(5) + " " + cursor.getString(6);
                    cursor.moveToNext();
                }
                else
                {
                    break;
                }
                if(i==2)
                {
                    notFound = false;
                }
            }
            movie = cursor.getString(2);
        }
        q.setText("Which star did not appear in the same movie with " + star.replace("\"", "") + "?");

        int count = 0;
        for(int i = 0; i < 4; i++)
        {
            if(correctAnswer == i)
            {
                cursor.moveToPosition(r.nextInt(cursorCount));
                choicesList.get(i).setText((cursor.getString(5) + " " + cursor.getString(6)).replace("\"", ""));
            }
            else
            {
                choicesList.get(i).setText(stars[count].replace("\"", ""));
                count++;
            }
        }
    }

    private void question7()
    {
        q.setText("Who directed " + cursor.getString(5).replace("\"", "") + " " + cursor.getString(6).replace("\"", "") + " in year " + String.valueOf(cursor.getInt(3)) + "?");
        int ori = cursor.getPosition();
        for(int i = 0; i < 4; i++)
        {
            if(correctAnswer == i)
            {
                cursor.moveToPosition(ori);
            }
            else
            {
                if(cursor.getPosition()+26 > cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.move(26);
            }
            choicesList.get(i).setText(cursor.getString(4).replace("\"", ""));
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
