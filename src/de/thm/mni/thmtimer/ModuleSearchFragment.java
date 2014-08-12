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
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Module;

public class ModuleSearchFragment extends Fragment {

	private List<Module> mModuleList;
	private List<Module> mAdapterData;
	private SearchView mSearch;
	private ModuleListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (mModuleList == null)
			mModuleList = new ArrayList<Module>();

		if (mAdapterData == null)
			mAdapterData = new ArrayList<Module>();

		if (mAdapter == null)
			mAdapter = new ModuleListAdapter(savedInstanceState);

		mModuleList.clear();
		mModuleList.addAll(ModuleDAO.getModuleList());

		mAdapterData.clear();
		mAdapterData.addAll(mModuleList);

		mAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout view = (RelativeLayout) inflater.inflate(
				R.layout.modulesearchfragment, container, false);

		mSearch = (SearchView) view.findViewById(R.id.searchfragment);
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
			public void onItemClick(AdapterView<?> aView, View view, int pos,
					long id) {

				Activity a = getActivity();
				if (a instanceof EnterModuleActivity) {

					EnterModuleActivity ea = (EnterModuleActivity) a;

					ea.closeSearch(mAdapter.getItem(pos).getId());
				}
			}
		});
		lv.setAdapter(mAdapter);
		return view;
	}

	public void clearFilter() {

		mAdapter.getFilter().filter("");

	}

	private class ModuleListAdapter extends ArrayAdapter<Module> {

		private Bundle mBundle;

		public ModuleListAdapter(Bundle bundle) {

			super(getActivity(), R.layout.modulelistitem, mAdapterData);
			mBundle = bundle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = getLayoutInflater(mBundle).inflate(
						R.layout.modulelistitem, parent, false);
			}

			TextView name = (TextView) convertView
					.findViewById(R.id.moduleName);
			TextView subtext = (TextView) convertView
					.findViewById(R.id.subtext);

			// Das was wir hier hatten hat das Serverteam nicht implementiert
			final Module module = getItem(position);

			name.setText(module.getName());
			subtext.setText(String.format("%s (%s)", module.getShortcut(),
					module.getNumber()));

			return convertView;
		}

		@Override
		public Filter getFilter() {

			return new Filter() {

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					constraint = constraint.toString().toLowerCase();
					FilterResults result = new FilterResults();

					if (constraint != null
							&& constraint.toString().length() > 0) {

						List<Module> found = new ArrayList<Module>();
						for (Module m : mModuleList) {

							if (getSearchStringForModule(m)
									.contains(constraint)) {

								found.add(m);
							}
						}

						result.values = found;
						result.count = found.size();
					} else {

						result.values = mModuleList;
						result.count = mModuleList.size();
					}

					return result;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {

					List<Module> res = (List<Module>) results.values;

					clear();
					for (Module item : res)
						add(item);

					notifyDataSetChanged();
				}

				/**
				 * Hier alles reinpacken nach dem gesucht werden kann
				 * 
				 * @param module
				 *            Das Modul
				 * @return String mit allen suchbaren Begriffen für dieses Modul
				 */
				private String getSearchStringForModule(Module module) {

					String ret = module.getName() + " " + module.getNumber()
							+ " " + module.getShortcut();

					return ret.toLowerCase();
				}
			};
		}
	}
}