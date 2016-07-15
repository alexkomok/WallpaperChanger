package com.komok.wallpaperchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.BaseHelper.Weekday;


public class LiveWallpaperListActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected ApplicationHolder getLiveWallpaper() {
		Map<String, String> selectedWallpapersMap = BaseHelper.loadWallpapersMap(this, getDay().name());
		int size = selectedWallpapersMap.size();
		int savedPosition = BaseHelper.loadWallpaperListPosition(this);
		
		if(size > 0){
			if(savedPosition < size){
				int nextPosition = savedPosition + 1 >= size ? 0 : savedPosition + 1;
				List<String> keys = new ArrayList<String>(selectedWallpapersMap.keySet());
				String className = keys.get(savedPosition);
				String packageName = selectedWallpapersMap.get(className);
				BaseHelper.saveWallpaperListPosition(nextPosition, this);
				return new ApplicationHolder(className, packageName);
			}
			
		} 
		return null;
		
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.List;
	}

}
