package de.thm.mni.thmtimer.model;

import de.thm.mni.thmtimer.util.StaticModuleData;

public class TimeTracking {

	private Long m_id;
	private Long m_categoryID;
	private TimeData m_time;

	public TimeTracking(Long id, Long categoryID, String description, TimeData time) {

		m_id = id;
		m_categoryID = categoryID;
		m_time = time;
	}

	public Long getID() {

		return m_id;
	}

	public Long getCategoryID() {

		return m_categoryID;
	}

	public TimeData getTime() {

		return m_time;
	}

	@Override
	public String toString() {
		return StaticModuleData.findTimeCategory(m_categoryID).toString() + " (" + m_time.getTimeStringHHMM() + ")";
	}
}