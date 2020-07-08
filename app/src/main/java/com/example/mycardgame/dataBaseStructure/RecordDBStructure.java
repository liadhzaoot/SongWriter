package com.example.mycardgame.dataBaseStructure;

public class RecordDBStructure {
    private String songPathKey;
    private String songName;

    public RecordDBStructure(){}

    public RecordDBStructure(String songPathKey, String songName) {
        this.songPathKey = songPathKey;
        this.songName = songName;
    }

    public String getSongPathKey() {
        return songPathKey;
    }

    public void setSongPathKey(String songNameKey) {
        this.songPathKey = songNameKey;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String text) {
        this.songName = text;
    }
}
