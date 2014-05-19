package de.thm.mni.thmtimer.model;



public class TimeCategory {
	
	private Long m_id;
	private String m_description;
	
	
	public TimeCategory(Long id, String description) {
		
		m_id = id;
		m_description = description;
	}
	
	
	
	public Long getID() {
		
		return m_id;
	}
	
	public String getDescription() {
		
		return m_description;
	}
	
	
	
	@Override
	public String toString() {
		
		return m_description;
	}
}