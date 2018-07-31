package com.whistledev.whistleplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.whistledev.whistleplayer.ControlsLayout;
import com.whistledev.whistleplayer.R;

public class SearchViewFragment extends Fragment {

    @Override
    public ControlsLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ControlsLayout layout = (ControlsLayout) inflater.inflate(R.layout.searchview_layout, container, false);

        return layout;
    }
}
