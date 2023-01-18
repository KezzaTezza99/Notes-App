package com.kyle.thornton.notesapp;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.textfield.TextInputEditText;
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

        editor.clear();
        //TODO: How do I want to store this, don't want to be overriding notes each new note added
/*
        editor.putString("Note Title", title);
        editor.putString("Note", note);
        editor.commit();
*/

        Intent intent = new Intent(this, NotesHome.class);
        startActivity(intent);
    }
}