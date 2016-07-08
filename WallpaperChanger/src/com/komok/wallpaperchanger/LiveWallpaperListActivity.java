package com.komok.wallpaperchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.komok.wallpaperchanger.WallpaperChangerHelper.Weekday;


public class LiveWallpaperListActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected LiveWallpaper getLiveWallpaper() {
		Map<String, String> selectedWallpapersMap = WallpaperChangerHelper.loadMap(this, getDay().name());
		int size = selectedWallpapersMap.size();
		int savedPosition = WallpaperChangerHelper.loadListPosition(this);
		
		if(size > 0){
			if(savedPosition < size){
				int nextPosition = savedPosition + 1 >= size ? 0 : savedPosition + 1;
				List<String> keys = new ArrayList<String>(selectedWallpapersMap.keySet());
				String className = keys.get(savedPosition);
				String packageName = selectedWallpapersMap.get(className);
				WallpaperChangerHelper.saveListPosition(nextPosition, this);
				return new LiveWallpaper(className, packageName);
			}
			
		} 
		return null;
		
	}

	@Override
	protected Weekday getDay() {
		return WallpaperChangerHelper.Weekday.List;
	}

}
