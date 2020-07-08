package com.example.mycardgame;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT = "text";


    // TODO: Rename and change types of parameters
    private String mText;

    private OnFragmentInteractionListener mListener;

    private TextView textView;
    private Button backBtn;
    public NoteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String text) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note,container,false);
        textView = view.findViewById(R.id.txtNote);
        backBtn = view.findViewById(R.id.backBtn);
        if(mText.equals(""))
        {
            textView.setText("No Note Was Written");
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        else {
            textView.setGravity(0);
            textView.setText(mText);
        }
        textView.requestFocus();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed("asd");
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }



//    public void loadFile(String fileName)
//    {
//        NoteDBStructure noteTextStructure = new NoteDBStructure();
//        noteTextStructure = noteDB.getNoteTextStructure(fileName);
//
//        FileInputStream fis = null;
//        try {
//            fis = openFileInput(noteTextStructure.getSongPathKey());
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String text;
//
//            while((text = br.readLine()) != null) {
//                sb.append(text).append("\n");
//
//            }
//            textView.setText(sb.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(fis != null)
//            {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    private String getFileName(int i)
//    {
//        String packName = pack.getPackCard().get(i).getSubject();
//        String fileName = "";
//        if(packName.equals("Theme"))
//        {
//            fileName = songName + NotePad.Theme_FILE;
//        }
//        else if(packName.equals("Melody"))
//        {
//            fileName = songName + NotePad.Melody_FILE;
//        }
//        else if(packName.equals("Instruments"))
//        {
//            fileName = songName + NotePad.Instruments_FILE;
//        }
//        else if(packName.equals("Lyrics"))
//        {
//            fileName = songName + NotePad.LYRICS_FILE;
//        }
//        else if(packName.equals("CordProgression"))
//        {
//            fileName = songName + NotePad.CordProgression_FILE;
//        }
//        noteDB.insertSong(fileName,songName);
//        return fileName;
//
//    }

}
