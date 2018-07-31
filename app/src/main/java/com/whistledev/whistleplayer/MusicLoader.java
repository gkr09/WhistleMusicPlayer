package com.whistledev.whistleplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.whistledev.whistleplayer.models.SongObject;

import java.util.ArrayList;

public class MusicLoader {
    /** Scans the device storage for music files and returns the as a list of SongObjects **/

    private ArrayList<SongObject> songList;
    private Context context;

    public MusicLoader(Context context) {

        songList = new ArrayList<>();
        this.context = context;
    }

    public ArrayList<SongObject> getSongList() {
        // Retrieves songs from the storage and adds them to songList

        ContentResolver resolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(musicUri, null, null, null, null);

        if(cursor!=null && cursor.moveToFirst()) {

            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            do {

                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);

                songList.add(new SongObject(thisId, thisTitle, thisArtist));
            }
            while (cursor.moveToNext());
        }
        return songList;
    }
}
