package com.komok.wallpaperchanger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AppListAdapter extends AbstractBaseAdapter<Tile> implements ListAdapter {

	private final LayoutInflater mInflater;
	private final PackageManager mPackageManager;

	@SuppressWarnings("unchecked")
	public AppListAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ApplicationInfo> list = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		List<ApplicationInfo> userAppList = new ArrayList<ApplicationInfo>();

		for (ApplicationInfo app : list) {
			if (isUserApp(app)) {
				userAppList.add(app);
			}
		}

		mTiles = new ArrayList<Tile>();

		new ApplicationEnumerator(context, this).execute(list);
	}

	private boolean isUserApp(ApplicationInfo ai) {
		int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
		return (ai.flags & mask) == 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.tile, parent, false);
		} else {
			view = convertView;
		}

		Tile tile = (Tile) mTiles.get(position);
		ImageView image = (ImageView) view.findViewById(R.id.wallpaper_image);
		image.setImageDrawable(tile.mThumbnail);

		TextView label = (TextView) view.findViewById(R.id.wallpaper_item_label);
		label.setText(tile.mLabel);

		return view;
	}

	private class ApplicationEnumerator extends AbstractEnumerator<ApplicationInfo, Tile, AppListAdapter> {

		public ApplicationEnumerator(Context context, AbstractBaseAdapter<Tile> adapter) {
			super(context, adapter);
		}

		@Override
		protected Void doInBackground(List<ApplicationInfo>... params) {
			final PackageManager packageManager = mContext.getPackageManager();

			List<ApplicationInfo> list = params[0];

			Collections.sort(list, new Comparator<ApplicationInfo>() {
				final Collator mCollator;

				{
					mCollator = Collator.getInstance();
				}

				public int compare(ApplicationInfo info1, ApplicationInfo info2) {
					return mCollator.compare(info1.loadLabel(packageManager), info2.loadLabel(packageManager));
				}
			});

			for (ApplicationInfo info : list) {

				Drawable thumb = info.loadIcon((packageManager));
				// Intent launchIntent =
				// packageManager.getLaunchIntentForPackage(info.packageName);
				Intent launchIntent = BaseHelper.getIntent(info.packageName, packageManager);
				String label = (String) packageManager.getApplicationLabel(info);

				if (launchIntent != null && label != null) {
					Tile application = new Tile(thumb, launchIntent, label);
					publishProgress(application);
				}
			}
			// Send a null object to show loading is finished
			publishProgress((Tile) null);

			return null;
		}

	}
}
