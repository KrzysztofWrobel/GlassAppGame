package com.github.krzysztofwrobel.glassappgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.krzysztofwrobel.glassappgame.CameraSurface;
import com.github.krzysztofwrobel.glassappgame.R;

public class CameraActivity extends Activity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    
	Camera mCamera;
	CameraSurface mPreview;
	ViewGroup previewView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        gestureDetector = new GestureDetector(this, this);
        initCameraView();
    }

	private void initCameraView()
	{
		previewView = (ViewGroup) findViewById(R.id.content);
		previewView.removeAllViews();
		mPreview = new CameraSurface(this);
		previewView.addView(mPreview);
		
		if (mCamera != null)
		{
			mCamera.release();
		}
		mCamera = Camera.open();
		mPreview.setCamera(mCamera);
	}
	
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //We need this to ensure it works on normal android devices
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);    //To change body of overridden methods use File | Settings | File Templates.
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
        super.onBackPressed();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture Example", "onDown");
//        startDescriptionActivity();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture Example", "onLongPress");

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Gesture Example", "onScroll: distanceX:" + distanceX + "   distanceY:" + distanceY);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture Test", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
    	mCamera.takePicture(new ShutterCallback()
		{

			@Override
			public void onShutter()
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
			}
		}, null, new PictureCallback()
		{

			@Override
			public void onPictureTaken(byte[] data, android.hardware.Camera camera)
			{
				Intent i = new Intent();
				i.putExtra("data", data);
				setResult(RESULT_OK, i);
				finish();
			}
		});
        return true;
    }

}
