package de.thm.mni.thmtimer.model;

public class Course {

	private Long mId;
	private Long mModuleID;
	private String mName;
	private String mTeacher;
	private Integer mStudentCount;

	public Course(Long id, Long moduleID, String name, String teacher, Integer studentCount) {

		mId = id;
		mModuleID = moduleID;
		mName = name;
		mTeacher = teacher;
		mStudentCount = studentCount;
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

	@Override
	public String toString() {
		return mName;
	}
}