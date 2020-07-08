package com.example.mycardgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycardgame.DataBase.NoteDB;
import com.example.mycardgame.dataBaseStructure.NoteDBStructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class NotePad extends AppCompatActivity{
    public static final String LYRICS_FILE = "LyricsFile";
    public static final String Theme_FILE = "ThemeFile";
    public static final String Melody_FILE = "MelodyFile";
    public static final String Instruments_FILE = "InstrumentsFile";
    public static final String CordProgression_FILE = "ChordFile";

    String fileContents = "Hello world!";
    private String packName;
    private File file;
    private EditText etNotePad;
    private String songName;
    private String fileName;
    private NoteDB noteDB;
    //String s = getFilesDir().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pad);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Note Pad");
        actionBar.setDisplayHomeAsUpEnabled(true);


        noteDB = new NoteDB(this);

        etNotePad = findViewById(R.id.etNotePad);

        Intent intent = getIntent();
        packName = intent.getStringExtra(GameActivity.PACK_TYPE);
        songName = intent.getStringExtra(GameActivity.SONG_NAME);
        fileName = getFileName();
        loadFile(fileName);
        //file = new File(this.getFilesDir(),LYRICS_FILE);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                saveFile(fileName,v);
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                saveFile(fileName);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void saveFile(String fileName)
    {
        fileContents = etNotePad.getText().toString();
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());

            //etNotePad.getText().clear();
            //TODO: CHANGE THE FILE DIR
            Toast.makeText(this, "save to " + getFilesDir() + " / " + fileName, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadFile(String fileName)
    {
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
            etNotePad.setText(sb.toString());
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
    }
    private String getFileName()
    {
        String fileName = "";
        if(packName.equals("Theme"))
        {
            fileName = songName + Theme_FILE;
        }
        else if(packName.equals("Melody"))
        {
            fileName = songName + Melody_FILE;
        }
        else if(packName.equals("Instruments"))
        {
            fileName = songName + Instruments_FILE;
        }
        else if(packName.equals("Lyrics"))
        {
            fileName = songName + LYRICS_FILE;
        }
        else if(packName.equals("Chord"))
        {
            fileName = songName + CordProgression_FILE;
        }
        noteDB.insertSong(fileName,songName);
        return fileName;

    }


    /**
     * when the back button clicked save the state
     */
    @Override
    public void onBackPressed() {
        saveFile(fileName);
        finish();
    }
}
