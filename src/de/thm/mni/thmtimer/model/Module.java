package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Module {

	private Long mId;
	private String mName;
	private Integer mCreditPoints;
	private String m_description;
	private String mModuleNumber;
	private List<Course> mCourseList = new ArrayList<Course>();
	private TimeData mTimeToInvestInHours;
	private Date mStartDate;

	// ToDo: Mehr Informationen zu einem Modul hinzufügen!

	public Module(Long id, String name, String moduleNumber, Integer creditPoints, Integer timeToInvestInHours,
			String description) {

		mId = id;
		mName = name;
		mModuleNumber = moduleNumber;
		mCreditPoints = creditPoints;
		m_description = description;
		mTimeToInvestInHours = new TimeData(timeToInvestInHours, 0);
		GregorianCalendar gc = new GregorianCalendar(2014, 4, 1);
		setStartDate(new Date(gc.getTimeInMillis()));
	}

	public Long getID() {

		return mId;
	}

	public String getName() {

		return mName;
	}

	public String getModuleNumber() {

		return mModuleNumber;
	}

	public Integer getCreditPoints() {

		return mCreditPoints;
	}

	public String getDescription() {
		return m_description;
	}

	public List<Course> getCourseList() {
		return mCourseList;
	}

	public void addCourse(Course course) {
		mCourseList.add(course);
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