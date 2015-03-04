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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            correct = extras.getInt("correct");
            total = extras.getInt("total");
            wrong = extras.getInt("wrong");
        }

        if(total!=0) {
            ((TextView) findViewById(R.id.result)).setText("You Got " + correct + "/" + total + " Correct!");
            ((TextView) findViewById(R.id.percent)).setText("Score: " + ((double)correct / (double)total)*100 + "/100");
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
