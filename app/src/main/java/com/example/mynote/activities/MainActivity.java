package com.example.mynote.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        initView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditNote("no",id);
                Toast.makeText(MainActivity.this,id+"",Toast.LENGTH_SHORT);
                Log.d("TAG","ListView id--------->"+id);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.execSQL("delete from Note where id = ?",new String[]{(id+1)+""});
                refreshNoteList();
                return false;
            }
        });

        //新建笔记
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("insert into Note(notetitle,notecontent) values(?,?)",
                        new String[]{"",""});
                goToEditNote("yes",noteList.size());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshNoteList();
    }

    private void initDataBase(){
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
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

    private void goToEditNote(String i,long id){
        Intent intent=new Intent(MainActivity.this,EditNote.class);
        intent.putExtra("is_the_first_time_goToEditNote",i);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
