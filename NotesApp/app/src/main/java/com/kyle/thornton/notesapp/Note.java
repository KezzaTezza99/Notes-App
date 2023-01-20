package com.kyle.thornton.notesapp;

//Used to store note information in the NotesHomeListView as well as a way to save the data through shared preferences
public class Note {
    private String noteTitle;
    private String noteText;
    private String noteTime;

    public Note(String title, String text, String time) {
        this.noteTitle = title;
        this.noteText = text;
        this.noteTime = time;
    }

    public String getNoteTitle() {
        return noteTitle;
    }
    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }
    public String getNoteText() {
        return noteText;
    }
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
    public String getNoteTime() {
        return noteTime;
    }
    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
}
