package com.example.mynote.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mynote.R;
import com.example.mynote.classes.Note;

import java.util.List;

/**
 * Created by 金晨 on 2016-06-02.
 */
public class NoteAdapter extends ArrayAdapter<Note>{
    private int resourceId;//ListView子项的布局

    public NoteAdapter(Context context, int textViewResourceId, List<Note> objects) {
        super(context,  textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView itemName = (TextView)view.findViewById(R.id.item_name);
        itemName.setText(note.getTitle());
        return view;
    }
}
