package com.wws.android.ocr.opencv;

import java.io.File;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class ImageShow extends Activity {
	 private static final String  TAG = "OCR::CV";
	 
	 private Bitmap 				myBitmap;
	 private Bitmap 				workingBitmap;
	 private Mat 				 	mrgba;
	 private Mat  					mIntermediateMat;
	 private ImageView 				myImage;
	 
	 private boolean gray;
	 
	 public static final String DATA_PATH = Environment
				.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	
	 private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i(TAG, "OpenCV loaded successfully");
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	        }
	    };
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);
		Intent myIntent= getIntent();
		Log.i(TAG,"IMGSHOW: "+ myIntent.getExtras().getString("FILE_NAME"));
		File file = new File(myIntent.getExtras().getString("FILE_NAME"));
		myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath()).copy(Bitmap.Config.ARGB_8888, false); 
		
		myImage = (ImageView) findViewById(R.id.imageView1);
		
//		mrgba = new Mat();
//		mIntermediateMat = new Mat();
//		Utils.bitmapToMat(myBitmap, mrgba);
//		
//		//Imgproc.Canny(mrgba, mIntermediateMat, 80, 90);
//		
//		Utils.matToBitmap(mrgba, myBitmap);
		set_up();
		myImage.setImageBitmap(myBitmap);
	}
	
	public void set_up(){
		ToggleButton graybutton = (ToggleButton) findViewById(R.id.toggleButton1);
		graybutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	gray = true;		        	
		        } else {
		        	gray = false;
		        }
		        render();
		    }
		});
	}
	
	
	public void render(){
		mrgba = new Mat();
		mIntermediateMat = new Mat();
		Utils.bitmapToMat(myBitmap, mrgba);
		workingBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		if(gray){
			Imgproc.cvtColor(mrgba, mrgba, Imgproc.COLOR_RGB2GRAY,3);
		}

		Utils.matToBitmap(mrgba, workingBitmap);
		myImage.setImageBitmap(workingBitmap);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_show, menu);
		
		return true;
	}

}
