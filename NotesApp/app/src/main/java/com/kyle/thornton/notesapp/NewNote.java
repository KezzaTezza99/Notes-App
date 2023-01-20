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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        //Getting the text area that contains the note
        TextInputEditText noteInputField = findViewById(R.id.newNoteNoteField);

        //Changing the title to be blank
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        //Want to handle the user going back to the home page. Going home will have two affects
        //If any characters have been inputted save the note and go home otherwise just go home
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), NotesHome.class);

                //Note is empty just go home otherwise save the note
                if(noteInputField.getEditableText().toString().isEmpty())
                    startActivity(intent);
                else
                    saveNote(noteInputField.getEditableText().toString());
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    //Want to save the note as well as saving the title as the first word in the edit field
    void saveNote(String note) {
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
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        ArrayList<Note> notesToSave = new ArrayList<>();

        //Before saving the note details need to check if there is already info saved to avoid overriding the note(s)
        boolean previousDataHasBeenSaved = sharedPreferences.getBoolean("Saved", false);
        int sizeOfList = previousDataHasBeenSaved ? sharedPreferences.getInt("Size", 0) : 0;

        //We have previous data need to save the new note in the next space in the ArrayList
        if(previousDataHasBeenSaved) {
            //Get the data and transform it back into an ArrayList<Note> then save the new note in the next space in the list, then save
            String oldData = sharedPreferences.getString("JSON", "");
            Type type = new TypeToken<ArrayList<Note>>(){}.getType();
            notesToSave = gson.fromJson(oldData, type);

            //Adding the note just created in to the Array
            notesToSave.add(new Note(title, note, "17:40"));
        }
        else {
            //There isn't any previously stored note(s) so just save in index 0
            notesToSave.add(new Note(title, note, "17:40"));
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
}