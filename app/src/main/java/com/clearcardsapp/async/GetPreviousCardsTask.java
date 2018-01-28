package com.clearcardsapp.async;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class GetPreviousCardsTask extends AsyncTask<Void, Void, String> {


	private Context context;
	public AppPref appPref;
	public NetworkStatus networkStatus;
	private static final String TAG = ">--GetCardsTask-->";

	public GetPreviousCardsTask(Context context) {
		this.context = context;
		this.appPref = new AppPref(context);
		this.networkStatus = new NetworkStatus(context);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
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
				if (appPref.getLastCard() > 0) {
					url += "?last=" + appPref.getLastCard();
				}
				result = Util.sendGETRequest(url);


				if (result != null && !"".equals(result)) {
					try {
						Type listType = new TypeToken<LinkedList<Card>>() {
						}.getType();
						List<Card> cards = new Gson().fromJson(result, listType);

						if (cards != null && cards.size() > 0) {
							int count = 0;
							Integer lastCardId = appPref.getLastCard();
							for (Card card : cards) {
								if (Card.save(context, card)) {
									count++;
									if (card.card_id < lastCardId) {
										lastCardId = card.card_id;
									}
								}
							}
							// Setting last card app Pref to get
							appPref.setLastCard(lastCardId);

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
