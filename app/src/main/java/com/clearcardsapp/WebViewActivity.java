package com.clearcardsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.clearcardsapp.util.AppPref;

public class WebViewActivity extends Activity {

	WebView content;
	String card_link;
	Context context;

	AppPref appPref;
	ProgressBar mProgress;
//	private int mProgressStatus = 0;
//	private Handler mHandler = new Handler();
//	private ProgressDialog dialog = null;
	private static final String TAG = ">--WebViewActivity-->";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		context = this;

		appPref = new AppPref(context);


		/*dialog = new ProgressDialog(this.context);
		dialog.setMessage("Loading page...");
		dialog.setTitle(R.string.app_name);
		dialog.show();*/

		card_link = getIntent().getStringExtra("card_link");


		mProgress = (ProgressBar) findViewById(R.id.progressBar);
		content = (WebView) findViewById(R.id.content);

		// Enabeling night view
		enableNightView();

		content.getSettings().setJavaScriptEnabled(true);
		content.loadUrl(card_link);

		content.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				mProgress.setProgress(newProgress);
				if (newProgress >= 100) {
					mProgress.setVisibility(View.GONE);
					/*if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}*/
				}
			}
		});

		content.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}


		});

		// Start lengthy operation in a background thread
		/*new Thread(new Runnable() {
			public void run() {
				while (mProgressStatus < 100) {
					mProgressStatus = doWork();

					// Update the progress bar
					mHandler.post(new Runnable() {
						public void run() {
							mProgress.setProgress(mProgressStatus);
						}
					});
				}
			}
		}).start();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	private void enableNightView() {
		if (appPref.getBoolean("night_view")) {
			content.setBackgroundColor(getResources().getColor(R.color.night));
		}
	}
}
