package com.clearcardsapp;


import com.clearcardsapp.async.GetCardsTask;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;
import com.clearcardsapp.receiver.GetCardBroadcaster;
import com.clearcardsapp.util.NetworkStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 2000;
	Context context;
	
	AppPref appPref;
	ImageView launcher_icon;
	LinearLayout splashBackground;
	private static final String TAG = ">--SplashScreen-->";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.splash_screen);
		context = this;
		appPref = new AppPref(context);

		splashBackground = (LinearLayout) findViewById(R.id.splash_background);
		launcher_icon = (ImageView) findViewById(R.id.launcher_icon);

		// Enabeling night view
		enableNightView();

		if (_active) {
			_active = false;

			Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation_animation);
			rotation.setRepeatCount(Animation.INFINITE);
			launcher_icon.startAnimation(rotation);

			// Intialize all the settings
			initializeSettings();

			// Starting alarm for fetching data time to time
			GetCardBroadcaster.startAlarm(context);

//			if (appPref.getCardViewed() + Constants.CARD_CLUSTER_SIZE > appPref.getCardExisted()) {
			if (appPref.getFirstCard() > 0) {
				// If there is card to be shown send request to server
				// but start activity here
//				new GetCardsTask(context, false).execute();
				if (appPref.getBoolean("background_download")) {

					new Thread() {
						@Override
						public void run() {
							try {
								int waited = 0;
								while (waited < _splashTime) {
									sleep(100);
									waited += 100;
								}
							} catch (InterruptedException e) {
								Log.e(Constants.TAG, e.getMessage(), e);
							} finally {
								Intent intent = new Intent(context, ScreenSlideActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							}

						}
					}.start();
				} else {
					// When background fetch is not selected fetch on starting of app
					new GetCardsTask(context, true).execute();
				}
			} else {
				if (new NetworkStatus(context).isNetWorkAvailable()) {
					// If there is no cards to be shown
					// Then start activity when the data returns from the server on post execute
					new GetCardsTask(context, true).execute();
					Log.d(TAG, "No Data found. GetCardTask being called.");
				} else {
					Log.d(TAG, "No internet connection and no cards");

					String alertDialogTitle = "";
					String alertDialogText = "";
					alertDialogText = Constants.NO_INTERNET_TEXT;
					alertDialogTitle = Constants.NO_INTERNET_TITLE;
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
					alt_bld.setMessage(alertDialogText).setCancelable(false).setTitle(alertDialogTitle).setPositiveButton("Try Again",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									Intent intent = getIntent();
									finish();
									startActivity(intent);
								}
							}).setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									finish();
								}
							});

					AlertDialog alert = alt_bld.create();
					alert.show();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initializeSettings() {
		if (!appPref.getBoolean("initialize_settings")){

			appPref.setBoolean("initialize_settings", true);

			appPref.setBoolean("is_premium", false);

			appPref.setBoolean("night_view", false);
			appPref.setBoolean("background_download", true);
			appPref.setBoolean("background_image", true);
			appPref.setBoolean("background_transparency", true);
			appPref.setBoolean("show_menu", false);
			appPref.setBoolean("download_wifi_only", false);
			appPref.setBoolean("download_roaming", true);
		}
	}

	private void enableNightView() {
		if (appPref.getBoolean("night_view")) {
			splashBackground.setBackgroundColor(getResources().getColor(R.color.night));
		}
	}
}
