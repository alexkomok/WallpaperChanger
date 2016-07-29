package com.komok.daydreamchanger;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.ExceptionHandler;
import com.komok.wallpaperchanger.R;

abstract public class AbstractDayDreamSetterActivity extends Activity {

	private static final String TAG = "AbstractAppSetterActivity";

	protected abstract ApplicationHolder getApp();

	protected abstract BaseHelper.Weekday getDay();

	@Override
	protected void onStart() {
		super.onStart();
		
		final Context mContext = getApplicationContext();
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		ApplicationHolder app = getApp();

		if (app == null) {
			ExceptionHandler.caughtException(new Exception(getString(R.string.error_update_list) + " for: " + getDay().name()), this);
			return;
		}

		Intent intent = new Intent();

		try {
			intent = Intent.parseUri(app.getUri(), 0);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Failed to set app: " + e);
			ExceptionHandler.caughtException(e, this);
		}

		Settings.Secure.putString(this.getContentResolver(), "screensaver_components", intent.getComponent().flattenToString());

		intent = new Intent(Intent.ACTION_MAIN);

		// Somnabulator is undocumented--may be removed in a future version...
		intent.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
		startActivity(intent);
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Settings.Secure.putString(mContext.getContentResolver(), "screensaver_components", new ComponentName(getApplication().getApplicationInfo().packageName, "om.komok.wallpaperchanger/com.komok.daydream.DayDreamService").flattenToString());
			}

		}, 200L);
		
		

	}

}