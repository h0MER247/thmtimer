package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.ModuleDAOListener;
import de.thm.thmtimer.entities.Course;


public class StudentFragment extends Fragment implements ModuleDAOListener {
	
	private final String TAG = StudentFragment.class.getSimpleName();
	
	private final int REQUEST_ADD_COURSE = 1;
	private final int REQUEST_TIMETRACKING = 2;
	
	private final int DAO_REQUEST_STUDENT_COURSELIST = 0;
	private final int DAO_ADD_STUDENT_TO_COURSE = 1;
	
	private StudentCourseListAdapter mAdapter;
	private List<Course> mViewData;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		
		if(mViewData == null)
			mViewData = new ArrayList<Course>();
		
		if(mAdapter == null)
			mAdapter = new StudentCourseListAdapter(savedInstanceState);
		
		
		// Alle Ressourcen anfordern, die wir benötigen
		ModuleDAO.beginJob();
		ModuleDAO.getStudentCourseListFromServer(DAO_REQUEST_STUDENT_COURSELIST);
		ModuleDAO.commitJob(getActivity(), this);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.studentfragment, menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.action_add:
			Intent intent = new Intent(getActivity(),
					                   EnterModuleActivity.class);
			intent.putExtra("fragment", "student");
			startActivityForResult(intent, REQUEST_ADD_COURSE);
			return true;
			
		default:
			return false;
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.studentfragment,
				                     container,
				                     false);
		
		ListView listView = (ListView)view.findViewById(R.id.studentModules);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					               View view,
					               int position,
					               long id) {
				
				final Course course = mAdapter.getItem(position);
				
				Intent intent = new Intent(getActivity(),
						                   TimeTrackingActivity.class);
				intent.putExtra("course_id", course.getId());
				startActivityForResult(intent, REQUEST_TIMETRACKING);
			}
		});
		
		return view;
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != Activity.RESULT_OK)
			return;

		switch(requestCode) {
		
		case REQUEST_TIMETRACKING:
			mAdapter.notifyDataSetChanged();
			
			/* Fix #11128 */
			if(getActivity() != null)
				((ModuleListActivity)getActivity()).refresh();
			break;
			
		/*
		case REQUEST_ADD_COURSE:
			Long courseID = data.getExtras().getLong("course_id");
			
			ModuleDAO.beginJob();
			ModuleDAO.addStudentToCourse(DAO_ADD_STUDENT_TO_COURSE, courseID);
			ModuleDAO.getStudentCourseListFromServer(DAO_REQUEST_STUDENT_COURSELIST);
			ModuleDAO.commitJob(this);
			break;
		*/
		}
	}
	
	
	
	@Override
	public void onDAOError(int requestID, String message) {
		
		switch(requestID) {
		
		case DAO_REQUEST_STUDENT_COURSELIST:
			Toast.makeText(getActivity(),
					       String.format("Fehler beim Laden der Studentenliste: %s", message),
					       Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	
	@Override
	public void onDAOFinished() {
		
		mViewData.clear();
		mViewData.addAll(ModuleDAO.getStudentCourseList());
		
		mAdapter.notifyDataSetInvalidated();
		
		if(mViewData.size() == 0) {
			
			Toast.makeText(getActivity(),
					       "Du scheinst noch keinem Kurs beigetreten zu sein.\nKurse fügst du mit dem + Zeichen hinzu!",
					       Toast.LENGTH_LONG).show();
		}
	}
	
	
	
	/**
	 * Adapter für das ListView
	 */
	private class StudentCourseListAdapter extends ArrayAdapter<Course> {

		private Bundle mBundle;
		private final long FOUR_MONTHS = 10368000000l;
		
		
		public StudentCourseListAdapter(Bundle bundle) {

			super(getActivity(), R.layout.studentlistitem, mViewData);
			
			mBundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView == null) {
				
				convertView = getLayoutInflater(mBundle).inflate(R.layout.studentlistitem,
						                                         parent,
						                                         false);
			}
			
			final Course course = getItem(position);
			

			TextView name = (TextView)convertView.findViewById(R.id.moduleName);
			TextView time = (TextView)convertView.findViewById(R.id.timeInvested);
			TextView subtext = (TextView)convertView.findViewById(R.id.subtext);

			// TODO: Stimmt auch net wirklich...
			name.setText(course.getName());
			subtext.setText(course.getLecturer().get(0).getLastName());
			
			// TODO: Serverteam muss investierte Zeit für einen Kurs verfügbar machen
			//       Möglich ist auch alle Expenditures abrufen und zusammenzählen -.-
			//
			TimeData timeInvested = new TimeData();
			timeInvested.setTimeInHours(123);// StaticModuleData.getStudentData().getTimeInvestedTotal(course.getId());
			Date startDate = course.getStart();
			
			if (startDate != null) {
				
				GradientDrawable shape;
				SharedPreferences settings;
				
				// Read settings
				settings = getActivity().getSharedPreferences(SettingsFragment.FILE_NAME, 0);

				int minutesYellow = 60 * settings.getInt(SettingsFragment.HOURS_YELLOW,
						                                 SettingsFragment.VAL_HOURS_YELLOW);
				int minutesGreen = 60 * settings.getInt(SettingsFragment.HOURS_GREEN,
						                                SettingsFragment.VAL_HOURS_GREEN);
				int minutesRed = 60 * settings.getInt(SettingsFragment.HOURS_RED,
						                              SettingsFragment.VAL_HOURS_RED);
				
				// Time of the current semester
				int cp = course.getModule().getCreditPoints();
				long deltaDate = new Date().getTime() - startDate.getTime();
				double percentSemester = (deltaDate / (FOUR_MONTHS / 100.0)) / 100.0;
				double thresholdMinutes = cp * 1800.0 * percentSemester;
				double deltaMinutes = timeInvested.getTimeInMinutes() - thresholdMinutes;
				
				int red = 255, green = 255;
				
				if (deltaMinutes < minutesYellow) {
					
					double percentGreen = (minutesYellow - deltaMinutes) /
							              (minutesYellow - minutesRed);
					
					percentGreen = Math.abs(percentGreen);
					percentGreen = percentGreen > 1.0 ? 0.0 : 1.0 - percentGreen;
					green = (int) (255 * percentGreen);
					green = green < 0 ? 0 : green;
				}
				else {
					
					double percentRed = (deltaMinutes - minutesYellow) /
							            (minutesGreen - minutesYellow);
					
					percentRed = Math.abs(percentRed);
					percentRed = percentRed > 1.0 ? 0.0 : 1.0 - percentRed;
					red = (int) (255 * percentRed);
					red = red < 0 ? 0 : red;
				}
				
				shape = (GradientDrawable)getResources().getDrawable(R.drawable.circle);
				shape.setColor(Color.rgb(red, green, 0));
				
				time.setText(String.format("%dh", (int)(deltaMinutes / 60.0)));
				time.setCompoundDrawablesWithIntrinsicBounds(null,
						                                     null,
						                                     shape,
						                                     null);
			}
			
			return convertView;
		}
	}
}