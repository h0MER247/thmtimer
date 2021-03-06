package de.thm.mni.thmtimer.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	private TabFactory mTf;

	public TabPagerAdapter(FragmentManager fm, TabFactory tf) {
		super(fm);
		this.mTf = tf;
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return mTf.firstTab();
		case 1:
			return mTf.secondTab();
		}
		return null;
	}

	@Override
	public int getCount() {
		return mTf.getNumberOfTabs(); // Number of Tabs
	}

	@Override
	public int getItemPosition(Object object) {
		//Without this, it won't update fragments in case they changed
		return POSITION_NONE;
	}
}