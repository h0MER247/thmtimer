package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import de.thm.mni.thmtimer.StopwatchDialog.StopwatchListener;
import de.thm.mni.thmtimer.util.AbstractAsyncListActivity;
import de.thm.mni.thmtimer.util.Connection;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.StaticModuleData;
import de.thm.thmtimer.entities.Expenditure;
import de.thm.thmtimer.entities.User;

public class TimeTrackingActivity extends AbstractAsyncListActivity implements StopwatchListener {
	
	private final int REQUEST_NEW = 1;
	
	private ArrayAdapter<Expenditure> mAdapter;
	private List<Expenditure> mTimeTrackingList;
	private Long mCourseID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		mCourseID = extras.getLong("course_id");
		mTimeTrackingList = new ArrayList<Expenditure>();
		
		mAdapter = new ArrayAdapter<Expenditure>(this, android.R.layout.simple_list_item_1, mTimeTrackingList);
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
	
	private class TimeTrackingTask extends AsyncTask<Void, Void, List<Expenditure>> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog();
		}

		@Override
		protected List<Expenditure> doInBackground(Void... params) {
			try {
				return ModuleDAO.getExpendituresByCourse(mCourseID);
			} catch (HttpClientErrorException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				return null;
			} catch (ResourceAccessException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Expenditure> result) {
			mTimeTrackingList = result;
			mAdapter.notifyDataSetChanged();
			dismissProgressDialog();
		}
	}
	
	
}