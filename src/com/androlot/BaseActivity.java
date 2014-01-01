package com.androlot;

import com.androlot.application.GameApplication;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameApplication.setContext(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		GameApplication.setContext(this);
	}
}
