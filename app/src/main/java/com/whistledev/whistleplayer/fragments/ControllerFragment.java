package com.whistledev.whistleplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whistledev.whistleplayer.R;

public class ControllerFragment extends Fragment {

    private ImageButton playButton;
    private TextView currentLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.controller_layout,container, false);

        //queueButton = view.findViewById(R.id.queueButton);
        playButton = view.findViewById(R.id.playButton);
        currentLabel = view.findViewById(R.id.currentTrackLabel);

        return view;
    }

    public void setPlaying() {

        playButton.setImageResource(R.drawable.pause_icon);
    }

    public void setPaused() {

         playButton.setImageResource(R.drawable.play_icon);
    }

    public void setTitle(String title) {

         currentLabel.setText(title);
    }
}
