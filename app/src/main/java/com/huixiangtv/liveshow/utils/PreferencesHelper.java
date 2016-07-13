package com.huixiangtv.liveshow.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

	SharedPreferences sp;
	SharedPreferences.Editor editor;
	Context context;

	public PreferencesHelper(Context c, String name) {
		context = c;
		sp = context.getSharedPreferences(name, 0);
		editor = sp.edit();
	}

	public void setValue(String key, String value) {
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getValue(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public String getValue(String key) {
		return sp.getString(key, null);
	}

	public void setBooleanValue(String key, boolean value) {
		editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBooleanValue(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}


	public void setLongValue(String key,long time) {
		editor = sp.edit();
		editor.putLong(key, time);
		editor.commit();
	}

	public long getLongValue(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}


	public void remove(String name) {
		editor.remove(name);
	}

	public void clear() {
		editor.clear();
		editor.commit();
	}

}
