package com.komok.daydreamchanger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.komok.common.AbstractAppSetterActivity;
import com.komok.common.ApplicationHolder;
import com.komok.common.BaseHelper;
import com.komok.common.BaseHelper.Weekday;


public class DayDreamListActivity extends AbstractAppSetterActivity {

	@Override
	protected ApplicationHolder getApp() {
		Map<String, String> selectedDreamssMap = BaseHelper.loadDreamsMap(this, getDay().name());
		int size = selectedDreamssMap.size();
		int savedPosition = BaseHelper.loadDreamListPosition(this);
		
		if(size > 0){
			if(savedPosition < size){
				int nextPosition = savedPosition + 1 >= size ? 0 : savedPosition + 1;
				List<String> keys = new ArrayList<String>(selectedDreamssMap.keySet());
				Collections.reverse(keys);
				String label = keys.get(savedPosition);
				String uri = selectedDreamssMap.get(label);
				BaseHelper.saveDreamListPosition(nextPosition, this);
				return new ApplicationHolder(label, uri);
			}
			
		} 
		return null;
		
	}

	@Override
	protected Weekday getDay() {
		return BaseHelper.Weekday.List;
	}

}
