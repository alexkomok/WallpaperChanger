package com.komok.wallpaperchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.komok.wallpaperchanger.BaseHelper.Weekday;


public class LiveWallpaperListActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected LiveWallpaper getLiveWallpaper() {
		Map<String, String> selectedWallpapersMap = BaseHelper.loadWallpapersMap(this, getDay().name());
		int size = selectedWallpapersMap.size();
		int savedPosition = BaseHelper.loadListPosition(this);
		
		if(size > 0){
			if(savedPosition < size){
				int nextPosition = savedPosition + 1 >= size ? 0 : savedPosition + 1;
				List<String> keys = new ArrayList<String>(selectedWallpapersMap.keySet());
				String className = keys.get(savedPosition);
				String packageName = selectedWallpapersMap.get(className);
				BaseHelper.saveListPosition(nextPosition, this);
				return new LiveWallpaper(className, packageName);
			}
			
		} 
		return null;
		
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.List;
	}

}
