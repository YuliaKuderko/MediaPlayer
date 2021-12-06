package com.example.mediaplayer;

import java.io.Serializable;

public class Song implements Serializable {
    private String songName;
    private String artist;
    private String imageResId;
    private String link;


    public Song(String name, String artist, String imageResId, String link) {
        this.songName = name;
        this.artist = artist;
        this.imageResId = imageResId;
        this.link = link;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getLink() {
        return link;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", artistName='" + artist + '\'' +
                ", link='" + link + '\'' +
                ", photoID=" + imageResId +
                '}';
    }
}

