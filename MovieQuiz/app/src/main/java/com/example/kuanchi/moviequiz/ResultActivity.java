package com.example.kuanchi.moviequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Kuanchi on 3/3/2015.
 */
public class ResultActivity extends Activity
{
    int correct = 0;
    int wrong = 0;
    int total = 0;
    long elapsed = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            correct = extras.getInt("correct");
            total = extras.getInt("total");
            wrong = extras.getInt("wrong");
            elapsed = extras.getLong("elapsed");
        }

        if(total!=0) {
            ((TextView) findViewById(R.id.result)).setText("You Got " + correct + "/" + total + " Correct!");
            ((TextView) findViewById(R.id.percent)).setText("Score: " + String.format("%.2f", (float)correct / (float)total * 100 )+ "/100");
            ((TextView) findViewById(R.id.time)).setText("Average Time spent on each question: " + String.format("%.2f", (elapsed / total) / 1000f) + " Seconds");
        }
        else
        {
            ((TextView) findViewById(R.id.result)).setText("No Question Answered");
        }
    }

    public void restart(View btn)
    {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
