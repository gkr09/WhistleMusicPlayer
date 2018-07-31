package com.whistledev.whistleplayer.listeners;

import android.content.ClipData;
import android.view.View;

public class DragTouchListener implements View.OnLongClickListener {
    /** Enables dragging in the main recyclerview items. **/

    @Override
    public boolean onLongClick(View view) {

        ClipData data = ClipData.newPlainText("", "");

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);

        return true;
    }
}
