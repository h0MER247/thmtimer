package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import de.thm.mni.thmtimer.model.CourseModel;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.AbstractAsyncFragment;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class StudentFragment extends AbstractAsyncFragment {
	private StudentCourseListAdapter mAdapter;
	private List<Long> mData;
	private boolean mHasFetchedData = false;

	protected static final int REQUEST_NEW = 1;
	protected static final int REQUEST_TIMETRACKING = 2;
	
	protected static final String TAG = StudentFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mData == null) {
			mData = new ArrayList<Long>();
			if (!mHasFetchedData)
				new StudentCoursesTask().execute();
			else
				mData.addAll(ModuleDAO.getStudentCourseIDs());
		}
		if (mAdapter == null) {
			mAdapter = new StudentCourseListAdapter(savedInstanceState);
			/*mAdapter.sort(new Comparator<Long>() {

				@Override
				public int compare(Long lhs, Long rhs) {
					return StaticModuleData
							.findCourse(lhs)
							.getName()
							.compareTo(
									StaticModuleData.findCourse(rhs).getName());
				}

			});*/
		}
	}

	private class StudentCourseListAdapter extends ArrayAdapter<Long> {

		private Bundle bundle;
		private final long FOUR_MONTHS = 10368000000l;

		public StudentCourseListAdapter(Bundle bundle) {

			super(getActivity(), R.layout.studentlistitem, mData);
			this.bundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = getLayoutInflater(bundle).inflate(
						R.layout.studentlistitem, parent, false);
			}

			final CourseModel course = ModuleDAO.findStudentCourse(mData.get(position));

			TextView name = (TextView) convertView
					.findViewById(R.id.moduleName);
			TextView time = (TextView) convertView
					.findViewById(R.id.timeInvested);
			TextView subtext = (TextView) convertView
					.findViewById(R.id.subtext);

			name.setText(course.getName());
			TimeData timeInvested = StaticModuleData.getStudentData()
					.getTimeInvestedTotal(course.getId());

			Date startDate = course.getStartDate();
			if (startDate != null) {
				// Time of the current semester
				long deltaDate = new Date().getTime() - startDate.getTime();
				double percentSemester = (deltaDate / (FOUR_MONTHS / 100.0)) / 100.0;
				int cp = StaticModuleData.findModule(course.getModuleID())
						.getCreditPoints();
				SharedPreferences settings = getActivity()
						.getSharedPreferences(SettingsFragment.FILE_NAME, 0);
				int minutesYellow = 60 * settings.getInt(
						SettingsFragment.HOURS_YELLOW,
						SettingsFragment.VAL_HOURS_YELLOW);
				double thresholdMinutes = cp * 1800.0 * percentSemester;
				double deltaMinutes = timeInvested.getTimeInMinutes()
						- thresholdMinutes;
				int red = 255, green = 255;
				int minutesGreen = 60 * settings.getInt(
						SettingsFragment.HOURS_GREEN,
						SettingsFragment.VAL_HOURS_GREEN);
				int minutesRed = 60 * settings.getInt(
						SettingsFragment.HOURS_RED,
						SettingsFragment.VAL_HOURS_RED);
				if (deltaMinutes < minutesYellow) {
					double percentGreen = (minutesYellow-deltaMinutes) / (minutesYellow-minutesRed);
					percentGreen = Math.abs(percentGreen);
					percentGreen = percentGreen > 1.0 ? 0.0
							: 1.0 - percentGreen;
					green = (int) (255 * percentGreen);
					green = green < 0 ? 0 : green;
				} else {
					double percentRed = (deltaMinutes-minutesYellow) / (minutesGreen-minutesYellow);
					percentRed = Math.abs(percentRed);
					percentRed = percentRed > 1.0 ? 0.0
							: 1.0 - percentRed;
					red = (int) (255 * percentRed);
					red = red < 0 ? 0 : red;
				}
				GradientDrawable shape = (GradientDrawable) getResources()
						.getDrawable(R.drawable.circle);
				shape.setColor(Color.rgb(red, green, 0));
				time.setCompoundDrawablesWithIntrinsicBounds(null, null, shape,
						null);
				String op = "";
				if (deltaMinutes < 0) {
					op = "-";
				} else {
					op = "+";
				}
				TimeData delta = new TimeData();
				delta.setTimeInMinutes(Math.abs((int) deltaMinutes));
				time.setText(op + delta.getTimeInHours() + "h");
			}
			subtext.setText(course.getTeacher());

			return convertView;
		}

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
			Intent intent = new Intent(getActivity(), EnterModuleActivity.class);
			intent.putExtra("fragment", "student");
			startActivityForResult(intent, REQUEST_NEW);
			return true;
		default:
			return false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.studentfragment, container, false);

		ListView listView = (ListView) view.findViewById(R.id.studentModules);

		listView.setAdapter(new StudentCourseListAdapter(savedInstanceState));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						TimeTrackingActivity.class);

				intent.putExtra("course_id", mAdapter.getItem(position));
				startActivityForResult(intent, REQUEST_TIMETRACKING);
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TIMETRACKING) {
			mAdapter.notifyDataSetChanged();
			((ModuleListActivity) getActivity()).refresh();
		}
	}
	
	protected void displayResponse() {
		Log.d(TAG, "Number of Courses loaded: "+ ModuleDAO.getStudentCourseIDs());
		mHasFetchedData = true;
		mData.addAll(ModuleDAO.getStudentCourseIDs());
		mAdapter.notifyDataSetChanged();
		((ModuleListActivity) getActivity()).refresh();
	}
	
	private class StudentCoursesTask extends AsyncTask<Void, Void, List<CourseModel>> {

		@Override
		protected void onPreExecute() {
			showProgressDialog(getString(R.string.connection_loading_courses));
		}
		
		@Override
		protected List<CourseModel> doInBackground(Void... params) {
			return ModuleDAO.getStudentCourseList();
		}
		
		@Override
		protected void onPostExecute(List<CourseModel> result) {
			dismissProgressDialog();
			displayResponse();
		}
	}
}
