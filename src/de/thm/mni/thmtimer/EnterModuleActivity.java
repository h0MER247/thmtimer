package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.util.MyPageTransformer;
import de.thm.mni.thmtimer.util.TabFactory;
import de.thm.mni.thmtimer.util.TabPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class EnterModuleActivity extends FragmentActivity {

	private boolean isSearch = true;

	private Fragment moduleDetail;
	private ModuleSearchFragment moduleSearch;
	private FragmentManager fragmentManager;
	private ViewPager pager;
	private TabPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(fragmentManager==null) {
			fragmentManager = getSupportFragmentManager();
		}
		if(savedInstanceState != null){
			isSearch = savedInstanceState.getBoolean("isSearch");
			moduleSearch = (ModuleSearchFragment) fragmentManager.getFragment(
					savedInstanceState, "moduleSearch");
			moduleDetail = fragmentManager.getFragment(
					savedInstanceState, "moduleDetail");
		}

		setContentView(R.layout.entermoduleactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(moduleSearch==null) {
			moduleSearch = new ModuleSearchFragment();
		}

		pager = (ViewPager) findViewById(R.id.entermodulepager);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),
				new TabFactory() {

			@Override
			public Fragment firstTab() {
				return moduleSearch;
			}

			@Override
			public Fragment secondTab() {
				return moduleDetail;
			}

			@Override
			public int getNumberOfTabs() {
				if(moduleDetail==null) {
					return 1;
				}
				else {
					return 2;
				}
			}
		});
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new MyPageTransformer());
	}

	public void openSearch() {
		isSearch = true;
		pager.setCurrentItem(0);
	}

	public void closeSearch(long id) {
		moduleDetail = new ModuleDetailFragment();
		Bundle b = new Bundle();
		b.putLong("id", id);
		moduleDetail.setArguments(b);
		pagerAdapter.notifyDataSetChanged();

		isSearch = false;
		pager.setCurrentItem(1);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isSearch", isSearch);
		fragmentManager.putFragment(outState, "moduleSearch", moduleSearch);
		if(moduleDetail!=null) {
			fragmentManager.putFragment(outState, "moduleDetail", moduleDetail);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBack();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		onBack();
	}

	private void onBack() {
		if(isSearch) {
			finish();
		}
		else {
			openSearch();
		}
	}
}
