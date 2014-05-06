package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.Module;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StudentFragment extends Fragment {
	private ListAdapter adapter;
	private List<Module> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if(data == null) {
			data = new ArrayList<Module>();
			data.add(new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h", false));
			data.add(new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h", false));
			data.add(new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h", true));
			data.add(new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h", false));
			data.add(new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h", false));
			data.add(new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h", true));
			data.add(new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h", false));
			data.add(new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h", false));
			data.add(new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h", true));
			data.add(new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h", false));
			data.add(new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h", false));
			data.add(new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h", true));

		}
		if(adapter==null) {
			adapter = new StudentModuleListAdapter(savedInstanceState);
		}

	}

	private class StudentModuleListAdapter extends ArrayAdapter<Module> {

		private Bundle bundle;

		public StudentModuleListAdapter(Bundle bundle) {
			super(getActivity(), R.layout.studentlistitem, data); 
			this.bundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView == null) {

				convertView = getLayoutInflater(bundle).inflate(R.layout.studentlistitem, parent, false);
			}


			final Module module = data.get(position);

			TextView name     = (TextView) convertView.findViewById(R.id.moduleName);
			TextView time     = (TextView) convertView.findViewById(R.id.timeInvested);
			TextView subtext     = (TextView) convertView.findViewById(R.id.subtext);
			
			name.setText(module.getName());
			time.setText(module.getTimeInvested());
			subtext.setText(module.getTeacher());

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
		switch(item.getItemId()) {
		case R.id.action_add:
			//Intent intent = new Intent(this, TARGET.class);
			//startActivityForResult(intent, 1);
			return true;
		default:
			return false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.studentfragment, container, false);

		ListView listView = (ListView)view.findViewById(R.id.studentModules);

		listView.setAdapter(new StudentModuleListAdapter(savedInstanceState));

		return view;
	}
}
