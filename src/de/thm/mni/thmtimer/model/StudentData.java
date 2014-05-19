package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentData {

	private ArrayList<Long> mCourseIDs;
	private HashMap<Long, ArrayList<TimeTracking>> mTimeTrackingData;
	
	
	public StudentData() {
		mCourseIDs = new ArrayList<Long>();
		mTimeTrackingData = new HashMap<Long, ArrayList<TimeTracking>>();
	}
	
	public void addCourse(Long courseID) {
		mCourseIDs.add(courseID);
	}
	
	public ArrayList<Long> getCourseIDs() {
		return mCourseIDs;
	}
	
	public boolean hasCourses() {
		return mCourseIDs.size() > 0;
	}
	
	public void addTimeTracking(Long courseID, TimeTracking data) {
		ArrayList<TimeTracking> list;
		if(mTimeTrackingData.get(courseID) != null) {
			list = mTimeTrackingData.get(courseID);
		}
		else {
			list = new ArrayList<TimeTracking>();
			mTimeTrackingData.put(courseID, list);
		}
		list.add(data);
	}
	
	public ArrayList<TimeTracking> getTimeTrackingData(Long courseID) {
		return mTimeTrackingData.get(courseID);
	}
	
	public boolean hasTimeTrackingData(Long courseID) {
		return mTimeTrackingData.get(courseID) != null &&
			   mTimeTrackingData.get(courseID).size() > 0;
	}
	
	
	//
	// Hilfsfunktionen
	//
	public TimeData getTimeInvestedTotal(Long courseID) {
		ArrayList<TimeTracking> trackingData = mTimeTrackingData.get(courseID);
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