package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
	
	private final int DAO_REQUEST_USER = 0;
	private final int DAO_REQUEST_STUDENT_COURSELIST = 1;
	private final int DAO_REQUEST_STUDENT_EXPENDITURES = 2;
	private final int DAO_REQUEST_TIMECATEGORYS = 3;
	private final int DAO_POST_EXPENDITURE = 4;
	
	private ArrayAdapter<Expenditure> mAdapter;
	private List<Expenditure> mTimeTrackingList;
	
	private Long mCourseID;
	private Course mCourse;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_timetracking);
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
				
				Toast.makeText(TimeTrackingActivity.this,
						       "TODO: Editierbarkeit?",
						       Toast.LENGTH_LONG).show();
			}
		});
		
		
		// Alle Ressourcen anfordern, die wir benötigen
		ModuleDAO.beginJob();
		ModuleDAO.getUserFromServer(DAO_REQUEST_USER);
		ModuleDAO.getStudentCourseListFromServer(DAO_REQUEST_STUDENT_COURSELIST);
		ModuleDAO.getStudentExpendituresFromServer(DAO_REQUEST_STUDENT_EXPENDITURES);
		ModuleDAO.getTimeCategorysFromServer(DAO_REQUEST_TIMECATEGORYS);
		ModuleDAO.commitJob(this, this);
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
		
		if(resultCode != Activity.RESULT_OK)
			return;
		
		
		switch(requestCode) {
		
		case REQUEST_ADD_TIMETRACKING:
			Long categoryID = data.getExtras().getLong("category_id");
			Integer timeInMinutes = data.getExtras().getInt("time");
			
			Category category = ModuleDAO.getTimeCategoryByID(categoryID);
			
			// Expenditure erstellen
			Expenditure e = new Expenditure();
			e.setCategory(category);
			e.setCourse(mCourse);
			e.setDescription("TODO"); // TODO
			e.setDuration(timeInMinutes.shortValue());
			e.setStart(new Date());
			e.setUser(ModuleDAO.getUser());
			e.setId(0);
			
			ModuleDAO.beginJob();
			ModuleDAO.postStudentExpenditureToServer(DAO_POST_EXPENDITURE, e);
			ModuleDAO.commitJob(this, this);
			
			mAdapter.notifyDataSetChanged();
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
		
		case DAO_REQUEST_USER:
			Toast.makeText(this,
				       String.format("Fehler beim Laden des Benutzers: %s", message),
				       Toast.LENGTH_LONG).show();
			break;
		
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
		
		finish();
	}
	
	@Override
	public void onDAOFinished() {
		
		mCourse = ModuleDAO.getStudentCourseByID(mCourseID);
		
		mTimeTrackingList.clear();
		mTimeTrackingList.addAll(ModuleDAO.getStudentExpendituresByCourseID(mCourseID));
		
		mAdapter.notifyDataSetChanged();
	}
	
	
	
	/**
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
			
			
			TextView category = (TextView)convertView.findViewById(R.id.expenditureCategory);
			TextView description = (TextView)convertView.findViewById(R.id.expenditureDescription);
			TextView duration = (TextView)convertView.findViewById(R.id.expenditureDuration);
			
			category.setText(expenditure.getCategory().getName());
			description.setText(expenditure.getDescription());
			duration.setText(t.toString());
			
			
			return convertView;
		}
	}
}