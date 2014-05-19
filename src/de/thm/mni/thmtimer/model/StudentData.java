package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.HashMap;



public class StudentData {

	private ArrayList<Long> m_courseIDs;
	private HashMap<Long, ArrayList<TimeTracking>> m_timeTrackingData;
	
	
	public StudentData() {
		
		m_courseIDs = new ArrayList<Long>();
		m_timeTrackingData = new HashMap<Long, ArrayList<TimeTracking>>();
	}
	
	
	
	public void addCourse(Long courseID) {
		
		m_courseIDs.add(courseID);
	}
	
	public ArrayList<Long> getCourseIDs() {
		
		return m_courseIDs;
	}
	
	public boolean hasCourses() {
		
		return m_courseIDs.size() > 0;
	}
	
	
	
	public void addTimeTracking(Long courseID, TimeTracking data) {
		
		ArrayList<TimeTracking> list;
		
		if(m_timeTrackingData.get(courseID) != null) {
			
			list = m_timeTrackingData.get(courseID);
		}
		else {
		
			list = new ArrayList<TimeTracking>();
			
			m_timeTrackingData.put(courseID, list);
		}
		
		
		list.add(data);
	}
	
	public ArrayList<TimeTracking> getTimeTrackingData(Long courseID) {
		
		return m_timeTrackingData.get(courseID);
	}
	
	public boolean hasTimeTrackingData(Long courseID) {
		
		return m_timeTrackingData.get(courseID) != null &&
			   m_timeTrackingData.get(courseID).size() > 0;
	}
	
	
	//
	// Hilfsfunktionen
	//
	public TimeData getTimeInvestedTotal(Long courseID) {
		
		ArrayList<TimeTracking> trackingData = m_timeTrackingData.get(courseID);
		Integer timeInSeconds = 0;
		
		//
		// Alle erfassten Zeiten zusammenzählen
		//
		if(trackingData != null) {

			for(TimeTracking t : trackingData) {
				
				timeInSeconds += t.getTime().getTimeInSeconds();
			}
		}
		
		TimeData data = new TimeData();
		data.setTimeInSeconds(timeInSeconds);
		
		return data;
	}
}