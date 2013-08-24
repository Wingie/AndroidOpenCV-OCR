package com.wws.android.ocr.opencv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

import com.googlecode.tesseract.android.TessBaseAPI;

import  com.wws.android.ocr.opencv.OcrResult;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ImageShow extends Activity {
	 private static final String  TAG = "OCR::CV";
	// You should have the trained data file in assets folder
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
	public static final String lang = "eng";
	 private Bitmap 				myBitmap;
	 private Bitmap 				workingBitmap;
	 private Mat 				 	mrgba;
	 private Mat  					mIntermediateMat;
	 private ImageView 				myImage;
	 
	 private boolean GRAY;
	 private boolean THRESHOLD;
	 
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
	protected com.wws.android.ocr.opencv.OcrResult ocrResult;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

//		for (String path : paths) {
//			File dir = new File(path);
//			if (!dir.exists()) {
//				if (!dir.mkdirs()) {
//					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
//					return;
//				} else {
//					Log.v(TAG, "Created directory " + path + " on sdcard");
//				}
//			}
//
//		}
//		
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
//		// This area needs work and optimization
//		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
//			try {
//
//				AssetManager assetManager = getAssets();
//				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
//				//GZIPInputStream gin = new GZIPInputStream(in);
//				OutputStream out = new FileOutputStream(DATA_PATH
//						+ "tessdata/" + lang + ".traineddata");
//
//				// Transfer bytes from in to out
//				byte[] buf = new byte[1024];
//				int len;
//				//while ((lenf = gin.read(buff)) > 0) {
//				while ((len = in.read(buf)) > 0) {
//					out.write(buf, 0, len);
//				}
//				in.close();
//				//gin.close();
//				out.close();
//				
//				Log.v(TAG, "Copied " + lang + " traineddata");
//			} catch (IOException e) {
//				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
//			}
//		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);
		Intent myIntent= getIntent();
		Log.i(TAG,"IMGSHOW: "+ myIntent.getExtras().getString("FILE_NAME"));
		File file = new File(myIntent.getExtras().getString("FILE_NAME"));
		myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath()).copy(Bitmap.Config.ARGB_8888, false); 
		workingBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
		myImage = (ImageView) findViewById(R.id.imageView1);
		
		set_up();
		myImage.setImageBitmap(myBitmap);

	}
	
	public void set_up(){
		ToggleButton graybutton = (ToggleButton) findViewById(R.id.toggleButton1);
		graybutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	GRAY = true;		        	
		        } else {
		        	GRAY = false;
		        }
		        render();
		    }
		});
		
		ToggleButton threshbutton = (ToggleButton) findViewById(R.id.toggleThreshold);
		threshbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	THRESHOLD = true;		        	
		        } else {
		        	THRESHOLD = false;
		        }
		        render();
		    }
		});
		
		Button buttonOCR = (Button) findViewById(R.id.buttonOCR);
		buttonOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

        		TessBaseAPI baseApi = new TessBaseAPI();
        		baseApi.setDebug(true);
        		baseApi.init(DATA_PATH, lang);
        		baseApi.setImage(workingBitmap);
        		
        		ocrResult = new OcrResult();
        	    ocrResult.setWordConfidences(baseApi.wordConfidences());
        	    ocrResult.setMeanConfidence( baseApi.meanConfidence());
        	    ocrResult.setRegionBoundingBoxes(baseApi.getRegions().getBoxRects());
        	    ocrResult.setTextlineBoundingBoxes(baseApi.getTextlines().getBoxRects());
        	    ocrResult.setWordBoundingBoxes(baseApi.getWords().getBoxRects());
        	    ocrResult.setStripBoundingBoxes(baseApi.getStrips().getBoxRects());
        	    ocrResult.setBitmap(workingBitmap);
        	    
        	    workingBitmap = ocrResult.getAnnotatedBitmap();
        	    myImage.setImageBitmap(workingBitmap);
        		String recognizedText = baseApi.getUTF8Text();
        		
        		Log.v(TAG, "OCR RESULT: "+recognizedText);
        		Toast.makeText(ImageShow.this, recognizedText, Toast.LENGTH_LONG).show();
        		baseApi.end();

            }
        });
	}
	
	
	public void render(){
		mrgba = new Mat();
		mIntermediateMat = new Mat();
		Utils.bitmapToMat(myBitmap, mrgba);
		workingBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		if(GRAY){
			Imgproc.cvtColor(mrgba, mrgba, Imgproc.COLOR_RGB2GRAY,3);
			}
		if(THRESHOLD){
			Imgproc.GaussianBlur(mrgba, mrgba, new Size(9,9),15);
			Imgproc.cvtColor(mrgba, mrgba, Imgproc.COLOR_RGB2GRAY,3);
			Imgproc.threshold(mrgba, mrgba,0,255,Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
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
