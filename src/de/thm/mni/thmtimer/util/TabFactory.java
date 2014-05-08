package de.thm.mni.thmtimer.util;

import android.support.v4.app.Fragment;

public interface TabFactory {
	public int getNumberOfTabs();

	public Fragment firstTab();

	public Fragment secondTab();
}
