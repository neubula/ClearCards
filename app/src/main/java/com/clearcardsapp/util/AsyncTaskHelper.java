package com.clearcardsapp.util;

import com.clearcardsapp.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskHelper extends AsyncTask<String, Integer, Entity> {
	private static final String TAG = "AsyncTaskHelper ***** ";
	private IAsyncTaskListener asyncTaskListener;
	private Context context = null;
	private ProgressDialog dialog = null;
	private String progressMessage = "Loading ... please wait";

	public AsyncTaskHelper(Context context) {
		this.context = context;
	}

	/**
	 * If progress bar should be displayed or not
	 *
	 * @param flag
	 */
	public void showProgressbar(boolean flag) {
//		PhrescoLogger.info(TAG + "showProgressbar()");
		try {
			if (flag) {
				/*if (dialog != null && dialog.isShowing()) {
					PhrescoLogger.info(TAG + "showProgressbar() - previous dialog is showling.. dismiss it");
					dialog.dismiss();
					dialog = null;
				}*/
				dialog = new ProgressDialog(this.context);
//				PhrescoLogger.info(TAG + "showProgressbar() - create new dialog");
			} else {
				dialog = null;
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	/**
	 * Show the message in progress bar
	 *
	 * @param msg
	 */
	public void setMessage(String msg) {
		this.progressMessage = msg;
	}

	protected void onPreExecute() {
		try {
//			PhrescoLogger.info(TAG + "onPreExecute()");
			if (dialog != null) {
				/*if (dialog.isShowing()) {

					PhrescoLogger.info(TAG + "onPreExecute() - previous dialog is showling.. dismiss it");
					dialog.dismiss();
					dialog = null;
					dialog = new ProgressDialog(this.context);
				}*/
				dialog.setMessage(progressMessage);
				dialog.setTitle(R.string.app_name);
				dialog.show();
//				PhrescoLogger.info(TAG + "onPreExecute() - show the dialog");
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	protected Entity doInBackground(String[] urls) {
		if (asyncTaskListener != null) {
			asyncTaskListener.processOnStart();
		}
		return null;
	}

	protected void onProgressUpdate(Integer[] progress) {
	}

	protected void onPostExecute(Entity results) {
//		PhrescoLogger.info(TAG + "onPostExecute()");
		if (dialog != null && dialog.isShowing()) {
//			PhrescoLogger.info(TAG + "onPostExecute() - previous dialog is showling.. dismiss it");
			dialog.dismiss();
			dialog = null;
		}
		if (asyncTaskListener != null) {
			asyncTaskListener.processOnComplete();
		}
	}

	/**
	 * Listener interface for current async task object
	 *
	 * @param wl
	 */
	public void setAsyncTaskListener(IAsyncTaskListener wl) {
		this.asyncTaskListener = wl;
	}

}
