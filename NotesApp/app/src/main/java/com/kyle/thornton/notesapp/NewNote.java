package com.kyle.thornton.notesapp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyle.thornton.notesapp.Note;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class NewNote extends AppCompatActivity {
    //Put these in there own private methods
    NoteUtilities utilities = new NoteUtilities();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Need to find out if the user is actually creating a new note or have they selected to edit an already existing note
        boolean editingNote = getIntent().getBooleanExtra("Editing a note", false);

        //Presuming that the user is adding a new note by default
        if(!editingNote) {
            //Changing the title to be blank
            Objects.requireNonNull(getSupportActionBar()).setTitle("");

            //Want to handle the user going back to the home page. Going home will have two affects
            //If any characters have been inputted save the note and go home otherwise just go home
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(getApplicationContext(), NotesHome.class);

                    //Note is empty just go home otherwise save the note
                    if(getNoteInputField().getEditableText().toString().isEmpty())
                        startActivity(intent);
                    else
                        saveNewNote(getNoteInputField().getEditableText().toString());
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);
        }
        else {
            //Getting the index of the note that was selected to be edited
            int noteIndex = getIntent().getIntExtra("Position", 0);

            //Getting the current full notes list in the form of the JSON string
            String allNotesData = sharedPreferences.getString("JSON", "");

            //Converting the String into a ArrayList<Note>
            ArrayList<Note> allNotes;
            allNotes = utilities.convertToNoteList(allNotesData);

            //Display the note in the text field
            getNoteInputField().setText(allNotes.get(noteIndex).getNoteText());
            //Copying the current note into a different string to compare if the user has made any changes - if so need to re-save the note
            String theOriginalNote = Objects.requireNonNull(getNoteInputField().getText()).toString();

            //TODO: Can I make this bit of code better so I don't repeat myself
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(getApplicationContext(), NotesHome.class);

                    //Note is the same as the original note - just go home no editing has been done
                    if(getNoteInputField().getEditableText().toString().equals(theOriginalNote))
                        startActivity(intent);
                    else
                        saveEditedNote(getNoteInputField().getEditableText().toString(), allNotes, noteIndex);
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);
        }
    }

    //User has created a new note. Want to save the note as well as saving the title as the first word in the edit field
    void saveNewNote(String note) {
        //Storing the first word in the note as the notes title
        String title = null;
        //Presuming the note contains N amount of words, if it doesn't the title is simply saved as the note after checking for whitespace
        boolean isWhiteSpace = false;

        //Looping through the note to find the first occurrence of a whitespace character
        for(int i = 0; i < note.length(); i++) {
            //Finding the index of the first white space
            if(Character.isWhitespace(note.charAt(i))) {
                //Found a whitespace character substring the title from the rest of the text
                title = note.substring(0, i);
                isWhiteSpace = true;
                break;
            }
        }
        //The note didn't contain any whitespace meaning the note was only one word
        if(!isWhiteSpace) {
            //Simply saving the title as the entire note 
            title = note;
        }
        //Now have the note and it's title saving this info to shared preferences then returning to home page
        ArrayList<Note> notesToSave = new ArrayList<>();

        //Before saving the note details need to check if there is already info saved to avoid overriding the note(s)
        boolean previousDataHasBeenSaved = sharedPreferences.getBoolean("Saved", false);
        int sizeOfList = previousDataHasBeenSaved ? sharedPreferences.getInt("Size", 0) : 0;

        //We have previous data need to save the new note in the next space in the ArrayList
        if(previousDataHasBeenSaved) {
            //Get the data and transform it back into an ArrayList<Note> then save the new note in the next space in the list, then save
            String oldData = sharedPreferences.getString("JSON", "");
            notesToSave = utilities.convertToNoteList(oldData);

            //Adding the note just created in to the Array
            notesToSave.add(new Note(title, note, utilities.getCurrentTimeStamp()));
        }
        else {
            //There isn't any previously stored note(s) so just save in index 0
            notesToSave.add(new Note(title, note, utilities.getCurrentTimeStamp()));
        }

        //Converting the ArrayList<Note> into a JSON object that can then be converted to a string for storage in the shared preferences
        String json = gson.toJson(notesToSave);

        //Saving the JSON in shared preferences
        editor.putString("JSON", json);
        editor.putBoolean("Saved", true);
        editor.putInt("Size", notesToSave.size());

        editor.apply();

        Intent intent = new Intent(this, NotesHome.class);
        startActivity(intent);
    }

    //User has edited a new note and want to save the new modifications and overwrite the original note
    void saveEditedNote(String newNote, ArrayList<Note> listOfAllNotes, int locationOfTheNote) {
        //Getting the title to save
        String title = getNoteTitleFromFullNote(newNote);

        //Replacing the original note that has now been edited with the new note - need to get its original location in the array
        listOfAllNotes.set(locationOfTheNote, new Note(title, newNote, utilities.getCurrentTimeStamp()));

        //Transforming the ArrayList<Note> back into a JSON string and storing in shared preferences
        String json = gson.toJson(listOfAllNotes);

        //Saving the JSON in shared preferences
        editor.putString("JSON", json);
        editor.putBoolean("Saved", true);
        editor.putInt("Size", listOfAllNotes.size());

        editor.apply();

        //Going back to the home page
        Intent intent = new Intent(this, NotesHome.class);
        startActivity(intent);
    }

    private String getNoteTitleFromFullNote(String note) {
        //Storing the first word in the note as the notes title
        String title = null;
        //Presuming the note contains N amount of words, if it doesn't the title is simply saved as the note after checking for whitespace
        boolean isWhiteSpace = false;

        //Looping through the note to find the first occurrence of a whitespace character
        for(int i = 0; i < note.length(); i++) {
            //Finding the index of the first white space
            if(Character.isWhitespace(note.charAt(i))) {
                //Found a whitespace character substring the title from the rest of the text
                title = note.substring(0, i);
                isWhiteSpace = true;
                break;
            }
        }
        //The note didn't contain any whitespace meaning the note was only one word
        if(!isWhiteSpace) {
            //Simply saving the title as the entire note
            title = note;
        }
        return title;
    }

    private TextInputEditText getNoteInputField() { return findViewById(R.id.newNoteNoteField); }
}