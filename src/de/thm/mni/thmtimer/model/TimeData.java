package de.thm.mni.thmtimer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javax.activity.InvalidActivityException;

public class TimeData implements Comparable<TimeData> {

	// Format HH:MM
	private final Pattern m_pattern = Pattern.compile("^(\\d+):([0-5]?\\d)$");
	private int mMinutes;

	public TimeData() {
		mMinutes = 0;
	}

	/**
	 * Parses a time string in the and sets the time accordingly
	 * 
	 * @param time
	 *            String (Format HH:MM)
	 * @throws IllegalArgumentException
	 *             When `time` has an invalid format
	 */
	public void parseString(String time) throws IllegalArgumentException {
		Matcher m = m_pattern.matcher(time);
		if (!m.matches())
			throw new IllegalArgumentException("Could not parse the given string: " + time);
		mMinutes = Integer.valueOf(m.group(2)) + 60 * Integer.valueOf(m.group(1));
	}

	/**
	 * Set the time in hours. Existing value will be overwritten.
	 * 
	 * @param hours
	 */
	public void setTimeInHours(int hours) {
		mMinutes = hours * 60;
	}

	/**
	 * Returns the time in hours (rounded down)
	 * 
	 * @return time in hours
	 */
	public int getTimeInHours() {
		return mMinutes / 60;
	}

	/**
	 * Set the time in minutes. Existing value will be overwritten.
	 * 
	 * @param minutes
	 *            time in minutes
	 */
	public void setTimeInMinutes(int minutes) {
		mMinutes = minutes;
	}

	/**
	 * Returns the time in minutes. Note that this can be greater than 60
	 * minutes.
	 * 
	 * @return time in minutes
	 */
	public int getTimeInMinutes() {
		return mMinutes;
	}

	@Override
	public String toString() {
		return String.format("%02dh", getTimeInHours());
	}

	@Override
	public int compareTo(TimeData another) {
		return getTimeInMinutes() - another.getTimeInMinutes();
	}
}