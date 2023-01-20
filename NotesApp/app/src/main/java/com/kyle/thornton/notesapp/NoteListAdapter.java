package com.kyle.thornton.notesapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

//Used to display my own custom list view layout
public class NoteListAdapter extends ArrayAdapter<Note> {
    ArrayList<Note> allNotes;
    Context myContext;

    public NoteListAdapter(@NonNull Context context, @NonNull ArrayList<Note> objects) {
        super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
        allNotes = objects;
        myContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.activity_notes_list_view, parent, false);

        //Getting the text views from the custom layout
        TextView noteTitle = view.findViewById(R.id.NoteListTitle);
        TextView noteTimeSaved = view.findViewById(R.id.NoteListTimeSaved);
        TextView noteAdditionalText = view.findViewById(R.id.NoteListAdditionalText);

        //Setting the text views to display the correct note details
        noteTitle.setText(allNotes.get(position).getNoteTitle());
        noteTimeSaved.setText(allNotes.get(position).getNoteTime());

        //Want to do the additional text a little differently - only display a part of the note
        String additionalText = allNotes.get(position).getNoteText();
        //Want to shorten the additional text displayed if the note holds more than 40 characters
        int wordCount = additionalText.length();
        int wordLimit = 40;

        String shortenedNote = (wordCount > wordLimit) ? additionalText.substring(0, wordLimit) : additionalText;
        noteAdditionalText.setText(shortenedNote);

        return view;
    }
}
