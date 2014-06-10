package de.thm.mni.thmtimer.model;

import java.util.Date;
import java.util.GregorianCalendar;

public class Course {

	private Long mId;
	private Long mModuleID;
	private String mName;
	private String mTeacher;
	private Integer mStudentCount;
	private Date mStartDate;

	public Course(Long id, Long moduleID, String name, String teacher, Integer studentCount) {
		mId = id;
		mModuleID = moduleID;
		mName = name;
		mTeacher = teacher;
		mStudentCount = studentCount;
		
		//Später entfernen
		GregorianCalendar gc = new GregorianCalendar(2014, 3, 1);
		setStartDate(new Date(gc.getTimeInMillis()));
	}

	public Long getID() {
		return mId;
	}

	public Long getModuleID() {
		return mModuleID;
	}

	public String getName() {
		return mName;
	}

	public String getTeacher() {
		return mTeacher;
	}

	public Integer getStudentCount() {
		return mStudentCount;
	}
	
	public Date getStartDate() {
		return mStartDate;
	}

	public void setStartDate(Date m_startDate) {
		this.mStartDate = m_startDate;
	}

	@Override
	public String toString() {
		return mName;
	}
}