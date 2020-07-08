package com.example.mycardgame;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycardgame.DataBase.DatabaseHandler;
import com.example.mycardgame.DataBase.RecordDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity implements SongNameEnterDialog.SongNameEnterDialogListener, backListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String SONG_NAME = "songName";
    //TODO: get from the DB
    private int counterOfSongs = 0;
    private RecyclerView songsView;
    private TextView numberOfSongs;
    private ImageButton shareIB;
    //    private ArrayList<ArrayList<String>> songNameArray = new ArrayList<>();
    private ArrayList<RecycleViewStructure> songNameArraySearchHelp = new ArrayList<>();
    private ArrayList<String> arrayOfSearchMoves = new ArrayList<>();
    private ArrayList<RecycleViewStructure> curOptinalResult = new ArrayList<>();
    //    private ArrayList<String> dateArray = new ArrayList<>();
    private EditText searchET;
    private RecyclerViewAdapter adapter;
    private ArrayList<RecycleViewStructure> songAndDateArray = new ArrayList<>();
    private String _songName = "liad";
    //check if the user search and try to open new song
    private int i = 0;
    public static final String IS_CREATED = "isCreated";
    private boolean isLastPageFinish = false;
    private RecordDB recordDB;
    private DrawerLayout drawerLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // isLastPageFinish = (boolean) getIntent().getSerializableExtra(FinalPage.IS_FINISH);

        //action bar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Clinical Card Game");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * navigation view
         */
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        /**
         * -----------------------------------
         */

        //actionBar.setDisplayHomeAsUpEnabled(true);
        //=========
        initAllViews();
        initArrays();
        changeStatusNotFinishSong();
        //=========


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundResource(android.R.drawable.ic_input_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the user search and try to open new song
                if (i == 0) {
                    openDialog(1);
                    replaceScreenAdapter();
                    //kyboard hide
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else {
                    Snackbar.make(view, "Dont search and open New Song", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        numberOfSongs.setText(songAndDateArray.size() + " Songs");

        searchSongTextBox();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.start_menu,menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.website:
//                //Toast.makeText(this, "web", Toast.LENGTH_SHORT).show();
////                Intent i = new Intent(this,WebsiteActivity.class);
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mayamusic.com/")); //open website
//                startActivity(i);
//                return true;
//            case R.id.howToPlay:
//                Toast.makeText(this, "howToPlay", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.rateUS:
//                Toast.makeText(this, "rateUS", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.contactUs:
//                //Toast.makeText(this, "rateUS", Toast.LENGTH_SHORT).show();
//                openDialog(2);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    /**
     * check if the song was finish -
     * if they have the last record
     */
    public void changeStatusNotFinishSong() {
//        boolean isExist = false;
//        String song = (String) getIntent().getSerializableExtra(GameActivity.SONG_NAME);
//        for (int j = 0; j <songAndDateArray.size() ; j++) {
//            isExist = adapter.isSubStringIn(songAndDateArray.get(j).getSongName(),song);
//            if(isExist)
//            {
//                songAndDateArray.get(j).setFinished(false);
//            }
//        }
        boolean isRawExist = false;
        for (int j = 0; j < songAndDateArray.size(); j++) {
            isRawExist = recordDB.isRowExist(getFilesDir() + "/" +
                    songAndDateArray.get(j).getSongName() +
                    "FinalSongRec" +
                    "File" + "AudioRecording.3gp");
            if (!isRawExist) {
                songAndDateArray.get(j).setFinished(false);
            }
        }

    }

    private void searchSongTextBox() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                i = count;
                curOptinalResult = copyArray(songNameArraySearchHelp, curOptinalResult);
                //clean list
                int arraySize = songAndDateArray.size();
                if (arraySize != 0) {
                    for (int i = 0; i < arraySize; i++) {
                        songAndDateArray.remove(0);
                    }
                }
                songAndDateArray = adapter.searchTextInArray(curOptinalResult, s);
                if (songAndDateArray.size() != 0 && songAndDateArray.get(0).getSongName().equals("1")) {

                    songAndDateArray.remove(0);
                    songAndDateArray = copyArray(songNameArraySearchHelp, songAndDateArray);
                }
                adapter.setSongNameArray(songAndDateArray);
                songsView.setAdapter(adapter);
                songsView.setLayoutManager(new LinearLayoutManager(getBaseContext()));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //------copy the first array to second------------
    private ArrayList<RecycleViewStructure> copyArray(ArrayList<RecycleViewStructure> copy, ArrayList<RecycleViewStructure> past) {
        int size = past.size();
        if (past.size() != 0) {
            for (int i = 0; i < size; i++) {
                past.remove(0);
            }
        }

        for (int i = 0; i < copy.size(); i++) {
            past.add(copy.get(i));
        }

        return past;
    }

    public void initArrays() {
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<RecycleViewStructure> list = db.getAllSongs();
        songAndDateArray.clear();
        songNameArraySearchHelp.clear();

        if (list != null) {

            for (int i = 0; i < list.size(); i++) {
                RecycleViewStructure recycleViewStructure = new RecycleViewStructure(list.get(i).getDate(), list.get(i).getSongName(), list.get(i).isFinished());
                songAndDateArray.add(recycleViewStructure);
            }
            for (int j = 0; j < list.size(); j++) {
                songNameArraySearchHelp.add(songAndDateArray.get(j));
            }

        }
        numberOfSongs.setText(songAndDateArray.size() + " Songs");
    }

    public void initAllViews() {
        numberOfSongs = findViewById(R.id.numberOfSongsTV);
        songsView = findViewById(R.id.SongsRV);
        searchET = findViewById(R.id.searchET);
        shareIB = findViewById(R.id.shareIB);
        recordDB = new RecordDB(this);
        replaceScreenAdapter();

    }

    private void addNewSong(String songName, String date) {
        RecycleViewStructure recycleViewStructure = new RecycleViewStructure(date, songName, false);
        songAndDateArray.add(recycleViewStructure);
        songNameArraySearchHelp = copyArray(songAndDateArray, songNameArraySearchHelp);
        numberOfSongs.setText(songAndDateArray.size() + " Songs");
    }

    public void replaceScreenAdapter() {
        adapter = new RecyclerViewAdapter(this, songAndDateArray, shareIB, this);
        songsView.setAdapter(adapter);
        songsView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * if the num == 1 open the SongNameEnterDialog
     * num == 2 open the ContactUsDialog
     *
     * @param num
     */
    private void openDialog(int num) {
        if (num == 1) {
            SongNameEnterDialog songNameEnterDialog = new SongNameEnterDialog();
            songNameEnterDialog.show(getSupportFragmentManager(), "songNameEnter");
        } else if (num == 2) {
            ContactUsDialog contactUsDialog = new ContactUsDialog();
            contactUsDialog.show(getSupportFragmentManager(), "contactUsDialog");

        }
    }

    @Override
    public void applyTexts(String songName) {
        // check if new song name not exist
        if (adapter.songNameIsExist(songAndDateArray, songName) && !songName.equals("")) {

            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(SONG_NAME, songName);
            intent.putExtra(IS_CREATED, true);
            startActivity(intent);

            _songName.equals(songName);
            addNewSong(_songName, "00,00,00");
            finish();

        } else if (!adapter.songNameIsExist(songAndDateArray, songName)) {
            Toast.makeText(this, "This song name already exist", Toast.LENGTH_SHORT).show();
        } else if (songName.equals("")) {
            Toast.makeText(this, "You must enter a Song Name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void backButtonClickListener(String songName) {

    }

    public void removeSearchText() {
        searchET.setText("");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_website:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mayamusic.com/")); //open website
                startActivity(i);
                break;
            case R.id.nav_how_to_play:
                Toast.makeText(this, "howToPlay", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rate_this_app:
                //Toast.makeText(this, "rateUS", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

                break;
            case R.id.nav_contact_us:
                //Toast.makeText(this, "rateUS", Toast.LENGTH_SHORT).show();
                openDialog(2);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "share this app", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}