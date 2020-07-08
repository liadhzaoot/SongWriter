package com.example.mycardgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SongNameEnterDialog extends AppCompatDialogFragment {
    public static final String SONG_NAME = "songName";
    private EditText editTextSongName;
    private SongNameEnterDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SongNameEnterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implement SongNameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_song_name_dialog, null);

        builder.setView(view)
                .setTitle("Song Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String songName = editTextSongName.getText().toString();
                        listener.applyTexts(songName);

                    }
                });
        editTextSongName = view.findViewById(R.id.songNameET);
        return builder.create();
    }

    public interface SongNameEnterDialogListener {
        void applyTexts(String songName);
    }
}
