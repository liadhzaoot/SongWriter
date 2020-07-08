package com.example.mycardgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ContactUsDialog extends AppCompatDialogFragment {
    public static final String SUBJECT = "subject";
    //public static final String SONG_NAME = "songName";
    private Button reportAProblemBtn;
    private Button sendAFeedBackBtn;
    private ContactUsDialog.ContactUsDialogListiner listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] chooseOption = {"Report A Problem", "Send A Feedback"};
        //------------------------------------------------------------
        builder.setItems((CharSequence[]) chooseOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)//reportAProblemBtn
                {
                    Intent intent = new Intent(getContext(), ContactUsActivity.class);
                    intent.putExtra(SUBJECT, EmailSubject.REPORT_PROBLEM); // report problem
                    startActivity(intent);
                    dialog.cancel();

                } else if (which == 1)// sendAFeedBackBtn
                {
                    Intent intent = new Intent(getContext(), ContactUsActivity.class);
                    intent.putExtra(SUBJECT, EmailSubject.GIVE_FEEDBACK);//send feed back
                    startActivity(intent);
                    dialog.cancel();
                }
            }
        });
        builder.setTitle("Report a problem")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        //editTextSongName = view.findViewById(R.id.songNameET);
        return builder.create();
    }

    public interface ContactUsDialogListiner {
        void applyTexts(String songName);
    }
}

