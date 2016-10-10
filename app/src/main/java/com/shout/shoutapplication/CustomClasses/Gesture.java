package com.shout.shoutapplication.CustomClasses;

import android.view.GestureDetector;
import android.view.MotionEvent;

class Gesture extends GestureDetector.SimpleOnGestureListener{
            public boolean onSingleTapUp(MotionEvent ev) {
                return false;
            }

            public void onLongPress(MotionEvent ev) {
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                return false;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                return false;
            }
        }