package com.kyle.thornton.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.kyle.thornton.notesapp.NoteListAdapter;

public class NotesHome extends AppCompatActivity {

    ListView listView;
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_home);

        //TEMP
        listView = findViewById(R.id.homeNotesListView);

        //TESTING SHARED PREFERENCES
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String title = sharedPreferences.getString("Note Title", "");
        Log.i("Note Title", title);

        //Checking shared preferences for a boolean if the boolean is false then need to hide list of notes and display no notes label
        //If it returns true then need to display the list of currently saved notes
        boolean hasNotes = sharedPreferences.getBoolean("Saved", false);

        if(!hasNotes) {
            listView.setVisibility(View.INVISIBLE);
        }

        //TODO: Break this into methods that are called to make code more readable

        //------------------------------------------------------------------------------------------
        // SETTING ON CLICK(s) FOR SELECTING NOTES
        //------------------------------------------------------------------------------------------
        //Creating a TEMP fake list of notes
        String notes[] = {"Apple", "Banana", "Grapes"};

/*        //Initialising the ListView (Holds the notes)
        listView = findViewById(R.id.homeNotesListView);*/

        //Creating an array adapter
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_notes_list_view, R.id.NoteTitle, notes);
        NoteListAdapter arrayAdapter = new NoteListAdapter(this, notes);

        listView.setAdapter(arrayAdapter);

        //User has clicked on a note, open the note
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            //TODO: Open the note as a new Intent
            Log.i("On item click", "Editing the note");
        });

        //User has long held on the note, ask to delete the note
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            //Display a dialog box that asks the user to confirm that they want to delete the note
            AlertDialog.Builder builder = new AlertDialog.Builder((view.getContext()));
            String msg = "Would you like to delete note: " + notes[i];

            DialogInterface.OnClickListener di = ((dialogInterface, j) -> {
                //Has the user pressed yes?
                if(j == DialogInterface.BUTTON_POSITIVE) {
                    //User wants to delete the selected note
                    Log.i("Deleting Note", "Note " + notes[i] + " is being deleted");
                    //TODO: Would delete the note here and then notify the adapter
                }
            });
            //Displaying the dialog interface to the user
            builder.setMessage(msg).setPositiveButton("Yes", di).setNegativeButton("No", di);
            builder.show();
            return true;
        });

        //------------------------------------------------------------------------------------------
        // ADDING FUNCTIONALITY FOR THE FLOATING ACTION BUTTON (ADDING A NOTE)
        //------------------------------------------------------------------------------------------
        //Still adding to the fake TEMP notes array for rapid prototyping
        button = findViewById(R.id.homeAddNoteButton);

        //Setting an OnClickListener to the add button
        button.setOnClickListener(view -> {
            //User wants to add a new note so opening notes page
            Intent intent = new Intent(this, NewNote.class);
            startActivity(intent);
        });
    }
}