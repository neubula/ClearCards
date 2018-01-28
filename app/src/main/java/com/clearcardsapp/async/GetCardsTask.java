package com.clearcardsapp.async;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.clearcardsapp.GuideActivity;
import com.clearcardsapp.ScreenSlideActivity;
import com.clearcardsapp.SplashScreen;
import com.clearcardsapp.model.Card;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;
import com.clearcardsapp.util.NetworkStatus;
import com.clearcardsapp.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class GetCardsTask extends AsyncTask<Void, Void, String> {

	// TODO: Implement Server Update Interval

	private Context context;
	public boolean startActivity = false;
	public AppPref appPref;
	public NetworkStatus networkStatus;
	private static final String TAG = ">--GetCardsTask-->";

	public GetCardsTask(Context context, boolean startActivity) {
		this.context = context;
		this.appPref = new AppPref(context);
		this.networkStatus = new NetworkStatus(context);
		this.startActivity = startActivity;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (this.startActivity) {
			Log.d(TAG, "ScreenSlideActivity is being called from GetCardsTaks");
			/*if (appPref.getBoolean("IS_GUIDED")) {*/
				Intent intent = new Intent(context, ScreenSlideActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				((Activity) context).finish();
			/*} else {
				Intent intent = new Intent(context, GuideActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				((Activity) context).finish();
			}*/
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String result = "";

		if (networkStatus.isNetWorkAvailable()) {

			boolean getCards = true;

			if (appPref.getBoolean("download_wifi_only")) {
				if(networkStatus.isNetWorkWiFi()) {
					getCards = true;
				} else {
					getCards = false;
				}
			}
			if (networkStatus.isNetWorkRoaming()) {
				if (appPref.getBoolean("download_roaming")) {
					getCards = true;
				} else {
					getCards = false;
				}
			}

			if (getCards) {

				String url = Constants.IN_PROTOCOL + Constants.BASE_URL + Constants.GET_ALL_CARDS;
//				+ "?offset=" + appPref.getCardExisted();
				if (appPref.getFirstCard() > 0) {
					url += "?first=" + appPref.getFirstCard();
				}
				result = Util.sendGETRequest(url);


				if (result != null && !"".equals(result)) {
					try {
						Type listType = new TypeToken<LinkedList<Card>>() {
						}.getType();
						List<Card> cards = new Gson().fromJson(result, listType);

						if (cards != null && cards.size() > 0) {
//					Card.deleteAll(context);
							int count = 0;
							Integer firstCardId = appPref.getFirstCard();
							for (Card card : cards) {
								if (Card.save(context, card)) {
									count++;
									if (card.card_id > firstCardId) {
										firstCardId = card.card_id;
									}
								}
							}
//					appPref.setCardExisted(count);
							// Setting first card app Pref to get
							appPref.setFirstCard(firstCardId);

							appPref.setCurrentCard(0);
							// Showing notification if new card updated in background
							if (count > 0 && !this.startActivity) {
//							Intent intent = new Intent(context, ScreenSlideActivity.class);
								Intent intent = new Intent(context, SplashScreen.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								String header = "Clear Cards";
								String content = "New cards has been added. Please check.";
								PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
								Util.showNotificationAlert(context, pendingIntent, header, content, null);
							}
							// Gettig cards and checking if cards are more than limit
							List<Card> oldCards = Card.getAll(context);
							if (oldCards.size() > Constants.CARD_MAX_LIMIT) {
								int oldCardCount = 0;
								for (Card card : oldCards) {
									oldCardCount++;
									if (oldCardCount > Constants.CARD_MAX_LIMIT) {
										Card.delete(context, card.card_id.toString());
									}
								}
							}

							Log.d(TAG, "Data saved. Card Saved: " + Integer.valueOf(count).toString() + ".");
						}
					} catch (JsonParseException e) {
						e.printStackTrace();
						Log.d(TAG, e.getMessage());
					}
				}
			}
		}

		return result;
	}

}
