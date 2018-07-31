package com.whistledev.whistleplayer;

import com.whistledev.whistleplayer.models.SongObject;

import java.util.ArrayList;
import java.util.Random;

public class MusicController {
    /** Controls music changes i.e, Shuffling, Finding next and previous songs, finding current playing title, id etc. **/

    private ArrayList<SongObject> songList;

    private Random randomGenerator;           // For shuffling

    private SongObject currSong = null;       // Holds the current playing song (SongObject)

    public MusicController() {

        randomGenerator = new Random();
    }

    public void setSongList(ArrayList<SongObject> songList) {
        // Setting the song list to be used i.e, main/queue/playlist

        this.songList = songList;
    }

    public void setCurrentSong(long id) {
        // For items selected from the main recyclerview.

        for(SongObject x:songList)
            if(x.equals(id))
                this.currSong = x;
    }

    public SongObject getShuffled() {

        int index = randomGenerator.nextInt(this.songList.size());

        if(!this.currSong.equals(this.songList.get(index).getId())) {

            this.currSong = this.songList.get(index);

            return this.currSong;
        }
        return getShuffled();
    }

    public long next(boolean shuffleStatus,boolean prev) {
        /** Finds next and previous songs based on value of prev (true = previous song, false = next song)
         *  shuffleStatus tells if shuffling is on or not if on, next/previous songs will be random **/

        int change = 1;   // Song will be this song's index+1 i.e, next song.

        if(prev)
            change = -1;  // Song will be previous.

        try {

            if (this.currSong != null)
                if (shuffleStatus)
                    this.currSong = getShuffled();
                else
                    this.currSong = songList.get(songList.indexOf(this.currSong)+change);
        }
        catch (IndexOutOfBoundsException e){

            // Do something.
        }
        return this.currSong.getId();
    }

    public String getTitle() {
        // Returns title for displaying in the bottom controls view.

        return this.currSong.getTitle();
    }

    public int getIndex() {
        // Just so that we can get the recyclerview to scroll to selected item on pressing next/previous.

        return songList.indexOf(this.currSong);
    }
}
