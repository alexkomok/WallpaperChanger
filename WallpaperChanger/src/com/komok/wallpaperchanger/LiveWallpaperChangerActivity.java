package com.komok.wallpaperchanger;

import com.komok.wallpaperchanger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LiveWallpaperChangerActivity extends Activity implements OnClickListener{
	
	Button random_button;
	Button monday_button;
	Button tuesday_button;
	Button wednesday_button;
	Button thursday_button;
	Button friday_button;
	Button saturday_button;
	Button sunday_button;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewsById();

		random_button.setOnClickListener(this);
		monday_button.setOnClickListener(this);
		tuesday_button.setOnClickListener(this);
		wednesday_button.setOnClickListener(this);
		thursday_button.setOnClickListener(this);
		friday_button.setOnClickListener(this);
		saturday_button.setOnClickListener(this);
		sunday_button.setOnClickListener(this);

	}
    
    private void findViewsById() {
        random_button = (Button) findViewById(R.id.random_button);
        monday_button = (Button) findViewById(R.id.monday_button);
        tuesday_button = (Button) findViewById(R.id.tuesday_button);
        wednesday_button = (Button) findViewById(R.id.wednesday_button);
        thursday_button = (Button) findViewById(R.id.thursday_button);
        friday_button = (Button) findViewById(R.id.friday_button);
        saturday_button = (Button) findViewById(R.id.saturday_button);
        sunday_button = (Button) findViewById(R.id.sunday_button);
    }

	@Override
	public void onClick(View v) {
		
		WallpaperChangerHelper.Weekday day = WallpaperChangerHelper.Weekday.Random;
		Intent intent = new Intent(this, LiveWallpaperSelectionActivity.class);
		
		switch (v.getId()) {
	      case R.id.random_button:
	    	  day = WallpaperChangerHelper.Weekday.Random;
	        break;
	      case R.id.monday_button:
	    	  day = WallpaperChangerHelper.Weekday.Monday;
	        break;
	      case R.id.tuesday_button:
	    	  day = WallpaperChangerHelper.Weekday.Tuesday;
	        break;
	      case R.id.wednesday_button:
	    	  day = WallpaperChangerHelper.Weekday.Wednesday;
	        break;
	      case R.id.thursday_button:
	    	  day = WallpaperChangerHelper.Weekday.Thursday;
	        break;
	      case R.id.friday_button:
	    	  day = WallpaperChangerHelper.Weekday.Friday;
	        break;
	      case R.id.saturday_button:
	    	  day = WallpaperChangerHelper.Weekday.Saturday;
	        break;
	      case R.id.sunday_button:
	    	  day = WallpaperChangerHelper.Weekday.Sunday;
	        break;
	      }
		
		
        // Create a bundle object
        Bundle b = new Bundle();
        b.putString(WallpaperChangerHelper.DAY, day.name());
 
        // Add the bundle to the intent.
        intent.putExtras(b);
 
        // start the ResultActivity
        startActivity(intent);
		
	}
}
