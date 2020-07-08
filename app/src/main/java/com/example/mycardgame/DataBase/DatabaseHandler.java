package com.example.mycardgame.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.mycardgame.RecycleViewStructure;
import com.example.mycardgame.dataBaseStructure.SongImageDBStructure;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "songTable";
    private static final String TABLE_NAME = "songImages";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SONG_NAME = "songName";
    private static final String COLUMN_THEME_CARD = "themeCard";
    private static final String COLUMN_MELODY_CARD = "melodyCard";
    private static final String COLUMN_LYRICS_CARD = "lyricsCard";
    private static final String COLUMN_CHORD_PROGRESSION_CARD = "chordProgressionCard";
    private static final String COLUMN_INSTUMENTS_CARD = "instumentCard";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IS_FINISHED = "isFinished";
    private static final String COLUMN_NOTE = "noteCard";
    private static final String COLUMN_AUDIO = "audioCard";
    private Cursor cursor;
    private SQLiteDatabase db;
    //String CREATE_ITEM_TABLE;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
// Category table create query
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                                    COLUMN_SONG_NAME + " TEXT  PRIMARY KEY, " +
                                    COLUMN_THEME_CARD + " INTEGER, " +
                                    COLUMN_MELODY_CARD + " INTEGER, " +
                                    COLUMN_LYRICS_CARD + " INTEGER, " +
                                    COLUMN_CHORD_PROGRESSION_CARD + " INTEGER, " +
                                    COLUMN_INSTUMENTS_CARD + " INTEGER, " +
                                    COLUMN_DATE + " TEXT, "+
                                    COLUMN_IS_FINISHED +" INTEGER" +
                                    ");";
        db.execSQL(CREATE_ITEM_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

// Create tables again
        onCreate(db);
    }

    /*
    Inserting new lable into lables table
    */
    public void insertSong(String songName,int themeCard, int melodyCard,int lyricsCard,int chordProgressionCard,int instumentsCard,String date,int bool){
        //check if the song already exist
        if(isRowExist(songName))
            deleteSong(songName);

         db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SONG_NAME, songName);//column name, column value
        values.put(COLUMN_THEME_CARD, themeCard);//column name, column value
        values.put(COLUMN_MELODY_CARD, melodyCard);//column name, column value
        values.put(COLUMN_LYRICS_CARD, lyricsCard);//column name, column value
        values.put(COLUMN_CHORD_PROGRESSION_CARD, chordProgressionCard);//column name, column value
        values.put(COLUMN_INSTUMENTS_CARD, instumentsCard);//column name, column value
        values.put(COLUMN_DATE, date);//column name, column value
        values.put(COLUMN_IS_FINISHED, bool);//column name, column value


// Inserting Row
        db.insert(TABLE_NAME, null, values);
//tableName, nullColumnHack, CotentValues
        db.close();
// Closing database connection
    }
    public void deleteSong(String songName)
    {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_SONG_NAME + " = '" + songName + "';",null);
        db.close();
    }
    public boolean isRowExist(String songName) {
//        int numOfRows = 0;
//        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME + " = '" + songName + "';";
//        db = this.getReadableDatabase();
//        cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments
//
//        if (cursor.moveToFirst()) {
//            do {
//                numOfRows = cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
//            } while (cursor.moveToNext());
//
//        }
//            cursor.close();
//
//            db.close();
//        if(numOfRows>=1)
//            return true;
//
//        else
//             return false;

        /**
         * worked
         */
        int numOfRows = 0;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME + " = '" + songName + "';";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst()) {
            do {
                numOfRows++;
            } while (cursor.moveToNext());

        }

        if(numOfRows >= 1)
            return true;

        else
             return false;
    }

    /**
    Getting the chosen song card
    **/
    public SongImageDBStructure getSongCard(String songName){
        SongImageDBStructure songImageDBStructure = new SongImageDBStructure();

// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME + " = '" + songName + "';";
  
         db = this.getReadableDatabase();
         cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

// looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                list.add(cursor.getString(1));//adding 2nd column data
//            } while (cursor.moveToNext());
//        }
        if (cursor.moveToFirst()) {
            do {
                songImageDBStructure.setSongNameKey(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME)));
                songImageDBStructure.setChordProgression(cursor.getInt(cursor.getColumnIndex(COLUMN_CHORD_PROGRESSION_CARD)));
                songImageDBStructure.setInstrumentCard(cursor.getInt(cursor.getColumnIndex(COLUMN_INSTUMENTS_CARD)));
                songImageDBStructure.setLyricsCard(cursor.getInt(cursor.getColumnIndex(COLUMN_LYRICS_CARD)));
                songImageDBStructure.setMelodyCard(cursor.getInt(cursor.getColumnIndex(COLUMN_MELODY_CARD)));
                songImageDBStructure.setThemeCard(cursor.getInt(cursor.getColumnIndex(COLUMN_THEME_CARD)));
                songImageDBStructure.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                songImageDBStructure.setIsFinished(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FINISHED)));
            } while (cursor.moveToNext());
        }



// closing connection
        cursor.close();

        db.close();

// returning songImageDBStructure
        return songImageDBStructure;
    }
    // getting all songs from DB
    public ArrayList getAllSongs()
    {
        //insertSong("sadsad",100,200,300,40,40);
        db = getReadableDatabase();
        ArrayList arrayList;
        arrayList = new ArrayList<RecycleViewStructure>();
        //RecycleViewStructure recycleViewStructure = new RecycleViewStructure();
        String selectQuery = "SELECT " +  COLUMN_SONG_NAME  + ", " + COLUMN_DATE + ", " + COLUMN_IS_FINISHED +" FROM " + TABLE_NAME + "";
            cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

 //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecycleViewStructure recycleViewStructure = new RecycleViewStructure();
                recycleViewStructure.setSongName(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME)));
                recycleViewStructure.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                recycleViewStructure.setFinished(intToBool(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FINISHED))));
                arrayList.add(recycleViewStructure);//adding 2nd column data
            } while (cursor.moveToNext());
        }

        //closing
        cursor.close();
        db.close();

        return arrayList;
    }

    //int To Bool
    public static boolean intToBool(int n) {
        if (n == 1)
            return true;
        else if (n == 0)
            return false;
        return false;
    }

}