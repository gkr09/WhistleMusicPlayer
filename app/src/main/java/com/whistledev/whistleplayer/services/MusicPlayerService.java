package com.whistledev.whistleplayer.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicPlayerService extends Service implements MediaPlayer.OnErrorListener {
    /** Implements a MediaPlayer object and binds it with a Service. **/

    private MediaPlayer player;
    private long currId;          // Stores the current song's Id
    private final IBinder musicBind = new MusicBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        player.stop();
        player.release();

        return false;
    }

    @Override
    public void onCreate() {
        // Creates the service and initialises the MediaPlayer Object.

        super.onCreate();

        player = new MediaPlayer();
        currId = 0;

        initMusicPlayer();
    }

    public void initMusicPlayer() {

        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnErrorListener(this);

        }

    public void playSong() {

        player.reset();

        Uri songUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currId);

        try{

            player.setDataSource(getApplicationContext(), songUri);
        }
        catch(Exception e){

            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        return false;
    }

    public void setSong(long songId) {
    // Used in MainActivity

        currId=songId;
    }
    public void pauseSong(boolean isPlaying) {

        if(isPlaying)
            player.pause();

        else
            player.start();
    }

    public MediaPlayer getPlayer() {
        // May be removed.

        return player;
    }

    public class MusicBinder extends Binder {

        public MusicPlayerService getService() {

            return MusicPlayerService.this;
        }
    }
}
