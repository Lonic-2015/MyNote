package com.example.mynote.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mynote.DataBase.MySQLiteOpenHelper;
import com.example.mynote.R;

/**
 * Created by 金晨 on 2016-06-02.
 */
public class EditNote extends Activity{
    private String id;
    private EditText noteTitle;
    private EditText noteContent;
    private Button saveNote;
    private Button queryNote;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        initDataBase();
        initView();
        Intent intent=getIntent();
        fromWhere =intent.getStringExtra("fromWhere");
        //if(fromWhere.equals("new"))
        if(fromWhere.equals("old")){
            resumeContent();
            Log.d("TAG","resumeContent执行了");
        }
        //保存笔记
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fromWhere){
                    case("new"):
                        db.execSQL("insert into Note(notetitle,notecontent) values(?,?)",
                                new String[]{"",""});
                        SQLiteStatement statement = db.compileStatement("select count(*) from Note");
                        long id_in_table = statement.simpleQueryForLong();
                        Log.d("TAG","id_in_table----->"+id_in_table);
                        //此处不写break
                    case("old"):
                        //************更新表项****************
                        if(noteTitle.getText().toString().equals("")==false
                                &&noteContent.getText().toString().equals("")==false){
                            db.execSQL("update Note set notetitle=? where id=?",
                                    new String[]{noteTitle.getText().toString(),id+""});
                            db.execSQL("update Note set notecontent=? where id=?",
                                    new String[]{noteContent.getText().toString(),id+""});
                        }else if(noteTitle.getText().toString().equals("")==true
                                &&noteContent.getText().toString().equals("")==false){
                            db.execSQL("update Note set notetitle=? where id=?",
                                    new String[]{"未命名笔记",id+""});
                        }else{
                            //删除数据库中的表项
                        }
                        //*************************************
                        break;
                }

            }
        });

        queryNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=db.rawQuery("select * from Note",null);
                if(cursor.moveToFirst()){
                    do{
                        int id=cursor.getInt(cursor.getColumnIndex("id"));
                        String noteTitle=cursor.getString(cursor.getColumnIndex("notetitle"));
                        String noteContent=cursor.getString(cursor.getColumnIndex("notecontent"));
                        Log.d("TAG","real id_in_table is"+id);
                        Log.d("TAG","title is"+noteTitle);
                        Log.d("TAG","content is"+noteContent);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("noteTitle",noteTitle.getText());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    private void resumeContent(){
        //****************将数据库中的内容显示到编辑界面*******************
        /*Cursor cursor=db.rawQuery("select * from Note where id =?",new String[]{id+""});
        cursor.moveToLast();
        noteTitle.setText(cursor.getString(cursor.getColumnIndex("notetitle")));
        noteContent.setText(cursor.getString(cursor.getColumnIndex("notecontent")));*/
        Cursor cursor=db.rawQuery("select * from Note where id =?",new String[]{id});
        Log.d("TAG","id_in_table in EditNote"+id);
        if(cursor.moveToFirst()){
            do{
                String title=cursor.getString(cursor.getColumnIndex("notetitle"));
                String content=cursor.getString(cursor.getColumnIndex("notecontent"));
                Log.d("TAG","title--------->"+title);
                Log.d("TAG","content------->"+content);
                noteTitle.setText(title);
                noteContent.setText(content);
            }while (cursor.moveToNext());

        }
        //**************************************************
    }

    private void initDataBase(){
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
    }

    private void initView(){
        noteTitle = (EditText)findViewById(R.id.note_title);
        noteContent = (EditText)findViewById(R.id.note_content);
        saveNote = (Button) findViewById(R.id.saveNote);
        queryNote = (Button)findViewById(R.id.queryNote);
    }

    //创建表项
    private void createDataBaseTableItem(){
        db.execSQL("insert into Note(notetitle,notecontent) values(?,?)",
                new String[]{noteTitle.getText().toString(),noteContent.getText().toString()});
    }
}
