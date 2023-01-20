package com.kyle.thornton.notesapp;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotesHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_home);

        //Getting access to the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

        //Checking shared preferences for a boolean if the boolean is false then need to hide list of notes and display no notes label
        //If it returns true then need to display the list of currently saved notes
        boolean hasNotes = sharedPreferences.getBoolean("Saved", false);

        //Need to do it differently for the notes list if hasNotes need to get the data from shared preferences to display in the list view otherwise simply hide the list view
        int visibility = hasNotes ? DisplayNotes() : View.INVISIBLE;
        getNoNotesLabel().setVisibility(visibility);

        //Hiding either the no notes text depending on if hasNotes : !hasNotes
        visibility = hasNotes ? View.INVISIBLE : View.VISIBLE;
        getNoNotesLabel().setVisibility(visibility);

        //Setting an OnClickListener to the add button
        getAddButton().setOnClickListener(view -> openNewNote());
    }

    int DisplayNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        ArrayList<Note> notes;
        Gson gson = new Gson();

        //Getting the note that is stored as a JSON object then transforming it back into ArrayList<Note> for displaying to the list view
        String jsonData = sharedPreferences.getString("JSON", "");

        Type type = new TypeToken<ArrayList<Note>>(){}.getType();
        notes = gson.fromJson(jsonData, type);

        //Creating an array adapter
        NoteListAdapter arrayAdapter = new NoteListAdapter(this, notes);

        getNotesListView().setAdapter(arrayAdapter);

        //User has clicked on a note, open the note
        getNotesListView().setOnItemClickListener((adapterView, view, i, l) -> {
            //TODO: Open the note as a new Intent
            Log.i("On item click", "Editing the note");
        });

        //User has long held on the note, ask to delete the note
        ArrayList<Note> finalNotes = notes;
        getNotesListView().setOnItemLongClickListener((adapterView, view, i, l) -> {
            //Display a dialog box that asks the user to confirm that they want to delete the note
            AlertDialog.Builder builder = new AlertDialog.Builder((view.getContext()));
            String msg = "Would you like to delete note: " + finalNotes.get(i);

            DialogInterface.OnClickListener di = ((dialogInterface, j) -> {
                //Has the user pressed yes?
                if(j == DialogInterface.BUTTON_POSITIVE) {
                    //User wants to delete the selected note
                    Log.i("Deleting Note", String.format("Note %s is being deleted", finalNotes.get(i)));
                    //TODO: Would delete the note here and then notify the adapter
                }
            });
            //Displaying the dialog interface to the user
            builder.setMessage(msg).setPositiveButton("Yes", di).setNegativeButton("No", di);
            builder.show();
            return true;
        });

        //Displaying the list view
        return View.VISIBLE;
    }

    void openNewNote() {
        //User wants to add a new note so opening notes page
        Intent intent = new Intent(this, NewNote.class);
        startActivity(intent);
    }

    private ListView getNotesListView() {
        return findViewById(R.id.homeNotesListView);
    }
    private FloatingActionButton getAddButton() {
        return findViewById(R.id.homeAddNoteButton);
    }
    private TextView getNoNotesLabel() {
        return findViewById(R.id.homeNoNotesLabel);
    }
}