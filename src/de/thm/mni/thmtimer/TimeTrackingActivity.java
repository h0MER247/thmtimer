package de.thm.mni.thmtimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.thm.mni.thmtimer.StopwatchDialog.StopwatchListener;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.ModuleDAOListener;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.Expenditure;


public class TimeTrackingActivity extends Activity implements StopwatchListener, ModuleDAOListener {
	
	private final int REQUEST_ADD_TIMETRACKING = 0;
	private final int REQUEST_EDIT_TIMETRACKING = 1;
	
	private final int DAO_POST_EXPENDITURE = 0;
	private final int DAO_PUT_EXPENDITURE = 1;
	private final int DAO_DELETE_EXPENDITURE = 2;
	
	private ArrayAdapter<Expenditure> mAdapter;
	private List<Expenditure> mTimeTrackingList;
	
	private Long mCourseID;
	private Course mCourse;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		ModuleDAO.setNewContext(this, this);
		
		this.setContentView(R.layout.timetrackingactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		if(mTimeTrackingList == null)
			mTimeTrackingList = new ArrayList<Expenditure>();
		
		if(mAdapter == null)
			mAdapter = new ExpenditureAdapter(this,
					                          R.layout.expenditurelistitem,
					                          mTimeTrackingList);
		
		mCourseID = getIntent().getExtras().getLong("course_id");
		
		
		ListView listView = (ListView)findViewById(R.id.expenditureList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					                View view,
					                int position,
					                long id) {
				
				Intent intent = new Intent(TimeTrackingActivity.this,
						                   TrackTimeActivity.class);
				intent.putExtra("expenditure_id", mAdapter.getItem(position).getId());
				
				startActivityForResult(intent, REQUEST_EDIT_TIMETRACKING);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					                       View view,
					                       final int position,
					                       long id) {
				
				AlertDialog.Builder b;
				
				b = new AlertDialog.Builder(TimeTrackingActivity.this);
				
				b.setTitle(getString(R.string.timetracking_action_choose));
				b.setItems(new String[] { getString(R.string.timetracking_action_edit),
						                  getString(R.string.timetracking_action_delete),
						                  getString(R.string.timetracking_action_cancel) },
						   new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						final Expenditure e = mAdapter.getItem(position);
						
						switch(which) {
						
						// Edit
						case 0:
							Intent intent = new Intent(TimeTrackingActivity.this,
									                   TrackTimeActivity.class);
							intent.putExtra("expenditure_id", e.getId());
							startActivityForResult(intent, REQUEST_EDIT_TIMETRACKING);
							break;
						
						// Delete
						case 1:
							ModuleDAO.beginJob();
							ModuleDAO.deleteStudentExpenditureFromServer(DAO_DELETE_EXPENDITURE, e.getId());
							ModuleDAO.commitJob(TimeTrackingActivity.this,
									            TimeTrackingActivity.this);
							break;
							
						// Cancel:
						case 2:
							break;
						}
					}
				});
				
				b.create().show();
				
				return true;
			}
		});		
		
		mCourse = ModuleDAO.getStudentCourseByID(mCourseID);
		
		mTimeTrackingList.clear();
		mTimeTrackingList.addAll(ModuleDAO.getStudentExpendituresByCourseID(mCourseID));
		
		mAdapter.notifyDataSetChanged();
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
			startActivityForResult(intent, REQUEST_ADD_TIMETRACKING);
			return true;
			
		case android.R.id.home:
			setResult(RESULT_OK);
			finish();
			return true;
			
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != Activity.RESULT_OK)
			return;
		
		switch(requestCode) {
		
		case REQUEST_ADD_TIMETRACKING:
		case REQUEST_EDIT_TIMETRACKING:
			Long categoryID    = data.getExtras().getLong("category_id");
			Integer duration   = data.getExtras().getInt("duration");
			String description = data.getExtras().getString("description");
			Long startTime     = data.getExtras().getLong("start_time");
			Long expenditureID = data.getExtras().getLong("expenditure_id");
			
			Category category = ModuleDAO.getTimeCategoryByID(categoryID);
			
			// Expenditure erstellen
			Expenditure e = new Expenditure();
			
			e.setCategory(category);
			e.setCourse(mCourse);
			e.setDescription(description);
			e.setDuration(duration.shortValue());
			e.setStart(new Date(startTime));
			e.setUser(ModuleDAO.getUser());
			e.setId(expenditureID);
			
			// Expenditure speichern
			ModuleDAO.beginJob();
			if(requestCode == REQUEST_ADD_TIMETRACKING)
				ModuleDAO.postStudentExpenditureToServer(DAO_POST_EXPENDITURE, e);
			else
				ModuleDAO.putStudentExpenditureToServer(DAO_PUT_EXPENDITURE, e);
			ModuleDAO.commitJob(this, this);
			break;
		}
	}

	@Override
	public void onStoppedTime(Long startTime, Integer timeInSeconds) {
		
		Intent intent = new Intent(this, TrackTimeActivity.class);
		intent.putExtra("course_id", mCourseID);
		intent.putExtra("stopped_time", timeInSeconds);
		intent.putExtra("start_time", startTime);
		
		startActivityForResult(intent, REQUEST_ADD_TIMETRACKING);
	}
	
	

	@Override
	public void onDAOError(int requestID, String message) {
		
		switch(requestID) {
			
		case DAO_POST_EXPENDITURE:
			Toast.makeText(this,
			               String.format(getString(R.string.timetracking_error_save), message),
			               Toast.LENGTH_LONG).show();
			break;
			
		case DAO_PUT_EXPENDITURE:
			Toast.makeText(this,
					       String.format(getString(R.string.timetracking_error_edit), message),
			               Toast.LENGTH_LONG).show();
			break;
			
		case DAO_DELETE_EXPENDITURE:
			Toast.makeText(this,
					       String.format(getString(R.string.timetracking_error_delete), message),
					       Toast.LENGTH_LONG).show();
			break;
		}
		
		finish();
	}
	
	@Override
	public void onDAOFinished() {

		mTimeTrackingList.clear();
		mTimeTrackingList.addAll(ModuleDAO.getStudentExpendituresByCourseID(mCourseID));
		
		mAdapter.notifyDataSetChanged();
	}
	
	
	
	/**
	 * 
	 * 
	 */
	private class ExpenditureAdapter extends ArrayAdapter<Expenditure> {

		public ExpenditureAdapter(Context context,
				                  int resource,
				                  List<Expenditure> objects) {
			
			super(context, resource, objects);
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			if(convertView == null) {
				
				convertView = getLayoutInflater().inflate(R.layout.expenditurelistitem,
						                                  parent,
						                                  false);
			}

			final Expenditure expenditure = getItem(position);
			
			TimeData t = new TimeData();
			t.setTimeInMinutes(expenditure.getDuration());
			
			
			TextView categoryView  = (TextView)convertView.findViewById(R.id.expenditureCategory);
			TextView startDateView = (TextView)convertView.findViewById(R.id.expenditureStart);
			TextView durationView  = (TextView)convertView.findViewById(R.id.expenditureDuration);
			
			String category  = expenditure.getCategory().getName();
			String startDate = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
					                                                SimpleDateFormat.SHORT).format(expenditure.getStart());
			String duration  = t.toString();
			
			categoryView.setText(category);
			startDateView.setText(startDate);
			durationView.setText(duration);
			
			
			return convertView;
		}
	}
}