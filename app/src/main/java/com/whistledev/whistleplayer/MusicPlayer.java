package com.whistledev.whistleplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MusicPlayer{

    private ArrayList<SongObject> songList;
    private Context context;

    public MusicPlayer(Context context){
        songList = new ArrayList<>();
        this.context = context;
    }

    public ArrayList<SongObject> getSongList() {
        //retrieve song info
        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new SongObject(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        return songList;
    }


}
