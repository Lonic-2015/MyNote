package com.example.mynote.DataBase;

import android.database.sqlite.SQLiteDatabase;

import com.example.mynote.MyApplication;
import com.example.mynote.activities.MainActivity;

/**
 * Created by 金晨 on 2016-06-03.
 */
public class NoteDB {
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;

    public NoteDB() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(MyApplication.getContext(),"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
    }

}
