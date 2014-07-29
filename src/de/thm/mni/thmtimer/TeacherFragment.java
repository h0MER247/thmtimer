package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.ModuleDAOListener;
import de.thm.thmtimer.entities.Course;
import android.app.Activity;
import android.content.Intent;
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

public class TeacherFragment extends Fragment implements ModuleDAOListener{

	private final String TAG = TeacherFragment.class.getSimpleName();

	private final int REQUEST_CREATE_COURSE = 1;
	private final int DAO_REQUEST_ADD_COURSE = 0;
	private final int DAO_REQUEST_TEACHER_COURSELIST = 1;
	private final int DAO_REQUEST_FULL_COURSELIST = 2;

	private TeacherCourseListAdapter mAdapter;
	private List<Course> mViewData;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mViewData == null)
			mViewData = new ArrayList<Course>();

		if (mAdapter == null)
			mAdapter = new TeacherCourseListAdapter(savedInstanceState);

		mViewData.clear();
		mViewData.addAll(ModuleDAO.getTeacherCourseList());

		mAdapter.notifyDataSetInvalidated();
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
			startActivityForResult(intent, REQUEST_CREATE_COURSE);
			return true;

		default:
			return false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.teacherfragment, container, false);

		ListView listView = (ListView) view.findViewById(R.id.teacherModules);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Course course = mAdapter.getItem(position);

				Intent intent = new Intent(getActivity(),
						TeacherCourseDetailActivity.class);
				intent.putExtra("course_id", course.getId());
				startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK)
			return;

		if (requestCode == REQUEST_CREATE_COURSE) {
			if (resultCode == Activity.RESULT_OK) {

				Long termID = data.getExtras().getLong("termID");
				Long moduleID = data.getExtras().getLong("moduleID");
				Long startDate = data.getExtras().getLong("startDate");
				String name = data.getExtras().getString("name");
				String description = data.getExtras().getString("description");

				ModuleDAO.beginJob();
				ModuleDAO.addCourse(DAO_REQUEST_ADD_COURSE,
						termID, moduleID, name, startDate, description);
				ModuleDAO.invalidateTeacherCourseList();
				ModuleDAO.getTeacherCourseListFromServer(DAO_REQUEST_TEACHER_COURSELIST);
				ModuleDAO.invalidateFullCourseList();
				ModuleDAO.getFullCourseListFromServer(DAO_REQUEST_FULL_COURSELIST);
				ModuleDAO.commitJob(getActivity(), this);
			}
		}
	}

	@Override
	public void onDAOError(int requestID, String errorMessage) {

		switch (requestID) {

		case DAO_REQUEST_ADD_COURSE:
			Toast.makeText(getActivity(),
					"Fehler beim erstellen eines Kurses: " + errorMessage,
					Toast.LENGTH_LONG).show();
			break;

		case DAO_REQUEST_TEACHER_COURSELIST:
		case DAO_REQUEST_FULL_COURSELIST:
			Toast.makeText(getActivity(),
					"Fehler beim Lesen der Kursliste: " + errorMessage,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public void onDAOFinished() {

		mViewData.clear();
		mViewData.addAll(ModuleDAO.getTeacherCourseList());

		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Adapter für das ListView
	 */
	private class TeacherCourseListAdapter extends ArrayAdapter<Course> {

		private Bundle mBundle;

		public TeacherCourseListAdapter(Bundle bundle) {

			super(getActivity(), R.layout.teacherlistitem, mViewData);

			mBundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = getLayoutInflater(mBundle).inflate(
						R.layout.teacherlistitem, parent, false);
			}

			final Course course = getItem(position);

			TextView name = (TextView) convertView
					.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView
					.findViewById(R.id.subtext);

			name.setText(course.getName());
			subtext.setText(String.format("%d %s", course.getUsers().size(),
					getString(R.string.students)));

			return convertView;
		}
	}
}