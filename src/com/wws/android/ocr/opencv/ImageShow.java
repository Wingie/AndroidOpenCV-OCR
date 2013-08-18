package com.wws.android.ocr.opencv;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class ImageShow extends Activity {
	 private static final String  TAG = "OCR::CV";
	 
	 public static final String DATA_PATH = Environment
				.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);
		Intent myIntent= getIntent();
		Log.i(TAG,"IMGSHOW: "+ myIntent.getExtras().getString("FILE_NAME"));
		File file = new File(myIntent.getExtras().getString("FILE_NAME"));
		Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		ImageView myImage = (ImageView) findViewById(R.id.imageView1);
		myImage.setImageBitmap(myBitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_show, menu);
		
		return true;
	}

}
