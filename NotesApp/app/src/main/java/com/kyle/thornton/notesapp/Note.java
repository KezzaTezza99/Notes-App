package com.kyle.thornton.notesapp;

//Used to store note information in the NotesHomeListView as well as a way to save the data through shared preferences
public class Note {
    private final String noteTitle;
    private final String noteText;
    private final String noteTime;

    public Note(String title, String text, String time) {
        this.noteTitle = title;
        this.noteText = text;
        this.noteTime = time;
    }

    public String getNoteTitle() {
        return noteTitle;
    }
    public String getNoteText() {
        return noteText;
    }
    public String getNoteTime() {
        return noteTime;
    }
}
