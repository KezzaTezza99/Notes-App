package com.w18024358.notesapp;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

//Important notes already
/*
    If you keep pressing enter in the field then you go off the screen. Think I need to put it into a view / container?
 */
//-----------------------------------------------
public class NotesPage extends AppCompatActivity implements SaveNoteDialog.SaveNoteDialogListener
{
    static final int RETURNED_VALUES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);

        GetSaveButton().setOnClickListener(view -> SaveNewNote());
    }

    private void SaveNewNote()
    {
        String tag = "NotesPage: SaveNewNote()";

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Checking to see if it is a new note - if so need to save the note to the SharedPreferences
        if(GetTheNotesTitle().getText().toString().equals("Untitled new note")
                && !GetTheNotes().getText().toString().isEmpty())
        {
            //Need to get the note and store it in a string for easier saving
            String theNote = GetTheNotes().getText().toString();
            Toast.makeText(this, theNote, Toast.LENGTH_SHORT).show();

            //Want the user to decide what to save the note as - give them a dialog
            SaveNoteDialog titleDialog = new SaveNoteDialog();
            titleDialog.show(getSupportFragmentManager(), "Title Dialog");

            //to some stuff?



            //TODO - Need to store a list of notes
            editor.putString("Note", theNote).apply();
        }
        else
        {
            //The user is editing a note that has already been saved before - need to resave the updated version
            Log.i(tag, "Inside the else statement after checking the notes title "
                            + sharedPreferences.getString("Note", ""));
        }
    }

    @Override
    public void ApplyNoteTitle(String title)
    {
        Log.i("About to cum", "nearly there");
        //Setting the title to equal the input from the dialog (SaveNoteDialog)
        GetTheNotesTitle().setText(title);

        //Setting result and finishing activity (returning home)
        finishActivity(RETURNED_VALUES);
        setResult(RETURNED_VALUES);
        finish();
    }

    private TextView GetTheNotesTitle() { return findViewById(R.id.notesTitle); }
    private TextInputLayout Tst() { return findViewById(R.id.notesNotesField);}
    private TextInputEditText GetTheNotes() { return findViewById(R.id.notesNotesFieldText); }
    private Button GetSaveButton() { return findViewById(R.id.notesAddNote); }
}