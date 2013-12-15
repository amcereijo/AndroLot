package com.androlot.util;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	private final static String FILE_SHARED = "fileSharedAndroLot";
	
	
	public static String saveLastCheck(Context context) {
		Calendar c = Calendar.getInstance();
		String moment = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
		
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
	
}
