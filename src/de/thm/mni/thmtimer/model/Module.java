package de.thm.mni.thmtimer.model;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Module {
	private long id;
	private String name;
	private int studentCount;
	private int cp;
	private Date startDate;
	private String teacher;
	private String semester;
	private List<TimeTracking> timeTracking = new ArrayList<TimeTracking>();

	public Module(int id, String name, int studentCount, String teacher, String semester, int cp) {
		this.id = id;
		this.name = name;
		this.studentCount = studentCount;
		this.teacher = teacher;
		this.semester = semester;
		this.cp = cp;
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

	public String getTimeInvested() {
		int time = 0;
		for (TimeTracking t : timeTracking) {
			time += t.minutes;
		}
		return String.format("%d:%02d", time / 60, time % 60);
	}

	@Override
	public String toString() {
		return name;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}