package de.thm.mni.thmtimer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import de.thm.mni.thmtimer.model.*;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTrackingActivity extends ListActivity {
	protected static final int REQUEST_NEW = 1;
	ArrayAdapter<TimeTracking> adapter;
	Module module;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		int moduleID = extras.getInt("module_id");
		this.module = StaticModuleData.findModule(moduleID);

		this.adapter = new ArrayAdapter<TimeTracking>(this,
				android.R.layout.simple_list_item_1, this.module.getTimeTracking());

		this.setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.studentfragment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(this, TrackTimeActivity.class);
			intent.putExtra("module_id",  this.module.getID());
			startActivityForResult(intent, REQUEST_NEW);
			return true;
		default:
			return false;
		}
	}

}
