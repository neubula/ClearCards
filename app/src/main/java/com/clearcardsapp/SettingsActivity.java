package com.clearcardsapp;

import com.clearcardsapp.util.AppPref;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SettingsActivity extends Activity {
	CheckBox night_view, background_download, background_image, background_transparency,
			show_menu, download_wifi_only, download_roaming;
	LinearLayout layoutView, layoutDowload;
	String card_link;
	Context context;
	AppPref appPref;
	ScrollView settingsBackground;
	private static final String TAG = ">--SettingActivity-->";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		context = this;

		appPref = new AppPref(context);

		settingsBackground = (ScrollView) findViewById(R.id.settings_background);
		layoutView = (LinearLayout) findViewById(R.id.layout_view);
		layoutDowload = (LinearLayout) findViewById(R.id.layout_download);

		night_view = (CheckBox) findViewById(R.id.night_view);
		background_download = (CheckBox) findViewById(R.id.background_download);
		background_image = (CheckBox) findViewById(R.id.background_image);
		background_transparency = (CheckBox) findViewById(R.id.background_transparency);
		show_menu = (CheckBox) findViewById(R.id.show_menu);
		download_wifi_only = (CheckBox) findViewById(R.id.download_wifi_only);
		download_roaming = (CheckBox) findViewById(R.id.download_roaming);

		night_view.setChecked(appPref.getBoolean("night_view"));
		background_download.setChecked(appPref.getBoolean("background_download"));
		background_image.setChecked(appPref.getBoolean("background_image"));
		background_transparency.setChecked(appPref.getBoolean("background_transparency"));
		show_menu.setChecked(appPref.getBoolean("show_menu"));
		download_wifi_only.setChecked(appPref.getBoolean("download_wifi_only"));
		download_roaming.setChecked(appPref.getBoolean("download_roaming"));

		night_view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("night_view", isChecked);
				enableNightView();
			}
		});

		background_download.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("background_download", isChecked);
			}
		});

		background_image.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("background_image", isChecked);
			}
		});

		background_transparency.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("background_transparency", isChecked);
			}
		});

		show_menu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("show_menu", isChecked);
			}
		});

		download_wifi_only.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("download_wifi_only", isChecked);
			}
		});

		download_roaming.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				appPref.setBoolean("download_roaming", isChecked);
			}
		});

		// Enabelling night view when set
		enableNightView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	private void enableNightView() {
		if (appPref.getBoolean("night_view")) {
			settingsBackground.setBackgroundColor(getResources().getColor(R.color.night));
			layoutView.setBackgroundColor(getResources().getColor(R.color.card_deep_gray));
			layoutDowload.setBackgroundColor(getResources().getColor(R.color.card_deep_gray));
		} else {
			settingsBackground.setBackgroundColor(getResources().getColor(R.color.day));
			layoutView.setBackgroundColor(getResources().getColor(R.color.white));
			layoutDowload.setBackgroundColor(getResources().getColor(R.color.white));
		}
	}
}
