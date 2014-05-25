package de.thm.mni.thmtimer;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import de.thm.mni.thmtimer.model.*;
import de.thm.mni.thmtimer.model.Stopwatch.AsyncStopwatchListener;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTrackingActivity extends ListActivity implements AsyncStopwatchListener {
	
	private final int REQUEST_NEW = 1;
	private final int REQUEST_STOPWATCH = 2;
	private ArrayAdapter<TimeTracking> mAdapter;
	private Long mCourseID;
	private List<TimeTracking> mTimeTrackingList;
	private Stopwatch mStopwatch;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		mCourseID = extras.getLong("course_id");
		
		mTimeTrackingList = StaticModuleData.getStudentData().getTimeTrackingData(mCourseID);
		mStopwatch = StaticModuleData.getStudentData().getStopwatch(mCourseID);
		mStopwatch.setListener(this);
		
		mAdapter = new ArrayAdapter<TimeTracking>(this, android.R.layout.simple_list_item_1, this.mTimeTrackingList);
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		getMenuInflater().inflate(R.menu.timetrackingactivity, menu);
		
		// Set stopwatch icon depending on the running state
		MenuItem stopwatch = menu.findItem(R.id.action_stopwatch);
		
		stopwatch.setIcon(mStopwatch.isRunning() ? R.drawable.ic_action_stop :
			                                       R.drawable.ic_action_start);
		
		stopwatch.setTitle(mStopwatch.isRunning() ? R.string.action_stopwatch_stop :
			                                        R.string.action_stopwatch_start);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.action_stopwatch:
			if(!mStopwatch.isRunning()) {
				mStopwatch.start();
			} else {
				mStopwatch.stop();
			}
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
		
		switch(requestCode) {
		case REQUEST_STOPWATCH:
			invalidateOptionsMenu();
		case REQUEST_NEW:
			if(resultCode == Activity.RESULT_OK) {
				mAdapter.notifyDataSetChanged();
				setResult(Activity.RESULT_OK); // <-- für was/wen ist dieses result?
			}
			break;
		}
	}
	
	@Override
	public void onStopwatchStart() {
		
		invalidateOptionsMenu();
	}

	@Override
	public void onStopwatchStop(Integer stoppedTime) {
		
		Intent intent = new Intent(this, TrackTimeActivity.class);
		intent.putExtra("course_id", mCourseID);
		intent.putExtra("stopped_time", stoppedTime); 
		
		startActivityForResult(intent, REQUEST_STOPWATCH);
	}
	
	@Override
	public void onServerError(String message) {
		
		Toast.makeText(getApplicationContext(), "Server Error: " + message, Toast.LENGTH_LONG).show();
	}
}