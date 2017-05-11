package com.kanthan.teqbuzz.utilities;

/**
 * Created by user on 3/29/2016.
 */

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.kanthan.teqbuzz.fragments.BusMapFragment;

public class TouchableWrapper extends FrameLayout {

    private int min_distance = 100;
    private float downX, downY, upX, upY;
    View v;

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) { // Check vertical and horizontal touches
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                //return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                //HORIZONTAL SCROLL
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > min_distance) {
                        // left or right
                        if (deltaX < 0) {
                            BusMapFragment.isUserMarkerAnimationEnabled = true;
                            //this.onLeftToRightSwipe();
                            //return true;
                        }
                        if (deltaX > 0) {
                            BusMapFragment.isUserMarkerAnimationEnabled = true;
                            //this.onRightToLeftSwipe();
                            //return true;
                        }
                    } else {
                        //not long enough swipe...
                        //return false;
                    }
                }
                //VERTICAL SCROLL
                else {
                    if (Math.abs(deltaY) > min_distance) {
                        // top or down
                        if (deltaY < 0) {
                            BusMapFragment.isUserMarkerAnimationEnabled = true;
                            //this.onTopToBottomSwipe();
                            //return true;
                        }
                        if (deltaY > 0) {
                            BusMapFragment.isUserMarkerAnimationEnabled = true;
                            //this.onBottomToTopSwipe();
                            //return true;
                        }
                    } else {
                        //not long enough swipe...
                        //return false;
                    }
                }
                //return false;
            }
        }
        //return false;
        return super.dispatchTouchEvent(event);
    }


}