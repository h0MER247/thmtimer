package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Module {
	
	private Long m_id;
	private String m_name;
	private Integer m_creditPoints;
	private String m_description;
	private String m_moduleNumber;
	private List<Course> m_courseList = new ArrayList<Course>();
	private TimeData m_timeToInvestInHours;
	private Date m_startDate;
	
	// ToDo: Mehr Informationen zu einem Modul hinzufügen!
	
	
	public Module(Long id, String name, String moduleNumber, Integer creditPoints, Integer timeToInvestInHours, String description) {
		
		m_id = id;
		m_name = name;
		m_moduleNumber = moduleNumber;
		m_creditPoints = creditPoints;
		m_description = description;
		m_timeToInvestInHours = new TimeData(timeToInvestInHours, 0);
		GregorianCalendar gc = new GregorianCalendar(2014, 4, 1);
		setStartDate(new Date(gc.getTimeInMillis()));
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



	public List<Course> getCourseList() {
		return m_courseList;
	}



	public void addCourse(Course course) {
		m_courseList.add(course);
	}



	public Date getStartDate() {
		return m_startDate;
	}



	public void setStartDate(Date m_startDate) {
		this.m_startDate = m_startDate;
	}
	
	@Override
	public String toString() {
		return m_name;
	}
}