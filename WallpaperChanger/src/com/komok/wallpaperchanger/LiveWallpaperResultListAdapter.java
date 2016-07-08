package com.komok.wallpaperchanger;

import java.util.List;

import android.content.Context;

import com.komok.itemtouchhelper.helper.OnStartDragListener;
import com.komok.itemtouchhelper.helper.RecyclerListAdapter;


public class LiveWallpaperResultListAdapter extends RecyclerListAdapter {

	public LiveWallpaperResultListAdapter(Context context, OnStartDragListener dragStartListener, List<WallpaperTile> selectedTilesList) {
		super(context, dragStartListener, selectedTilesList);
	}

}
