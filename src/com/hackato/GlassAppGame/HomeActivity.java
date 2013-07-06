package com.hackato.GlassAppGame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class HomeActivity extends Activity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        gestureDetector = new GestureDetector(this, this);

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("Gesture Example", "onBackPressed");
        Toast.makeText(getApplicationContext(), "Go Back", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture Example", "onDown");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Gesture Example", "onFling: velocityX:" + velocityX + "   velocityY:" + velocityY);
        if (velocityX < -3500) {
            Toast.makeText(getApplicationContext(), "Fling Right", Toast.LENGTH_SHORT).show();
        } else if (velocityX > 3500) {
            Log.d("Gesture Example", "OnFlingLeft");
            Toast.makeText(getApplicationContext(), "Fling Left", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture Example", "onLongPress");
        Toast.makeText(getApplicationContext(), "Long Press", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Gesture Example", "onScroll: distanceX:" + distanceX + "   distanceY:" + distanceY);
        if (distanceX < -1) {
            //Scroll Right or Scroll Up Command Here
        } else if (distanceX > 1) {
            Log.d("Gesture Example", "OnFlingLeft");
            //Scroll left or Scroll Down Command Here
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture Test", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Gesture Test", "onSingleTapUp");
        Toast.makeText(getApplicationContext(), "Single Tap Up", Toast.LENGTH_SHORT).show();
        return true;
    }

}
