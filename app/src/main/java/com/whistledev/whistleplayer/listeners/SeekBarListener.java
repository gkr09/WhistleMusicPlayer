package com.whistledev.whistleplayer.listeners;

import android.media.MediaPlayer;
import android.widget.SeekBar;

public class SeekBarListener implements SeekBar.OnSeekBarChangeListener{

    private MediaPlayer player;

    public SeekBarListener(MediaPlayer player) {

        this.player = player;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(this.player != null && fromUser){

            this.player.seekTo(progress * 1000);
        }
    }
}
