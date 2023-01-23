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
import java.util.concurrent.atomic.AtomicBoolean;

//TODO: Save the current date / time of the note being saved
public class NotesHome extends AppCompatActivity {
    //TEMP
    NoteUtilities utilities = new NoteUtilities();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_home);

        //Getting access to the shared preferences
        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

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

    //If this is called then the user has to have a saved note(s) in the shared preferences, display this data and also be able to interact with the data
    int DisplayNotes() {
        ArrayList<Note> notes;

        //Getting the note that is stored as a JSON object then transforming it back into ArrayList<Note> for displaying to the list view
        String jsonData = sharedPreferences.getString("JSON", "");
        notes = utilities.convertToNoteList(jsonData);

        //Creating an array adapter
        NoteListAdapter arrayAdapter = new NoteListAdapter(this, notes);

        //Setting the array adapter
        getNotesListView().setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        //User has clicked on a note, open the note
        getNotesListView().setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, NewNote.class);
            intent.putExtra("Editing a note", true);
            intent.putExtra("Position", i);
            startActivity(intent);
        });

        //User has long held on the note, ask to delete the note
        ArrayList<Note> finalNotes = notes;

        getNotesListView().setOnItemLongClickListener((adapterView, view, i, l) -> {
            //Display a dialog box that asks the user to confirm that they want to delete the note
            AlertDialog.Builder builder = new AlertDialog.Builder((view.getContext()));
            String msg = "Would you like to delete note: " + finalNotes.get(i).getNoteTitle();


            DialogInterface.OnClickListener di = ((dialogInterface, j) -> {
                //Has the user pressed yes?
                if(j == DialogInterface.BUTTON_POSITIVE) {
                    //User wants to delete the selected note
                    finalNotes.remove(i);

                    //Notifying the array adapter of the changes to update the listview
                    arrayAdapter.notifyDataSetChanged();

                    String data = utilities.convertToString(finalNotes);
                    sharedPreferences.edit().putString("JSON", data).apply();

                    //Now need to display the no note label if the notes array is empty
                    int visibility = utilities.isNoteListEmpty(finalNotes) ? View.VISIBLE : View.INVISIBLE;
                    getNoNotesLabel().setVisibility(visibility);

                    //Setting data saved to false if the list is empty but the reverse as true will be returned if emptied
                    boolean dataSaved = !utilities.isNoteListEmpty(finalNotes);
                    sharedPreferences.edit().putBoolean("Saved", dataSaved).apply();
                }
            });
            //Displaying the dialog interface to the user
            builder.setMessage(msg).setPositiveButton("Yes", di).setNegativeButton("No", di);
            builder.show();
            return true;
        });
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