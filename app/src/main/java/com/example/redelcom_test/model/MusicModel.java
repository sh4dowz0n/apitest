package com.example.redelcom_test.model;

import android.widget.ImageButton;

public class MusicModel {
    private String artworkUrl100;
    private String trackName;
    private String artistName;
    private String previewUrl;
    private String collectionId;
    private String collectionName;
    private ImageButton imageButton;

    public MusicModel(String artworkUrl100, String trackName, String artistName, String previewUrl,
                      String collectionId, String collectionName, ImageButton imageButton) {
        this.artworkUrl100 = artworkUrl100;
        this.trackName = trackName;
        this.artistName = artistName;
        this.previewUrl = previewUrl;
        this.collectionId = collectionId;
        this.collectionName = collectionName;
        this.imageButton = imageButton;
    }

    public MusicModel() {
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }
}
