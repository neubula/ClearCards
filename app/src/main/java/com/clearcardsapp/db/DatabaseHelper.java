package com.clearcardsapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DatabaseHelper {

	public synchronized static void saveConfig(Context context, String key, String value) {

		Cursor cursor = context.getContentResolver().query(DatabaseConstants.URI_CONFIG, new String[] { DatabaseConstants.KEY }, " KEY =?", new String[] { key },
				null);
		if (cursor!= null && cursor.moveToNext()) {
			cursor.close();
			
			ContentValues values = new ContentValues(1);
			values.put(DatabaseConstants.KEY, key);
			values.put(DatabaseConstants.VALUE, value);

			context.getContentResolver().update(DatabaseConstants.URI_CONFIG, values, DatabaseConstants.KEY + "=?", new String[] { key });
		} else {
			if (cursor != null)
				cursor.close();
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.KEY, key);
			values.put(DatabaseConstants.VALUE, value);
			context.getContentResolver().insert(DatabaseConstants.URI_CONFIG, values);
		}
	}

	public synchronized static String getConfig(Context context, String key) {
		Cursor cursor= null;
		try {
			cursor = context.getContentResolver().query(DatabaseConstants.URI_CONFIG, new String[] { DatabaseConstants.VALUE }, " KEY =?",
					new String[] { key }, null);
			if (cursor != null && cursor.moveToFirst()) {
				return cursor.getString(cursor.getColumnIndex(DatabaseConstants.VALUE));
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static int deleteConfig(Context context, String key) {
		return context.getContentResolver().delete(DatabaseConstants.URI_CONFIG, " KEY =?", new String[] { key });
	}

}
