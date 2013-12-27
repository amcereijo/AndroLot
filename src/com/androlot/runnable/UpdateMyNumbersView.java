package com.androlot.runnable;

import android.app.Activity;
import android.text.Html;
import android.widget.TextView;

public class UpdateMyNumbersView {

	private UpdateButton updateButton;
	private TextView textView;
	private Activity activity;
	
	public UpdateMyNumbersView(Activity activity, TextView textView) {
		this.activity = activity;
		this.textView = textView;
	}
	
	public UpdateMyNumbersView(Activity activity, TextView textView, UpdateButton updateButton) {
		this.activity = activity;
		this.textView = textView;
		this.updateButton = updateButton;
	}
	
	public void updateView(final String price){
		final UpdateButton finalUpdateButton = updateButton;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(Html.fromHtml(price));
				if(finalUpdateButton!=null){
					finalUpdateButton.changeMessage();
				}
			}
		});
	}
}
