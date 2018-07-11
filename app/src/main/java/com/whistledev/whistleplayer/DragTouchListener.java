package com.whistledev.whistleplayer;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class DragTouchListener implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
       // view.setVisibility(View.INVISIBLE);
        return true;
    }
}