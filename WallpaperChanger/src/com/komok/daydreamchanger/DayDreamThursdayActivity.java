package com.komok.daydreamchanger;

import com.komok.common.AbstractAppSetterActivity;
import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.BaseHelper.Weekday;


public class DayDreamThursdayActivity extends AbstractAppSetterActivity {

	@Override
	protected ApplicationHolder getApp() {
		return  BaseHelper.loadDream(this, getDay());
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.Thursday;
	}

}
