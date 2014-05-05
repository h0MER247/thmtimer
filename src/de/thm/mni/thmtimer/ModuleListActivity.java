package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.util.TabPagerAdapter;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class ModuleListActivity extends FragmentActivity {
	private ViewPager tab;
	private TabPagerAdapter tabAdapter;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.modulelistactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if(tabAdapter == null) {
			tabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		}

		if(tab == null) {
			tab = (ViewPager)findViewById(R.id.pager);
			tab.setOnPageChangeListener(
					new ViewPager.OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							actionBar = getActionBar();
							actionBar.setSelectedNavigationItem(position);  
							tab.getChildAt(position).setAlpha(1);
						}

						@Override
						public void onPageScrollStateChanged(int position) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onPageScrolled(int position, float positionOffset,
								int positionOffsetPixels) {
							// TODO Auto-generated method stub
							tab.getChildAt(0).setAlpha(1-positionOffset);
							if(positionOffset!=0) {
								tab.getChildAt(1).setAlpha(positionOffset);
							}
						}
					});
			tab.setAdapter(tabAdapter);
		}

		if(actionBar == null) {
			actionBar = getActionBar();

			//Enable Tabs on Action Bar
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			ActionBar.TabListener tabListener = new ActionBar.TabListener(){

				@Override
				public void onTabReselected(android.app.ActionBar.Tab tab,
						FragmentTransaction ft) {

				}
				@Override
				public void onTabSelected(ActionBar.Tab actionTab, FragmentTransaction ft) {
					tab.setCurrentItem(actionTab.getPosition());
				}

				@Override
				public void onTabUnselected(android.app.ActionBar.Tab tab,
						FragmentTransaction ft) {

				}};

				//Add tabs to actionbar
				actionBar.addTab(actionBar.newTab().setText(
						getString(R.string.tab1)).setTabListener(tabListener));
				actionBar.addTab(actionBar.newTab().setText(
						getString(R.string.tab2)).setTabListener(tabListener));
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
}