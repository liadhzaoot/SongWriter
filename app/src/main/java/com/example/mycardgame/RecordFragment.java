package com.example.mycardgame;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mycardgame.DataBase.RecordDB;
import com.example.mycardgame.dataBaseStructure.RecordDBStructure;

import java.io.IOException;



public class RecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String REC_PATH = "rec";
    private static final String SONG_NAME = "songName";

    // TODO: Rename and change types of parameters
    private String mRecPath;
    private String mSongName;
    private SeekBar seekBarAudio;
    private TextView seekBarHint;
    private ImageButton buttonPlayStopLastRecordAudio;
    private boolean playClick = false;
    private MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer1;
    private OnFragmentInteractionListener mListener;
    private Runnable runnable;
    private Handler handler;
    private RecordDB recordDB;
    private Button backBtn;
    private Boolean isPlayRec = true;
    //private String AudioSavePathInDevice = "";


    public RecordFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance( String recPath) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        //args.putString(SONG_NAME, songName);
        args.putString(REC_PATH, recPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecPath = getArguments().getString(REC_PATH);
            mSongName = getArguments().getString(SONG_NAME);
        }
        mediaPlayer1 = new MediaPlayer();
        mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBarAudio.setMax(mp.getDuration());
                changeSeekbar();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        seekBarAudio = v.findViewById(R.id.seekBarAudio);
        seekBarHint = v.findViewById(R.id.seekBarHint);
        buttonPlayStopLastRecordAudio = v.findViewById(R.id.playBtn);
        backBtn = v.findViewById(R.id.backBtn);
        recordDB = new RecordDB(v.getContext());
        handler = new Handler();

        buttonPlayStopLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlayRec = true; //check if the record have file;
                RecordDBStructure recordDBStructure = recordDB.getNoteTextStructure(mRecPath);
                seekBarAudio.setProgress(0);
                if (!playClick) {
                    //buttonPlayStopLastRecordAudio.setEnabled(true);

                    try {
                        mediaPlayer1.setDataSource(mRecPath);
                        mediaPlayer1.prepare();
                        seekBarAudio.setMax(mediaPlayer1.getDuration());
                       // Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "There is No Record Flie", Toast.LENGTH_SHORT).show();
                        isPlayRec = false;
                    }
                    if (isPlayRec) {
                        buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_pause);
                        mediaPlayer1.start();
                        Toast.makeText(view.getContext(), "Recording Playing",
                                Toast.LENGTH_LONG).show();
                        playClick = !playClick;
                        new Thread(String.valueOf(this)).start();
                    }
                    } else {
                        playClick = !playClick;
//                    buttonStop.setEnabled(false);
//                    buttonStart.setEnabled(true);
                        buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
//                    buttonPlayStopLastRecordAudio.setEnabled(false);
//                    buttonPlayLastRecordAudio.setEnabled(true);
                        if (mediaPlayer1 != null) {
                            mediaPlayer1.stop();
                            mediaPlayer1.reset();
                            MediaRecorderReady();
                        }
                    }

            }

            });
        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarHint.setVisibility(View.VISIBLE);
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

                if(mediaPlayer1 != null && fromTouch){
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


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed("asd");
            }
        });
        mediaPlayer1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playClick = !playClick;
//                    buttonStop.setEnabled(false);
//                    buttonStart.setEnabled(true);
                buttonPlayStopLastRecordAudio.setImageResource(android.R.drawable.ic_media_play);
                seekBarHint.setText("00:00");
//                    buttonPlayStopLastRecordAudio.setEnabled(false);
//                    buttonPlayLastRecordAudio.setEnabled(true);
                if (mediaPlayer1 != null) {
                    mediaPlayer1.stop();
                    mediaPlayer1.reset();
                    MediaRecorderReady();
                }
            }
        });

        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("asd");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void changeSeekbar() {

        seekBarAudio.setProgress(mediaPlayer1.getCurrentPosition());
        if(mediaPlayer1.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekbar();

                    seekBarHint.setText(" " +secToTime((int)Math.ceil(mediaPlayer1.getCurrentPosition()/1000f)));
                }
            };

            handler.postDelayed(runnable,100);

        }
    }
    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(mRecPath);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    private String secToTime(int sec)
    {
        int min = 0;
        int hour = 0;
        min = sec / 60;
        sec = sec % 60;
        hour = min / 60;
        min = min % 60;
        if(sec<10 && min<10)
            return "" + "0" + min  + ":0"+sec ;
        else if(sec<10&&min>=10)
            return "" + "" + min  + ":0"+sec ;
        else if(sec>=10&&min>=10)
        {
            return "" + "" + min  + ":"+sec ;
        }
        else if(sec>=10&&min<10)
        {
            return "" + "0" + min  + ":"+sec ;
        }
        return "" +0;
    }

public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }


}
