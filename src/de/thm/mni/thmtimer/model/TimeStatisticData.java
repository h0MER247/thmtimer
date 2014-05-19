package de.thm.mni.thmtimer.model;

public class TimeStatisticData {

	private Long mCategoryID;
	private TimeData mTime;

	public TimeStatisticData(Long categoryID, TimeData time) {
		mCategoryID = categoryID;
		mTime = time;
	}

	public Long getCategoryID() {
		return mCategoryID;
	}

	public TimeData getTime() {
		return mTime;
	}
}