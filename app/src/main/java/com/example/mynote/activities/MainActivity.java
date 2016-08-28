package com.example.mynote.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.mynote.DataBase.MySQLiteOpenHelper;
import com.example.mynote.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lv;
    private FloatingActionButton addNewNote;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor cursor1;
    SimpleCursorAdapter adapter;
    String[] time;//用于删除笔记时，判断当前时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,"NoteStore.db",null,1);
        db = mySQLiteOpenHelper.getWritableDatabase();
        initView();

        cursor=db.query("Note",null,null,null,null,null,null);
        adapter = new SimpleCursorAdapter(
                MainActivity.this,R.layout.note_item_layout,cursor,
                new String[]{"notetitle","notecontent","modifytime"},
                new int[]{R.id.item_title,R.id.item_content,R.id.modify_time});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,EditNote.class);
                intent.putExtra("modifytime", ((TextView)view.findViewById(R.id.modify_time)).getText());
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                time = new String[]{((TextView)view.findViewById(R.id.modify_time)).getText().toString()};
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("是否删除？");
                alertDialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("Note","modifytime = ?",time);
                        refreshListView();
                    }
                });
                alertDialog.setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
                return true;//TODO 有问题
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        cursor1.close();
        db.close();
    }

    private void initView(){
        lv=(ListView)findViewById(R.id.lv);
        addNewNote = (FloatingActionButton) findViewById(R.id.add_new_note);
        addNewNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_new_note:
                Intent intent = new Intent(MainActivity.this,EditNote.class);
                startActivity(intent);
                break;
        }
    }

    private void refreshListView(){
        //动态更新ListView
        /*这个动态更新ListView的方法已经被废弃了
        adapter.getCursor().requery();
        adapter.notifyDataSetChanged();*/
        cursor1 = db.query("Note",null,null,null,null,null,null);
        adapter.changeCursor(cursor1);
    }
}
