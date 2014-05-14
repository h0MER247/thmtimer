package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class TeacherFragment extends Fragment {
	
	private ListAdapter adapter;
	private TeacherData data;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(data == null) {
			
			data = StaticModuleData.getTeacherData();
		}
		if(adapter == null) {
			
			adapter = new TeacherModuleListAdapter(savedInstanceState);
		}
	}

	private class TeacherModuleListAdapter extends ArrayAdapter<Long> {

		private Bundle bundle;

		public TeacherModuleListAdapter(Bundle bundle) {
			
			super(getActivity(), R.layout.teacherlistitem, data.getCourseIDs());
			this.bundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				
				convertView = getLayoutInflater(bundle).inflate(R.layout.teacherlistitem, parent, false);
			}
			

			final Long courseID = data.getCourseIDs().get(position);
			final Course course = StaticModuleData.findCourse(courseID);

			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);

			name.setText(course.getName());
			subtext.setText(course.getStudentCount().toString() + " " + getString(R.string.students));
			
			
			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.teacherfragment, container, false);

		ListView listView = (ListView) view.findViewById(R.id.teacherModules);

		listView.setAdapter(new TeacherModuleListAdapter(savedInstanceState));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Intent intent = new Intent(getActivity(), TeacherCourseDetailActivity.class);
				
				//Toast.makeText(getActivity().getApplicationContext(), "Touched ID " + ((Long)adapter.getItem(position)).toString(), Toast.LENGTH_LONG).show();

				intent.putExtra("course_id", (Long)adapter.getItem(position));
				startActivity(intent);
			}
		});
		
		return view;
	}
}
