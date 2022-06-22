package com.w18024358.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity
{
    //TODO - Need to implement the list view that shows notes - maybe have a message that says no notes if there isnt any saved yet
    static final int RETURNED_VALUES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        GetAddNote().setOnClickListener(view -> CreateNewNote());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //User has saved a new note - need to update the list
        if(requestCode == RETURNED_VALUES || resultCode == RETURNED_VALUES)
        {

        }
    }

    private void CreateNewNote()
    {
        Intent intent = new Intent(this, NotesPage.class);
        startActivityForResult(intent, RETURNED_VALUES);
    }

    private ListView GetAllNotes() { return findViewById(R.id.homeListOfAllNotes); }
    private ImageView GetAddNote() { return findViewById(R.id.homeAddNotesButton); }
}