package com.example.kuanchi.moviequiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kuanchi on 3/4/2015.
 */
public class MovieDB extends SQLiteOpenHelper
{
    public static String CREATE_TABLE = "Create table movieinfo " +
            "(entryID integer primary key AUTOINCREMENT, " +
            "starID integer, movieID integer, " +
            "movietitle varchar(350), year integer, " +
            "director varchar(150), " +
            "starFirstName varchar(50), " +
            "starLastName varchar(50));";

    public static String CREATE_STAT_TABLE = "Create table stat " +
            "(entryID integer primary key AUTOINCREMENT, " +
            "correct integer, wrong integer, total integer, elapsed integer);";

    public static String FILE_NAME = "all2.csv";
    public static String TABLE_NAME="movieinfo";
    public static String STAT_TABLE_NAME="stat";

    public static String DATABASE_NAME ="movieDB";
    public static int DATABASE_VERSION = 1;
    public SQLiteDatabase db;
    public Context context;
    public String[] columnNames = new String[7];
    public String[] statColumnNames = new String[4];
    public MovieDB(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        context = ctx;
        columnNames[0] = "movietitle";
        columnNames[1] = "year";
        columnNames[2] = "director";
        columnNames[3] = "starFirstName";
        columnNames[4] = "starLastName";
        statColumnNames[0] = "wrong";
        statColumnNames[1] = "correct";
        statColumnNames[2] = "total";
        statColumnNames[3] = "elapsed";

        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_STAT_TABLE);
        ContentValues statContent = new ContentValues();
        statContent.put("wrong", 0);
        statContent.put("correct", 0);
        statContent.put("total", 0);
        statContent.put("elapsed", 0);


        db.insert(STAT_TABLE_NAME, null,statContent);
        // populate database
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(context.getAssets().open(FILE_NAME)));
            String line;
            int eid = 1;
            while((line=in.readLine())!=null)
            {
                int count = 0;
                Scanner sc = new Scanner(line);
                sc.useDelimiter("%");
                ContentValues values = new ContentValues();
                values.put("entryID", eid);
                eid++;
                while(sc.hasNext() && count!=5) {
                    String value = sc.next();
                    values.put(columnNames[count], value);
                    count++;
                }
                sc.close();
                db.insert(TABLE_NAME, null, values);
            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STAT_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getStat(){

        return db.query(STAT_TABLE_NAME, statColumnNames, null, null, null, null, null);
    }

    public void updateStat(ContentValues values){
        db.update(STAT_TABLE_NAME, values, null, null);
    }
    public Cursor fetchAll() {
        return db.query(TABLE_NAME, columnNames, null, null, null, null, null);
    }

    public Cursor groupByStar()
    {
        return db.query(TABLE_NAME, columnNames, null, null, null, null, "starLastName", null);
    }

    public Cursor groupByMovie()
    {
        return db.query(TABLE_NAME, columnNames, null, null, null, null, "movietitle", null);
    }
}
