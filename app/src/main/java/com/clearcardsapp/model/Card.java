package com.clearcardsapp.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.clearcardsapp.db.DatabaseConstants;
import com.clearcardsapp.util.AppPref;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Card {
	
	public Integer card_id;
	public String time;
	public String text;
	public String image;
	public String source;
	public String link;
	public String type;
	public Boolean appreciate =  false;

	
	public static List<Card> cards = new LinkedList<Card>();

	private static class CardComparator implements Comparator<Card> {

		@Override
		public int compare(Card card1, Card card2) {
			if(card1.card_id < card2.card_id){
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	
	public static boolean save(Context context, Card card) {

		if (card == null || card.card_id == null)
			return false;

		Cursor cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, new String[] { DatabaseConstants.VALUE }, " KEY =?",
				new String[] { card.card_id.toString() }, null);
		if (cursor != null && cursor.moveToFirst()) {

			if (cursor != null)
				cursor.close();
			
			return false;
			
		} else {
			
			ContentValues values = new ContentValues();
			values.put(DatabaseConstants.KEY, card.card_id);
			values.put(DatabaseConstants.VALUE, new Gson().toJson(card));
			context.getContentResolver().insert(DatabaseConstants.URI_CARDS, values);

			if (cursor != null)
				cursor.close();
			
			return true;
		}
	}

	public static boolean update(Context context, Card card) {

		if (card == null || card.card_id == null)
			return false;

		Cursor cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, new String[] { DatabaseConstants.VALUE }, " KEY =?",
				new String[] { card.card_id.toString() }, null);
		if (cursor != null && cursor.moveToFirst()) {

			ContentValues values = new ContentValues(1);
			values.put(DatabaseConstants.VALUE, new Gson().toJson(card));

			context.getContentResolver().update(DatabaseConstants.URI_CARDS, values, "KEY=?", new String[] { card.card_id.toString() });

			if (cursor != null)
				cursor.close();
			
			return true;
			
		} else {
			
			if (cursor != null)
				cursor.close();
			
			return false;
		}
	}

	public static Card get(Context context, String id) {
		if (id == null)
			return null;

		Cursor cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, new String[] { DatabaseConstants.VALUE }, " KEY =?",
				new String[] { id }, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String json = cursor.getString(cursor.getColumnIndex(DatabaseConstants.VALUE));
				if (json != null) {
					cursor.close();
					return new Gson().fromJson(json, Card.class);
				}
			}
			cursor.close();
		}
		return null;
	}

	public static List<Card> getAll(Context context) {
		Cursor cursor = null;
		AppPref appPref = new AppPref(context);
		int count = 0;
		int offset = appPref.getCardViewed() - appPref.getCardDeleted();
		cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, null, null, null, null);
		List<Card> cards = new LinkedList<Card>();
		if (cursor!= null) {
			while ( cursor.moveToNext()) {
				String json = cursor.getString(cursor.getColumnIndex(DatabaseConstants.VALUE));
				if (json != null) {
					count++;
//					if (count > offset)
						cards.add(new Gson().fromJson(json, Card.class));
					
//					if (count - offset >= Constants.CARD_CLUSTER_SIZE) {
//						break;
//					}
				}
			}
			cursor.close();
		}
		Collections.sort(cards, new CardComparator());

		return cards;
	}
	
	public static boolean exist(Context context) {
		Cursor cursor = null;
		cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, null, null, null, null);
		if (cursor!= null) {
			while ( cursor.moveToNext()) {
				String json = cursor.getString(cursor.getColumnIndex(DatabaseConstants.VALUE));
				if (json != null) {
					cursor.close();
					return true;
				}
			}
			cursor.close();
		}
		return false;
	}
	public static boolean exist(Context context, Card cards) {

		if (cards == null || cards.card_id == null)
			return false;

		Cursor cursor = context.getContentResolver().query(DatabaseConstants.URI_CARDS, new String[] { DatabaseConstants.VALUE }, " KEY =?",
				new String[] { cards.card_id.toString() }, null);
		if (cursor != null && cursor.moveToFirst()) {
			if (cursor != null)
				cursor.close();
			return true;
		} else {
			if (cursor != null)
				cursor.close();
			return false;
		}
	}

	public static void deleteAll(Context context) {

		context.getContentResolver().delete(DatabaseConstants.URI_CARDS, null, null);
	}

	public static void delete(Context context, String key) {

		context.getContentResolver().delete(DatabaseConstants.URI_CARDS, " KEY =?", new String[] { key });
	}
	
}
