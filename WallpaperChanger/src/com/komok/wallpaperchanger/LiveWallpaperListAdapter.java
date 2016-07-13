package com.komok.wallpaperchanger;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LiveWallpaperListAdapter extends AbstractBaseAdapter<Tile> implements ListAdapter {
	private static final String LOG_TAG = "LiveWallpaperListAdapter";

	private final LayoutInflater mInflater;
	private final PackageManager mPackageManager;

	@SuppressWarnings("unchecked")
	public LiveWallpaperListAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();

		List<ResolveInfo> list = mPackageManager.queryIntentServices(new Intent(WallpaperService.SERVICE_INTERFACE), PackageManager.GET_META_DATA);

		mTiles = new ArrayList<Tile>();

		new LiveWallpaperEnumerator(context, this).execute(list);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.tile, parent, false);
		} else {
			view = convertView;
		}

		Tile tile = mTiles.get(position);
		ImageView image = (ImageView) view.findViewById(R.id.wallpaper_image);
		image.setImageDrawable(tile.mThumbnail);

		TextView label = (TextView) view.findViewById(R.id.wallpaper_item_label);
		label.setText(tile.mLabel);

		return view;
	}

	private class LiveWallpaperEnumerator extends AbstractEnumerator<ResolveInfo, Tile, LiveWallpaperListAdapter> {

		public LiveWallpaperEnumerator(Context context, AbstractBaseAdapter<Tile> adapter) {
			super(context, adapter);
		}

		@Override
		protected Void doInBackground(List<ResolveInfo>... params) {
			final PackageManager packageManager = mContext.getPackageManager();

			List<ResolveInfo> list = params[0];

			Collections.sort(list, new Comparator<ResolveInfo>() {
				final Collator mCollator;

				{
					mCollator = Collator.getInstance();
				}

				public int compare(ResolveInfo info1, ResolveInfo info2) {
					return mCollator.compare(info1.loadLabel(packageManager), info2.loadLabel(packageManager));
				}
			});

			for (ResolveInfo resolveInfo : list) {
				WallpaperInfo info = null;
				try {
					info = new WallpaperInfo(mContext, resolveInfo);
				} catch (XmlPullParserException e) {
					Log.w(LOG_TAG, "Skipping wallpaper " + resolveInfo.serviceInfo, e);
					continue;
				} catch (IOException e) {
					Log.w(LOG_TAG, "Skipping wallpaper " + resolveInfo.serviceInfo, e);
					continue;
				}

				Drawable thumb = info.loadThumbnail(packageManager);
				Intent launchIntent = new Intent(WallpaperService.SERVICE_INTERFACE);
				launchIntent.setClassName(info.getPackageName(), info.getServiceName());
				Tile tile = new Tile(thumb, launchIntent, (String) info.loadLabel(mPackageManager));
				publishProgress(tile);
			}
			// Send a null object to show loading is finished
			publishProgress((Tile) null);

			return null;
		}

	}
}
