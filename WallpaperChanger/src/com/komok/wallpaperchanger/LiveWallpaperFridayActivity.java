package com.komok.wallpaperchanger;

import com.komok.wallpaperchanger.WallpaperChangerHelper.Weekday;


public class LiveWallpaperFridayActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected LiveWallpaper getLiveWallpaper() {
		return  WallpaperChangerHelper.loadLiveWallpaper(this, getDay());
	}

	@Override
	protected Weekday getDay() {
		return WallpaperChangerHelper.Weekday.Wednesday;
	}

}
