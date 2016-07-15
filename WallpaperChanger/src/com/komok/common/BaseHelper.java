package com.komok.common;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.OrderedJSONObject;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.service.wallpaper.WallpaperService;


public class BaseHelper {

	private static final String wallpaperSettings = "wallpaperChangerSettings";
	private static final String appSettings = "appRunnerSettings";
	public static final String DAY = "day";
	public static final String APPS = "apps";
	public static final String ERROR = "error";
	public static final String positionKey = "positionKey";

	public enum Weekday {
		Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, Random, List
	};

	public enum Apps {
		All, Sys, User
	};

	public static boolean isLiveWallpaperValid(ApplicationHolder liveWallpaper, Activity activity) {
		boolean result = false;
		String className = liveWallpaper.getLabel();
		String packageName = liveWallpaper.getUri();
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

	public static ApplicationHolder loadLiveWallpaper(Context context, Weekday day) {
		Map<String, String> outputMap = loadWallpapersMap(context, day.name());
		String className = null;
		String packageName = null;
		for (Map.Entry<String, String> entry : outputMap.entrySet()) {
			className = entry.getKey();
			packageName = entry.getValue();
		}
		return new ApplicationHolder(className, packageName);

	}

	public static ApplicationHolder loadApp(Context context, Weekday day) {
		Map<String, String> outputMap = loadAppsMap(context, day.name());
		String className = null;
		String packageName = null;
		for (Map.Entry<String, String> entry : outputMap.entrySet()) {
			className = entry.getKey();
			packageName = entry.getValue();
		}
		return new ApplicationHolder(className, packageName);

	}

	public static void saveAppListPosition(int position, Context context) {
		saveListPosition(position, context, appSettings);
	}
	
	public static void saveWallpaperListPosition(int position, Context context) {
		saveListPosition(position, context, wallpaperSettings);
	}	
	
	public static void saveListPosition(int position, Context context, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			Editor editor = pSharedPref.edit();
			editor.remove(positionKey).commit();
			editor.putInt(positionKey, position).commit();
		}
	}
	
	public static int loadWallpaperListPosition(Context context) {
		return loadListPosition(context, wallpaperSettings);
	}
	
	public static int loadAppListPosition(Context context) {
		return loadListPosition(context, appSettings);
	}	

	public static int loadListPosition(Context context, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			return pSharedPref.getInt(positionKey, 0);
		} else
			return 0;
	}

	public static void saveWallpapersMap(Map<String, String> inputMap, Context context, String randomMap) {
		saveMap(inputMap, context, randomMap, wallpaperSettings);
	}

	public static void saveAppsMap(Map<String, String> inputMap, Context context, String randomMap) {
		saveMap(inputMap, context, randomMap, appSettings);
	}
	

	private static void saveMap(Map<String, String> inputMap, Context context, String randomMap, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			
			OrderedJSONObject jsonObject = new OrderedJSONObject();
			try {
				jsonObject = new OrderedJSONObject(inputMap);
			} catch (JSONException e) {
				e.printStackTrace();
				ExceptionHandler.caughtException(e, context);
			}


			String jsonString = jsonObject.toString();
			Editor editor = pSharedPref.edit();
			editor.remove(randomMap).commit();
			editor.putString(randomMap, jsonString);
			editor.commit();
		}
	}

	public static Map<String, String> loadWallpapersMap(Context context, String mapName) {
		return loadMap(context, mapName, wallpaperSettings);
	}

	public static Map<String, String> loadAppsMap(Context context, String mapName) {
		return loadMap(context, mapName, appSettings);
	}

	private static Map<String, String> loadMap(Context context, String mapName, String settings) {
		Map<String, String> outputMap = new LinkedHashMap<String, String>();
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		try {
			if (pSharedPref != null) {
				String jsonString = pSharedPref.getString(mapName, (new OrderedJSONObject()).toString());
				OrderedJSONObject jsonObject = new OrderedJSONObject(jsonString);
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

	public static Intent getIntent(String packageName, PackageManager pm) {
		Intent intent = new Intent();
		intent.setPackage(packageName);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

		if (resolveInfos.size() > 0) {
			ResolveInfo launchable = resolveInfos.get(0);
			ActivityInfo af = launchable.activityInfo;
			ComponentName name = new ComponentName(af.applicationInfo.packageName, af.name);
			Intent i = new Intent(Intent.ACTION_MAIN);

			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			i.setComponent(name);

			return i;
			// activity.getApplication().startActivity(i);
		} else {
			return null;
		}
	}

}