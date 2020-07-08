package com.example.mycardgame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycardgame.DataBase.DatabaseHandler;
import com.example.mycardgame.DataBase.NoteDB;
import com.example.mycardgame.DataBase.RecordDB;
import com.example.mycardgame.dataBaseStructure.SongImageDBStructure;

import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.RecyclerView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {
    private static final String TAG = "RecyclerViewAdapter";
    public static final String SONG_NAME = "songName";
    public static final String CHORD_POGRESSION = "chordPogression";
    public static final String INSTRUMENT = "instrument";
    public static final String LYRICS = "lyrics";
    public static final String MELODY = "melody";
    public static final String THEME = "theme";


    private  Intent shareIntent = new Intent();
    private ArrayList<RecycleViewStructure>  songNameAndDateArray = new ArrayList<>();
    private ArrayList<String> dateArray = new ArrayList<>();
    private ImageButton shareIB;
    Context context;
    private StartActivity startActivity;
    private TextView tv ;
    public RecyclerViewAdapter(Context context, ArrayList<RecycleViewStructure>  songNameAndDateArray, ImageButton shareIB, StartActivity startActivity) {
        this.songNameAndDateArray = songNameAndDateArray;
        this.context = context;
        this.shareIB = shareIB;
        this.startActivity = startActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //view group parent
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final int newI  = songNameAndDateArray.size() - i - 1;
        viewHolder.songNameTV.setText(songNameAndDateArray.get(newI).getSongName());
        viewHolder.dateTV.setText(songNameAndDateArray.get(newI).getDate());
        if(!songNameAndDateArray.get(newI).isFinished())
        {
            //viewHolder.itemView.animate().;
            //viewHolder.itemView.setBackgroundResource(R.color.IndianRed);
            //viewHolder.itemView.setBackgroundResource(R.color.IndianRed);
            viewHolder.isFinished.setVisibility(View.VISIBLE);
        }
        viewHolder.shareIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Song Name - '" + songNameAndDateArray.get(newI).getSongName().toString() + "' " +
                        "\n Open Date - " + " '" + songNameAndDateArray.get(newI).getDate() + "' \n Application Owner : Liad Hazoot & Maya Haddi Zebli");
                shareIntent.setType("text/plain");
                PopupMenu popup = new PopupMenu(startActivity, v);
                //popup.inflate(R.menu.popup_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.share:

                                startActivity.startActivity(shareIntent);

                                return true;
                            case R.id.delete: {
                                deleteAlertDialog(newI);


                                //StartActivity.replaceScreenAdapter();
                                //StartActivity.initArrays();
                            }
                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.popup_menu);
                popup.show();


            }
        });
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),GameActivity.class);
                DatabaseHandler db = new DatabaseHandler(v.getContext());
                SongImageDBStructure songImageDBStructure;
                songImageDBStructure = db.getSongCard(songNameAndDateArray.get(newI).getSongName().toString());

                intent.putExtra(SONG_NAME,songNameAndDateArray.get(newI).getSongName().toString());
                intent.putExtra(CHORD_POGRESSION,songImageDBStructure.getChordProgression());
                intent.putExtra(INSTRUMENT,songImageDBStructure.getInstrumentCard());
                intent.putExtra(LYRICS,songImageDBStructure.getLyricsCard());
                intent.putExtra(MELODY,songImageDBStructure.getMelodyCard());
                intent.putExtra(THEME,songImageDBStructure.getThemeCard());

                startActivity.startActivity(intent);
                startActivity.finish();
                Toast.makeText(context, songNameAndDateArray.get(newI).getDate() + " " + songNameAndDateArray.get(newI).getDate(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songNameAndDateArray.size();
    }


    public void setSongNameArray(ArrayList<RecycleViewStructure> songNameArray) {
        this.songNameAndDateArray = songNameArray;
    }

    public ArrayList<String> getDateArray() {
        return dateArray;
    }

    public void setDateArray(ArrayList<String> dateArray) {
        this.dateArray = dateArray;
    }

    public ArrayList<RecycleViewStructure> searchTextInArray(ArrayList<RecycleViewStructure> arrayList, CharSequence subString) {
        int index = 0;
        ArrayList<RecycleViewStructure> stringArrayList = new ArrayList<>();
        RecycleViewStructure recycleViewStructure = new RecycleViewStructure();
        //ArrayList<String> list2 = new ArrayList<>();
        if(subString.length() == 0)
        {
            /** if the first object in the list is "1" so show all the options **/
            recycleViewStructure.setDate("1");
            recycleViewStructure.setSongName("1");
            stringArrayList.add(recycleViewStructure);
            return stringArrayList;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (isSubStringIn(arrayList.get(i).getSongName().trim(), subString.toString().trim())) {
//                ArrayList<String> list2 = new ArrayList<>();
//                list2.add(arrayList.get(i).get(0));
//                list2.add(arrayList.get(i).get(1));

                stringArrayList.add(arrayList.get(i));

            }
        }


        return stringArrayList;
    }
    //check if the song name is exist
    public boolean songNameIsExist( ArrayList<RecycleViewStructure> arrayList ,String subString)
    {

        for (int i = 0; i < arrayList.size(); i++) {

            if(arrayList.get(i).getSongName().toUpperCase().trim().equals(subString.toUpperCase().trim()))
            {
                return false;
            }
        }
        return true;
    }

    //check if substring includ the long string
    public boolean isSubStringIn(String longStr, CharSequence subString) {
        if(longStr == null || subString == null)
        {
            return false;
        }
        boolean isEqual = true;
        if (subString.length() > longStr.length())
        {
            return false;
        }
        longStr = longStr.toLowerCase();
        subString = subString.toString().toLowerCase();
        for (int i = 0; i < subString.length(); i++) {
            if (longStr.charAt(i) != subString.charAt(i))
                isEqual = false;
        }
        return isEqual;
    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameTV, dateTV,isFinished;
        private ImageButton shareIB, listenIB;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTV = itemView.findViewById(R.id.songNameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            shareIB = itemView.findViewById(R.id.shareIB);
            //listenIB = itemView.findViewById(R.id.listenIB);
            relativeLayout = itemView.findViewById(R.id.RelativeLayout);
            isFinished = itemView.findViewById(R.id.isFinished);
        }


    }
    public void deleteAlertDialog(final int position)
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(startActivity);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to Delete ?");

        // Set Alert Title
        builder.setTitle("Delete Song ");

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
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                DatabaseHandler db = new DatabaseHandler(startActivity);
                                NoteDB noteDB = new NoteDB(startActivity);
                                RecordDB recordDB = new RecordDB(startActivity);

                                db.deleteSong(songNameAndDateArray.get(position).getSongName().toString());
                                noteDB.deleteSong(songNameAndDateArray.get(position).getSongName(),context.getFilesDir());
                                recordDB.deleteSong(songNameAndDateArray.get(position).getSongName(),getExternalStorageDirectory());

                                Toast.makeText(context, "the song \""+songNameAndDateArray.get(position).getSongName() + "\" Deleted", Toast.LENGTH_SHORT).show();
                                songNameAndDateArray.remove(position);
                                startActivity.removeSearchText();
                                //StartActivity.setSongNumberAfterDelete();
                                //StartActivity.deleteFromSongAndDateArray(position);
                                startActivity.initArrays();
                                startActivity.initAllViews();
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
                                                int which)
                            {

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
