package de.thm.mni.thmtimer.model;

// -----------------------------------------------------------------------------------
// Sebastian: Das hier ist erstmal vorl�ufig damit ich irgendwas anzeigen lassen kann.
//            Es wird im Endeffekt sowieso ganz anders gemacht ^^
// -----------------------------------------------------------------------------------
public class ModuleData {	

	private int     m_id;             // ModulID
	private String  m_name;           // Name
	private int     m_studentCount;   // Anzahl Studenten in diesem Modul
	private String  m_teacher;        // Dozent
	private String  m_semester;       // Semester
	private String  m_timeInvested;   // Gesamte investierte Zeit in dieses Modul
	private boolean m_timeLogRunning; // Zeiterfassung l�uft (Ja / Nein)
	
	
	
	public ModuleData(int id, String name, int studentCount, String teacher, String semester, String timeInvested, boolean timeLogRunning) {
		
		m_id = id;
		m_name = name;
		m_studentCount = studentCount;
		m_teacher = teacher;
		m_semester = semester;
		m_timeInvested = timeInvested;
		m_timeLogRunning = timeLogRunning;
	}
	
	
	
	public int getID() {
		
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
	
	public String getTimeInvested() {
		
		return m_timeInvested;
	}
	
	public boolean getTimeLogRunning() {
		
		return m_timeLogRunning;
	}
}