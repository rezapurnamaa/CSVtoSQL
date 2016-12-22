package com.example.reza.csvtosqlite;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by reza on 15/12/16.
 */

public class DBHelper extends AbstractDBAdapter{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_BIRTHNAME = "birthname";
    public static final String KEY_SURNAME = "surname";
    public int maxLevelOnCurrentMenu = 1;

    public int getMaxLevelOnCurrentMenu(){
        return maxLevelOnCurrentMenu;
    }

    public void setMaxLevelOnCurrentMenu(int maxLevelOnCurrentMenu){
        this.maxLevelOnCurrentMenu = maxLevelOnCurrentMenu;
    }

    public static final String DATABASE_TABLE = "stumbl";

    public DBHelper(Context context){
        super(context);
    }

    public long insertDB(String key_name, String key_birthname, String key_surname){
        ContentValues initiaValues = new ContentValues();
        initiaValues.put(KEY_NAME, key_name);
        initiaValues.put(KEY_BIRTHNAME, key_birthname);
        initiaValues.put(KEY_SURNAME, key_surname);
        return mDB.insert(DATABASE_TABLE, null, initiaValues);
    }

    public long insertDB(String[] rowData){
        long result = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, rowData[0]);
        values.put(KEY_BIRTHNAME,rowData[1]);
        values.put(KEY_SURNAME, rowData[2]);
        result = mDB.insert(DATABASE_TABLE, null, values);
        return result;
    }
}
