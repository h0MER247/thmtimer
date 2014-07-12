package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.util.AbstractAsyncFragment;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.model.CourseModel;
import android.content.Intent;
import android.os.AsyncTask;
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

public class TeacherFragment extends AbstractAsyncFragment {
	private static final int REQUEST_NEW = 1;
	private static final int REQUEST_CREATECOURSE = 2;
	private TeacherCourseListAdapter mAdapter;
	private List<Long> mData;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mData == null) {
			mData = new ArrayList<Long>();
		}
		if (mAdapter == null) {

			mAdapter = new TeacherCourseListAdapter(savedInstanceState);
			/*mAdapter.sort(new Comparator<Long>() {

				@Override
				public int compare(Long arg0, Long arg1) {
					return StaticModuleData
							.findCourse(arg0)
							.getName()
							.compareTo(
									StaticModuleData.findCourse(arg1).getName());
				}

			});*/
		}
		new TeacherCoursesTask().execute();
	}

	private class TeacherCourseListAdapter extends ArrayAdapter<Long> {

		private Bundle bundle;

		public TeacherCourseListAdapter(Bundle bundle) {

			super(getActivity(), R.layout.teacherlistitem, mData);
			this.bundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = getLayoutInflater(bundle).inflate(
						R.layout.teacherlistitem, parent, false);
			}

			final CourseModel course = ModuleDAO.findTeacherCourse(mData.get(position));

			TextView name = (TextView) convertView
					.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView
					.findViewById(R.id.subtext);

			name.setText(course.getName());
			subtext.setText(Integer.toString(course.getStudentCount()) + " "
					+ getString(R.string.students));

			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.teacherfragment, container, false);

		ListView listView = (ListView) view.findViewById(R.id.teacherModules);

		listView.setAdapter(new TeacherCourseListAdapter(savedInstanceState));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						TeacherCourseDetailActivity.class);
				intent.putExtra("course_id", mAdapter.getItem(position));
				startActivity(intent);
			}
		});

		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.teacherfragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intent = new Intent(getActivity(), EnterModuleActivity.class);
			intent.putExtra("fragment", "teacher");
			startActivityForResult(intent, REQUEST_NEW);
			return true;
		default:
			return false;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CREATECOURSE) {
			mAdapter.notifyDataSetChanged();
			((ModuleListActivity)getActivity()).refresh();
		}
	}
	
	protected void displayResponse() {
		mData.addAll(ModuleDAO.getStudentCourseIDs());
		mAdapter.notifyDataSetChanged();
		//((ModuleListActivity) getActivity()).refresh();
	}
	
	private class TeacherCoursesTask extends AsyncTask<Void, Void, List<CourseModel>> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog();
		}
		
		@Override
		protected List<CourseModel> doInBackground(Void... params) {
			return ModuleDAO.getTeacherCourseList();
		}
		
		@Override
		protected void onPostExecute(List<CourseModel> result) {
			dismissProgressDialog();
			displayResponse();
		}
	}
}
