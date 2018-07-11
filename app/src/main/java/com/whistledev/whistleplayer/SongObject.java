package com.whistledev.whistleplayer;

import android.support.annotation.NonNull;

public class SongObject implements Comparable<SongObject> {
    protected String title;
    protected String artist;
    //protected String path;

    public SongObject(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public int compareTo(@NonNull SongObject songObject) {
        return title.compareToIgnoreCase(songObject.title);
    }
}