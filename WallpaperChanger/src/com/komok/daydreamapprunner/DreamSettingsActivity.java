package com.komok.daydreamapprunner;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.komok.common.BaseHelper;
import com.komok.common.IItemChecked;
import com.komok.common.MainActivity;
import com.komok.common.Tile;
import com.komok.wallpaperchanger.R;

public class DreamSettingsActivity extends Activity implements OnClickListener, IItemChecked {
	
	Button button;
	ListView listView;
	CheckBox checkBox;
	DreamListAdapter mAdapter;
	List<String> selectedList;
	String message;
 
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		findViewsById();
		mAdapter = new DreamListAdapter(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(mAdapter);
		message = getString(R.string.select_one_or_more);
		button.setOnClickListener(this);
    }
    
	public void onStart() {
		super.onStart();

		selectedList = BaseHelper.loadDreamChoice(this);
		
/*		if(selectedList == null || selectedList.size() == 0){
			Intent intent = new Intent(this, MainActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();
			b.putString(BaseHelper.ERROR, getString(R.string.select_one));

			// Add the bundle to the intent.
			intent.putExtras(b);

			// start the ResultActivity
			//startActivity(intent);
		} else {
			setItemChecked();
		}

		if (BaseHelper.Weekday.Random.name().equals(day) || BaseHelper.Weekday.List.name().equals(day)) {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			checkBox.setVisibility(View.VISIBLE);
			message = getString(R.string.select_one_or_more);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			checkBox.setVisibility(View.GONE);
			message = getString(R.string.select_one);
		}

		if (BaseHelper.ERROR.equals(error)) {
			Toast.makeText(this, getString(R.string.error_update_list), Toast.LENGTH_LONG).show();
		} else if (error != null) {
			Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		}*/

		

	}
    
	private void findViewsById() {
		listView = (ListView) findViewById(android.R.id.list);
		button = (Button) findViewById(R.id.testbutton);
		checkBox = (CheckBox) findViewById(R.id.select_all);

	}
	
	
	public void onClick(View v) {
		SparseBooleanArray checked = listView.getCheckedItemPositions();
		selectedList = new ArrayList<String>();

		boolean isAnyChecked = false;
		for (int i = 0; i < checked.size(); i++) {
			// Item position in adapter
			int position = checked.keyAt(i);

			if (checked.valueAt(i)) {
				isAnyChecked = true;
				Tile tile = (Tile) mAdapter.getItem(position);
				String label = tile.mLabel;
				try {
					selectedList.add(tile.mLabel);
				} catch (IllegalArgumentException e) {
					Toast.makeText(this, "Duplicated name. Please, deselect: " + label, Toast.LENGTH_LONG).show();
					return;
				}
			}
		}

		if (!isAnyChecked) {
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return;
		}

		BaseHelper.saveDreamChoice(selectedList, this);

/*		Intent intent = new Intent(this, AppResultActivity.class);

		// Create a bundle object
		Bundle b = new Bundle();
		b.putString(BaseHelper.DAY, day);

		// Add the bundle to the intent.
		intent.putExtras(b);

		// start the ResultActivity
		startActivity(intent);*/
	}	
	
	public void setItemChecked() {
		
		if(listView.getAdapter().getCount() == 0){
			Intent intent = new Intent(this, MainActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();
			b.putString(BaseHelper.ERROR, getString(R.string.select_one));

			// Add the bundle to the intent.
			intent.putExtras(b);

			// start the ResultActivity
			startActivity(intent);
		} else {

			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				Tile tile = (Tile) listView.getItemAtPosition(i);
				String label = tile.mLabel;
				if (selectedList.contains(label)){
					listView.setItemChecked(i, true);
				} else {
					listView.setItemChecked(i, false);
				}
			}
		}
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
