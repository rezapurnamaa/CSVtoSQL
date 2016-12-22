package com.example.reza.csvtosqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by reza on 14/12/16.
 */

public abstract class AbstractDBAdapter {

    protected static final String TAG = "StumblDBAdapter";
    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDB;

    protected static final String CONFIG_TABLE_CREATE =
            "create table stumbl (_id integer primary key," + "config_name text not null," + "config_value text," + " createdAt text, " + " updatedAt text);";


    protected static final String DATABASE_NAME = "stumbl";
    protected static final int DATABASE_VERSION = 2;

    protected final Context context;

    public AbstractDBAdapter(Context context){
        this.context = context;
    }

    public AbstractDBAdapter open() throws SQLException {
        mDBHelper = new DatabaseHelper(context);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDBHelper != null){
            mDBHelper.close();
        }
    }

    protected static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CONFIG_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CONFIG_TABLE_CREATE");
        }
    }
}
