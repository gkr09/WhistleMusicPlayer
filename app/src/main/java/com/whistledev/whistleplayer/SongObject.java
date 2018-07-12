package com.whistledev.whistleplayer;

import android.support.annotation.NonNull;

public class SongObject implements Comparable<SongObject> {
    protected String title;
    protected String artist;
    protected long id;
    //protected String path;

    public SongObject(long id, String title, String artist) {
        this.title = title;
        this.artist = artist;
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull SongObject songObject) {
        return title.compareToIgnoreCase(songObject.title);
    }
}