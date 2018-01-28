package com.clearcardsapp.util;



import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;



public class NetworkStatus {
	Context context = null;
	Typeface tf_Lato_Regular = null,tf_Lato_Bold = null;
	ConnectivityManager connMgr;

	public NetworkStatus(Context context) {

		this.context = context;
		this.connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
	}

	public  boolean isNetWorkAvailable() {
		
		try {
			if (connMgr == null) {
				return false;
			}
			
			else if (connMgr.getActiveNetworkInfo() != null
				&& connMgr.getActiveNetworkInfo().isAvailable()
				&& connMgr.getActiveNetworkInfo().isConnected()
			) {
				return true;
			}

			else {
				
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public  boolean isNetWorkRoaming() {

		try {
			if (connMgr == null) {
				return false;
			}

			else if (connMgr.getActiveNetworkInfo() != null
				&& connMgr.getActiveNetworkInfo().isRoaming()
			) {
				return true;
			}

			else {

				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public  boolean isNetWorkWiFi() {

		try {
			if (connMgr == null) {
				return false;
			}

			else if (connMgr.getActiveNetworkInfo() != null
				&& connMgr.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI
			) {
				return true;
			}

			else {

				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
}
