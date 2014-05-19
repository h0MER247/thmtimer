package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleComparator;
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
	
	private StudentModuleListAdapter adapter;
	private List<Module> data;

	protected static final int REQUEST_NEW = 1;
	protected static final int REQUEST_TIMETRACKING = 2;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		if(data == null) {
			
			data = new ArrayList<Module>();
			for(Long l:StaticModuleData.getStudentData().getCourseIDs()) {
				data.add(StaticModuleData.findModule(StaticModuleData.findCourse(l).getModuleID()));
			}
		}
		if(adapter == null) {
			
			adapter = new StudentModuleListAdapter(savedInstanceState);
			adapter.sort(new ModuleComparator());
		}
	}

	
	
	private class StudentModuleListAdapter extends ArrayAdapter<Module> {
		
		private Bundle bundle;
		private final long FOUR_MONTHS = 10368000000l;

		public StudentModuleListAdapter(Bundle bundle) {
			
			super(getActivity(), R.layout.studentlistitem, data);
			this.bundle = bundle;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = getLayoutInflater(bundle).inflate(R.layout.studentlistitem, parent, false);
			}
			
			final Module module = data.get(position);

			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			TextView time = (TextView) convertView.findViewById(R.id.timeInvested);
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);

			name.setText(module.getName());
			int timeInvested = 0;
			for(Course c:module.getCourseList()) {
				timeInvested += StaticModuleData.getStudentData().getTimeInvestedTotal(c.getID()).getTimeInMinutes();
			}
			TimeData td = new TimeData();
			td.setTimeInMinutes(timeInvested);
			time.setText(td.toString());
			Date d = module.getStartDate();
			if(d!=null) {
				//Time of the current semester
				long delta = new Date().getTime()-d.getTime();
				double percentSemester = delta/(FOUR_MONTHS/100.0);
				double percentInvested = timeInvested/60
						/(module.getCreditPoints()*30.0/100);
				double percentDelta = percentInvested - percentSemester;
				int red = 255, green = 255;
				if(percentDelta>0) {
					red=(int) (10*percentDelta);
				}
				else if(percentDelta<0) {
					green=(int) (10*percentDelta);
				}
				GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.circle);
				shape.setColor(Color.rgb(red, green, 0));
				time.setCompoundDrawablesWithIntrinsicBounds(null, null, shape, null);
			}
			subtext.setText(module.getCourseList().get(0).getTeacher());

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.studentfragment, container, false);
		

		ListView listView = (ListView) view.findViewById(R.id.studentModules);
		
		listView.setAdapter(new StudentModuleListAdapter(savedInstanceState));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Intent intent = new Intent(getActivity(), TimeTrackingActivity.class);
				
				intent.putExtra("course_id", adapter.getItem(position).getCourseList().get(0).getID()); //  (int) ((Module) adapter.getItem(position)).getID());
				startActivityForResult(intent, REQUEST_TIMETRACKING);
			}
		});

		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TIMETRACKING) {
			adapter.notifyDataSetChanged();
		}
	}
}
