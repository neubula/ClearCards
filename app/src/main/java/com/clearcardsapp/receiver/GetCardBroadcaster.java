package com.clearcardsapp.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clearcardsapp.async.GetCardsTask;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;

public class GetCardBroadcaster extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(Constants.TAG, "Get Card Reciever called");

		if (new AppPref(context).getBoolean("background_download")) {
			new GetCardsTask(context, false).execute();
		}
	}

	public static void startAlarm(Context context) {
		try {
			boolean alarmUp = (PendingIntent.getBroadcast(context, 0, new Intent(context, GetCardBroadcaster.class), PendingIntent.FLAG_NO_CREATE) != null);
			if (!alarmUp) {
				Log.d(Constants.TAG, "Get Card Alarm is active");
				AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				Intent intent = new Intent(context, GetCardBroadcaster.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//				alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
				// AlarmManager.INTERVAL_HALF_DAY 43200000 mili seconds i.e. 12 hours
				alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.CARD_REFRESH_INTERVAL, pendingIntent);
			}
		} catch (Exception e) {
			Log.e(Constants.TAG, e.getMessage(), e);
		}
	}
}
