package com.androlot.runnable;

import android.widget.Button;

public class UpdateButton {
	private Button button;
	private int message;
	
	public UpdateButton(Button button, int message){
		this.button = button;
		this.message = message;
	}
	public void changeMessage(){
		this.button.setText(message);
	}
}