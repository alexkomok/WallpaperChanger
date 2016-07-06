package com.komok.wallpaperchanger;

import java.util.List;

import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LiveWallpaperSelectedListAdapter extends BaseAdapter implements ListAdapter {

	private final LayoutInflater mInflater;
	private final PackageManager mPackageManager;

	private List<WallpaperTile> mWallpapers;
	OnClickListener mViewOnClickListener;
	Context mContext;

	public LiveWallpaperSelectedListAdapter(final Context context, List<WallpaperTile> selectedTilesList) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();
		mWallpapers = selectedTilesList;
		mContext = context;
	}

	public int getCount() {
		if (mWallpapers == null) {
			return 0;
		}
		return mWallpapers.size();
	}

	public WallpaperTile getItem(int position) {
		return mWallpapers.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.live_wallpaper, parent, false);
		} else {
			view = convertView;
		}

		final WallpaperTile wallpaperTile = mWallpapers.get(position);
		ImageView image = (ImageView) view.findViewById(R.id.wallpaper_image);
		ImageView icon = (ImageView) view.findViewById(R.id.wallpaper_icon);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.myCheckBox);
		checkBox.setVisibility(View.GONE);
		if (wallpaperTile.mThumbnail != null) {
			image.setImageDrawable(wallpaperTile.mThumbnail);
			icon.setVisibility(View.GONE);
		} else {
			icon.setImageDrawable(wallpaperTile.mWallpaperInfo.loadIcon(mPackageManager));
			icon.setVisibility(View.VISIBLE);
		}

		mViewOnClickListener = new OnClickListener() {
			public void onClick(View v) {

				WallpaperInfo wi = wallpaperTile.mWallpaperInfo;
				if (wi != null && wi.getSettingsActivity() != null) {

					Intent intent = new Intent();
					intent.setComponent(new ComponentName(wallpaperTile.mWallpaperInfo.getPackageName(), wi.getSettingsActivity()));
					mContext.startActivity(intent);

				} else

					Toast.makeText(v.getContext(), mContext.getString(R.string.no_settings), Toast.LENGTH_LONG).show();
			}
		};
		
		view.setOnClickListener(mViewOnClickListener);

		TextView label = (TextView) view.findViewById(R.id.wallpaper_item_label);
		label.setText(wallpaperTile.mWallpaperInfo.loadLabel(mPackageManager));

		return view;
	}

}
