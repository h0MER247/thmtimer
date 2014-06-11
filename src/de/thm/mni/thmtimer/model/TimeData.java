package de.thm.mni.thmtimer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activity.InvalidActivityException;

public class TimeData implements Comparable<TimeData> {

	// Format HH:MM
	private final Pattern m_pattern = Pattern.compile("^(\\d+):([0-5]?\\d)$");
	private int mMinutes;

	public TimeData() {
		mMinutes = 0;
	}

	public void parseString(String time) throws IllegalArgumentException {
		Matcher m = m_pattern.matcher(time);
		if (!m.matches())
			throw new IllegalArgumentException("Could not parse the given string: " + time);
		mMinutes = Integer.valueOf(m.group(2)) + 60 * Integer.valueOf(m.group(1));
	}

	public void setTimeInHours(int hours) {
		mMinutes = hours * 60;
	}

	public int getTimeInHours() {
		return mMinutes / 60;
	}

	public void setTimeInMinutes(int minutes) {
		mMinutes = minutes;
	}

	public int getTimeInMinutes() {
		return mMinutes;
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d", mMinutes / 60, mMinutes % 60);
	}

	@Override
	public int compareTo(TimeData another) {
		return getTimeInMinutes() - another.getTimeInMinutes();
	}
}