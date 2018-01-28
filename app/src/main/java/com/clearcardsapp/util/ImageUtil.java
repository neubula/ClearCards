package com.clearcardsapp.util;

import com.clearcardsapp.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class ImageUtil {

	public static void loadImage(Context context, String imageUrl, ImageView imageView, int defaultImage) {
		if (defaultImage == 0) {
			defaultImage = R.drawable.ic_launcher;
		}
		// Loading image
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(defaultImage)
				.showImageForEmptyUri(defaultImage)
				.showImageOnFail(defaultImage)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		
		ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(imageLoaderConfig);
		imageLoader.displayImage(imageUrl, imageView, imageOptions,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
				}
			});
	}
	
	public static Drawable drawable_from_url(String url, String src_name) throws 
	   java.net.MalformedURLException, java.io.IOException {
	   return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), src_name);
	}
	
}
