package com.komok.daydream;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.service.dreams.DreamService;

import com.komok.common.BaseHelper;
import com.komok.wallpaperchanger.R;

public class DayDreamService extends DreamService {
	List<String> selectedList;

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		setInteractive(false);

		setFullscreen(false);

	}

	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		
		selectedList = BaseHelper.loadDreamChoice(this);
		
		if (selectedList.size() == 0) {
			Intent intent = new Intent(this, DayDreamSettingsActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();

			String error = getString(R.string.select_one_or_more);

			b.putString(BaseHelper.ERROR, error);

			// Add the bundle to the intent.
			intent.putExtras(b);

			startActivity(intent);
			finish();
		} else {

			Intent homeIntent = new Intent(Intent.ACTION_MAIN);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(homeIntent);
			PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
			WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
					"TAG");
			wakeLock.acquire();
			wakeLock.release();
		}
		
	}

}
