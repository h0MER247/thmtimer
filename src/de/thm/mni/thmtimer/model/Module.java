package de.thm.mni.thmtimer.model;



public class Module {
	
	private Long m_id;
	private String m_name;
	private Integer m_creditPoints;
	private String m_description;
	private String m_moduleNumber;
	private TimeData m_timeToInvestInHours;
	
	// ToDo: Mehr Informationen zu einem Modul hinzufügen!
	
	
	public Module(Long id, String name, String moduleNumber, Integer creditPoints, Integer timeToInvestInHours, String description) {
		
		m_id = id;
		m_name = name;
		m_moduleNumber = moduleNumber;
		m_creditPoints = creditPoints;
		m_description = description;
		m_timeToInvestInHours = new TimeData(timeToInvestInHours, 0, 0);
	}
	
	
	
	public Long getID() {
		
		return m_id;
	}
	
	public String getName() {
		
		return m_name;
	}
	
	public String getModuleNumber() {
		
		return m_moduleNumber;
	}
	
	public Integer getCreditPoints() {
		
		return m_creditPoints;
	}
	
	public String getDescription() {
		
		return m_description;
	}
	
	public TimeData getTimeToInvestInHours() {
		
		return m_timeToInvestInHours;
	}
	
	
	
	@Override
	public String toString() {
		
		return m_name;
	}
}