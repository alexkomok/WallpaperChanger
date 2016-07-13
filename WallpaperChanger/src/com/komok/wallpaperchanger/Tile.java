package com.komok.wallpaperchanger;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class Tile {


	public Drawable mThumbnail;
	public Intent mIntent;
	public String mLabel;

	public Tile(Drawable mThumbnail, Intent mIntent, String mLabel) {
		super();
		this.mThumbnail = mThumbnail;
		this.mIntent = mIntent;
		this.mLabel = mLabel;
	}
}
