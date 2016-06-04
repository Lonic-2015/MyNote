package com.example.mynote.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mynote.DataBase.MySQLiteOpenHelper;
import com.example.mynote.MyApplication;
import com.example.mynote.R;
import com.example.mynote.adapters.NoteAdapter;
import com.example.mynote.classes.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected List<Note> noteList = new ArrayList<>();
    private ListView lv;
    private Button newNote;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
        initView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditNote("old");
            }
        });

        //新建笔记
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditNote("new");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNoteList();
    }

    private void initView(){
        lv=(ListView)findViewById(R.id.lv);
        newNote=(Button)findViewById(R.id.newNote);
    }


    private void refreshNoteList(){
        noteList.clear();
        Cursor cursor=db.rawQuery("select * from Note",null);
        if(cursor.moveToFirst()){
            do{
                String noteTitle=cursor.getString(cursor.getColumnIndex("notetitle"));
                noteList.add(new Note(noteTitle));
            }while (cursor.moveToNext());
        }
        cursor.close();
        NoteAdapter adapter = new NoteAdapter(MainActivity.this,R.layout.note_item_layout,noteList);
        lv.setAdapter(adapter);
    }

    private void goToEditNote(String fromWhere){
        Intent intent=new Intent(MainActivity.this,EditNote.class);
        intent.putExtra("fromWhere",fromWhere);
        startActivity(intent);
    }
}
