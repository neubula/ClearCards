package com.clearcardsapp.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

	public static DBHelper mDBHelper = null;
	SQLiteDatabase db = null;
	
	public static SQLiteDatabase getDB(){
		SQLiteDatabase db = DatabaseProvider.mDBHelper.getReadableDatabase();
		long avilablesize = db.getMaximumSize();
		db.setMaximumSize(avilablesize);
		return db;
	}

	public static class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createTable(db);
			long avilablesize = db.getMaximumSize();
			db.setMaximumSize(avilablesize);
		}

		private void createTable(SQLiteDatabase db) {
			long avilablesize = db.getMaximumSize();
			db.setMaximumSize(avilablesize);

			StringBuffer configQuery = new StringBuffer("create table " + DatabaseConstants.TABLE_CONFIG + "(");
			configQuery.append(" KEY VARCHAR PRIMARY KEY,");
			configQuery.append(" VALUE VARCHAR);");
			db.execSQL(configQuery.toString());

			StringBuffer postQuery = new StringBuffer("create table " + DatabaseConstants.TABLE_CARDS + "(");
			postQuery.append(" KEY VARCHAR PRIMARY KEY,");
			postQuery.append(" VALUE VARCHAR);");
			db.execSQL(postQuery.toString());
			
			/*StringBuffer postQuery = new StringBuffer("create table " + DatabaseConstants.TABLE_POST + "(");
			postQuery.append(DatabaseConstants.COLUMN_ID + " VARCHAR  PRIMARY KEY,");
			postQuery.append(DatabaseConstants.COLUMN_CAPTION + " VARCHAR,");
			postQuery.append(DatabaseConstants.COLUMN_TAGS + " VARCHAR,");
			postQuery.append(DatabaseConstants.COLUMN_URL + " VARCHAR);"); 
			db.execSQL(postQuery.toString()); ß*/
			
			StringBuffer tagQuery = new StringBuffer("create table " + DatabaseConstants.TABLE_TAG + "(");
			tagQuery.append(" KEY VARCHAR PRIMARY KEY,");
			tagQuery.append(" VALUE VARCHAR);");
			/*StringBuffer postUserQuery = new StringBuffer("create table " + DatabaseConstants.TABLE_POST_USER + "(");
			postUserQuery.append(DatabaseConstants.COLUMN_ID + " VARCHAR  PRIMARY KEY,");
			postUserQuery.append(DatabaseConstants.COLUMN_CAPTION + " VARCHAR,");
			postUserQuery.append(DatabaseConstants.COLUMN_TAGS + " VARCHAR,");
			postUserQuery.append(DatabaseConstants.COLUMN_URL + " VARCHAR);");*/
			db.execSQL(tagQuery.toString());
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);

			long available_size = db.getMaximumSize();
			db.setMaximumSize(available_size);

			if (!db.isReadOnly()) {
				// Enable foreign key constraints
				db.execSQL("PRAGMA foreign_keys=ON;");

			}
		}
	}

	@Override
	public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {

		db = getDB();
		int deleted = 0;

		if (DatabaseConstants.URI_CONFIG.equals(uri)) {
			deleted = db.delete(DatabaseConstants.TABLE_CONFIG, selection, selectionArgs);
		} else if (DatabaseConstants.URI_CARDS.equals(uri)) {
			deleted = db.delete(DatabaseConstants.TABLE_CARDS, selection, selectionArgs);
		} else if (DatabaseConstants.URI_TAG.equals(uri)){
			deleted = db.delete(DatabaseConstants.TABLE_TAG, selection, selectionArgs);
		}

		return deleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		
		db = getDB();
		long rowId = -1;

		if (DatabaseConstants.URI_CONFIG.equals(uri)) {
			rowId = db.insert(DatabaseConstants.TABLE_CONFIG, null, values);
		} else if (DatabaseConstants.URI_CARDS.equals(uri)) {
			rowId = db.insert(DatabaseConstants.TABLE_CARDS, null, values);
		} else if (DatabaseConstants.URI_TAG.equals(uri)) {
			rowId = db.insert(DatabaseConstants.TABLE_TAG, null, values);
		}
		return uri;
	}

	@Override
	public boolean onCreate() {
		mDBHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		db = getDB();
		Cursor cursor = null;

		if (DatabaseConstants.URI_CONFIG.equals(uri)) {
			cursor = db.query(DatabaseConstants.TABLE_CONFIG, projection, selection, selectionArgs, null, null, null);
		} else if (DatabaseConstants.URI_CARDS.equals(uri)) {
			cursor = db.query(DatabaseConstants.TABLE_CARDS, projection, selection, selectionArgs, null, null, null);
		} else if (DatabaseConstants.URI_TAG.equals(uri)) {
			cursor = db.query(DatabaseConstants.TABLE_TAG, projection, selection, selectionArgs, null, null, null);
		} 
		return cursor;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int upCount = 0;
		db = getDB();
		if ( DatabaseConstants.URI_CONFIG.equals(uri)) {
			upCount = db.update(DatabaseConstants.TABLE_CONFIG, values, selection, selectionArgs);
		} else if (DatabaseConstants.URI_CARDS.equals(uri)) {
			upCount = db.update(DatabaseConstants.TABLE_CARDS, values, selection, selectionArgs);
		} else if (DatabaseConstants.URI_TAG.equals(uri)) {
			upCount = db.update(DatabaseConstants.TABLE_TAG, values, selection, selectionArgs);
		}
		System.out.println("\n update count   : " + upCount);
		return upCount;
	}

}
