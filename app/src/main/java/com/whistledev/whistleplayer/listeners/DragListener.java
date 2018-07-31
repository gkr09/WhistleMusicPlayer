package com.whistledev.whistleplayer.listeners;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

class DragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(TAG, "-------> STARTED DRAG ");
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG, "-------> ENTERED VIEW ");
                   // v.setBackgroundDrawable(enterShape);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG, "-------> EXITED VIEW ");
                   // v.setBackgroundDrawable(normalShape);
                    break;

                case DragEvent.ACTION_DROP:
                    Log.d(TAG, "-------> DROPPED DRAG ");
                    // Dropped, reassign View to ViewGroup
                  //  View view = (View) event.getLocalState();
                  //  ViewGroup owner = (ViewGroup) view.getParent();
                  //  owner.removeView(view);
                  //  LinearLayout container = (LinearLayout) v;
                  //  container.addView(view);
                  //  view.setVisibility(View.VISIBLE);
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG, "-------> DRAG ENDED ");
                   // v.setBackgroundDrawable(normalShape);

                default:
                    break;

            }
            return true;
        }
        }
