package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import de.thm.mni.thmtimer.StopwatchDialog.StopwatchListener;
import de.thm.mni.thmtimer.model.CourseModel;
import de.thm.mni.thmtimer.util.AbstractAsyncListActivity;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Expenditure;


public class TimeTrackingActivity extends AbstractAsyncListActivity implements StopwatchListener {
	
	private final int REQUEST_ADD_TIMETRACKING = 0;
	private final int REQUEST_EDIT_TIMETRACKING = 1;
	
	private final int DAO_REQUEST_STUDENT_EXPENDITURES = 0;
	private final int DAO_REQUEST_TIMECATEGORYS = 1;
	private final int DAO_POST_EXPENDITURE = 2;
	
	private ArrayAdapter<Expenditure> mAdapter;
	private List<Expenditure> mTimeTrackingList;
	
	private Long mCourseID;
	private CourseModel mCourse;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		if(mTimeTrackingList == null)
			mTimeTrackingList = new ArrayList<Expenditure>();
		
		if(mAdapter == null)
			mAdapter = new ArrayAdapter<Expenditure>(this,
					                                 android.R.layout.simple_list_item_1,
					                                 mTimeTrackingList);
		setListAdapter(mAdapter);		
		
		
		mCourseID = getIntent().getExtras().getLong("course_id");
		mCourse = ModuleDAO.getStudentCourseByID(mCourseID);
		
		
		ModuleDAO.setJobSize(2);
		ModuleDAO.loadStudentExpendituresFromServer(this, DAO_REQUEST_STUDENT_EXPENDITURES);
		ModuleDAO.loadTimeCategorysFromServer(this, DAO_REQUEST_TIMECATEGORYS);
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
			startActivityForResult(intent, REQUEST_ADD_TIMETRACKING);
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
		switch(requestCode) {
		
		case REQUEST_ADD_TIMETRACKING:
			Long categoryID = data.getExtras().getLong("category_id");
			Integer timeInMinutes = data.getExtras().getInt("time");
			
			Category category = ModuleDAO.getTimeCategoryByID(categoryID);
			
			// Expenditure erstellen
			Expenditure e = new Expenditure();
			e.setCourse(mCourse);
			e.setCategory(category);
			e.setDescription("Test");
			e.setDuration(timeInMinutes.shortValue());
			e.setStart(new Date());
			e.setUser(ModuleDAO.getUser());
			
			ModuleDAO.setJobSize(1);
			ModuleDAO.postStudentExpenditureToServer(this, DAO_POST_EXPENDITURE, e);
			
			Log.d("LOG", String.format("Category: %d, Time: %d", categoryID, timeInMinutes));
			
			mAdapter.notifyDataSetChanged();
			setResult(Activity.RESULT_OK);
			break;
			
		case REQUEST_EDIT_TIMETRACKING:
			break;
		}
	}

	@Override
	public void onStoppedTime(Integer timeInMinutes) {
		
		Intent intent = new Intent(this, TrackTimeActivity.class);
		intent.putExtra("course_id", mCourseID);
		intent.putExtra("stopped_time", timeInMinutes); 
		
		startActivityForResult(intent, REQUEST_ADD_TIMETRACKING);
	}
	
	

	@Override
	public void onDAOError(int requestID, String message) {
		
		switch(requestID) {
		
		case DAO_REQUEST_STUDENT_EXPENDITURES:
			Toast.makeText(this,
					       String.format("Fehler beim Laden der Aktivitäten: %s", message),
					       Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_TIMECATEGORYS:
			Toast.makeText(this,
				           String.format("Fehler beim Laden der Zeitkategorien: %s", message),
				           Toast.LENGTH_LONG).show();
			break;
			
		case DAO_POST_EXPENDITURE:
			Toast.makeText(this,
			           String.format("Fehler beim Speichern der Aktivität: %s", message),
			           Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void onDAOSuccess(int requestID) {
		
		switch(requestID) {
		
		case DAO_REQUEST_STUDENT_EXPENDITURES:
			mTimeTrackingList.clear();
			mTimeTrackingList.addAll(ModuleDAO.getStudentExpendituresByCourseID(mCourseID));
			break;
			
		case DAO_REQUEST_TIMECATEGORYS:
			Log.d("LOG", "Loaded " + ModuleDAO.getTimeCategorys().size());
			break;
			
		case DAO_POST_EXPENDITURE:
			break;
		}
	}

	@Override
	public void onDAOFinished() {
		
		mAdapter.notifyDataSetChanged();
	}
}