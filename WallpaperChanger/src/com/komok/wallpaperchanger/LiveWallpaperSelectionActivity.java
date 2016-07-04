package com.komok.wallpaperchanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mytestlist.R;

import android.app.ListActivity;
import android.app.WallpaperInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class LiveWallpaperSelectionActivity extends ListActivity implements OnClickListener {

	Button button;
	ListView listView;
	CheckBox checkBox;
	LiveWallpaperListAdapter mAdapter;
	String day;
	Map<String, String> selectedWallpapersMap;
	String message;
	String error;
	static List<WallpaperTile> selectedTilesList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		findViewsById();

		mAdapter = new LiveWallpaperListAdapter(this);

		listView.setAdapter(mAdapter);
		button.setOnClickListener(this);

	}

	public void onStart() {
		super.onStart();
		Bundle b = getIntent().getExtras();
		day = b.getString(WallpaperChangerHelper.DAY);
		error = b.getString(WallpaperChangerHelper.ERROR);
		checkBox.setChecked(false);
		selectedWallpapersMap = WallpaperChangerHelper.loadMap(this, day);

		if (WallpaperChangerHelper.Weekday.Random.name().equals(day)) {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			checkBox.setVisibility(View.VISIBLE);
			message = getString(R.string.select_one_or_more);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			checkBox.setVisibility(View.GONE);
			message = getString(R.string.select_one);
		}
		
		if(WallpaperChangerHelper.ERROR.equals(error)){
			Toast.makeText(this, getString(R.string.error_update_list), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		}

	}

	public void setItemChecked() {

		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			WallpaperTile lwp = (WallpaperTile) listView.getItemAtPosition(i);
			if (selectedWallpapersMap.containsKey(lwp.mWallpaperInfo.getServiceName())) {
				listView.setItemChecked(i, true);
			}
		}
	}

	private void findViewsById() {
		listView = (ListView) findViewById(android.R.id.list);
		button = (Button) findViewById(R.id.testbutton);
		checkBox = (CheckBox) findViewById(R.id.select_all);
	}

	public void onClick(View v) {
		SparseBooleanArray checked = listView.getCheckedItemPositions();
		selectedWallpapersMap = new HashMap<String, String>();
		selectedTilesList = new ArrayList<WallpaperTile>();

		if (checked.size() == 0) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return;
		}

		for (int i = 0; i < checked.size(); i++) {
			// Item position in adapter
			int position = checked.keyAt(i);
			if (checked.valueAt(i)) {
				
				selectedTilesList.add(mAdapter.getItem(position));
				
				WallpaperInfo info = mAdapter.getItem(position).mWallpaperInfo;
				selectedWallpapersMap.put(info.getServiceName(), info.getPackageName());
			}
		}

		WallpaperChangerHelper.saveMap(selectedWallpapersMap, this, day);

		Intent intent = new Intent(this, LiveWallpaperResultActivity.class);

		// Create a bundle object
		Bundle b = new Bundle();
		b.putString(WallpaperChangerHelper.DAY, day);

		// Add the bundle to the intent.
		intent.putExtras(b);

		// start the ResultActivity
		startActivity(intent);
	}

	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which checkbox was clicked
		switch (view.getId()) {
		case R.id.select_all:
			if (checked) {
				for (int i = 0; i < listView.getAdapter().getCount(); i++) {
					listView.setItemChecked(i, true);
				}
			} else {
				for (int i = 0; i < listView.getAdapter().getCount(); i++) {
					listView.setItemChecked(i, false);
				}
			}
			break;

		}
	}

}
