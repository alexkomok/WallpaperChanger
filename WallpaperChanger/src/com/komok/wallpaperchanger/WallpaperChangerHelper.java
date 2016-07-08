package com.komok.wallpaperchanger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.service.wallpaper.WallpaperService;

public class WallpaperChangerHelper {

	private static final String settings = "wallpaperChangerSettings";
	public static final String DAY = "day";
	public static final String ERROR = "error";
	public static final String positionKey = "positionKey";

	enum Weekday {
		Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, Random, List
	};

	public static boolean isLiveWallpaperValid(LiveWallpaper liveWallpaper, Activity activity) {
		boolean result = false;
		String className = liveWallpaper.getClassName();
		String packageName = liveWallpaper.getPackageName();
		if (className != null && packageName != null) {
			Map<String, String> availableWallpapersMap = getAvailableWallpapersMap(activity);
			if (availableWallpapersMap.containsKey(className) && packageName.equals(availableWallpapersMap.get(className))) {
				result = true;
			}
		}

		return result;
	}

	public static Map<String, String> getAvailableWallpapersMap(Activity activity) {
		Map<String, String> availableWallpapersMap = new LinkedHashMap<String, String>();
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> availableWallpapersList = packageManager.queryIntentServices(new Intent(WallpaperService.SERVICE_INTERFACE),
				PackageManager.GET_META_DATA);

		for (ResolveInfo resolveInfo : availableWallpapersList) {
			availableWallpapersMap.put(resolveInfo.serviceInfo.name, resolveInfo.serviceInfo.packageName);
		}
		return availableWallpapersMap;
	}

	public static void saveLiveWallpaper(LiveWallpaper liveWallpaper, Context context, Weekday day) {
		Map<String, String> inputMap = new LinkedHashMap<String, String>();
		inputMap.put(liveWallpaper.getClassName(), liveWallpaper.getPackageName());
		saveMap(inputMap, context, day.name());
	}

	public static LiveWallpaper loadLiveWallpaper(Context context, Weekday day) {
		Map<String, String> outputMap = loadMap(context, day.name());
		String className = null;
		String packageName = null;
		for (Map.Entry<String, String> entry : outputMap.entrySet()) {
			className = entry.getKey();
			packageName = entry.getValue();
		}
		return new LiveWallpaper(className, packageName);

	}
	
	public static void saveListPosition(int position, Context context) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			Editor editor = pSharedPref.edit();
			editor.remove(positionKey).commit();
			editor.putInt(positionKey, position).commit();
		}
	}
	
	public static int loadListPosition(Context context) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			return pSharedPref.getInt(positionKey, 0);
		} else 
			return 0;
	}

	public static void saveMap(Map<String, String> inputMap, Context context, String randomMap) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			JSONObject jsonObject = new JSONObject(inputMap);
			String jsonString = jsonObject.toString();
			Editor editor = pSharedPref.edit();
			editor.remove(randomMap).commit();
			editor.putString(randomMap, jsonString);
			editor.commit();
		}
	}

	public static Map<String, String> loadMap(Context context, String mapName) {
		Map<String, String> outputMap = new LinkedHashMap<String, String>();
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		try {
			if (pSharedPref != null) {
				String jsonString = pSharedPref.getString(mapName, (new JSONObject()).toString());
				JSONObject jsonObject = new JSONObject(jsonString);
				Iterator<String> keysItr = jsonObject.keys();
				while (keysItr.hasNext()) {
					String key = keysItr.next();
					String value = (String) jsonObject.get(key);
					outputMap.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.caughtException(e, context);
		}
		return outputMap;
	}

	public static String capitalizeFirstLetter(String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
}
