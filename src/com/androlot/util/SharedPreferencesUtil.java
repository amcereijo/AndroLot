package com.androlot.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	private final static String FILE_SHARED = "fileSharedAndroLot";
	private final static String MOMENT_FORMAT = "%s:%s";
	
	public static String saveLastCheck(Context context) {
		Calendar c = Calendar.getInstance();
		String moment = String.format(MOMENT_FORMAT, format2Digits(c.get(Calendar.HOUR_OF_DAY)),
				format2Digits(c.get(Calendar.MINUTE)))	;
		
		SharedPreferences sharedPref = context.getSharedPreferences(FILE_SHARED,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("lastCheck", moment);
		editor.commit();
		return moment;
	}
	
	public static String readLastCheck(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(FILE_SHARED,Context.MODE_PRIVATE);
		String moment = sharedPref.getString("lastCheck", "-");
		return moment;
	}
	
	
	private static String format2Digits(int digit){
		String text = String.valueOf(digit);
		if(text.length() == 1){
			return "0"+text;
		}
		return text;
	}
}
