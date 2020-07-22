package com.example.mycardgame;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mycardgame.DataBase.RecordDB;
import com.example.mycardgame.dataBaseStructure.RecordDBStructure;

import java.io.IOException;

public class RecordHelper {
    private ImageButton _recordButton, _buttonPlayStopLastRecordAudio;
    private SeekBar _seekBar, _seekBarAudio;
    private TextView _recPress, _seekBarHint;
    private Chronometer _chronometer;
    private String _recordFileName;
    boolean recordClick = false, playClick = false;
    private MediaPlayer mediaPlayer1;
    private MediaRecorder mediaRecorder;
    private String _songName;
    //private ArrayList<CardsPack> _pack;
    private Handler handler;
    private Runnable runnable;
    private boolean isRawExist;
    private RecordDB recordDB;
    private Context _context;
    public static final int RequestPermissionCode = 1;
    String AudioSavePathInDevice = null;

    //private boolean _isRecordButtonClicked;
    public RecordHelper(SeekBar seekBarAudio, ImageButton buttonPlayStopLastRecordAudio,
                        TextView recPress, TextView seekBarHint, String recordFileName, Context context,
                        String songName, Chronometer chronometer, ImageButton recordButton) {
        /**
         * make the tools
         */
        //-------------------------------------------------
        //_isRecordButtonClicked = isRecordButtonClicked;
        _buttonPlayStopLastRecordAudio = buttonPlayStopLastRecordAudio;
        _seekBarAudio = seekBarAudio;
        _recPress = recPress;
        _seekBarHint = seekBarHint;
        _recordFileName = recordFileName;
        _recordButton = recordButton;
        // _pack = pack;
        _context = context;
        _songName = songName;
        _chronometer = chronometer;
        recordDB = new RecordDB(_context);
        //--------------------------------------------------

        handler = new Handler();
        mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _seekBarAudio.setMax(mp.getDuration());
                changeSeekbar();
                //_recordButton.setEnabled(true);

            }
        });

        //-------------------------------timer-------------------------------------------------------
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                _chronometer = chronometerChanged;
            }
        });
        /**
         * seek bar
         */

        //-------------------------------------------------------------------------------------------
        _seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                _seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

                if (mediaPlayer1 != null && fromTouch) {
                    mediaPlayer1.seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer1 != null && mediaPlayer1.isPlaying()) {
                    mediaPlayer1.seekTo(seekBar.getProgress());
                }
            }
        });
//        isRawExist = recordDB.isRowExist(_recordFileName);
//        if (isRawExist) {
//            playClick = !playClick;
//            buttonPlayStopLastRecordAudio.setEnabled(true);
//        }
        //---------------------------------------------------------------------------------------------
        _buttonPlayStopLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                boolean isSuccessPlayRec = true;
                RecordDBStructure recordDBStructure = recordDB.getNoteTextStructure(_recordFileName);
                _seekBarAudio.setProgress(0);
                /**
                 * if you can the btn
                 */
                if (!playClick) {
                    _buttonPlayStopLastRecordAudio.setEnabled(true);
                    _recordButton.setEnabled(true);

                    //MediaPlayer mediaPlayer = new MediaPlayer();
                    //mediaPlayer1 = new MediaPlayer();
                    try {
                        //crash i dont know why
                       // mediaPlayer1.setDataSource(recordDBStructure.getSongPathKey());
                        //try
                        mediaPlayer1.setDataSource(_recordFileName);
                        mediaPlayer1.prepare();
                        _seekBarAudio.setMax(mediaPlayer1.getDuration());
                    } catch (IOException e) {
                        Toast.makeText(view.getContext(), "There is No Record Flie", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        isSuccessPlayRec = false;
                    }
                    if (isSuccessPlayRec) {
                        _buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_pause);
                        _recordButton.setEnabled(false);
                        mediaPlayer1.start();
//                    Toast.makeText(_context, "Recording Playing",
//                            Toast.LENGTH_LONG).show();
                        playClick = !playClick;
                    }
                    new Thread(String.valueOf(this)).start();
                } else {
                    playClick = !playClick;
                    _buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
                    _recordButton.setEnabled(true);
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.stop();
                        mediaPlayer1.reset();
                        MediaRecorderReady();
                    }
                }
            }


        });


        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playClick = !playClick;
//                    buttonStop.setEnabled(false);
//                    buttonStart.setEnabled(true);
                _buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
                _recordButton.setEnabled(true);
                _seekBarHint.setText("00:00");
//                    buttonPlayStopLastRecordAudio.setEnabled(false);
//                    buttonPlayLastRecordAudio.setEnabled(true);
                if (mediaPlayer1 != null) {
                    mediaPlayer1.stop();
                    mediaPlayer1.reset();
                    MediaRecorderReady();
                }
            }
        });
    }


    public void startRecord() {
        rec();
    }

    public void rec() {
        if (!recordClick) {
            if (checkPermission()) {
                isRawExist = recordDB.isRowExist(_recordFileName);
                if (isRawExist) {
                    ReplaceRecordAlertDialog(0);
                } else {
                    recHelper();
                }
            } else {
                requestPermission();
            }

        } else {
            recordClick = !recordClick;
            mediaRecorder.stop();
            _chronometer.setBase(SystemClock.elapsedRealtime());
            _chronometer.stop();
            //_recPress.setVisibility(View.VISIBLE);
            _chronometer.setVisibility(View.INVISIBLE);
            _buttonPlayStopLastRecordAudio.setEnabled(true);
            Toast.makeText(_context, "Recording Completed",
                    Toast.LENGTH_LONG).show();
            _recordButton.setImageResource(R.drawable.ic_speak);
            //playClick = false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) _context, new
                String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result1 = 1;
        int result2 = 1;
        if (ContextCompat.checkSelfPermission(_context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            result1 = 0;
        }
        if (ContextCompat.checkSelfPermission(_context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            result2 = 0;
        }
        if (result1 == 1 && result2 == 1)
            return true;
        else
            return false;
    }

    private void changeSeekbar() {
        _seekBarAudio.setProgress(mediaPlayer1.getCurrentPosition());
        if (mediaPlayer1.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();
                        _seekBarHint.setText(" " + secToTime((int) Math.ceil(mediaPlayer1.getCurrentPosition() / 1000f)));
                }
            };

            handler.postDelayed(runnable, 100);

        }
    }

    private String secToTime(int sec) {
        int min = 0;
        int hour = 0;
        min = sec / 60;
        sec = sec % 60;
        hour = min / 60;
        min = min % 60;
        if (sec < 10 && min < 10)
            return "" + "0" + min + ":0" + sec;
        else if (sec < 10 && min >= 10)
            return "" + "" + min + ":0" + sec;
        else if (sec >= 10 && min >= 10) {
            return "" + "" + min + ":" + sec;
        } else if (sec >= 10 && min < 10) {
            return "" + "0" + min + ":" + sec;
        }
        return "" + 0;
    }

    /**
     * open a dialog if want to replace the record
     * @param position
     */
    public void ReplaceRecordAlertDialog(final int position) {

        playClick = false; // set the play click to false
        // Create the object of
        // AlertDialog Builder class

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(_context);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to Replace your Record?");

        // Set Alert Title
        builder.setTitle("Replace Record ");

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
                                // then app will close
                                _recordButton.setImageResource(android.R.drawable.ic_media_pause);
                                recHelper();
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
                                _recordButton.setImageResource(R.drawable.ic_speak);
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    public void recHelper() {
        //   Toast.makeText(_context, "checking premmition", Toast.LENGTH_SHORT).show();
//        AudioSavePathInDevice = _context.getFilesDir() + "/" +
//                _recordFileName + "AudioRecording.3gp";
        _recordButton.setImageResource(android.R.drawable.ic_media_pause);
        recordDB.insertSong(_recordFileName, _songName);
        MediaRecorderReady();
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recordClick = !recordClick;
        _buttonPlayStopLastRecordAudio.setEnabled(false);
        //buttonStartAndStop.setImageResource(android.R.drawable.ic_media_pause);
        // _recPress.setVisibility(View.INVISIBLE);
        _chronometer.setVisibility(View.VISIBLE);
        _chronometer.setBase(SystemClock.elapsedRealtime());
        _chronometer.start();
        Toast.makeText(_context, "Record Started", Toast.LENGTH_SHORT).show();
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(_recordFileName);
    }

    public void clearMediaPlayer() {
        mediaPlayer1.stop();
        mediaPlayer1.release();
        mediaPlayer1 = null;
    }

    /**
     * finish activity needs to stop the record
     */
    public void stopRecFActivity() {
        // app icon in action bar clicked; go home
        playClick = !playClick;
//                    buttonStop.setEnabled(false);
//                    buttonStart.setEnabled(true);
        _buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
//                    buttonPlayStopLastRecordAudio.setEnabled(false);
//                    buttonPlayLastRecordAudio.setEnabled(true);
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
            mediaPlayer1.reset();
            //MediaRecorderReady();
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; go home
//                playClick = !playClick;
////                    buttonStop.setEnabled(false);
////                    buttonStart.setEnabled(true);
//                _buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
////                    buttonPlayStopLastRecordAudio.setEnabled(false);
////                    buttonPlayLastRecordAudio.setEnabled(true);
//                if (mediaPlayer1 != null) {
//                    mediaPlayer1.stop();
//                    mediaPlayer1.reset();
//                    ();
//                }
//                finish();
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    //}

}
