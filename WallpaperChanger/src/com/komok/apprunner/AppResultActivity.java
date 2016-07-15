package com.komok.apprunner;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.komok.common.BaseHelper;
import com.komok.common.Tile;
import com.komok.itemtouchhelper.OnStartDragListener;
import com.komok.itemtouchhelper.SimpleItemTouchHelperCallback;
import com.komok.wallpaperchanger.R;

public class AppResultActivity extends Activity implements OnClickListener, OnStartDragListener {
	String day;
	Button set_wallpaper;
	RecyclerView listView;
	private ItemTouchHelper mItemTouchHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Bundle b = getIntent().getExtras();
		day = b.getString(BaseHelper.DAY);

		Button mButton = (Button) findViewById(R.id.set_wallpaper);
		if (BaseHelper.Weekday.Random.name().equals(day)) {
			mButton.setText(getResources().getString(R.string.set_rendom_app));
		} else {
			mButton.setText(getResources().getString(R.string.set_app));
		}

		mButton.setOnClickListener(this);

		listView = (RecyclerView) findViewById(R.id.outputList);
		AppResultListAdapter adapter = new AppResultListAdapter(this, this, AppSelectionActivity.selectedTilesList);

		listView.setHasFixedSize(true);
		listView.setAdapter(adapter);
		listView.setLayoutManager(new LinearLayoutManager(this));

		ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
		mItemTouchHelper = new ItemTouchHelper(callback);
		mItemTouchHelper.attachToRecyclerView(listView);
	}

	@Override
	public void onClick(View v) {

		Map<String, String> selectedAppsMap = new LinkedHashMap<String, String>();

		for (Tile tile : AppSelectionActivity.selectedTilesList) {
			selectedAppsMap.put(tile.mLabel, tile.mIntent.toUri(0));
		}

		BaseHelper.saveAppsMap(selectedAppsMap, this, day);

		if (selectedAppsMap.size() == 0) {
			Intent intent = new Intent(this, AppSelectionActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();
			b.putString(BaseHelper.DAY, day);

			String error = getString(R.string.select_one);

			if (BaseHelper.Weekday.Random.name().equals(day)) {
				error = getString(R.string.select_one_or_more);
			}

			b.putString(BaseHelper.ERROR, error);

			// Add the bundle to the intent.
			intent.putExtras(b);

			startActivity(intent);
			finish();
		} else {

			if (BaseHelper.Weekday.Random.name().equals(day)) {
				startActivity(new Intent(this, AppRandomActivity.class));
			} else if (BaseHelper.Weekday.Monday.name().equals(day)) {
				startActivity(new Intent(this, AppMondayActivity.class));
			} else if (BaseHelper.Weekday.Tuesday.name().equals(day)) {
				startActivity(new Intent(this, AppTuesdayActivity.class));
			} else if (BaseHelper.Weekday.Wednesday.name().equals(day)) {
				startActivity(new Intent(this, AppWednesdayActivity.class));
			} else if (BaseHelper.Weekday.Thursday.name().equals(day)) {
				startActivity(new Intent(this, AppThursdayActivity.class));
			} else if (BaseHelper.Weekday.Friday.name().equals(day)) {
				startActivity(new Intent(this, AppFridayActivity.class));
			} else if (BaseHelper.Weekday.Saturday.name().equals(day)) {
				startActivity(new Intent(this, AppSaturdayActivity.class));
			} else if (BaseHelper.Weekday.Sunday.name().equals(day)) {
				startActivity(new Intent(this, AppSundayActivity.class));
			} else if (BaseHelper.Weekday.List.name().equals(day)) {
				startActivity(new Intent(this, AppListActivity.class));
			}
		}

	}

	@Override
	public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
		mItemTouchHelper.startDrag(viewHolder);
	}

}