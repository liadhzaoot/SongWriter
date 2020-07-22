package com.example.mycardgame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mycardgame.DataBase.NoteDB;
import com.example.mycardgame.DataBase.RecordDB;
import com.example.mycardgame.dataBaseStructure.NoteDBStructure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class FinalPage extends AppCompatActivity implements Serializable, View.OnClickListener,
                                                            NoteFragment.OnFragmentInteractionListener,
                                                            CardFragment.OnFragmentInteractionListener,
                                                            RecordFragment.OnFragmentInteractionListener{
    public static final String IS_FINISH = "is_finish";
    final int NUMOFRAW = 5;
    final int NUMOFCOLUM = 3;


    private ArrayList<CardsPack> pack;
    /**
     * make the Cards Btn
     */

    private TextView ThemeCard, ThemeNote, ThemeRec;
    private TextView MelodyCard, MelodyNote, MelodyRec;
    private TextView LyricsCard, LyricsNote, LyricsRec;
    private TextView InstrumentCard, InstrumentNote, InstrumentRec;
    private TextView ChordCard, ChordNote, ChordRec;
    private TextView[][] btnArray;
    private FrameLayout notePadFragment , cardPadFragment,recordPadFragmant;
    private TextView recPressTxt;
    private SeekBar seekBarAudio;
    private TextView seekBarHint;
    private NoteDB noteDB;
    private RecordDB recordDB;
    private TextView songTitle;
    private String songName;
    NoteFragment noteFragment;
    CardFragment cardFragment;
    RecordFragment recordFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ImageButton recButton;
    private ImageButton playBtn;
    private RecordHelper recordHelper;
    private Chronometer timeCrometer;
    private boolean isStartRec = false;
    private boolean ifUserRecord = false; // check if the user ifUserRecord
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        songName = (String) getIntent().getSerializableExtra(GameActivity.SONG_NAME);
        //fragmentTransaction.hide(noteFragment);
        setView();
        defineArry();
        pack = (ArrayList<CardsPack>) getIntent().getSerializableExtra(GameActivity.PACK);
        songTitle.setText(songName);
        recordHelper = new RecordHelper(seekBarAudio,playBtn,recPressTxt,seekBarHint,
                getRecordFileName(0,true),this,songName,timeCrometer,recButton);


        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ifUserRecord = true;
                // check if start recording for swich the icon
                if(!isStartRec) {
                    recButton.setImageResource(android.R.drawable.ic_media_pause);
                    isStartRec = !isStartRec;
                }
                else
                {
                    recButton.setImageResource(R.drawable.ic_speak);
                    isStartRec = !isStartRec;
                }
                recordHelper.startRecord();
            }
        });


        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Final Record");
        actionBar.setDisplayHomeAsUpEnabled(true);


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ifUserRecord = recordDB.isRowExist(getRecordFileName(0,true));
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                recordHelper.stopRecFActivity();
                finish();
                return true;
            /**
             * done page - done the song
             */
            case R.id.doneTV:
                if(ifUserRecord) {
                    Intent intent = new Intent(this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    isDoneThePageAlertDialog();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.final_done_page,menu);

        return true;
    }

    /*           0 pack - ThemeCard,
            * 1 pack - Melody,
            * 2 pack - Instruments,
            * 3 pack - Lyrics,
            * 4 pack - CardProgression,

    */
    @Override
    public void onClick(View v) {

        for (int i = 0; i <NUMOFRAW ; i++){
            for (int j = 0; j <NUMOFCOLUM ; j++) {

                if(btnArray[i][j].getId() == v.getId())
                {
                    switch(j){
                        case 0:
                            //TODO: nothing

                            openFragment(0,NUMOFRAW - 1 - i);
                            break;
                        case 1:
                            //TODO: open Note
                            openFragment(1,NUMOFRAW - 1 - i);
                            break;
                        case 2:
                            //TODO: Open Rec
                            openFragment(2,NUMOFRAW - 1 - i);
                            //pack.getPackCard().get(i).
                            break;
                    }

                }
            }
        }
    }

    private void openFragment(int theCase,int cardSubjectNum) {
        switch(theCase){
            case 0:
                //TODO: nothing
                cardFragment = CardFragment.newInstance(pack.get(cardSubjectNum).getChoosenCard().getImg());
                openFragmentHelper();
                fragmentTransaction.add(R.id.cardFragment,cardFragment,"CARD_FRAGMENT").commit();
                break;
            case 1:
                //TODO: open Note
                String s = loadFile(getNoteFileName(cardSubjectNum));
                noteFragment = NoteFragment.newInstance(s);
                openFragmentHelper();
                fragmentTransaction.add(R.id.noteFragment,noteFragment,"NOTE_FRAGMENT").commit();
                break;
            case 2:
                //TODO: Open Rec

                recordFragment = RecordFragment.newInstance(getRecordFileName(cardSubjectNum,false));
                openFragmentHelper();
                fragmentTransaction.add(R.id.recordFragment,recordFragment,"RECORD_FRAGMENT").commit();
                break;
        }

    }
    private void openFragmentHelper()
    {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fadein,R.anim.fadeout,
                R.anim.fadein,R.anim.fadeout);
        fragmentTransaction.addToBackStack(null);
    }

    private String getNoteFileName(int cardSubjectNum) {
        String fileName =  songTitle.getText().toString() + pack.get(cardSubjectNum).getSubject()+"File" ;
        noteDB.insertSong(fileName,songTitle.getText().toString());
        return fileName;
    }

    /**
     * check if the ifUserRecord is the last song ifUserRecord
     * yes - return the FinalSongRec file name
     * no - return the subject of the card file name
     * @param cardSubjectNum
     * @param isfinalRec
     * @return
     */

    private String getRecordFileName(int cardSubjectNum,boolean isfinalRec){
        String fileName = "";
        if(!isfinalRec) {
             fileName = getFilesDir() + "/" +
                    songTitle.getText().toString() +
                    pack.get(cardSubjectNum).getSubject()
                    + "File" + "AudioRecording.3gp";
        }
        else
        {
//             fileName = getFilesDir() + "/" +
//                    songTitle.getText().toString() +
//                    "FinalSongRec"
//                    + "File" + "AudioRecording.3gp";


            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    songTitle.getText().toString() +
                    pack.get(cardSubjectNum).getSubject()
                    + "File" + "AudioRecording.ogg";

        }
        //recordDB.insertSong(fileName,songTitle.getText().toString());

        return fileName;
    }

    private void defineArry()
    {
        btnArray = new TextView[5][3];
        btnArray[0][0] = ThemeCard;
        btnArray[0][1] = ThemeNote;
        btnArray[0][2] = ThemeRec;

        btnArray[1][0] = MelodyCard;
        btnArray[1][1] = MelodyNote;
        btnArray[1][2] = MelodyRec;

        btnArray[2][0] = LyricsCard;
        btnArray[2][1] = LyricsNote;
        btnArray[2][2] = LyricsRec;

        btnArray[3][0] = InstrumentCard;
        btnArray[3][1] = InstrumentNote;
        btnArray[3][2] = InstrumentRec;

        btnArray[4][0] = ChordCard;
        btnArray[4][1] = ChordNote;
        btnArray[4][2] = ChordRec;



        for (int i = 0; i <5 ; i++) {
            for (int j = 0; j <3 ; j++) {
                btnArray[i][j].setOnClickListener(this);
            }
        }

    }

    @SuppressLint("WrongViewCast")
    private void setView()
    {
        noteDB = new NoteDB(this);
        recordDB = new RecordDB(this);
        notePadFragment = findViewById(R.id.noteFragment);
        cardPadFragment = findViewById(R.id.cardFragment);
        recordPadFragmant = findViewById(R.id.recordFragment);
        recButton = findViewById(R.id.StartRecBtn);
        songTitle = findViewById(R.id.songTitle);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        seekBarHint = findViewById(R.id.seekBarHint);
        playBtn = findViewById(R.id.playBtn);
        //recPressTxt = findViewById(R.id.recPressTxt);
        timeCrometer = findViewById(R.id.time);
        //----------------the matrix-----------------------
        ThemeCard = findViewById(R.id.ThemeCard);
        ThemeNote = findViewById(R.id.ThemeNote);
        ThemeRec = findViewById(R.id.ThemeRec);

        MelodyCard = findViewById(R.id.MelodyCard);
        MelodyNote = findViewById(R.id.MelodyNote);
        MelodyRec = findViewById(R.id.MelodyRec);

        LyricsCard = findViewById(R.id.LyricsCard);
        LyricsNote = findViewById(R.id.LyricsNote);
        LyricsRec = findViewById(R.id.LyricsRec);

        InstrumentCard = findViewById(R.id.InstrumentCard);
        InstrumentNote = findViewById(R.id.InstrumentNote);
        InstrumentRec = findViewById(R.id.InstrumentRec);

        ChordCard = findViewById(R.id.ChordCard);
        ChordNote = findViewById(R.id.ChordNote);
        ChordRec = findViewById(R.id.ChordRec);
        //------------------------------------------------------

    }


    @Override
    public void onFragmentInteraction(String uri) {
        onBackPressed();
    }

    public String loadFile(String fileName)
    {
        String s = "";
        NoteDBStructure noteTextStructure = new NoteDBStructure();
        noteTextStructure = noteDB.getNoteTextStructure(fileName);

        FileInputStream fis = null;
        try {
            fis = openFileInput(noteTextStructure.getSongPathKey());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");

            }
            s = (sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordHelper.clearMediaPlayer();
    }

    /**
     * open a dialog if want to replace the ifUserRecord
     *
     */
    public void isDoneThePageAlertDialog() {
        // Create the object of
        // AlertDialog Builder class
        final Intent intent1 = new Intent(this, StartActivity.class);
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(this);

        // Set the message show for the Alert time
        builder.setMessage("You did not finish,\n do you want to exit the page?");

        // Set Alert Title
        builder.setTitle("EXIT ");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // When the user click yes button
                                // then page will close
                                startActivity(intent1);
                                finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

}
