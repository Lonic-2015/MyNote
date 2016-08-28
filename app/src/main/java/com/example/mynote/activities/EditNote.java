package com.example.mynote.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mynote.DataBase.MySQLiteOpenHelper;
import com.example.mynote.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 金晨 on 2016-06-02.
 */
public class EditNote extends Activity implements View.OnClickListener{
    private EditText noteTitle;
    private EditText noteContent;
    private Button deleteNote;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private String intentExtra;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
        initView();
        Intent intent=getIntent();
        intentExtra =intent.getStringExtra("modifytime");
        if (intentExtra != null){
            cursor = db.rawQuery("select * from Note where modifytime =?",new String[]{intentExtra});
            if (cursor !=null){
                if(cursor.moveToFirst()){
                    do{
                        String title=cursor.getString(cursor.getColumnIndex("notetitle"));
                        String content=cursor.getString(cursor.getColumnIndex("notecontent"));
                        noteTitle.setText(title);
                        noteContent.setText(content);
                    }while (cursor.moveToNext());

                }
            }
        }
    }

    private void initView(){
        noteTitle = (EditText)findViewById(R.id.note_title);
        noteContent = (EditText)findViewById(R.id.note_content);
        deleteNote = (Button)findViewById(R.id.deleteNote);
        deleteNote.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.deleteNote:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditNote.this);
                alertDialog.setMessage("是否删除？");
                alertDialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("Note","modifytime = ?",new String[]{intentExtra});
                        finish();
                    }
                });
                alertDialog.setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
                break;
        }

    }
    //TODO 放到Utility类中，改成静态方法
    private String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String currentTime = formatter.format(curDate);
        return currentTime;
    }

    @Override
    public void onBackPressed() {
        //保存笔记
        if (!(TextUtils.isEmpty(noteTitle.getText()) && TextUtils.isEmpty(noteContent.getText()))){
            if (intentExtra != null){
                ContentValues values = new ContentValues();
                values.put("notetitle",noteTitle.getText().toString());
                values.put("notecontent",noteContent.getText().toString());
                values.put("modifytime",getCurrentTime());
                db.update("Note",values,"modifytime = ?",new String[]{intentExtra});
                values.clear();
            }else{
                ContentValues values = new ContentValues();
                values.put("notetitle",noteTitle.getText().toString());
                values.put("notecontent",noteContent.getText().toString());
                values.put("modifytime",getCurrentTime());
                db.insert("Note",null,values);
                values.clear();
            }
        }
        super.onBackPressed();
    }
}
