package com.example.kuanchi.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kuanchi on 3/3/2015.
 */
public class StatActivity extends Activity
{
    int correct = 0;
    int wrong = 0;
    int total = 0;
    long elapsed = 0;


    MovieDB db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        db = new MovieDB(this);

        Cursor cursor = db.getStat();
        while(cursor.moveToNext())
        {
            wrong = cursor.getInt(cursor.getColumnIndex("wrong"));
            correct = cursor.getInt(cursor.getColumnIndex("correct"));
            total = cursor.getInt(cursor.getColumnIndex("total"));
            elapsed = cursor.getInt(cursor.getColumnIndex("elapsed"));
            break;
        }

//        SharedPreferences settings = getSharedPreferences("history", 0);
//
//        total = settings.getInt("total", 0);
//        wrong = settings.getInt("wrong", 0);
//        correct = settings.getInt("correct", 0);
//        elapsed = settings.getLong("elapsed", 0);


        if(total!=0) {
            ((TextView) findViewById(R.id.result)).setText("You Got " + correct + "/" + total + " Correct!");
            ((TextView) findViewById(R.id.percent)).setText("Score: " + String.format("%.2f", (float)correct / (float)total * 100 )+ "/100");
            ((TextView) findViewById(R.id.time)).setText("Average Time spent on each question: " + String.format("%.2f", (elapsed / total) / 1000f) + " Seconds");
        }
        else
        {
            ((TextView) findViewById(R.id.result)).setText("You answered no questions.");
        }
    }

    public void restart(View btn)
    {
        Intent intent = new Intent(StatActivity.this, MainActivity.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
