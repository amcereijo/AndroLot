package com.androlot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androlot.application.GameApplication;
import com.androlot.enums.GameTypeEnum;

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
		startGame(GameTypeEnum.ChristMas);
	}
	
	public void openKidGame(View view){
		startGame(GameTypeEnum.Kid);
	}
	
	private void startGame(GameTypeEnum gameType){
		GameApplication.setGameType(gameType);
		Intent newGame = new Intent(this, AndroLotActivity.class);
		startActivity(newGame);
	}
	
}
