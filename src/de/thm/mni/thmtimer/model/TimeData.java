package de.thm.mni.thmtimer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeData implements Comparable<TimeData> {
	// Format: HH:MM(:SS) --> Sekunden sind optional !!!
	private final Pattern m_pattern = Pattern.compile("^(\\d+):([0-5]?\\d)(?>:([0-5]?\\d))?$");

	private Integer mHours;
	private Integer mMinutes;

	public TimeData() {
		mHours = 0;
		mMinutes = 0;
	}

	public TimeData(String time) {
		if (!parseString(time)) {
			mHours = 0;
			mMinutes = 0;
		}
	}

	public TimeData(Integer hours, Integer minutes) {
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

	public void setHours(Integer hours) {
		mHours = hours;
	}

	public Integer getHours() {
		return mHours;
	}

	public void setMinutes(Integer minutes) {
		mMinutes = minutes;
	}

	public Integer getMinutes() {
		return mMinutes;
	}

	public void setTimeInSeconds(Integer seconds) {
		mHours = seconds / 3600;
		mMinutes = (seconds - (mHours * 3600)) / 60;
	}

	public Integer getTimeInSeconds() {
		return mHours * 3600 + mMinutes * 60;
	}

	public void setTimeInMinutes(Integer minutes) {
		mHours = minutes / 60;
		mMinutes = minutes - (mHours * 60);
	}

	public Integer getTimeInMinutes() {
		return (mHours * 60) + mMinutes;
	}

	public String getTimeStringHHMM() {
		return String.format("%02d:%02d", mHours, mMinutes);
	}

	public String getTimeStringHHMMSS() {
		return String.format("%02d:%02d", mHours, mMinutes);
	}

	@Override
	public String toString() {
		return getTimeStringHHMM();
	}

	@Override
	public int compareTo(TimeData another) {
		Integer me = getTimeInSeconds();
		Integer other = another.getTimeInSeconds();

		if (me > other) {
			return -1;
		} else if (me == other) {
			return 0;
		} else {
			return 1;
		}
	}
}