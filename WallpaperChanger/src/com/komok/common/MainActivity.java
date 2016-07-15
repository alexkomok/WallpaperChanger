package com.komok.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.komok.apprunner.AppChangerActivity;
import com.komok.wallpaperchanger.LiveWallpaperChangerActivity;
import com.komok.wallpaperchanger.R;

public class MainActivity extends Activity implements OnClickListener {

	Button apps_button;
	Button wallpapers_button;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewsById();

		apps_button.setOnClickListener(this);
		wallpapers_button.setOnClickListener(this);

	}

	private void findViewsById() {
		apps_button = (Button) findViewById(R.id.apps_button);
		wallpapers_button = (Button) findViewById(R.id.wallpapers_button);
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();

		switch (v.getId()) {
		case R.id.apps_button:
			intent = new Intent(this, AppChangerActivity.class);
			break;
		case R.id.wallpapers_button:
			intent = new Intent(this, LiveWallpaperChangerActivity.class);
			break;

		}

		startActivity(intent);

	}
}
