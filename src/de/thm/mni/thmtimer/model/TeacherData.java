package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.HashMap;



public class TeacherData {
	
	// ToDo: Mehr Statistikdaten als die Zeitstatistik hinzuf�gen
	private ArrayList<Long> m_courseIDs;
	private HashMap<Long, ArrayList<TimeStatisticData>> m_timeStatistic;
	
	
	
	public TeacherData() {
		
		m_courseIDs = new ArrayList<Long>();
		m_timeStatistic = new HashMap<Long, ArrayList<TimeStatisticData>>();
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
	
	
	
	public void addTimeStatistic(Long courseID, TimeStatisticData data) {
		
		ArrayList<TimeStatisticData> list;
		
		if(m_timeStatistic.get(courseID) != null) {
			
			list = m_timeStatistic.get(courseID);
		}
		else {
		
			list = new ArrayList<TimeStatisticData>();
			
			m_timeStatistic.put(courseID, list);
		}
		
		
		list.add(data);
	}
	
	public ArrayList<TimeStatisticData> getTimeStatistic(Long courseID) {
		
		return m_timeStatistic.get(courseID);
	}
	
	
	
	//
	// Hilfsfunktionen
	//
	public TimeData getTimeInvestedTotal(Long courseID) {
		
		ArrayList<TimeStatisticData> statisticData = m_timeStatistic.get(courseID);
		Integer timeInSeconds = 0;
		
		//
		// Alle erfassten Zeiten zusammenz�hlen
		//
		if(statisticData != null) {

			for(TimeStatisticData t : statisticData) {
				
				timeInSeconds += t.getTime().getTimeInSeconds();
			}
		}
		
		TimeData data = new TimeData();
		data.setTimeInSeconds(timeInSeconds);
		
		return data;
	}
}