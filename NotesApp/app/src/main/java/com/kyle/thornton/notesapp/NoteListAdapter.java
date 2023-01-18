package com.kyle.thornton.notesapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

//Used to display my own custom list view layout
public class NoteListAdapter extends ArrayAdapter<String> {
    String[] allNotes;
    Context myContext;

    public NoteListAdapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        allNotes = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_notes_list_view, parent, false);

        //Getting the text views from the custom layout
        TextView noteTitle = view.findViewById(R.id.NoteListTitle);
        TextView noteTimeSaved = view.findViewById(R.id.NoteListTimeSaved);
        TextView noteAdditionalText = view.findViewById(R.id.NoteListAdditionalText);

        //TODO: Actually get the correct information to display
        //Setting the text views to display the correct note details
        noteTitle.setText(allNotes[position]);

        return view;
    }
}
