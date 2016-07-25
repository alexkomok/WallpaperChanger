package com.komok.daydreamchanger;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.view.LayoutInflater;

import com.komok.common.AbstractBaseAdapter;
import com.komok.common.AbstractEnumerator;
import com.komok.common.BaseHelper;
import com.komok.common.Tile;
import com.komok.wallpaperchanger.R;

public class DayDreamListAdapter extends AbstractBaseAdapter<Tile> {

	private final PackageManager mPackageManager;

	@SuppressWarnings("unchecked")
	public DayDreamListAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();
		List<ResolveInfo> list = mPackageManager.queryIntentServices(new Intent(DreamService.SERVICE_INTERFACE), PackageManager.GET_META_DATA);
		mTiles = new ArrayList<Tile>();
		new DayDreamEnumerator(context, this).execute(list);
	}

	private class DayDreamEnumerator extends AbstractEnumerator<ResolveInfo, Tile, DayDreamListAdapter> {

		public DayDreamEnumerator(Context context, AbstractBaseAdapter<Tile> adapter) {
			super(context, adapter);
		}

		@SuppressWarnings("unchecked")
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
/*				WallpaperInfo info = null;
				try {
					info = new WallpaperInfo(mContext, resolveInfo);
				} catch (XmlPullParserException e) {
					Log.w(LOG_TAG, "Skipping wallpaper " + resolveInfo.serviceInfo, e);
					continue;
				} catch (IOException e) {
					Log.w(LOG_TAG, "Skipping wallpaper " + resolveInfo.serviceInfo, e);
					continue;
				}*/

				Drawable thumb = resolveInfo.serviceInfo.loadIcon(packageManager);
				Intent launchIntent = new Intent(DreamService.SERVICE_INTERFACE);
				launchIntent.setClassName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
				launchIntent.setPackage(resolveInfo.serviceInfo.packageName);
				Tile tile = new Tile(thumb, launchIntent, (String) resolveInfo.serviceInfo.loadLabel(mPackageManager));
/*				
				Intent intentSetting;
			    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
			    	intentSetting = new Intent(Settings.ACTION_DREAM_SETTINGS);
			    } else {
			    	intentSetting = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
			    }
			    intentSetting.setPackage(resolveInfo.serviceInfo.packageName);*/
				
				tile.mSettingsActivity = resolveInfo.serviceInfo.applicationInfo.className;
				
				Integer object = 0;
				XmlResourceParser xml = null;
				String name = "";
				Bundle metadata = resolveInfo.serviceInfo.metaData;
	            if(metadata != null) {
	            	object = metadata.getInt("android.service.dream");
	            	xml = mContext.getResources().getXml(object);
	            	name = mContext.getResources().getResourceName(object);
	            }
	            int test = R.xml.settings_dream;
				
				BaseHelper.getIntentTest(resolveInfo.serviceInfo.packageName, packageManager);
				
				publishProgress(tile);
			}
			// Send a null object to show loading is finished
			publishProgress((Tile) null);

			return null;
		}

	}
}
