package de.thm.mni.thmtimer;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import de.thm.mni.thmtimer.model.*;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTrackingActivity extends ListActivity {
	
	ArrayAdapter<TimeTracking> adapter;
	Module module;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		int moduleID = extras.getInt("module_id");
		this.module = StaticModuleData.findModule(moduleID);
		
		this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, 
				this.module.getTimeTracking());
		
		
		this.setListAdapter(adapter);
	}

}
