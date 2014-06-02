package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.content.Intent;
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

public class StudentFragment extends Fragment {
	private StudentCourseListAdapter mAdapter;
	private List<Long> mData;

	protected static final int REQUEST_NEW = 1;
	protected static final int REQUEST_TIMETRACKING = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mData == null) {

			mData = new ArrayList<Long>();
			mData.addAll(StaticModuleData.getStudentData().getCourseIDs());
		}
		if (mAdapter == null) {

			mAdapter = new StudentCourseListAdapter(savedInstanceState);
			mAdapter.sort(new Comparator<Long>() {

				@Override
				public int compare(Long lhs, Long rhs) {
					return StaticModuleData
							.findCourse(lhs)
							.getName()
							.compareTo(
									StaticModuleData.findCourse(rhs).getName());
				}

			});
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

			final Course course = StaticModuleData.findCourse(mData
					.get(position));

			TextView name = (TextView) convertView
					.findViewById(R.id.moduleName);
			TextView time = (TextView) convertView
					.findViewById(R.id.timeInvested);
			TextView subtext = (TextView) convertView
					.findViewById(R.id.subtext);

			name.setText(course.getName());
			TimeData timeInvested = StaticModuleData.getStudentData()
					.getTimeInvestedTotal(course.getID());

			Date startDate = course.getStartDate();
			if (startDate != null) {
				// Time of the current semester
				long deltaDate = new Date().getTime() - startDate.getTime();
				double percentSemester = (deltaDate / (FOUR_MONTHS / 100.0)) / 100.0;
				int cp = StaticModuleData.findModule(course.getModuleID())
						.getCreditPoints();
				double thresholdMinutes = cp * 180.0 * percentSemester;
				double deltaMinutes = timeInvested.getTimeInMinutes()
						- thresholdMinutes;
				int red = 255, green = 255;
				if (deltaMinutes < 0) {
					green = (int) (255 + deltaMinutes);
					green = green < 0 ? 0 : green;
				} else {
					red = (int) (255 - deltaMinutes);
					red = red < 0 ? 0 : red;
				}
				GradientDrawable shape = (GradientDrawable) getResources()
						.getDrawable(R.drawable.circle);
				shape.setColor(Color.rgb(red, green, 0));
				time.setCompoundDrawablesWithIntrinsicBounds(null, null, shape,
						null);
				String op = "";
				if(deltaMinutes<0) {
					op = "-";
				}
				else {
					op = "+";
				}
				TimeData delta = new TimeData();
				delta.setTimeInMinutes(Math.abs((int)deltaMinutes));
				time.setText(op+delta.toString());
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
}
