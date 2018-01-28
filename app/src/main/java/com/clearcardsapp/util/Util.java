package com.clearcardsapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.clearcardsapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

@SuppressLint("SimpleDateFormat")
public class Util {


	/**
	 * Returns distance in meters of two lat langs.
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return Returns distance in meters
	 */
	public static double getDistanceFrom(Double lat1, Double lng1, Double lat2, Double lng2) {
		if (lat1 == null || lng1 == null || lat2 == null || lng2 == null)
			return 1000000d;
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	public static String sendGETRequest(String url) {
		String str = null;
		try {
			HttpClient httpClient = null;
			if (url.contains("example.com")) {
				httpClient = SSLHttpClient.getMyhttpClient();
			} else {
				httpClient = new DefaultHttpClient();
			}

			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept-Encoding", "gzip");

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			InputStream inputStream = entity.getContent();

			// check if we got a compressed resonse back
			if (entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
				return uncompressInputStream(inputStream);
			} else {
				return convertStreamToString(inputStream);
			}

		} catch (Exception ae) {
			System.out.println(ae.toString());

		}
		return str;

	}

	private static String uncompressInputStream(InputStream inputStream) throws IOException {
		StringBuilder value = new StringBuilder();

		GZIPInputStream gzipIn = null;
		InputStreamReader inputReader = null;
		BufferedReader reader = null;

		try {
			gzipIn = new GZIPInputStream(inputStream);
			inputReader = new InputStreamReader(gzipIn, "UTF-8");
			reader = new BufferedReader(inputReader);

			String line = "";
			while ((line = reader.readLine()) != null) {
				value.append(line + "\n");
			}
		} finally {
			try {
				if (gzipIn != null) {
					gzipIn.close();
				}

				if (inputReader != null) {
					inputReader.close();
				}

				if (reader != null) {
					reader.close();
				}

			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return value.toString();
	}

	private static String convertStreamToString(InputStream inputStream) throws IOException {
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}

			return stringBuilder.toString();
		} finally {
			inputStreamReader.close();
			bufferedReader.close();
		}
	}

	private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	public static Date sqlDate(String date) {

		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String dateFormatChange(String date) {

		Date d = null;
		try {
			d = sdf1.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			return sdf2.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sqlLiteDate(Date date) {
		if (date == null)
			return "";
		String con_date = sdf3.format(date);
		return con_date;
	}

	public static String sqlLiteDate(Long date) {
		if (date == null)
			return "";
		String con_date = sdf3.format(new Date(date));
		return con_date;
	}

	public static void showDialogWithTwoButtons(final Context context, String message, final String positive_button, final String negative_button) {
		System.out.println(context.getClass());
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
		alt_bld.setMessage(message).setCancelable(false)
		// .setTitle(context.getResources().getString(R.string.app_name))
				.setPositiveButton(positive_button, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

						((Activity) context).finish();
					}
				}).setNegativeButton(negative_button, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// ((Activity) context).finish();
					}
				});

		AlertDialog alert = alt_bld.create();
		// alert.setTitle("\t" + title);
		// alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
		alert.show();
	}

	// convert miliseconds(in long) to a string date with required format
	@SuppressLint("SimpleDateFormat")
	public static String convertMiliSecondsToString(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public static String sendPostRequest(String post_url, Map<String, String> params) {

		try {

			HttpClient httpClient = null;
			if (post_url.contains("zoyride.com")) {
				httpClient = SSLHttpClient.getMyhttpClient();
			} else {
				httpClient = new DefaultHttpClient();
			}

			HttpPost httpPost = new HttpPost(post_url);
			httpPost.addHeader("Accept-Encoding", "gzip");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
			}

			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
			httpPost.setEntity(urlEncodedFormEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream inputStream = httpResponse.getEntity().getContent();

			// check if we got a compressed resonse back
			if (httpResponse.getEntity().getContentEncoding() != null && "gzip".equalsIgnoreCase(httpResponse.getEntity().getContentEncoding().getValue())) {
				return uncompressInputStream(inputStream);
			} else {
				return convertStreamToString(inputStream);
			}

		} catch (Exception e) {
			Log.e(Constants.EXCEPTION_TAG, e.getMessage());
		}

		return null;
	}

	public static String sendPostJsonRequest(String post_url, String json) {

		HttpClient httpClient = null;
		if (post_url.contains("zoyride.com")) {
			httpClient = SSLHttpClient.getMyhttpClient();
		} else {
			httpClient = new DefaultHttpClient();
		}

		HttpPost httpPost = new HttpPost(post_url);
		httpPost.addHeader("Accept-Encoding", "gzip");

		try {
			StringEntity entity = new StringEntity(json, HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream inputStream = httpResponse.getEntity().getContent();

			// check if we got a compressed resonse back
			if (httpResponse.getEntity().getContentEncoding() != null && "gzip".equalsIgnoreCase(httpResponse.getEntity().getContentEncoding().getValue())) {
				return uncompressInputStream(inputStream);
			} else {
				return convertStreamToString(inputStream);
			}

		} catch (Exception e) {
			if (e != null) {
				Log.e(Constants.EXCEPTION_TAG, e.getMessage());
			}
		}

		return null;
	}

	/*public static String sendPostJsonZippedRequest(String post_url, String json) {

		HttpClient httpClient = null;
		if (post_url.contains("zoyride.com")) {
			httpClient = SSLHttpClient.getMyhttpClient();
		} else {
			httpClient = new DefaultHttpClient();
		}

		HttpPost httpPost = new HttpPost(post_url);
		httpPost.addHeader("Accept-Encoding", "gzip");

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			gzos.write(json.getBytes("UTF-8"));
			byte[] fooGzippedBytes = baos.toByteArray();

			MultipartEntity entity = new MultipartEntity();
			entity.addPart("json-data", new ByteArrayBody(fooGzippedBytes, "json-data"));

			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream inputStream = httpResponse.getEntity().getContent();

			// check if we got a compressed resonse back
			if (httpResponse.getEntity().getContentEncoding() != null && "gzip".equalsIgnoreCase(httpResponse.getEntity().getContentEncoding().getValue())) {
				return uncompressInputStream(inputStream);
			} else {
				return convertStreamToString(inputStream);
			}

		} catch (Exception e) {
			if (e != null) {
				Log.e(Constants.EXCEPTION_TAG, e.getMessage());
			}
		}

		return null;
	}*/

	private double distanceBetween2GeoPoints(double lat1, double lon1, double lat2, double lon2) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e(Constants.EXCEPTION_TAG, e.getMessage());
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static void genericToast(Context context, String msg, int duration) {
		if (context == null || msg == null)
			return;

		if (duration == 0) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}

	public static String byteToHexString(byte[] abc) {

		StringBuilder sb = new StringBuilder(abc.length * 2);
		for (byte b : abc)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	public static boolean locationEnabled(Context context) {

		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		} else {
			locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}
	}

	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		context.getContentResolver().notifyChange(uri, null);
		ContentResolver cr = context.getContentResolver();

		Bitmap bitmap = null;
		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
			return bitmap;
		} catch (Exception e) {
			Log.e(Constants.EXCEPTION_TAG, e.getMessage());
		}
		return null;
	}

	public static void log(String s) {
		System.out.println(s);

	}

	/**
	 * This return Time in String like "10:12 AM" if provided time is of
	 * Today's.<br>
	 * Else provides date like "12-9-2015".
	 * 
	 * @param time
	 * @return
	 */
	public static String getCustomDateTime(Long time) {
		if (time == null)
			return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);

		Calendar ct = Calendar.getInstance();
		if (ct.get(6) == c.get(Calendar.DAY_OF_YEAR) && ct.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
			// is today
			return c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + " " + (c.get(Calendar.HOUR_OF_DAY) <= 12 ? "AM" : "PM");
		}

		return c.get(Calendar.DATE) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
	}

	public static String getCustomFloatTime(Float time) {
		int minute = getMin(time);
		int hr = getHours(time) ;
		return (hr > 12? (hr-12): hr) + " : " + (minute>9 ? minute : ("0"+minute)) + ((time < 12) ? " AM" : " PM");
	}

	public static Integer getHours(Float time) {
		return (int) ((time * 100) - (time * 100) % 100) / 100;
	}

	public static Integer getMin(Float time) {
		return (int) (time * 100) % 100;
	}

	public static void showNotificationAlert(Context context, PendingIntent pendingIntent, String header, String content, Notification notification) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_clear_cards).setContentTitle(header)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(content)).setContentText(content);

		if (pendingIntent != null)
			mBuilder.setContentIntent(pendingIntent);
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, mBuilder.build());
	}


	public static void cancelNotificationAlert(Context context) {
		NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancel(0);
	}

	public static int getscrOrientation(Context con) {
		Display getOrient = ((Activity) con).getWindowManager().getDefaultDisplay();

		int orientation = getOrient.getOrientation();

		// Sometimes you may get undefined orientation Value is 0
		// simple logic solves the problem compare the screen
		// X,Y Co-ordinates and determine the Orientation in such cases
		if (orientation == Configuration.ORIENTATION_UNDEFINED) {

			Configuration config = con.getResources().getConfiguration();
			orientation = config.orientation;

			if (orientation == Configuration.ORIENTATION_UNDEFINED) {
				// if height and widht of screen are equal then
				// it is square orientation
				if (getOrient.getWidth() == getOrient.getHeight()) {
					orientation = Configuration.ORIENTATION_SQUARE;
				} else { // if widht is less than height than it is portrait
					if (getOrient.getWidth() < getOrient.getHeight()) {
						orientation = Configuration.ORIENTATION_PORTRAIT;
					} else { // if it is not any of the above it will defineitly
								// be landscape
						orientation = Configuration.ORIENTATION_LANDSCAPE;
					}
				}
			}
		}
		return orientation; // return value 1 is portrait and 2 is Landscape
							// Mode
	}

	public static int getOrientation(Context con) {
		int configOrientation;
		int value = 1;
		configOrientation = con.getResources().getConfiguration().orientation;
		switch (configOrientation) {
		case Configuration.ORIENTATION_LANDSCAPE: {
			value = 2;
			break;
		}
		case Configuration.ORIENTATION_PORTRAIT: {
			value = 1;
			break;
		}
		case Configuration.ORIENTATION_SQUARE: {
			value = 3;
			break;
		}

		case Configuration.ORIENTATION_UNDEFINED:
		default: {
			value = 4;
			break;
		}

		}
		return value;
	}

	public static Float getCurrentTimeOnly() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY) + (c.get(Calendar.MINUTE) / 100f);
	}

	private static PowerManager.WakeLock wl;
	private static final AtomicInteger wakeLockCount = new AtomicInteger(0);

	public synchronized static void getWakeLock(Context context) {
		if (wakeLockCount.incrementAndGet() == 1) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.zoyride");
			// Acquire the lock
			wl.acquire();
		}
	}

	public synchronized static void releaseWakeLock() {
		if (wakeLockCount.decrementAndGet() == 0 && wl != null && wl.isHeld()) {
			wl.release();
			wl = null;
		}
	}

	public static Float getCurrentTimeInFloat() {

		Float current_time = null;

		Calendar calender = Calendar.getInstance();

		Integer hour = calender.get(Calendar.HOUR_OF_DAY);
		Integer min = calender.get(Calendar.MINUTE);
		current_time = Float.valueOf((hour + (min / 100f)));

		return current_time;
	}

	public static Integer getDifferenceTimeInMin(Float time1, Float time2) {

		Integer min1 = getFloatTimeInMin(time1);
		Integer min2 = getFloatTimeInMin(time2);

		return min1 - min2;
	}

	public static boolean diffLessThanHalfHour(Float fromTime, Float toTime) {
		if (fromTime == null || toTime == null)
			return false;

		return Math.abs(((fromTime.intValue()) + ((fromTime % 1) * 100 / 60)) - ((toTime.intValue()) + ((toTime % 1) * 100 / 60))) < 0.5;

	}

	public static Integer getFloatTimeInMin(Float time) {

		Float hours = Math.round(time) * 1.0f;
		Float min = (time - hours) * 100;

		return (Math.round(time) * 60) + Math.round(min);
	}

	public static String formatIntegerTime(Integer time) {
		return ((time / 10000 >= 13) ? (time / 10000) % 12 : time / 10000) + " : " + ((time % 10000) / 100) + " : " + (time % 100)
				+ ((time / 10000 <= 12) ? " AM" : " PM");
	}

	public static Integer getTodayTimeZero() {
		Calendar cal = Calendar.getInstance();

		return (cal.get(Calendar.YEAR) * 10000) + ((cal.get(Calendar.MONTH) + 1) * 100) + cal.get(Calendar.DATE);
	}

	public static Integer getIntDate(String date) {
		Integer intDate = null;
		if (date != null && !"".equals(date)) {
			String[] splitDate = date.split("-");
			intDate = Integer.valueOf(splitDate[0]) * 10000 + Integer.valueOf(splitDate[1]) * 100 + Integer.valueOf(splitDate[2]);
		}
		return intDate;
	}

	public static String getDateFromInteger(Integer date) {
		return (date / 10000) + "-" + ((date % 10000) / 100) + "-" + (date % 100);
	}

	public static Integer getYear(Integer date) {
		return (date / 10000);
	}

	public static Integer getMonth(Integer date) {
		return ((date % 10000) / 100);
	}

	public static Integer getMonthDate(Integer date) {
		return (date % 100);
	}

	public static boolean sendMessage(String phoneNo, String sms) {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, sms, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getNetworkClass(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = mTelephonyManager.getNetworkType();
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "2G";
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "3G";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "4G";
		default:
			return "Unknown";
		}
	}

	public static void debugLog(String message, Context context) {
		if (new AppPref(context).getBoolean("debug"))
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static String calculateHash(String hashAlgorithm, String payload) {
		byte[] hashseq = payload.getBytes();
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest algorithm = MessageDigest.getInstance(hashAlgorithm);
			algorithm.reset();
			algorithm.update(hashseq);
			byte messageDigest[] = algorithm.digest();

			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append("0");
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException nsae) {
		}

		return hexString.toString();
	}
}
