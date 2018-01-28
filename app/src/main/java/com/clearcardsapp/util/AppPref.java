package com.clearcardsapp.util;

import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPref {

	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	public AppPref(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(Constants.APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public Integer getFirstCard() {
		return appSharedPrefs.getInt("FIRST_CARD", 0);
	}

	public void setFirstCard(Integer value) {
		prefsEditor.putInt("FIRST_CARD", value);
		prefsEditor.commit();
	}

	public Integer getLastCard() {
		return appSharedPrefs.getInt("LAST_CARD", 0);
	}

	public void setLastCard(Integer value) {
		prefsEditor.putInt("LAST_CARD", value);
		prefsEditor.commit();
	}
	
	public int getCurrentCard() {
		return getInt("CURRENT_CARD");
	}
	
	public void setCurrentCard(int value) {
		setInt("CURRENT_CARD", value);
	}
	
	public int getCardViewed() {
		return getInt("CARD_VIEWED");
	}
	
	public void setCardViewed(int value) {
		int cardViewed = getInt("CARD_VIEWED");
		setInt("CARD_VIEWED", cardViewed + value);
	}
	
	public int getCardExisted() {
		return getInt("CARD_EXISTED");
	}
	
	public void setCardExisted(int value) {
		int cardExisted = getInt("CARD_EXISTED");
		setInt("CARD_EXISTED", cardExisted + value);
	}
	
	public int getCardDeleted() {
		return getInt("CARD_DELETED");
	}
	
	public void setCardDeleted(int value) {
		int cardDeleted = getInt("CARD_DELETED");
		setInt("CARD_DELETED", cardDeleted + value);
	}

	public String get(String key) {
		return appSharedPrefs.getString(key.toUpperCase(Locale.US), null);
	}
	
	public void set(String key, String value) {
		prefsEditor.putString(key.toUpperCase(Locale.US), value);
		prefsEditor.commit();
	}
	
	public void unset(String key) {
		prefsEditor.putString(key.toUpperCase(Locale.US), null);
		prefsEditor.commit();
	}
	
	public int getInt(String key) {
		return appSharedPrefs.getInt(key.toUpperCase(Locale.US), 0);
	}
	
	public void setInt(String key, int value) {
		prefsEditor.putInt(key.toUpperCase(Locale.US), value);
		prefsEditor.commit();
	}
	
	public void unsetInt(String key) {
		prefsEditor.putInt(key.toUpperCase(Locale.US), 0);
		prefsEditor.commit();
	}
	
	public float getLong(String key) {
		return appSharedPrefs.getLong(key.toUpperCase(Locale.US), 0);
	}
	
	public void setLong(String key, long value) {
		prefsEditor.putLong(key.toUpperCase(Locale.US), value);
		prefsEditor.commit();
	}
	
	public void unsetLong(String key) {
		prefsEditor.putLong(key.toUpperCase(Locale.US), 0);
		prefsEditor.commit();
	}
	
	public float getFloat(String key) {
		return appSharedPrefs.getFloat(key.toUpperCase(Locale.US), 0);
	}
	
	public void setFloat(String key, float value) {
		prefsEditor.putFloat(key.toUpperCase(Locale.US), value);
		prefsEditor.commit();
	}
	
	public void unsetFloat(String key) {
		prefsEditor.putFloat(key.toUpperCase(Locale.US), 0);
		prefsEditor.commit();
	}
	
	public boolean getBoolean(String key) {
		return appSharedPrefs.getBoolean(key.toUpperCase(Locale.US), false);
	}
	
	public void setBoolean(String key, boolean value) {
		prefsEditor.putBoolean(key.toUpperCase(Locale.US), value);
		prefsEditor.commit();
	}
	
	public void unsetBoolean(String key) {
		prefsEditor.putBoolean(key.toUpperCase(Locale.US), false);
		prefsEditor.commit();
	}
	
	/**
	 * 
	 * @param key
	 * @return Set&lt;String&gt; 
	 * <br>Default value: null
	 */
	public Set<String> getSet(String key) {
		return appSharedPrefs.getStringSet(key.toUpperCase(Locale.US), null);
	}
	
	public void setSet(String key, Set<String> values) {
		prefsEditor.putStringSet(key.toUpperCase(Locale.US), values);
		prefsEditor.commit();
	}
	
	public void unsetSet(String key) {
		prefsEditor.putStringSet(key.toUpperCase(Locale.US), null);
		prefsEditor.commit();
	}

	public void clearPref() {
		prefsEditor.clear();
		prefsEditor.commit();
	}

}