package com.wws.android.ocr.opencv;

import java.io.FileOutputStream;
import java.util.List;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;

public class CameraController extends JavaCameraView implements PictureCallback {

    private static final String TAG = "OCR::CV";
    private String mPictureFileName;
    private CameraScreen caller;
    public CameraController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getEffectList() {
        return mCamera.getParameters().getSupportedColorEffects();
    }

    public boolean isEffectSupported() {
        return (mCamera.getParameters().getColorEffect() != null);
    }

    public String getEffect() {
        return mCamera.getParameters().getColorEffect();
    }

    public void setEffect(String effect) {
        Camera.Parameters params = mCamera.getParameters();
        params.setColorEffect(effect);
        mCamera.setParameters(params);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }
    
  
    	  
    public void takePicture(final String fileName, CameraScreen c) {
        Log.i(TAG, "Taking picture");
        this.mPictureFileName = fileName;
        this.caller = c;
    	mCamera.autoFocus(myAutoFocusCallback);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // Write the image in a file (in jpeg format)
        try {
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(data);
            fos.close();

        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

    }
    
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

    	  @Override
    	  public void onAutoFocus(boolean arg0, Camera arg1) {
    		  Log.i(TAG, "FOCUS CALLBACK");
    		  mCamera.setPreviewCallback(null);
    		  arg1.takePicture(null, null, myPictureCallback_JPG);
    	  }
    	  
    	  PictureCallback myPictureCallback_JPG = new PictureCallback(){
    		  @Override
    		  public void onPictureTaken(byte[] data, Camera arg1) {
    		   // TODO Auto-generated method stub
    		   mCamera.startPreview();
    		// Write the image in a file (in jpeg format)
    	        try {
    	            FileOutputStream fos = new FileOutputStream(mPictureFileName);
    	            Log.i(TAG, "SAVING " + mPictureFileName);
    	            fos.write(data);
    	            fos.close();

    	        } catch (java.io.IOException e) {
    	            Log.e("PictureDemo", "Exception in photoCallback", e);
    	        }
    	        caller.finishedPic();
    		  }
    	  };
    };

   
}