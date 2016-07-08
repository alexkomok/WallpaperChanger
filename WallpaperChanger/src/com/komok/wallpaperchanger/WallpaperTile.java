package com.komok.wallpaperchanger;

import android.app.WallpaperInfo;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class WallpaperTile {

	public Drawable mThumbnail;
	public WallpaperInfo mWallpaperInfo;
	public Intent mIntent;

	public WallpaperTile(Drawable thumbnail, WallpaperInfo info, Intent intent) {
		mThumbnail = thumbnail;
		mWallpaperInfo = info;
		mIntent = intent;
	}

}
