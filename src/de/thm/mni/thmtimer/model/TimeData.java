package de.thm.mni.thmtimer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeData implements Comparable<TimeData> {
	
	// Format HH:MM
	private final Pattern m_pattern = Pattern.compile("^(\\d+):([0-5]?\\d)$");
	
	private int mHours;
	private int mMinutes;

	public TimeData() {
		mHours = 0;
		mMinutes = 0;
	}

	public TimeData(int hours, int minutes) {
		mHours = hours;
		mMinutes = minutes;
	}

	public boolean parseString(String time) {
		Matcher m = m_pattern.matcher(time);
		if (m.matches()) {
			mHours = Integer.valueOf(m.group(1));
			mMinutes = Integer.valueOf(m.group(2));
			return true;
		}

		// Hat nicht geklappt :(
		return false;
	}

	public void setHours(int hours) {
		mHours = hours;
	}

	public int getHours() {
		return mHours;
	}

	public void setTimeInSeconds(int seconds) {
		mHours = seconds / 3600;
		mMinutes = (seconds - (mHours * 3600)) / 60;
	}

	public int getTimeInSeconds() {
		return mHours * 3600 + mMinutes * 60;
	}

	public void setTimeInMinutes(int minutes) {
		mHours = minutes / 60;
		mMinutes = minutes - (mHours * 60);
	}

	public int getTimeInMinutes() {
		return (mHours * 60) + mMinutes;
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d", mHours, mMinutes);
	}

	@Override
	public int compareTo(TimeData another) {
		return getTimeInMinutes() - another.getTimeInMinutes();
	}
}