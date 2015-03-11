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
import android.os.SystemClock;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Kuanchi on 3/3/2015.
 */
public class QuizActivity extends Activity
{
    int correctAnswer = 0;
    private TextView mTimeLabel;
    private Handler mHandler = new Handler();

    private static final long duration = 180000;
    int correct = 0;
    int wrong = 0;
    int totalQ = 0;
    MovieDB db;
    int cursorCount = 0;
    ArrayList<Button> choicesList = new ArrayList<Button>();
    Cursor cursor;
    TextView q;

    long elapsed = 0;


    private Runnable updateTask = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long timeLeft = duration - elapsed;
            if (elapsed < duration)
            {
                int seconds = (int) (timeLeft / 1000);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                if (seconds < 10) {
                    mTimeLabel.setText("" + minutes + ":0" + seconds);
                } else {
                    mTimeLabel.setText("" + minutes + ":" + seconds);
                }
                elapsed += 1000;
                mHandler.postAtTime(this, now + 1000);
            }
            else {
                elapsed = 0;
                mHandler.removeCallbacks(this);
                finish();
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("total", totalQ);
                intent.putExtra("correct", correct);
                intent.putExtra("wrong", wrong);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("Question", q.getText().toString());
        savedInstanceState.putString("c1", choicesList.get(0).getText().toString());
        savedInstanceState.putString("c2", choicesList.get(1).getText().toString());
        savedInstanceState.putString("c3", choicesList.get(2).getText().toString());
        savedInstanceState.putString("c4", choicesList.get(3).getText().toString());
        savedInstanceState.putInt("total", totalQ);
        savedInstanceState.putInt("correct", correct);
        savedInstanceState.putLong("elapsed", elapsed);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        totalQ = savedInstanceState.getInt("total");
        correct = savedInstanceState.getInt("correct");
        q.setText(savedInstanceState.getString("Question"));
        choicesList.get(0).setText(savedInstanceState.getString("c1"));
        choicesList.get(1).setText(savedInstanceState.getString("c2"));
        choicesList.get(2).setText(savedInstanceState.getString("c3"));
        choicesList.get(3).setText(savedInstanceState.getString("c4"));
        elapsed = savedInstanceState.getLong("elapsed");
    }

    @Override
    public void onPause()
    {
        mHandler.removeCallbacks(updateTask);
        super.onPause();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        mHandler.post(updateTask);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        db = new MovieDB(this);
        q = (TextView)findViewById(R.id.question);
        mTimeLabel = (TextView)findViewById(R.id.clock);



        Button b1 = (Button)findViewById(R.id.choice1);
        Button b2 = (Button)findViewById(R.id.choice2);
        Button b3 = (Button)findViewById(R.id.choice3);
        Button b4 = (Button)findViewById(R.id.choice4);
        this.choicesList.add(b1);
        this.choicesList.add(b2);
        this.choicesList.add(b3);
        this.choicesList.add(b4);
        this.cursor = db.fetchAll();
        Random r = new Random();
        this.cursorCount = cursor.getCount();
        cursor.move(r.nextInt(cursorCount-1));
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
        q.setText("Who directed the movie " + cursor.getString(0) + "?");
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
            choicesList.get(i).setText(cursor.getString(2).replace("\"",""));
        }
    }

    private void question1()
    {
        q.setText("When was the movie " + cursor.getString(0) + " released?");
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                choicesList.get(i).setText(String.valueOf(cursor.getInt(1)));
            }
            else
            {
                choicesList.get(i).setText(String.valueOf(cursor.getInt(1)+1+i));
            }
        }
    }

    private void question2()
    {
        q.setText("Which star was in the movie " + cursor.getString(0) + "?");
        int originalPos = cursor.getPosition();
        Random r = new Random();
        HashSet<String> choices = new HashSet<String>();
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                cursor.moveToPosition(originalPos);
            }
            else
            {
                int jump = r.nextInt(40)+20;
                if(cursor.getPosition()+jump >= cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.moveToPosition(cursor.getPosition() + jump);
            }
            while(choices.contains(cursor.getString(3).replace("\"", "")+" " + cursor.getString(4).replace("\"", "")))
            {
                cursor.move(1);
            }
            String text = cursor.getString(3).replace("\"", "")+" " + cursor.getString(4).replace("\"", "");
            choices.add(text);
            choicesList.get(i).setText(text);
        }
    }

    private void question3()
    {
        cursor = db.groupByMovie();
        Random r = new Random();
        cursor.moveToPosition(r.nextInt(cursorCount-1));
        String starA = "";
        String starB = "";
        String movie = cursor.getString(0);
        String s = cursor.getString(3) + " " + cursor.getString(4);
        cursor.moveToNext();
        if(cursor.isAfterLast())
        {
            cursor.moveToFirst();
        }
        while(!cursor.getString(0).equals(movie) || s.equals(cursor.getString(3) + " " + cursor.getString(4)))
        {
            movie = cursor.getString(0);
            s = cursor.getString(3) + " " + cursor.getString(4);
            cursor.moveToNext();
            if(cursor.isAfterLast())
            {
                cursor.moveToFirst();
            }
        }
        starB = s;
        starA = cursor.getString(3) + " " + cursor.getString(4);
        HashSet<String> choices = new HashSet<String>();
        movie = movie.replace("\"", "");
        choices.add(movie);
        for(int i = 0; i<4; i++)
        {
            if(i==correctAnswer)
            {
                choicesList.get(i).setText(movie);
            }
            else
            {
                if(cursor.getPosition()+20 > cursorCount)
                {
                    cursor.move(-200);
                }
                cursor.moveToPosition(cursor.getPosition()+20);
                while(choices.contains(cursor.getString(0).replace("\"", "")))
                {
                    cursor.move(1);
                }
                String text = cursor.getString(0).replace("\"", "");
                choices.add(text);
                choicesList.get(i).setText(text);
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
        HashSet<String> choices = new HashSet<String>();
        choices.add(cursor.getString(2).replace("\"", ""));
        q.setText("Which director directed " + cursor.getString(3).replace("\"", "") + " " + cursor.getString(4).replace("\"", "") + " before?");
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
                while(choices.contains(cursor.getString(2).replace("\"", "")))
                {
                    cursor.move(1);
                }
            }
            String text = cursor.getString(2).replace("\"", "");
            choicesList.get(i).setText(text);
            choices.add(text);
        }
    }

    private void question5()
    {
        Random r = new Random();
        cursor = db.groupByStar();
        cursor.moveToPosition(r.nextInt(cursorCount));
        String star = cursor.getString(3) + " " + cursor.getString(4);
        cursor.moveToNext();
        while(!(cursor.getString(3) + " "+ cursor.getString(4)).equals(star))
        {
            star = cursor.getString(3) + " " + cursor.getString(4);
            cursor.moveToNext();
            if(cursor.isAfterLast())
            {
                cursor.moveToFirst();
            }
        }

        String movieA = cursor.getString(0);
        cursor.moveToPrevious();
        String movieB = cursor.getString(0);

        q.setText("Which star appears in both " + movieA + " and " + movieB + "?");

        HashSet<String> choices = new HashSet<String>();
        String c = star.replace("\"", "");
        choices.add(c);
        for(int i = 0; i< 4; i++)
        {
            if(correctAnswer == i)
            {
               choicesList.get(i).setText(c);
            }
            else
            {
                if(cursor.getPosition()+15 > cursorCount)
                {
                    cursor.move(-270);
                }
                cursor.move(15);
                while(choices.contains((cursor.getString(3) + " " + cursor.getString(4)).replace("\"", "")))
                {
                    cursor.move(1);
                }
                String text = (cursor.getString(3) + " " + cursor.getString(4)).replace("\"", "");
                choices.add(text);
                choicesList.get(i).setText(text);
            }
        }
    }

    private void question6()
    {
        Random r = new Random();
        cursor = db.groupByMovie();
        cursor.moveToPosition(r.nextInt(cursorCount));
        String movie = cursor.getString(0);
        String star = cursor.getString(3) + " " + cursor.getString(4);
        String[] stars = new String[3];
        HashSet<String> choices = new HashSet<String>();
        choices.add(star);
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
                if(cursor.getString(0).equals(movie) && !choices.contains(cursor.getString(3) + " " + cursor.getString(4)))
                {
                    stars[i] = cursor.getString(3) + " " + cursor.getString(4);
                    cursor.moveToNext();
                    if(cursor.isAfterLast())
                    {
                        break;
                    }
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
            movie = cursor.getString(0);
        }
        star = star.replace("\"", "");
        q.setText("Which star did not appear in the same movie with " + star + "?");

        int count = 0;
        for(int i = 0; i < 4; i++)
        {
            if(correctAnswer == i)
            {
                while(choices.contains(cursor.getString(3) + " " + cursor.getString(4)))
                {
                    cursor.moveToPosition(r.nextInt(cursorCount));
                }
                choicesList.get(i).setText((cursor.getString(3) + " " + cursor.getString(4)).replace("\"", ""));
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
        q.setText("Who directed the actor/actress " + cursor.getString(3).replace("\"", "") + " " + cursor.getString(4).replace("\"", "") + " in year " + String.valueOf(cursor.getInt(1)) + "?");
        int ori = cursor.getPosition();
        HashSet<String> choices = new HashSet<String>();
        choices.add(cursor.getString(2));
        for(int i = 0; i < 4; i++)
        {
            if(correctAnswer == i)
            {
                cursor.moveToPosition(ori);
            }
            else {
                cursor.move(26);
                if (cursor.isAfterLast())
                {
                    cursor.move(-210);
                }
                while(choices.contains(cursor.getString(2)))
                {
                    cursor.move(1);
                }
            }
            String text = cursor.getString(2);
            choices.add(text);
            choicesList.get(i).setText(text.replace("\"", ""));
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
        intent.putExtra("elapsed", elapsed);
        mHandler.removeCallbacks(updateTask);
        startActivity(intent);
    }

}
