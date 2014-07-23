package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.util.AbstractAsyncFragment;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Course;
import android.content.Intent;
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
import android.widget.Toast;


public class TeacherFragment extends AbstractAsyncFragment {
	
	private final String TAG = TeacherFragment.class.getSimpleName();
	
	private final int REQUEST_NEW = 1;
	private final int REQUEST_CREATECOURSE = 2;
	
	private final int DAO_REQUEST_TEACHER_COURSELIST = 0;
	
	private TeacherCourseListAdapter mAdapter;
	private List<Course> mViewData;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		
		if(mViewData == null)
			mViewData = new ArrayList<Course>();
		
		if(mAdapter == null)
			mAdapter = new TeacherCourseListAdapter(savedInstanceState);
		
		
		ModuleDAO.beginJob();
		ModuleDAO.getTeacherCourseListFromServer(DAO_REQUEST_TEACHER_COURSELIST);
		ModuleDAO.commitJob(this);
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
			Intent intent = new Intent(getActivity(),
					                   EnterModuleActivity.class);
			intent.putExtra("fragment", "teacher");
			startActivityForResult(intent, REQUEST_NEW);
			return true;
			
		default:
			return false;
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.teacherfragment,
				                     container,
				                     false);
		
		ListView listView = (ListView) view.findViewById(R.id.teacherModules);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					                View view,
					                int position,
					                long id) {
				
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
		
		if (requestCode == REQUEST_CREATECOURSE) {
			
			mAdapter.notifyDataSetChanged();
			
			/* Fix #11128 */
			if(getActivity() != null)
				((ModuleListActivity)getActivity()).refresh();
		}
	}
	
	
	
	@Override
	public void onDAOError(int requestID, String message) {
		
		switch(requestID) {
		
		case DAO_REQUEST_TEACHER_COURSELIST:
			Toast.makeText(getActivity(),
					       String.format("Fehler beim Laden der Dozentenliste: %s", message),
					       Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	@Override
	public void onDAOFinished() {
		
		mViewData.clear();
		mViewData.addAll(ModuleDAO.getTeacherCourseList());
	
		mAdapter.notifyDataSetInvalidated();
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

				convertView = getLayoutInflater(mBundle).inflate(R.layout.teacherlistitem,
						                                         parent,
						                                         false);
			}

			final Course course = getItem(position);
			
			TextView name = (TextView)convertView.findViewById(R.id.moduleName);
			TextView subtext = (TextView)convertView.findViewById(R.id.subtext);

			name.setText(course.getName());
			subtext.setText(String.format("%d %s",
					                      course.getUsers().size(),
					                      getString(R.string.students)));
			
			return convertView;
		}
	}
}