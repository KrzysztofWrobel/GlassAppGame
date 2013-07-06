package com.github.krzysztofwrobel.glassappgame;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    public Camera mCamera;
    
	public CameraSurface(Context context) {
        super(context);
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    
    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            requestLayout();
        }
    }

    @Override
	public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        if(mCamera!=null)
        {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Size> sizes = parameters.getSupportedPreviewSizes();
            Size s = sizes.get(0);
			parameters.setPreviewSize(s.width, s.height);
			mCamera.setParameters(parameters);
			try {
				mCamera.startPreview();
			} catch(RuntimeException e) {
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException ignored) {}
				mCamera.startPreview();
			}
			
        }
    }
    
    
    public void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1)
        {
        }
}

    
}