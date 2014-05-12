package de.thm.mni.thmtimer.model;

import java.util.LinkedList;
import java.util.List;

public class Module {
	private long id;
	private String name;
	private int studentCount;
	private String teacher;
	private String semester;
	private boolean timeLogRunning;
	private List<TimeTracking> timeTracking = new LinkedList<TimeTracking>();

	public Module(int id, String name, int studentCount, String teacher, String semester, boolean timeLogRunning) {
		this.id = id;
		this.name = name;
		this.studentCount = studentCount;
		this.teacher = teacher;
		this.semester = semester;
		this.timeLogRunning = timeLogRunning;
	}

	public List<TimeTracking> getTimeTracking() {
		return this.timeTracking;
	}

	public void addTimeTracking(TimeTracking time) {
		this.timeTracking.add(time);
	}

	public long getID() {

		return id;
	}

	public String getName() {

		return name;
	}

	public int getStudentCount() {

		return studentCount;
	}

	public String getTeacher() {

		return teacher;
	}

	public String getSemester() {

		return semester;
	}

	public float getTimeInvested() {
		float time = 0;
		for (TimeTracking t : timeTracking) {
			time += t.hours;
			time += t.minutes / 60;
		}
		return time;
	}

	public boolean getTimeLogRunning() {

		return timeLogRunning;
	}

	@Override
	public String toString() {
		return name;
	}
}