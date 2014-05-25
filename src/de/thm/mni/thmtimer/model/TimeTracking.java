package de.thm.mni.thmtimer.model;

import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTracking {

	private Long mId;
	private Long mCategoryID;
	private TimeData mTime;

	public TimeTracking(Long id, Long categoryID, String description, TimeData time) {
		mId = id;
		mCategoryID = categoryID;
		mTime = time;
	}

	public Long getID() {
		return mId;
	}

	public Long getCategoryID() {
		return mCategoryID;
	}

	public TimeData getTime() {
		return mTime;
	}

	@Override
	public String toString() {
		return StaticModuleData.findTimeCategory(mCategoryID).toString() + " (" + mTime.toString() + ")";
	}
}