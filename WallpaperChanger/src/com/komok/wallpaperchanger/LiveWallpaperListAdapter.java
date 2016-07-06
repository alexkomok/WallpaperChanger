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
import android.os.AsyncTask;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LiveWallpaperListAdapter extends BaseAdapter implements ListAdapter {
	private static final String LOG_TAG = "LiveWallpaperListAdapter";

	private final LayoutInflater mInflater;
	private final PackageManager mPackageManager;

	private List<WallpaperTile> mWallpapers;

	@SuppressWarnings("unchecked")
	public LiveWallpaperListAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();

		List<ResolveInfo> list = mPackageManager.queryIntentServices(new Intent(WallpaperService.SERVICE_INTERFACE), PackageManager.GET_META_DATA);

		mWallpapers = new ArrayList<WallpaperTile>();

		new LiveWallpaperEnumerator(context).execute(list);
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

		WallpaperTile wallpaperTile = mWallpapers.get(position);
		ImageView image = (ImageView) view.findViewById(R.id.wallpaper_image);
		ImageView icon = (ImageView) view.findViewById(R.id.wallpaper_icon);
		if (wallpaperTile.mThumbnail != null) {
			image.setImageDrawable(wallpaperTile.mThumbnail);
			icon.setVisibility(View.GONE);
		} else {
			icon.setImageDrawable(wallpaperTile.mWallpaperInfo.loadIcon(mPackageManager));
			icon.setVisibility(View.VISIBLE);
		}

		TextView label = (TextView) view.findViewById(R.id.wallpaper_item_label);
		label.setText(wallpaperTile.mWallpaperInfo.loadLabel(mPackageManager));

		return view;
	}

	private class LiveWallpaperEnumerator extends AsyncTask<List<ResolveInfo>, WallpaperTile, Void> {
		private Context mContext;
		private int mWallpaperPosition;

		public LiveWallpaperEnumerator(Context context) {
			super();
			mContext = context;
			mWallpaperPosition = 0;
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
				WallpaperTile wallpaper = new WallpaperTile(thumb, info, launchIntent);
				publishProgress(wallpaper);
			}
			// Send a null object to show loading is finished
			publishProgress((WallpaperTile) null);

			return null;
		}

		@Override
		protected void onProgressUpdate(WallpaperTile... infos) {
			for (WallpaperTile info : infos) {
				if (info == null) {
					LiveWallpaperListAdapter.this.notifyDataSetChanged();
					break;
				}
				if (info.mThumbnail != null) {
					info.mThumbnail.setDither(true);
				}
				if (mWallpaperPosition < mWallpapers.size()) {
					mWallpapers.set(mWallpaperPosition, info);
				} else {
					mWallpapers.add(info);
				}
				mWallpaperPosition++;
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mContext instanceof LiveWallpaperSelectionActivity) {
				((LiveWallpaperSelectionActivity) mContext).setItemChecked();
			}
		}
	}
}
