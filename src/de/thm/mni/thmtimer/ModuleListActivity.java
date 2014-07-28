package de.thm.mni.thmtimer;

import java.lang.reflect.Field;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import de.thm.mni.thmtimer.util.FixedSpeedScroller;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.TabFactory;
import de.thm.mni.thmtimer.util.TabPagerAdapter;
import de.thm.mni.thmtimer.util.ZoomPageTransformer;
import de.thm.thmtimer.entities.User;

public class ModuleListActivity extends FragmentActivity {
	private final String TAG = ModuleListActivity.class.getSimpleName();
	private ViewPager mPager;
	private TabPagerAdapter mTabAdapter;
	private ActionBar mActionBar;
	private StudentFragment mStudentFragment;
	private TeacherFragment mTeacherFragment;
	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modulelistactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(mUser == null)
			mUser = ModuleDAO.getUser();
		// Fragmente initialisieren
		if(mStudentFragment == null)
			mStudentFragment = new StudentFragment();
		if((mTeacherFragment == null) && mUser.isLecteur())
			mTeacherFragment = new TeacherFragment();
		// TabAdapter
		if(mTabAdapter == null) {
			mTabAdapter = new TabPagerAdapter(getSupportFragmentManager(), new TabFactory() {
				@Override
				public Fragment firstTab() { return mStudentFragment; }
				@Override
				public Fragment secondTab() { return mTeacherFragment; }
				@Override
				public int getNumberOfTabs() { return mUser.isLecteur() ? 2 : 1; }
			});
		}
		// ViewPager
		if(mPager == null) {
			mPager = (ViewPager)findViewById(R.id.pager);
			// Custom Scroller
			try {
				Field scroller = ViewPager.class.getDeclaredField("mScroller");
				scroller.setAccessible(true);
				scroller.set(mPager, new FixedSpeedScroller(this));
			}
			catch(NoSuchFieldException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
			catch(IllegalArgumentException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
			catch(IllegalAccessException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
			// PageListener
			mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					//mActionBar = getActionBar();
					if(mActionBar.getSelectedNavigationIndex() != position)
						mActionBar.setSelectedNavigationItem(position);
				}
				@Override
				public void onPageScrollStateChanged(int position) {}
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			});
			mPager.setPageTransformer(true, new ZoomPageTransformer());
			mPager.setAdapter(mTabAdapter);
		}
		// Add Actionbar Tabs if the user is a lecturer
		if((mActionBar == null) && mUser.isLecteur()) {
			mActionBar = getActionBar();
			// Enable Tabs on Action Bar
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			ActionBar.TabListener tabListener = new ActionBar.TabListener() {
				@Override
				public void onTabSelected(ActionBar.Tab actionTab, FragmentTransaction ft) {
					if(mPager.getCurrentItem() != actionTab.getPosition())
						mPager.setCurrentItem(actionTab.getPosition());
				}
				@Override
				public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {}
				@Override
				public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {}
			};
			// Add tabs to ActionBar
			mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab1)).setTabListener(tabListener));
			mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab2)).setTabListener(tabListener));
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}
	public void refresh() {
		if(mTabAdapter != null) {
			mTabAdapter.notifyDataSetChanged();
		}
	}
}