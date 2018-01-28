package com.clearcardsapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clearcardsapp.async.GetCardsTask;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;
import com.clearcardsapp.util.NetworkStatus;

/**
 * Created by quadri on 08/01/16.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.TAG, "Connectivity Change Reciever Called");

        if (new AppPref(context).getBoolean("background_download")) {
            if (new NetworkStatus(context).isNetWorkAvailable()) {
                new GetCardsTask(context, false).execute();
                Log.d(Constants.TAG, "Connectivity Change Reciever Get Task Called");
            }
        }
    }
}
