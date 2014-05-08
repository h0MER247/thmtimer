package de.thm.mni.thmtimer.model;

import java.util.LinkedList;
import java.util.List;

// -----------------------------------------------------------------------------------
// Sebastian: Das hier ist erstmal vorlaeufig damit ich irgendwas anzeigen lassen kann.
//            Es wird im Endeffekt sowieso ganz anders gemacht ^^
// -----------------------------------------------------------------------------------
public class Module {	

	private static final long serialVersionUID = -556636604371414680L;
	private long    m_id;             // ModulID
	private String  m_name;           // Name
	private int     m_studentCount;   // Anzahl Studenten in diesem Modul
	private String  m_teacher;        // Dozent
	private String  m_semester;       // Semester
	private String  m_timeInvested;   // Gesamte investierte Zeit in dieses Modul
	private boolean m_timeLogRunning; // Zeiterfassung laeuft (Ja / Nein)
	private List<TimeTracking> timeTracking = new LinkedList<>();
	
	
	
	public Module(int id, String name, int studentCount, String teacher, String semester, String timeInvested, boolean timeLogRunning) {
		
		m_id = id;
		m_name = name;
		m_studentCount = studentCount;
		m_teacher = teacher;
		m_semester = semester;
		m_timeInvested = timeInvested;
		m_timeLogRunning = timeLogRunning;
	}
	
	
	public List<TimeTracking> getTimeTracking(){
		return this.timeTracking;
	}
	
	public void addTimeTracking(TimeTracking time){
		this.timeTracking.add(time);
	}
	
	
	public long getID() {
		
		return m_id;
	}
	
	public String getName() {
		
		return m_name;
	}
	
	public int getStudentCount() {
		
		return m_studentCount;
	}
	
	public String getTeacher() {
		
		return m_teacher;
	}
	
	public String getSemester() {
		
		return m_semester;
	}
	
	public float getTimeInvested() {
		float time = 0;
		for(TimeTracking t:timeTracking){
			time += t.hours;
			time += t.minutes/60;
		}
		return time;
	}
	
	public boolean getTimeLogRunning() {
		
		return m_timeLogRunning;
	}
	
	@Override
	public String toString() {
		return m_name;
	}
}