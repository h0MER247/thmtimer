package de.thm.mni.thmtimer;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import de.thm.mni.thmtimer.StopwatchDialog.StopwatchListener;
import de.thm.mni.thmtimer.model.*;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTrackingActivity extends ListActivity implements StopwatchListener {
	
	private final int REQUEST_NEW = 1;
	
	private ArrayAdapter<TimeTracking> mAdapter;
	private List<TimeTracking> mTimeTrackingList;
	private Long mCourseID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		mCourseID = extras.getLong("course_id");
		
		mTimeTrackingList = StaticModuleData.getStudentData().getTimeTrackingData(mCourseID);
		
		mAdapter = new ArrayAdapter<TimeTracking>(this, android.R.layout.simple_list_item_1, this.mTimeTrackingList);
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.timetrackingactivity, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.action_stopwatch:
			StopwatchDialog dialog = new StopwatchDialog();
			dialog.setListener(this);
			dialog.show(getFragmentManager(), "STOPWATCH");
			return true;
			
		case R.id.action_add:
			Intent intent = new Intent(this, TrackTimeActivity.class);
			intent.putExtra("course_id", mCourseID);
			startActivityForResult(intent, REQUEST_NEW);
			return true;
			
		case android.R.id.home:
			finish();
			return true;
			
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != Activity.RESULT_OK) {
			return;
		}
		
		if(requestCode == REQUEST_NEW) {
			mAdapter.notifyDataSetChanged();
			setResult(Activity.RESULT_OK);
		}
	}

	@Override
	public void onStoppedTime(Integer timeInMinutes) {
		
		Intent intent = new Intent(this, TrackTimeActivity.class);
		intent.putExtra("course_id", mCourseID);
		intent.putExtra("stopped_time", timeInMinutes); 
		
		startActivityForResult(intent, REQUEST_NEW);
	}
}