package com.komok.daydreamapprunner;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.komok.common.BaseHelper;
import com.komok.common.MainActivity;
import com.komok.common.Tile;
import com.komok.wallpaperchanger.R;

public class DreamSettingsActivity extends Activity {
	
	Button button;
	ListView listView;
	CheckBox checkBox;
	DreamListAdapter mAdapter;
	List<String> selectedList;
 
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		findViewsById();
		mAdapter = new DreamListAdapter(this, BaseHelper.Apps.All);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setAdapter(mAdapter);
/*        setContentView(R.layout.activity_dream_settings);
        final SharedPreferences settings = getSharedPreferences(PREFS_KEY, 0);
        boolean animate = settings.getBoolean("animateDream", true);
 
        final ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_animate_button);
        toggle.setChecked(animate);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 
            @Override
            public void onCheckedChanged(final CompoundButton buttonView,
                    final boolean isChecked) {
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putBoolean("animateDream", isChecked);
                prefEditor.commit();
            }
        });*/
    }
    
	public void onStart() {
		super.onStart();

		selectedList = BaseHelper.loadDreamChoice(this);
		
		if(selectedList == null || selectedList.size() == 0){
			Intent intent = new Intent(this, MainActivity.class);

			// Create a bundle object
			Bundle b = new Bundle();
			b.putString(BaseHelper.ERROR, getString(R.string.select_one));

			// Add the bundle to the intent.
			intent.putExtras(b);

			// start the ResultActivity
			startActivity(intent);
		} else {
			setItemChecked();
		}

/*		if (BaseHelper.Weekday.Random.name().equals(day) || BaseHelper.Weekday.List.name().equals(day)) {
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
		button = (Button) findViewById(R.id.choice_button);
		checkBox = (CheckBox) findViewById(R.id.select_all);

	}
	
	public void setItemChecked() {

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
