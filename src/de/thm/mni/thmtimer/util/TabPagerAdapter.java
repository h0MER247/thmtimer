package de.thm.mni.thmtimer.util;

import de.thm.mni.thmtimer.StudentFragment;
import de.thm.mni.thmtimer.TeacherFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			//Fragment for Student Tab
			return new StudentFragment();
		case 1:
			//Fragement for Teacher Tab
			return new TeacherFragment();
		}	
		return null;
	}
	@Override
	public int getCount() {
		return 2; //Number of Tabs
	}
}