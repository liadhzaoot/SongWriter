package com.example.mycardgame;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycardgame.DataBase.RecordDB;

import java.util.Random;

public class RecordActivity extends AppCompatActivity {

    ImageButton buttonStartAndStop, buttonPlayStopLastRecordAudio;
    //    buttonPlayLastRecordAudio,
//            buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random;
    String RandomAudioFileName = "Androidstudiopro";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer1;
    boolean recordClick = false, playClick = false;
    private String songName;
    private String packName;
    private RecordDB recordDB;
    private String recordFileName;
    private boolean isRawExist;
    private Chronometer chronometer;
    private boolean start;
    private TextView packNameTv,recPress,seekBarHint;
    private ImageView packImgView;
    private CardsPack pack;
    private SeekBar seekBarAudio;
    private Runnable runnable;
    private Handler handler;
    private RecordHelper recordHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        random = new Random();
        recordDB = new RecordDB(this);
        songName = (String) getIntent().getSerializableExtra(GameActivity.SONG_NAME);
        packName = (String) getIntent().getSerializableExtra(GameActivity.PACK_TYPE);
        pack = (CardsPack) getIntent().getSerializableExtra(GameActivity.PACK);
        //---------------------------------------------------------------------------
        buttonStartAndStop = (ImageButton) findViewById(R.id.StartRecBtn);
        //buttonStop = (Button) findViewById(R.id.recordBtn);
        buttonPlayStopLastRecordAudio = (ImageButton) findViewById(R.id.playBtn);
        //buttonPlayStopLastRecordAudio.setEnabled(false);
        packNameTv = findViewById(R.id.packNameTxV);
        packNameTv.setText(packName);
        recPress = findViewById(R.id.RecTV);
        packImgView = findViewById(R.id.packBackImgV);
        packImgView.setImageResource(pack.getBackCardImg());
        seekBarAudio = findViewById(R.id.seekBarAudio);
        seekBarHint = findViewById(R.id.seekBarHint);
        chronometer = findViewById(R.id.time);
        recordFileName = getFileName();
        AudioSavePathInDevice = getFilesDir() + "/" +
                recordFileName + "AudioRecording.3gp";
        //--------------------------create the record class ---------------------------
        recordHelper = new RecordHelper(seekBarAudio, buttonPlayStopLastRecordAudio, recPress, seekBarHint,
                AudioSavePathInDevice, this, songName, chronometer,buttonStartAndStop);


        buttonStartAndStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordHelper.startRecord();
            }
        });


        //----------------------------action bar-------------------------------------------

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Record");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //---------------------------------------------------------------------------------
    }


    //-----------------------------------------------------------------------------
//        handler = new Handler();
//        mediaPlayer1 = new MediaPlayer();
//        mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                seekBarAudio.setMax(mp.getDuration());
//                changeSeekbar();
//            }
//        });
//
//        //-------------------------------timer-------------------------------------------------------
//        chronometer = findViewById(R.id.time);
//        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            @Override
//            public void onChronometerTick(Chronometer chronometerChanged) {
//                chronometer = chronometerChanged;
//            }
//        });
//        //-------------------------------------------------------------------------------------------
//        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                seekBarHint.setVisibility(View.VISIBLE);
//            }
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
//
//                if(mediaPlayer1 != null && fromTouch){
//                    mediaPlayer1.seekTo(progress);
//                }
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mediaPlayer1 != null && mediaPlayer1.isPlaying()) {
//                    mediaPlayer1.seekTo(seekBar.getProgress());
//                }
//            }
//        });
//
//        //----------------------------action bar-------------------------------------------
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Record");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        //---------------------------------------------------------------------------------
//        recordFileName = getFileName();
//        isRawExist = recordDB.isRowExist(getFilesDir() + "/" +
//                recordFileName + "AudioRecord" +
//                " " +
//                "ing.3gp");
//        if(isRawExist)
//        {
//            playClick = !playClick;
//            buttonPlayStopLastRecordAudio.setEnabled(true);
//        }
//
//
//        buttonStartAndStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rec();
//            }
//        });
//
//
//
//        buttonPlayStopLastRecordAudio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) throws IllegalArgumentException,
//                    SecurityException, IllegalStateException {
//
//                RecordDBStructure recordDBStructure = recordDB.getNoteTextStructure( getFilesDir() + "/" +
//                        recordFileName + "AudioRecording.3gp");
//                seekBarAudio.setProgress(0);
//                if (!playClick) {
//                    buttonPlayStopLastRecordAudio.setEnabled(true);
//                    buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_pause);
//                    //MediaPlayer mediaPlayer = new MediaPlayer();
//                    //mediaPlayer1 = new MediaPlayer();
//                    try {
//                        mediaPlayer1.setDataSource(recordDBStructure.getSongPathKey());
//                        mediaPlayer1.prepare();
//                        seekBarAudio.setMax(mediaPlayer1.getDuration());
//                    } catch (IOException e) {
//                        Toast.makeText(view.getContext(), "There is No Record Flie", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                    mediaPlayer1.start();
//                    Toast.makeText(RecordActivity.this, "Recording Playing",
//                            Toast.LENGTH_LONG).show();
//                    playClick = !playClick;
//                    new Thread(String.valueOf(this)).start();
//                } else {
//                    playClick = !playClick;
////                    buttonStop.setEnabled(false);
////                    buttonStart.setEnabled(true);
//                    buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
////                    buttonPlayStopLastRecordAudio.setEnabled(false);
////                    buttonPlayLastRecordAudio.setEnabled(true);
//                    if (mediaPlayer1 != null) {
//                        mediaPlayer1.stop();
//                        mediaPlayer1.reset();
//                        MediaRecorderReady();
//                    }
//                }
//            }
//
//
//        });
//
//
//        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                playClick = !playClick;
////                    buttonStop.setEnabled(false);
////                    buttonStart.setEnabled(true);
//                buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
//                seekBarHint.setText("00:00");
////                    buttonPlayStopLastRecordAudio.setEnabled(false);
////                    buttonPlayLastRecordAudio.setEnabled(true);
//                if (mediaPlayer1 != null) {
//                    mediaPlayer1.stop();
//                    mediaPlayer1.reset();
//                    MediaRecorderReady();
//                }
//            }
//        });
//    }
//
//    private void changeSeekbar() {
//
//        seekBarAudio.setProgress(mediaPlayer1.getCurrentPosition());
//        if(mediaPlayer1.isPlaying()){
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    changeSeekbar();
//
//                    seekBarHint.setText(" " +secToTime((int)Math.ceil(mediaPlayer1.getCurrentPosition()/1000f)));
//                }
//            };
//
//            handler.postDelayed(runnable,100);
//
//        }
//    }
//    private String secToTime(int sec)
//    {
//        int min = 0;
//        int hour = 0;
//        min = sec / 60;
//        sec = sec % 60;
//        hour = min / 60;
//        min = min % 60;
//        if(sec<10 && min<10)
//        return "" + "0" + min  + ":0"+sec ;
//        else if(sec<10&&min>=10)
//          return "" + "" + min  + ":0"+sec ;
//        else if(sec>=10&&min>=10)
//        {
//            return "" + "" + min  + ":"+sec ;
//        }
//        else if(sec>=10&&min<10)
//        {
//            return "" + "0" + min  + ":"+sec ;
//        }
//        return "" +0;
//    }
//
//    public void run() {
//        int currentPosition = mediaPlayer1.getCurrentPosition();
//        int total = mediaPlayer1.getDuration();
//        while (mediaPlayer1 != null && mediaPlayer1.isPlaying() && currentPosition < total) {
//            try {
//                Thread.sleep(1000);
//                currentPosition = mediaPlayer1.getCurrentPosition();
//            } catch (InterruptedException e) {
//                return;
//            } catch (Exception e) {
//                return;
//            }
//            seekBarAudio.setProgress(currentPosition);
//        }
//    }
//
//
//    public void MediaRecorderReady() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setOutputFile(AudioSavePathInDevice);
//    }
//
////    public String CreateRandomAudioFileName(int string) {
////        StringBuilder stringBuilder = new StringBuilder(string);
////        int i = 0;
////        while (i < string) {
////            stringBuilder.append(RandomAudioFileName.
////                    charAt(random.nextInt(RandomAudioFileName.length())));
////            i++;
////        }
////        return stringBuilder.toString();
////    }
//
    private String getFileName()
    {
        String fileName = "";
        if(packName.equals("Theme"))
        {
            fileName = songName + NotePad.Theme_FILE;
        }
        else if(packName.equals("Melody"))
        {
            fileName = songName + NotePad.Melody_FILE;
        }
        else if(packName.equals("Instrument"))
        {
            fileName = songName + NotePad.Instruments_FILE;
        }
        else if(packName.equals("Lyrics"))
        {
            fileName = songName + NotePad.LYRICS_FILE;
        }
        else if(packName.equals("Chord"))
        {
            fileName = songName + NotePad.CordProgression_FILE;
        }
        return fileName;

    }

//    private void requestPermission() {
//
//        ActivityCompat.requestPermissions(RecordActivity.this, new
//                String[]{ Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length > 0) {
//                    boolean StoragePermission = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    boolean RecordPermission = grantResults[1] ==
//                            PackageManager.PERMISSION_GRANTED;
//                }
//                break;
//        }
//    }
//
//    public boolean checkPermission() {
//        int result1 = 1;
//        int result2 = 1;
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            result1 = 0;
//        }
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//        {
//            result2 = 0;
//        }
//        if(result1 == 1 && result2 == 1 )
//            return true;
//        else
//            return false;
//    }
//    public void ReplaceRecordAlertDialog(final int position)
//    {
//
//        // Create the object of
//        // AlertDialog Builder class
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(this);
//
//        // Set the message show for the Alert time
//        builder.setMessage("Do you want to Replace your Record?");
//
//        // Set Alert Title
//        builder.setTitle("Replace Record ");
//
//        // Set Cancelable false
//        // for when the user clicks on the outside
//        // the Dialog Box then it will remain show
//        builder.setCancelable(false);
//
//        // Set the positive button with yes name
//        // OnClickListener method is use of
//        // DialogInterface interface.
//
//        builder
//                .setPositiveButton(
//                        "Yes",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // When the user click yes button
//                                // then app will close
//                                recHelper();
//                            }
//                        });
//
//        // Set the Negative button with No name
//        // OnClickListener method is use
//        // of DialogInterface interface.
//        builder
//                .setNegativeButton(
//                        "No",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // If user click no
//                                // then dialog box is canceled.
//                                dialog.cancel();
//                            }
//                        });
//
//        // Create the Alert dialog
//        AlertDialog alertDialog = builder.create();
//
//        // Show the Alert Dialog box
//        alertDialog.show();
//    }
//
//    public void rec()
//    {
//        if (!recordClick) {
//            if (checkPermission()) {
//                isRawExist = recordDB.isRowExist(getFilesDir() + "/" +
//                        recordFileName + "AudioRecording.3gp");
//                if(isRawExist)
//                {
//                    ReplaceRecordAlertDialog(0);
//                }
//                else {
//                    recHelper();
//                }
//            } else {
//                requestPermission();
//            }
//
//        } else {
//            recordClick = !recordClick;
//            mediaRecorder.stop();
//            chronometer.setBase(SystemClock.elapsedRealtime());
//            chronometer.stop();
//            recPress.setVisibility(View.VISIBLE);
//            chronometer.setVisibility(View.INVISIBLE);
//            //buttonStartAndStop.setImageResource(android.R.drawable.ic_btn_speak_now);
//            buttonPlayStopLastRecordAudio.setEnabled(true);
//            //buttonStart.setEnabled(true);
//            //buttonStopPlayingRecording.setEnabled(false);
//            Toast.makeText(RecordActivity.this, "Recording Completed",
//                    Toast.LENGTH_LONG).show();
//            playClick = false;
//        }
//    }
//    public void recHelper()
//    {
//        Toast.makeText(RecordActivity.this, "checking premmition", Toast.LENGTH_SHORT).show();
//        AudioSavePathInDevice = getFilesDir() + "/" +
//                recordFileName + "AudioRecording.3gp";
//        recordDB.insertSong(AudioSavePathInDevice,songName);
//        MediaRecorderReady();
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////                        buttonStart.setEnabled(false);
////                        buttonStop.setEnabled(true);
//        recordClick = !recordClick;
//        buttonPlayStopLastRecordAudio.setEnabled(false);
//        //buttonStartAndStop.setImageResource(android.R.drawable.ic_media_pause);
//        recPress.setVisibility(View.INVISIBLE);
//        chronometer.setVisibility(View.VISIBLE);
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        chronometer.start();
//        Toast.makeText(RecordActivity.this, "Record Started", Toast.LENGTH_SHORT).show();
//    }

//    private void clearMediaPlayer() {
//        mediaPlayer1.stop();
//        mediaPlayer1.release();
//        mediaPlayer1 = null;
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordHelper.clearMediaPlayer();
        recordHelper.stopRecFActivity();
    }
    // todo: dont forget
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                recordHelper.stopRecFActivity();
            }
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
