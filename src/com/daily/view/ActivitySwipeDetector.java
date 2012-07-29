/**
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details at http://www.gnu.org/copyleft/gpl.html
 */
package com.daily.view;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * The Class ActivitySwipeDetector. Detects and recognizes on-touch events.
 */
public class ActivitySwipeDetector implements View.OnTouchListener {
    
    static final String logTag = "ActivitySwipeDetector";
    private SwipeDetectorHandler handler;
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private View view;
    
    /**
     * Instantiates a new activity swipe detector.
     *
     * @param handler the handler
     * @param view the view which we are detecting the swipe on.
     */
    public ActivitySwipeDetector(SwipeDetectorHandler handler, View view) {
        this(handler, view, MIN_DISTANCE);
    }
    
    /**
     * Instantiates a new activity swipe detector.
     *
     * @param handler the handler
     * @param view the view
     * @param minDistance the min distance
     */
    public ActivitySwipeDetector(SwipeDetectorHandler handler, View view, int minDistance) {
        this.handler = handler;
        this.view = view;
        
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                //this.view.onTouchEvent(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();
                
                float deltaX = downX - upX;
                float deltaY = downY - upY;
                
                // swipe horizontal?
                if ((Math.abs(deltaX) > MIN_DISTANCE) && (Math.abs(deltaY) < MIN_DISTANCE / 2) ) {
                    // left or right
                    if (deltaX < 0) {
                        this.handler.onRightToLeftSwipe(event);
                    }
                    if (deltaX > 0) {
                        this.handler.onLeftToRightSwipe(event);
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }
                //this.view.onTouchEvent(event);
                break;
                // swipe vertical
//                if (Math.abs(deltaY) > MIN_DISTANCE) {
//                    // top or down
//                    if (deltaY < 0) {
//                        this.handler.onTopToBottomSwipe(event);
//                        return true;
//                    }
//                    if (deltaY > 0) {
//                        this.handler.onBottomToTopSwipe(event);
//                        return true;
//                    }
//                } else {
//                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
//                }
            }
        }
        try {
            this.view.onTouchEvent(event);
        } catch (Exception e) {
            Log.i(logTag, "Prpoagate touch event to WebView failed. " + e.getMessage());
        }
        return true;
    }
}
