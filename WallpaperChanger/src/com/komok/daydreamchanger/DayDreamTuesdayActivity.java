package com.komok.daydreamchanger;

import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.BaseHelper.Weekday;


public class DayDreamTuesdayActivity extends AbstractDreamSetterActivity {

	@Override
	protected ApplicationHolder getApp() {
		return  BaseHelper.loadLiveWallpaper(this, getDay());
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.Tuesday;
	}

}
