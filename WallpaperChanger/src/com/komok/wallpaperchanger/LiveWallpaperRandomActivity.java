package com.komok.wallpaperchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.komok.wallpaperchanger.BaseHelper.Weekday;

public class LiveWallpaperRandomActivity extends AbstractLiveWallpaperSetterActivity {

	@Override
	protected LiveWallpaper getLiveWallpaper() {
		Map<String, String> selectedWallpapersMap = BaseHelper.loadWallpapersMap(this, getDay().name());

		if (selectedWallpapersMap.size() > 0) {
			Random random = new Random();
			List<String> keys = new ArrayList<String>(selectedWallpapersMap.keySet());
			String className = keys.get(random.nextInt(keys.size()));
			String packageName = selectedWallpapersMap.get(className);

			return new LiveWallpaper(className, packageName);
		} else {
			return null;
		}
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.Random;
	}

}
