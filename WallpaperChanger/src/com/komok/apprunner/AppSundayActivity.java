package com.komok.apprunner;

import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.BaseHelper.Weekday;


public class AppSundayActivity extends AbstractAppSetterActivity {

	@Override
	protected ApplicationHolder getApp() {
		return  BaseHelper.loadApp(this, getDay());
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.Sunday;
	}

}
