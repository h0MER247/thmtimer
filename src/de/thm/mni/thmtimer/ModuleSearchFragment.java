package de.thm.mni.thmtimer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.util.StaticModuleData;



public class ModuleSearchFragment extends Fragment {

	private ModuleListAdapter adapter;
	private ArrayList<Course> data;
	//private List<Module> data;
	private SearchView search;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		if(data == null) {
			
			data = StaticModuleData.getCourseList();
		}
		if (adapter == null) {
			
			adapter = new ModuleListAdapter(savedInstanceState);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.modulesearchfragment, container, false);
		
		search = (SearchView) view.findViewById(R.id.searchfragment);
		search.setQueryHint(getString(R.string.search_hint));
		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				search.clearFocus();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return true;
			}
		});
		search.setFocusable(false);
		search.setFocusableInTouchMode(false);

		ListView lv = (ListView) view.findViewById(R.id.searchlist);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View view, int pos, long id) {
				
				Activity a = getActivity();
				if (a instanceof EnterModuleActivity) {
					
					EnterModuleActivity ea = (EnterModuleActivity) a;
					
					//ea.closeSearch(data.get((int) id).getID());
					ea.closeSearch(adapter.getItem(pos).getID());
				}
			}
		});
		lv.setAdapter(adapter);
		return view;
	}
	
	
	//private class ModuleListAdapter extends ArrayAdapter<Module> {
	private class ModuleListAdapter extends ArrayAdapter<Course> {

		private Bundle bundle;
		//private List<Module> origData;

		public ModuleListAdapter(Bundle bundle) {
			super(getActivity(), R.layout.modulelistitem, data);
			this.bundle = bundle;

			//origData = new ArrayList<Module>(data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater(bundle).inflate(R.layout.modulelistitem, parent, false);
			}
			//final Module module = getItem(position);
			final Course course = getItem(position);
			
			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			//name.setText(module.getName());
			name.setText(course.getName());
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);
			//subtext.setText(module.getTeacher());
			subtext.setText(course.getTeacher());
			
			return convertView;
		}
		
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
