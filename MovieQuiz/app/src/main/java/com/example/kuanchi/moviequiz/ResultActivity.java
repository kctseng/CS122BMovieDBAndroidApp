package com.example.kuanchi.moviequiz;

import android.app.Activity;
import android.content.ContentValues;
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
public class ResultActivity extends Activity
{
    int correct = 0;
    int wrong = 0;
    int total = 0;
    long elapsed = 0;

    MovieDB db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MovieDB(this);

        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            correct = extras.getInt("correct");
            total = extras.getInt("total");
            wrong = extras.getInt("wrong");
            elapsed = extras.getLong("elapsed");
        }



        Cursor cursor = db.getStat();
        while(cursor.moveToNext())
        {
            int newwrong = wrong + cursor.getInt(cursor.getColumnIndex("wrong"));
            int newcorrect = correct +  cursor.getInt(cursor.getColumnIndex("correct"));
            int newtotal = total + cursor.getInt(cursor.getColumnIndex("total"));
            long newelapsed  =  elapsed + cursor.getInt(cursor.getColumnIndex("elapsed"));
            ContentValues statContent = new ContentValues();
            statContent.put("wrong", newwrong);
            statContent.put("correct", newcorrect);
            statContent.put("total", newtotal);
            statContent.put("elapsed", newelapsed);
            db.updateStat(statContent);
            break;
        }



        //SharedPreferences settings = getSharedPreferences("history", 0);



//        SharedPreferences.Editor editor = settings.edit();
//        editor.putInt("total", oldtotal + total);
//        editor.putInt("correct", oldcorrect + correct);
//        editor.putInt("wrong", oldwrong + wrong);
//        editor.putLong("elapsed", oldelapsed + elapsed);
//        // Commit the edits!
//        editor.commit();

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
