package com.example.mycardgame.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.mycardgame.dataBaseStructure.NoteDBStructure;
import com.example.mycardgame.dataBaseStructure.RecordDBStructure;

import java.io.File;
import java.util.ArrayList;


public class RecordDB  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "recordTable";
    private static final String COLUMN_SONG_NAME_PATH = "songNamePath";
    private static final String TABLE_NAME = "recordTable";
    private static final String COLUMN_SONG_NAME = "songName";
    private Cursor cursor;
    private SQLiteDatabase db;

    public RecordDB( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// Category table create query
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_SONG_NAME_PATH + " TEXT  PRIMARY KEY, " +
                COLUMN_SONG_NAME + " TEXT " +
                ");";
        db.execSQL(CREATE_ITEM_TABLE);
    }

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
    public void insertSong(String notePath,String songName){
        //check if the song already exist
        if(isRowExist(notePath))
            deleteRaw(notePath);

        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SONG_NAME_PATH, notePath);//column name, column value
        values.put(COLUMN_SONG_NAME, songName);//column name, column value

// Inserting Row
        db.insert(TABLE_NAME, null, values);
//tableName, nullColumnHack, CotentValues
        db.close();
// Closing database connection
    }
    public void deleteSong(String songName, File myFile)
    {
        Cursor cursor1;
        SQLiteDatabase db1;
        ArrayList<NoteDBStructure> noteDBStructuresArray = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_SONG_NAME_PATH +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_SONG_NAME + " = '" + songName + "';";

        db1 = this.getReadableDatabase();
        cursor1 = db1.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor1.moveToFirst()) {
            do {
                //deleteRaw(cursor1.getString(cursor1.getColumnIndex(COLUMN_SONG_NAME_PATH)));
                noteDBStructuresArray.add(new NoteDBStructure(cursor1.getString(cursor1.getColumnIndex(COLUMN_SONG_NAME_PATH)),
                        null));
            } while (cursor1.moveToNext());

        }

        db1.close();


        db1 = this.getWritableDatabase();


        //File dir = myFile;
        //File dir = new File(noteDBStructuresArray.get(i).getSongPathKey());

        for (int i = 0; i < noteDBStructuresArray.size(); i++) {
            File file = new File(noteDBStructuresArray.get(i).getSongPathKey());
            file.delete();
            //db1.delete(TABLE_NAME,COLUMN_SONG_NAME + " = '" + songName + "';",null);
            db1.delete(TABLE_NAME,COLUMN_SONG_NAME_PATH + " = '" + noteDBStructuresArray.get(i).getSongPathKey() + "';",null);
        }

        db1.close();


    }
    public void deleteRaw(String songNotePath)
    {

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_SONG_NAME_PATH + " = '" + songNotePath + "';",null);
        db.close();
    }
    public boolean isRowExist(String songPath) {
        int numOfRows = 0;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME_PATH + " = '" + songPath + "';";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        if (cursor.moveToFirst()) {
            do {
                numOfRows++;
            } while (cursor.moveToNext());

        }

        if (numOfRows >= 1)
            return true;

        else
            return false;
    }

    /**
     getNoteTextStructure
     **/
    public RecordDBStructure getNoteTextStructure(String songPath){
        RecordDBStructure recordDBStructure = new RecordDBStructure();

// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SONG_NAME_PATH + " = '" + songPath + "';";

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
                recordDBStructure.setSongPathKey(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME_PATH)));
                recordDBStructure.setSongName(cursor.getString(cursor.getColumnIndex(COLUMN_SONG_NAME)));
            } while (cursor.moveToNext());
        }



// closing connection
        cursor.close();

        db.close();

// returning songImageDBStructure
        return recordDBStructure;
    }
}



