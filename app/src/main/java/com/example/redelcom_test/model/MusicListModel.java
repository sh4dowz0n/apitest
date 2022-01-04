package com.example.redelcom_test.model;

public class MusicListModel {
    private String artwork100;
    private String trackName;
    private String artist;
    private String collectionName;
    private int collectionId;
    private int trackId;
    private boolean checked;

    public MusicListModel(String artwork100, String tackName, String artist, String collectionName,
                          int collectionId, int trackId, boolean checked) {
        this.artwork100 = artwork100;
        this.trackName = tackName;
        this.artist = artist;
        this.collectionName = collectionName;
        this.collectionId = collectionId;
        this.trackId = trackId;
        this.checked = checked;
    }

    public MusicListModel() {
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtwork100() {
        return artwork100;
    }

    public void setArtwork100(String artwork100) {
        this.artwork100 = artwork100;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
