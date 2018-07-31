package com.whistledev.whistleplayer.models;

import android.support.annotation.NonNull;

public class SongObject implements Comparable<SongObject> {
    /** Represents a Song with it's Title, Artist and ID **/

    private String title;
    private String artist;
    private long id;

    public SongObject(long id, String title, String artist) {

        this.title = title;
        this.artist = artist;
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull SongObject songObject) {
        // So that the Song list can easily be sorted using Collections.

        return title.compareToIgnoreCase(songObject.title);
    }

    public boolean equals(long id) {
        // Checking if two SongObjects are equal by comparing their ID

        if(id == this.id)
            return true;
        return false;
    }

    public long getId() {

        return id;
    }

    public String getTitle() {

        return title;
    }

    public String getArtist() {

        return artist;
    }
}
