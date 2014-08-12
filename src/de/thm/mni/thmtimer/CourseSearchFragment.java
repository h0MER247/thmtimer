package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.User;


public class CourseSearchFragment extends Fragment {

	private List<Course> mCourseList;
	private List<Course> mAdapterData;
	private SearchView mSearch;
	private CourseListAdapter mAdapter;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		
		if(mCourseList == null)
			mCourseList = new ArrayList<Course>();
		
		if(mAdapterData == null)
			mAdapterData = new ArrayList<Course>();
		
		if(mAdapter == null)
			mAdapter = new CourseListAdapter(savedInstanceState);
		
		
		mCourseList.clear();
		mCourseList.addAll(ModuleDAO.getFullCourseList());
		
		mAdapterData.clear();
		mAdapterData.addAll(mCourseList);
		
		mAdapter.notifyDataSetChanged();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.modulesearchfragment,
				                                               container,
				                                               false);
		
		mSearch = (SearchView)view.findViewById(R.id.searchfragment);
		mSearch.setFocusable(false);
		mSearch.setFocusableInTouchMode(false);
		mSearch.setQueryHint(getString(R.string.search_hint));
		mSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				
				mSearch.clearFocus();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				
				mAdapter.getFilter().filter(newText);
				return true;
			}
		});
		
		
		ListView lv = (ListView) view.findViewById(R.id.searchlist);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView,
					                View view,
					                int pos,
					                long id) {
				
				Activity a = getActivity();
				if (a instanceof EnterModuleActivity) {
					
					EnterModuleActivity ea = (EnterModuleActivity) a;
					
					ea.closeSearch(mAdapter.getItem(pos).getId());
				}
			}
		});
		mAdapter.sort(new Comparator<Course>(){

			@Override
			public int compare(Course lhs, Course rhs) {
				
				return lhs.getName().compareTo(rhs.getName());
			}
			
		});
		lv.setAdapter(mAdapter);
		return view;
	}
	
	
	public void clearFilter() {
		
		mAdapter.getFilter().filter("");
	}
	
	
	
	private class CourseListAdapter extends ArrayAdapter<Course> {

		private Bundle mBundle;
		
		public CourseListAdapter(Bundle bundle) {
			
			super(getActivity(), R.layout.modulelistitem, mAdapterData);
			mBundle = bundle;
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				
				convertView = getLayoutInflater(mBundle).inflate(R.layout.modulelistitem,
						                                         parent,
						                                         false);
			}
			
			TextView name = (TextView)convertView.findViewById(R.id.moduleName);
			TextView subtext = (TextView)convertView.findViewById(R.id.subtext);
			
			
			final Course course = getItem(position);
			
			String lecturer = "";
			
			for(User u : course.getLecturer()) {
				
				lecturer += String.format("%s%s %s",
						                  (lecturer.length() > 0) ? ", " : "",
						                  u.getFirstName(),
						                  u.getLastName());
			}
			
			name.setText(course.getName());
			subtext.setText(String.format("%s (%s)",
					                      course.getModule().getNumber(),
					                      lecturer));
			
			return convertView;
		}

		@Override
		public Filter getFilter() {
			
			return new Filter() {

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					
					constraint = constraint.toString().toLowerCase();
					FilterResults result = new FilterResults();
					
					if(constraint != null && constraint.toString().length() > 0) {
						
						List<Course> found = new ArrayList<Course>();
						for(Course m : mCourseList) {
							
							if(getSearchStringForCourse(m).contains(constraint)) {
								
								found.add(m);
							}
						}

						result.values = found;
						result.count = found.size();
					}
					else {
						
						result.values = mCourseList;
						result.count = mCourseList.size();
					}
					
					return result;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					
					List<Course> res = (List<Course>)results.values;
					
					clear();
					for(Course item : res)
						add(item);
					
					notifyDataSetChanged();
				}
				
				
				/**
				 * Hier alles reinpacken nach dem gesucht werden kann
				 * 
				 * @param Course
				 *   Der Kurs
				 * @return
				 *   String mit allen suchbaren Begriffen für diesen Kurs
				 */
				private String getSearchStringForCourse(Course Course) {
					
					String ret = Course.getName() + " " + Course.getModule().getNumber() + " " + Course.getModule().getShortcut();
					
					for(User lecturer : Course.getLecturer()) {
						
						ret += " " + lecturer.getFirstName() + " " + lecturer.getLastName();
					}
					
					return ret.toLowerCase();
				}
			};
		}
	}
}