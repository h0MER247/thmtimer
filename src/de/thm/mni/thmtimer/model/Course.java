package de.thm.mni.thmtimer.model;



public class Course {

	private Long m_id;
	private Long m_moduleID;
	private String m_name;
	private String m_teacher;
	private Integer m_studentCount;
	
	
	public Course(Long id, Long moduleID, String name, String teacher, Integer studentCount) {
		
		m_id = id;
		m_moduleID = moduleID;
		m_name = name;
		m_teacher = teacher;
		m_studentCount = studentCount;
	}
	
	
	
	public Long getID() {
		
		return m_id;
	}
	
	public Long getModuleID() {
		
		return m_moduleID;
	}
	
	public String getName() {
		
		return m_name;
	}
	
	public String getTeacher() {
		
		return m_teacher;
	}
	
	public Integer getStudentCount() {
		
		return m_studentCount;
	}
	
	
	
	@Override
	public String toString() {
		
		return m_name;
	}
}