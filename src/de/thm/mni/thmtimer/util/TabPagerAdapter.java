package de.thm.mni.thmtimer.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	private TabFactory tf;
	
	public TabPagerAdapter(FragmentManager fm, TabFactory tf) {
		super(fm);
		this.tf = tf;
	}
	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return tf.firstTab();
		case 1:
			return tf.secondTab();
		}	
		return null;
	}
	
	@Override
	public int getCount() {
		return tf.getNumberOfTabs(); //Number of Tabs
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}