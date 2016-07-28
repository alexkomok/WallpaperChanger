package com.komok.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.OrderedJSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class BaseHelper {

	private static final String wallpaperChangerSettings = "wallpaperChangerSettings";
	private static final String appChangerSettings = "appChangerSettings";
	private static final String dreamChangerSettings = "dreamChangerSettings";
	private static final String dreamSettings = "dayDreamSettings";
	public static final String DAY = "day";
	public static final String APPS = "apps";
	public static final String ERROR = "error";
	public static final String positionKey = "positionKey";
	public static final String dreamChoice = "dreamChoice";
	public static final String splitter = ": ";

	public enum Weekday {
		Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, Random, List
	};

	public enum Apps {
		All, Sys, User
	};

	public enum Components {
		LiveWallpaper, Application, DayDream
	};

	public static String getFormattedComponentName(Components component, Weekday day) {
		return getFormattedComponentName(component.name(), day.name());
	}

	public static String getFormattedComponentName(String component, String day) {
		return component + splitter + day;
	}

	public static List<String> loadComponentsList(Context context) {
		List<String> results = new ArrayList<String>();

		for (Weekday day : Weekday.values()) {
			Map<String, String> map = null;
			for (Components component : Components.values()) {
				map = null;
				if (Components.LiveWallpaper.equals(component)) {
					map = loadWallpapersMap(context, day.name());
				} else if (Components.Application.equals(component)) {
					map = loadAppsMap(context, day.name());
				} else if (Components.DayDream.equals(component)) {
					//map = load(context, day.name());
				}

				if (map != null && map.size() > 0)
					results.add(getFormattedComponentName(component, day));
			}
		}
		return results;
	}

	public static ApplicationHolder loadLiveWallpaper(Context context, Weekday day) {
		return loadComponent(loadWallpapersMap(context, day.name()));
	}

	public static ApplicationHolder loadApp(Context context, Weekday day) {
		return loadComponent(loadAppsMap(context, day.name()));
	}
	
	public static ApplicationHolder loadDream(Context context, Weekday day) {
		return loadComponent(loadDreamsMap(context, day.name()));
	}
	
	private static ApplicationHolder loadComponent(Map<String, String> outputMap){
		String className = null;
		String packageName = null;
		for (Map.Entry<String, String> entry : outputMap.entrySet()) {
			className = entry.getKey();
			packageName = entry.getValue();
		}
		return new ApplicationHolder(className, packageName);
	}

	public static void saveAppListPosition(int position, Context context) {
		saveListPosition(position, context, appChangerSettings);
	}

	public static void saveWallpaperListPosition(int position, Context context) {
		saveListPosition(position, context, wallpaperChangerSettings);
	}

	public static void saveDreamListPosition(int position, Context context) {
		saveListPosition(position, context, dreamChangerSettings);
	}

	public static void saveListPosition(int position, Context context, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			Editor editor = pSharedPref.edit();
			editor.remove(positionKey).commit();
			editor.putInt(positionKey, position).commit();
		}
	}

	public static void saveDreamChoice(List<String> choice, Context context) {
		SharedPreferences pSharedPref = context.getSharedPreferences(dreamSettings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			JSONArray jsonArray = new JSONArray();
			try {
				jsonArray = new JSONArray(choice);
			} catch (JSONException e) {
				e.printStackTrace();
				ExceptionHandler.caughtException(e, context);
			}

			Editor editor = pSharedPref.edit();
			editor.remove(dreamChoice).commit();
			editor.putString(dreamChoice, jsonArray.toString()).commit();
		}
	}

	public static List<String> loadDreamChoice(Context context) {
		SharedPreferences pSharedPref = context.getSharedPreferences(dreamSettings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			String pref = pSharedPref.getString(dreamChoice, "");
			if (!"".equals(pref) && pref != null) {
				try {

					return new JSONArray(pref);
				} catch (JSONException e) {
					e.printStackTrace();
					ExceptionHandler.caughtException(e, context);
				}
			}
		}
		return new ArrayList<String>();
	}

	public static int loadWallpaperListPosition(Context context) {
		return loadListPosition(context, wallpaperChangerSettings);
	}

	public static int loadAppListPosition(Context context) {
		return loadListPosition(context, appChangerSettings);
	}

/*	public static int loadDreamListPosition(Context context) {
		return loadListPosition(context, dreamSettings);
	}*/
	
	public static int loadDreamListPosition(Context context) {
		return loadListPosition(context, dreamChangerSettings);
	}	

	public static int loadListPosition(Context context, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		if (pSharedPref != null) {
			return pSharedPref.getInt(positionKey, 0);
		} else
			return 0;
	}

	public static void saveWallpapersMap(Map<String, String> inputMap, Context context, String randomMap) {
		saveMap(inputMap, context, randomMap, wallpaperChangerSettings);
	}

	public static void saveAppsMap(Map<String, String> inputMap, Context context, String randomMap) {
		saveMap(inputMap, context, randomMap, appChangerSettings);
	}
	
	public static void saveDreamsMap(Map<String, String> inputMap, Context context, String randomMap) {
		saveMap(inputMap, context, randomMap, dreamChangerSettings);
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
		return loadMap(context, mapName, wallpaperChangerSettings);
	}

	public static Map<String, String> loadAppsMap(Context context, String mapName) {
		return loadMap(context, mapName, appChangerSettings);
	}
	
	public static Map<String, String> loadDreamsMap(Context context, String mapName) {
		return loadMap(context, mapName, dreamChangerSettings);
	}	

	private static Map<String, String> loadMap(Context context, String mapName, String settings) {
		SharedPreferences pSharedPref = context.getSharedPreferences(settings, Context.MODE_PRIVATE);
		OrderedJSONObject jsonObject = new OrderedJSONObject();
		if (pSharedPref != null) {
			String jsonString = pSharedPref.getString(mapName, (new OrderedJSONObject()).toString());
			try {
				jsonObject = new OrderedJSONObject(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
				ExceptionHandler.caughtException(e, context);
			}
		}

		return jsonObject;
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
