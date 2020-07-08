package com.example.mycardgame;

public class RecycleViewStructure {
    private String date;
    private String songName;
    private boolean isFinished = true;

    public RecycleViewStructure(String date, String songName, boolean isFinished) {
        this.date = date;
        this.songName = songName;
        this.isFinished = isFinished;
    }
    public RecycleViewStructure(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
