package com.komok.daydreamapprunner;

import android.app.WallpaperInfo;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class WallpaperTile {

	Drawable mThumbnail;
	WallpaperInfo mWallpaperInfo;
	Intent mIntent;

	public WallpaperTile(Drawable thumbnail, WallpaperInfo info, Intent intent) {
		mThumbnail = thumbnail;
		mWallpaperInfo = info;
		mIntent = intent;
	}

}
