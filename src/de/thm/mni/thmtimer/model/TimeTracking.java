package de.thm.mni.thmtimer.model;



public class TimeTracking {
	
	private Long m_id;
	private Long m_categoryID;
	private String m_description;
	private TimeData m_time;
	
	
	public TimeTracking(Long id, Long categoryID, String description, TimeData time) {
		
		m_id = id;
		m_categoryID = categoryID;
		m_description = description;
		m_time = time;
	}
	
	
	
	public Long getID() {
		
		return m_id;
	}
	
	public String getDescription() {
		
		return m_description;
	}
	
	public Long getCategoryID() {
		
		return m_categoryID;
	}
	
	public TimeData getTime() {
		
		return m_time;
	}
	
	

	@Override
	public String toString() {
		
		return m_description + " (" + m_time.getTimeInMinutes().toString() + " min)";
	}
}