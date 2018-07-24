package com.whistledev.whistleplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ControlsLayout extends ViewGroup {

    /** Custom Layout that is pretty much similar to vertical linearlayout but with a top or bottom shadow.
     *  Used to contain the player controls at the bottom and searchView at the top.
     *  Background needed to be drawn manually so that it doesn't draw behind the shadow and make it opaque.
     *  Couldn't find any other solutions that fit this use case. **/

    private boolean isVertical;

    private Drawable shadowDrawable;
    private Drawable backgroundDrawable;

    private int shadowHeight = 5; // dp

    public ControlsLayout(Context c, AttributeSet attrs){

        super(c,attrs);
        this.setWillNotDraw(false);

        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.ControlsLayout, 0, 0);
        try {
            isVertical = a.getBoolean(R.styleable.ControlsLayout_isVertical,true);
        } finally {
            a.recycle();
        }

        if(isVertical)
            shadowDrawable = getResources().getDrawable(R.drawable.shadow_up);           // Shadow
        else
            shadowDrawable = getResources().getDrawable(R.drawable.shadow_down);

        backgroundDrawable = getResources().getDrawable(R.drawable.controls_bg);     // White Background
    }

    @Override
    protected void onDraw(Canvas c){

        super.onDraw(c);
        final int right = getRight();
        final int left = getLeft();
        final int top,bottom;

        // Background is drawn so that it doesn't cover the shadow and make it opaque.
        if(isVertical) {
            // Shadow on Top

            top = 0;      // Apparently 0 is the top of the layout.
            bottom = shadowHeight;
            backgroundDrawable.setBounds(left,bottom,right,getBottom());
        }
        else{
            // Shadow on Bottom

            top = this.getBottom()-shadowHeight;
            bottom = this.getBottom();
            backgroundDrawable.setBounds(left,0,right,this.getBottom()-shadowHeight);
        }

        shadowDrawable.setBounds(left, top, right, bottom);

        shadowDrawable.draw(c);
        backgroundDrawable.draw(c);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3){
        // Vertical Linear layout starting from below the shadow.

        int count = getChildCount();
        int prevChildLeft = 0;
        int prevChildTop = 0;//change this
        if(isVertical)       // If Vertical, start drawing child from below shadow.
            prevChildTop = shadowHeight;

        for (int j = 0; j < count; j++) {

            final View child = getChildAt(j);

            child.layout(prevChildLeft, prevChildTop, prevChildLeft + child.getMeasuredWidth(), prevChildTop + child.getMeasuredHeight());
            prevChildTop += child.getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = ((View)this.getParent()).getWidth();  // = match_parent
        int height = 0;
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            height += child.getMeasuredHeight();          // = wrap_content
        }
        this.setMeasuredDimension(width,height);
    }

    @Override
    public boolean shouldDelayChildPressedState(){

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        // This prevents the layout from being click-through ie, passing touch events to the recyclerview behind it.

        return true;
    }
}
