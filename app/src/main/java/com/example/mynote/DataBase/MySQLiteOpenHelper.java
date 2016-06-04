package com.example.mynote.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 金晨 on 2016-06-02.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public static final String CREATE_NOTE="create table Note("
            +"id integer primary key autoincrement, "
            +"notetitle text, "
            +"notecontent text, "
            +"id2 integer)";

    private Context mContext;
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
        Toast.makeText(mContext,"数据库创建成功",Toast.LENGTH_SHORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
