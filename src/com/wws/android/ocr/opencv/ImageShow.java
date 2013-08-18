package com.wws.android.ocr.opencv;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ImageShow extends Activity {
	 private static final String  TAG = "OCR::CV";
	 
	 public static final String DATA_PATH = Environment
				.getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_show, menu);
		Intent myIntent= getIntent();
		return true;
	}

}
