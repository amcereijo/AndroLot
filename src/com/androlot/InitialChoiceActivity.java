package com.androlot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * @author amcereijo
 *
 */
public class InitialChoiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_select_layout);
	}
	
	public void openChristmasGame(View view){
		//prepare christmas game
	}
	
	public void openKidGame(View view){
		//prepare KID game
	}
	
}
