package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.ModuleComparator;
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
	
	private TeacherModuleListAdapter adapter;
	private List<Module> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(data == null) {
			data = new ArrayList<Module>();
			for(Long l:StaticModuleData.getTeacherData().getCourseIDs()) {
				data.add(StaticModuleData.findModule(StaticModuleData.findCourse(l).getModuleID()));
			}
		}
		if(adapter == null) {
			
			adapter = new TeacherModuleListAdapter(savedInstanceState);
			adapter.sort(new ModuleComparator());
		}
	}

	private class TeacherModuleListAdapter extends ArrayAdapter<Module> {

		private Bundle bundle;

		public TeacherModuleListAdapter(Bundle bundle) {
			
			super(getActivity(), R.layout.teacherlistitem, data);
			this.bundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				
				convertView = getLayoutInflater(bundle).inflate(R.layout.teacherlistitem, parent, false);
			}
			

			final Module module = data.get(position);

			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);

			name.setText(module.getName());
			int studentCount = 0;
			for(Course c:module.getCourseList()) {
				studentCount+=c.getStudentCount();
			}
			subtext.setText(Integer.toString(studentCount) + " " + getString(R.string.students));
			
			
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

				intent.putExtra("course_id", adapter.getItem(position).getCourseList().get(0).getID());
				startActivity(intent);
			}
		});
		
		return view;
	}
}
