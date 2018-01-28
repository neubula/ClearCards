package com.clearcardsapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseConstants {

	public static final String DB_NAME = "com.clearcardsapp.db";
	public static final int DB_VERSION = 1;

	public static final String COLUMN_ID = BaseColumns._ID;
	public static String AUTHORITY = "com.clearcardsapp";

	public static final String TABLE_CONFIG = "TABLE_CONFIG";
	public static final String TABLE_CARDS = "TABLE_CARDS";
	public static final String TABLE_PREFERENCES = "TABLE_PREFERENCES";
	public static final String TABLE_TAG = "TABLE_TAG";
	/**
	 * Profile Columns
	 */
	public static final String KEY = "KEY";
	public static final String VALUE = "VALUE";
	
	
	/**
	 * All preferences table columns
	 */
	public static final String EMAIL_NOTIFICATIONS = "emailNotifications";
	public static final String PRIVACY = "privacy";
	public static final String FACEBOOK_ACTIVITY = "facebookActivity";
	
	

	/**
	 * All URIs for Tables
	 */
	public static Uri URI_CONFIG = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CONFIG);
	public static Uri URI_CARDS = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CARDS);
	
	public static Uri URI_TAG = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TAG);
}
