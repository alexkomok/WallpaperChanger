package com.komok.wallpaperchanger;

import java.util.List;

import android.widget.BaseAdapter;

public abstract class AbstractBaseAdapter<T extends Tile> extends BaseAdapter {
	
	public List<T> mTiles;
	
	public long getItemId(int position) {
		return position;
	}
	
	public int getCount() {
		if (mTiles == null) {
			return 0;
		}
		return mTiles.size();
	}

	public Tile getItem(int position) {
		return mTiles.get(position);
	}

}
