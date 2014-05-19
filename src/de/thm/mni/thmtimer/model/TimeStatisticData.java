package de.thm.mni.thmtimer.model;

public class TimeStatisticData {

	private Long m_categoryID;
	private TimeData m_time;

	public TimeStatisticData(Long categoryID, TimeData time) {
		m_categoryID = categoryID;
		m_time = time;
	}

	public Long getCategoryID() {
		return m_categoryID;
	}

	public TimeData getTime() {
		return m_time;
	}
}