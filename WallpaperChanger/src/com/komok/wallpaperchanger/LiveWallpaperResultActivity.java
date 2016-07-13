package com.komok.wallpaperchanger;

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

import com.komok.itemtouchhelper.helper.OnStartDragListener;
import com.komok.itemtouchhelper.helper.SimpleItemTouchHelperCallback;

public class LiveWallpaperResultActivity extends Activity implements OnClickListener, OnStartDragListener {
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
			mButton.setText(getResources().getString(R.string.set_rendom_wallpaper));
		} else {
			mButton.setText(getResources().getString(R.string.set_wallpaper));
		}

		mButton.setOnClickListener(this);

		listView = (RecyclerView) findViewById(R.id.outputList);
		//LiveWallpaperResultListAdapter adapter = new LiveWallpaperResultListAdapter(this, this, LiveWallpaperSelectionActivity.selectedTilesList);
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
		
		Map<String, String> selectedWallpapersMap = new LinkedHashMap<String, String>();
		
		for(Tile tile : LiveWallpaperSelectionActivity.selectedTilesList){
			selectedWallpapersMap.put(tile.mLabel, "");
		}

		BaseHelper.saveWallpapersMap(selectedWallpapersMap, this, day);
		
		if (BaseHelper.Weekday.Random.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperRandomActivity.class));
		} else if (BaseHelper.Weekday.Monday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperMondayActivity.class));
		} else if (BaseHelper.Weekday.Tuesday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperTuesdayActivity.class));
		} else if (BaseHelper.Weekday.Wednesday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperWednesdayActivity.class));
		} else if (BaseHelper.Weekday.Thursday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperThursdayActivity.class));
		} else if (BaseHelper.Weekday.Friday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperFridayActivity.class));
		} else if (BaseHelper.Weekday.Saturday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperSaturdayActivity.class));
		} else if (BaseHelper.Weekday.Sunday.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperSundayActivity.class));
		} else if (BaseHelper.Weekday.List.name().equals(day)) {
			startActivity(new Intent(this, LiveWallpaperListActivity.class));
		}

	}

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
