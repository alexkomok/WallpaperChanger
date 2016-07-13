package com.komok.wallpaperchanger;

import com.komok.wallpaperchanger.BaseHelper.Weekday;


public class LiveWallpaperWednesdayActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected LiveWallpaper getLiveWallpaper() {
		return  BaseHelper.loadLiveWallpaper(this, getDay());
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.Wednesday;
	}

}
