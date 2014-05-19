package de.thm.mni.thmtimer.model;

public class TimeCategory {
	private Long mId;
	private String mDescription;

	public TimeCategory(Long id, String description) {
		mId = id;
		mDescription = description;
	}

	public Long getID() {
		return mId;
	}

	public String getDescription() {
		return mDescription;
	}

	@Override
	public String toString() {
		return mDescription;
	}
}