package com.w18024358.notesapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SaveNoteDialog extends AppCompatDialogFragment
{
    private EditText noteTitle;
    private SaveNoteDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.note_name_dialog, null);

        noteTitle = view.findViewById(R.id.dialogNoteName);

        builder.setView(view)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    //Could do some stuff here
                })
                .setPositiveButton("Okay", (dialogInterface, i) ->
                {
                    if (noteTitle.getText().toString().length() == 0)
                    {
                        Toast.makeText(getContext(), "The title is null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String noteSavedTitle = noteTitle.getText().toString();
                    listener.ApplyNoteTitle(noteSavedTitle);
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try
        {
            listener = (SaveNoteDialogListener)context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " does not implement SaveNoteDialogListener");
        }
    }

    public interface SaveNoteDialogListener
    {
        void ApplyNoteTitle(String title);
    }
}
