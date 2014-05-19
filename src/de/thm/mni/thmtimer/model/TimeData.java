package de.thm.mni.thmtimer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TimeData implements Comparable<TimeData> {
	
	// Format: HH:MM(:SS) --> Sekunden sind optional !!!
	private final Pattern m_pattern = Pattern.compile("^(\\d+):([0-5]?\\d)(?>:([0-5]?\\d))?$");
	
	private Integer m_hours;
	private Integer m_minutes;
	
	
	public TimeData() {
		
		m_hours = 0;
		m_minutes = 0;
	}
	
	public TimeData(String time) {
		
		if(!parseString(time)) {
			
			m_hours = 0;
			m_minutes = 0;
		}
	}
	
	public TimeData(Integer hours, Integer minutes) {
		
		m_hours = hours;
		m_minutes = minutes;
	}
	
	
	
	public boolean parseString(String time) {
		
		Matcher m = m_pattern.matcher(time);
		
		if(m.matches()) {
			
			m_hours = Integer.valueOf(m.group(1));
			m_minutes = Integer.valueOf(m.group(2));
			return true;
		}
		
		// Hat nicht geklappt :(
		return false;
	}
	
	
	
	public void setHours(Integer hours) {
		
		m_hours = hours;
	}
	
	public Integer getHours() {
		
		return m_hours;
	}
	
	public void setMinutes(Integer minutes) {
		
		m_minutes = minutes;
	}
	
	public Integer getMinutes() {
		
		return m_minutes;
	}
	
	
	public void setTimeInSeconds(Integer seconds) {
		
		m_hours = seconds / 3600;
		m_minutes = (seconds - (m_hours * 3600)) / 60;
	}
	
	public Integer getTimeInSeconds() {
		
		return m_hours * 3600 + m_minutes * 60;
	}
	
	public void setTimeInMinutes(Integer minutes) {
		
		m_hours   = minutes / 60;
		m_minutes = minutes - (m_hours * 60);
	}
	
	public Integer getTimeInMinutes() {
		
		return (m_hours * 60) + m_minutes; 
	}
	
	public String getTimeStringHHMM() {
		
		return String.format("%02d:%02d", m_hours, m_minutes);
	}
	
	public String getTimeStringHHMMSS() {
		
		return String.format("%02d:%02d", m_hours, m_minutes);
	}
	
	@Override
	public String toString() {
		
		return getTimeStringHHMM();
	}
	
	@Override
	public int compareTo(TimeData another) {
		
		Integer me    = getTimeInSeconds();
		Integer other = another.getTimeInSeconds();
		
		if(me > other) {
			
			return -1;
		}
		else if(me == other) {
			
			return 0;
		}
		else {
			
			return 1;
		}
	}
}