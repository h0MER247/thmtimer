package de.thm.mni.thmtimer;

import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TeacherFragment extends Fragment {
	private ListAdapter adapter;
	private List<Module> data;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(data == null) {
			data = StaticModuleData.data;
		}
		if(adapter==null) {
			adapter = new TeacherModuleListAdapter(savedInstanceState);
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
			
			if(convertView == null) {
				
				convertView = getLayoutInflater(bundle).inflate(R.layout.teacherlistitem, parent, false);
			}
			
			
			final Module module = data.get(position);
			
			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);
			
			name.setText(module.getName());
			subtext.setText(module.getStudentCount()+" "+getString(R.string.students));
			return convertView;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacherfragment, container, false);

		ListView listView = (ListView)view.findViewById(R.id.teacherModules);

		listView.setAdapter(new TeacherModuleListAdapter(savedInstanceState));
		
		return view;
	}
}
