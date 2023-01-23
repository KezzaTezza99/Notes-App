package com.kyle.thornton.notesapp;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//A helper class that can do some common functionality - stops repeat code keeps it DRY
public class NoteUtilities extends AppCompatActivity {
    //Convert ArrayList<Note> to JSON String object
    String convertToString(ArrayList<Note> noteList) { return getGSON().toJson(noteList); }
    //Convert from JSON String to ArrayList<Note>
    ArrayList<Note> convertToNoteList(String data) { return getGSON().fromJson(data, getNoteType()); }
    //Checks to see if the ArrayList<Note> is empty
    boolean isNoteListEmpty(ArrayList<Note> list) { return list.size() == 0 ? Boolean.TRUE : Boolean.FALSE; }
    //Gets the current system date and time
    @SuppressLint("SimpleDateFormat")
    String getCurrentTimeStamp() { return new SimpleDateFormat("dd/MM/yy hh:mm:ss").format(Calendar.getInstance().getTime()); }

    //Helpful getters for stuff that needs to be accessed a lot
    SharedPreferences getSP() {
        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        return sp;
    }
    SharedPreferences.Editor getEditor() { return getSP().edit(); }
    Gson getGSON() { return new Gson(); }
    Type getNoteType() { return new TypeToken<ArrayList<Note>>(){}.getType(); }
}
