package com.example.mycardgame.dataBaseStructure;

public class SongImageDBStructure {

    private int themeCard;
    private int lyricsCard;
    private int instrumentCard;
    private int chordProgression;
    private String songNameKey;
    private int melodyCard;
    private String date;
    private int isFinished;

    public SongImageDBStructure(){};
    public SongImageDBStructure(int themeCard, int lyricsCard, int instrumentCard,
                                int chordProgression, String songNameKey, int melodyCard,String date,int isFinished) {
        this.themeCard = themeCard;
        this.lyricsCard = lyricsCard;
        this.instrumentCard = instrumentCard;
        this.chordProgression = chordProgression;
        this.songNameKey = songNameKey;
        this.melodyCard = melodyCard;
        this.date = date;
        this.isFinished = isFinished;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getThemeCard() {
        return themeCard;
    }

    public void setThemeCard(int themeCard) {
        this.themeCard = themeCard;
    }

    public int getLyricsCard() {
        return lyricsCard;
    }

    public void setLyricsCard(int lyricsCard) {
        this.lyricsCard = lyricsCard;
    }

    public int getInstrumentCard() {
        return instrumentCard;
    }

    public void setInstrumentCard(int instrumentCard) {
        this.instrumentCard = instrumentCard;
    }

    public int getChordProgression() {
        return chordProgression;
    }

    public void setChordProgression(int chordProgression) {
        this.chordProgression = chordProgression;
    }

    public String getSongNameKey() {
        return songNameKey;
    }

    public void setSongNameKey(String songNameKey) {
        this.songNameKey = songNameKey;
    }

    public int getMelodyCard() {
        return melodyCard;
    }

    public void setMelodyCard(int melodyCard) {
        this.melodyCard = melodyCard;
    }
}
