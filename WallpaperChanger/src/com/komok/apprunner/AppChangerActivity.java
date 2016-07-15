package com.komok.apprunner;

import com.komok.common.AbstractChangerActivity;


public class AppChangerActivity extends AbstractChangerActivity<AppSelectionActivity> {

	@Override
	public Class<AppSelectionActivity> getSelectionActivityClass() {
		return AppSelectionActivity.class;
	}
	

}
