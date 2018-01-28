package com.clearcardsapp.util;

import android.content.res.AssetManager;

public class Constants {
	
	public static boolean DEBUG_MODE = false;

	public static AssetManager assetManager;
	
	public static final int CARD_CLUSTER_SIZE = 50;

	public static final int CARD_MAX_LIMIT = 100;

	public static final long CARD_REFRESH_INTERVAL = 21600000;//mili seconds i.e. 6 hours
	
	public static final String CARD_END_TEXT = "Thats all for now. \nWhen you come back again, you will find more cards!";
	
	public static final String NO_CARD_TEXT = "We will update more cards soon for you to keep smiling.";
	
	public static final String NO_CARD_TITLE = "Wow! You finished them all!";
	
	public static final String NO_INTERNET_TEXT = "Please check your internet connection and try again.";
	
	public static final String NO_INTERNET_TITLE = "Unable to reach our server";
	
	public static final String APP_SHARED_PREFS = "com.clearcardsapp.pref";
	
	public static final String EXCEPTION_TAG = "com.clearcardsapp || Exception";

	public static final String TAG = "com.clearcardsapp";
	
	public static String BASE_URL = "clearcardsapp.com";
//	public static String BASE_URL = "192.168.43.200/clearcards";
	
	public static final String IN_PROTOCOL = "http://";

	// General urls
	public static String GET_ALL_CARDS = "/clearcards.php";

	public static final String GOOGLE_API_KEY = "AIzaSyCcqAq3NoD27ojQyo2Y-d_WyLTjbUYuZnQ";
	
	

}
