package de.thm.mni.thmtimer;

import java.util.ArrayList;
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
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.ModuleComparator;
import de.thm.mni.thmtimer.util.StaticModuleData;

public class ModuleSearchFragment extends Fragment {

	private ModuleListAdapter mAdapter;
	private List<Module> mData;
	private SearchView mSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mData == null) {
			mData = StaticModuleData.getModuleList();
		}
		if (mAdapter == null) {
			mAdapter = new ModuleListAdapter(savedInstanceState);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.modulesearchfragment, container, false);

		mSearch = (SearchView) view.findViewById(R.id.searchfragment);
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
		mSearch.setFocusable(false);
		mSearch.setFocusableInTouchMode(false);

		ListView lv = (ListView) view.findViewById(R.id.searchlist);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View view, int pos, long id) {

				Activity a = getActivity();
				if (a instanceof EnterModuleActivity) {

					EnterModuleActivity ea = (EnterModuleActivity) a;

					ea.closeSearch(mAdapter.getItem(pos).getID());
				}
			}
		});
		mAdapter.sort(new ModuleComparator());
		lv.setAdapter(mAdapter);
		return view;
	}

	private class ModuleListAdapter extends ArrayAdapter<Module> {

		private Bundle bundle;
		private List<Module> origData;

		public ModuleListAdapter(Bundle bundle) {
			super(getActivity(), R.layout.modulelistitem, mData);
			this.bundle = bundle;

			origData = new ArrayList<Module>(mData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater(bundle).inflate(R.layout.modulelistitem, parent, false);
			}
			final Module module = getItem(position);

			TextView name = (TextView) convertView.findViewById(R.id.moduleName);
			name.setText(module.getName());
			TextView subtext = (TextView) convertView.findViewById(R.id.subtext);
			subtext.setText(module.getCourseList().get(0).getTeacher());

			return convertView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					constraint = constraint.toString().toLowerCase();
					FilterResults result = new FilterResults();

					if (constraint != null && constraint.toString().length() > 0) {
						List<Module> founded = new ArrayList<Module>();
						for (Module item : origData) {
							if (item.toString().toLowerCase().contains(constraint)) {
								founded.add(item);
							}
						}

						result.values = founded;
						result.count = founded.size();
					} else {
						result.values = origData;
						result.count = origData.size();
					}
					return result;

				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					clear();
					for (Module item : (List<Module>) results.values) {
						add(item);
					}
					notifyDataSetChanged();

				}
			};
		}
	}

	public void clearFilter() {
		mAdapter.getFilter().filter("");
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
