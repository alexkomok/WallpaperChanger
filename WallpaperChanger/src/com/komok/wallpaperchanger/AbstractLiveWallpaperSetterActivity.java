package com.komok.wallpaperchanger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;

abstract class AbstractLiveWallpaperSetterActivity extends Activity {

	private static final String TAG = "AbstractLiveWallpaperSetterActivity";

	protected abstract LiveWallpaper getLiveWallpaper();

	protected abstract WallpaperChangerHelper.Weekday getDay();

	Method method;
	Object objIWallpaperManager;
	boolean isPermissionGranted;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		isPermissionGranted = checkSetWallpaperComponentPermission();

		if (isPermissionGranted) {

			WallpaperManager manager = WallpaperManager.getInstance(this);

			try {
				method = WallpaperManager.class.getMethod("getIWallpaperManager", null);
				objIWallpaperManager = method.invoke(manager, null);
				Class[] param = new Class[1];
				param[0] = ComponentName.class;
				method = objIWallpaperManager.getClass().getMethod("setWallpaperComponent", param);

			} catch (NoSuchMethodException e) {
				Log.e(TAG, "Error onCreate", e);
				ExceptionHandler.caughtException(e, this);
			} catch (IllegalAccessException e) {
				Log.e(TAG, "Error onCreate", e);
				ExceptionHandler.caughtException(e, this);
			} catch (InvocationTargetException e) {
				Log.e(TAG, "Error onCreate", e);
				ExceptionHandler.caughtException(e, this);
			}
		}

	}
	
	private boolean checkSetWallpaperComponentPermission()
	{
	    String permission = "android.permission.SET_WALLPAPER_COMPONENT";
	    int res = this.checkCallingOrSelfPermission(permission);
	    return (res == PackageManager.PERMISSION_GRANTED);            
	}

	@Override
	protected void onStart() {
		super.onStart();
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		LiveWallpaper wallpaper = getLiveWallpaper();
		Intent intent = new Intent();
		boolean isSuccess = false;
		String error = WallpaperChangerHelper.ERROR;

		if (!isPermissionGranted && Build.VERSION.SDK_INT > 15) {

			intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			String packageName = wallpaper.getPackageName();
			String className = wallpaper.getClassName();
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(packageName, className));
			isSuccess = true;
			this.startActivityForResult(intent, 0);
		} else if (isPermissionGranted) {
			try {
				intent = new Intent(WallpaperService.SERVICE_INTERFACE);

				// if (WallpaperChangerHelper.isLiveWallpaperValid(wallpaper,
				// this)) {
				intent.setClassName(wallpaper.getPackageName(), wallpaper.getClassName());
				method.invoke(objIWallpaperManager, intent.getComponent());
				isSuccess = true;

				Handler mHandler = new Handler();
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Intent homeIntent = new Intent(Intent.ACTION_MAIN);
						homeIntent.addCategory(Intent.CATEGORY_HOME);
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(homeIntent);
					}

				}, 200L);

				// }
			} catch (IllegalAccessException e) {
				Log.e(TAG, "Failed to set wallpaper: " + e);
			} catch (InvocationTargetException e) {
				Log.e(TAG, "Failed to set wallpaper: " + e);
			}
		} else {
			error = getString(R.string.no_permission);
		}

		if (!isSuccess) {

			intent = new Intent(this, LiveWallpaperSelectionActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();
			b.putString(WallpaperChangerHelper.DAY, getDay().name());
			b.putString(WallpaperChangerHelper.ERROR, error);

			// Add the bundle to the intent.
			intent.putExtras(b);

			startActivity(intent);

		}

	}

}
