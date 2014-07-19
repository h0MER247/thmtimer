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

public class ModuleListActivity extends FragmentActivity {
	private ViewPager mPager;
	private TabPagerAdapter mTabAdapter;
	private ActionBar mActionBar;
	
	protected static final String TAG = ModuleListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.modulelistactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (mTabAdapter == null) {
			mTabAdapter = new TabPagerAdapter(getSupportFragmentManager(), new TabFactory() {

				@Override
				public Fragment firstTab() {
					return new StudentFragment();
				}

				@Override
				public Fragment secondTab() {
					if (ModuleDAO.getUser().isLecteur())
						return new TeacherFragment();
					return null;
				}

				@Override
				public int getNumberOfTabs() {
					if (ModuleDAO.getUser().isLecteur()) {
						Log.d(TAG, "User is a Teacher, give him two tabs");
						return 2;
					}
					Log.d(TAG, "User is only a student, give him one tab");
					return 1;
				}
			});
		}

		if (mPager == null) {
			mPager = (ViewPager) findViewById(R.id.pager);
			mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					mActionBar = getActionBar();
					mActionBar.setSelectedNavigationItem(position);
				}

				@Override
				public void onPageScrollStateChanged(int position) {

				}

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}
			});
			mPager.setAdapter(mTabAdapter);
			mPager.setPageTransformer(true, new ZoomPageTransformer());
			try {
				Field scroller = ViewPager.class.getDeclaredField("mScroller");
				scroller.setAccessible(true);
				scroller.set(mPager, new FixedSpeedScroller(this));
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

		if (mActionBar == null) {
			mActionBar = getActionBar();

			// Enable Tabs on Action Bar
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			ActionBar.TabListener tabListener = new ActionBar.TabListener() {

				@Override
				public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {

				}

				@Override
				public void onTabSelected(ActionBar.Tab actionTab, FragmentTransaction ft) {
					mPager.setCurrentItem(actionTab.getPosition());
				}

				@Override
				public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {

				}
			};

			// Add tabs to actionbar
			mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab1)).setTabListener(tabListener));
			mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab2)).setTabListener(tabListener));
		}

	}
	
	public void refresh() {
		mTabAdapter.notifyDataSetChanged();
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
}