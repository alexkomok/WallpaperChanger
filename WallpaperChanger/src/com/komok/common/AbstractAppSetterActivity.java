package com.komok.common;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.komok.wallpaperchanger.R;

abstract public class AbstractAppSetterActivity extends Activity {

	private static final String TAG = "AbstractAppSetterActivity";

	protected abstract ApplicationHolder getApp();

	protected abstract BaseHelper.Weekday getDay();

	@Override
	protected void onStart() {
		super.onStart();
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		ApplicationHolder app = getApp();

		if (app == null) {
			ExceptionHandler.caughtException(new Exception(getString(R.string.error_update_list) + " for: " + getDay().name()), this);
			return;
		}

/*		Intent intent = new Intent();

		try {
			intent = Intent.parseUri(app.getUri(), 0);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Failed to set app: " + e);
			ExceptionHandler.caughtException(e, this);
		}
		startActivity(intent);
		*/
		
		Intent intent = new Intent();

		try {
			intent = Intent.parseUri(app.getUri(), 0);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Failed to set app: " + e);
			ExceptionHandler.caughtException(e, this);
		}
		
		
/*		ServiceConnection mConnection = new ServiceConnection() {
			  
			  public void onServiceDisconnected(ComponentName name) {
			   Toast.makeText(AbstractAppSetterActivity.this, "Service is disconnected", 1000).show();

			  }
			  
			  public void onServiceConnected(ComponentName name, IBinder service) {
			   Toast.makeText(AbstractAppSetterActivity.this, "Service is connected", 1000).show();
			  }
			 };
		
		bindService(intent, mConnection, BIND_AUTO_CREATE);*/
		//IBinder binder = (IBinder) this.getSystemService("dreams");
		
		intent = new Intent(Intent.ACTION_MAIN);

            // Somnabulator is undocumented--may be removed in a future version...
            intent.setClassName("com.android.systemui",
                                "com.android.systemui.Somnambulator");
            startActivity(intent);
		//startService(intent);	
		
		
		


	}

}
