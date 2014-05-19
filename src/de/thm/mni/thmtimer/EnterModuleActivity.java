package de.thm.mni.thmtimer;

import java.lang.reflect.Field;

import de.thm.mni.thmtimer.util.DepthPageTransformer;
import de.thm.mni.thmtimer.util.FixedSpeedScroller;
import de.thm.mni.thmtimer.util.ZoomPageTransformer;
import de.thm.mni.thmtimer.util.TabFactory;
import de.thm.mni.thmtimer.util.TabPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class EnterModuleActivity extends FragmentActivity {

	private Fragment moduleDetail;
	private ModuleSearchFragment moduleSearch;
	private FragmentManager fragmentManager;
	private ViewPager pager;
	private TabPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (fragmentManager == null) {
			fragmentManager = getSupportFragmentManager();
		}
		if (savedInstanceState != null) { 
			moduleSearch = (ModuleSearchFragment) fragmentManager.getFragment(savedInstanceState, "moduleSearch");
			moduleDetail = fragmentManager.getFragment(savedInstanceState, "moduleDetail");
		}

		setContentView(R.layout.entermoduleactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (moduleSearch == null) {
			moduleSearch = new ModuleSearchFragment();
		}

		pager = (ViewPager) findViewById(R.id.entermodulepager);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), new TabFactory() {

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
				if (moduleDetail == null) {
					return 1;
				} else {
					return 2;
				}
			}
		});
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new DepthPageTransformer());
		try {
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			scroller.set(pager, new FixedSpeedScroller(this));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void openSearch() {
		pager.setCurrentItem(0);
	}

	public void closeSearch(long id) {
		
		moduleDetail = new ModuleDetailFragment();
		Bundle b = new Bundle();
		b.putLong("id", id); // id ist jetzt ne kursid!!!
		moduleDetail.setArguments(b);
		pagerAdapter.notifyDataSetChanged();

		pager.setCurrentItem(1);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		fragmentManager.putFragment(outState, "moduleSearch", moduleSearch);
		if (moduleDetail != null) {
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
		int cur = pager.getCurrentItem();
		if (cur == 0) {
			finish();
		} else {
			openSearch();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		moduleSearch.clearFilter();
	}
}
