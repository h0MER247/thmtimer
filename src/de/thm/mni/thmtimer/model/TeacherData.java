package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherData {

	// ToDo: Mehr Statistikdaten als die Zeitstatistik hinzuf�gen
	private ArrayList<Long> mCourseIDs;
	private HashMap<Long, ArrayList<TimeStatisticData>> mTimeStatistic;

	public TeacherData() {
		mCourseIDs = new ArrayList<Long>();
		mTimeStatistic = new HashMap<Long, ArrayList<TimeStatisticData>>();
	}

	public void addCourse(Long courseID) {
		mCourseIDs.add(courseID);
	}

	public ArrayList<Long> getCourseIDs() {
		return mCourseIDs;
	}

	public boolean hasCourses() {
		return mCourseIDs.size() > 0;
	}

	public void addTimeStatistic(Long courseID, TimeStatisticData data) {
		ArrayList<TimeStatisticData> list;
		if (mTimeStatistic.get(courseID) != null) {
			list = mTimeStatistic.get(courseID);
		} else {
			list = new ArrayList<TimeStatisticData>();
			mTimeStatistic.put(courseID, list);
		}
		list.add(data);
	}

	public ArrayList<TimeStatisticData> getTimeStatistic(Long courseID) {
		return mTimeStatistic.get(courseID);
	}

	//
	// Hilfsfunktionen
	//
	public TimeData getTimeInvestedTotal(Long courseID) {
		ArrayList<TimeStatisticData> statisticData = mTimeStatistic.get(courseID);
		Integer timeInMinutes = 0;
		//
		// Alle erfassten Zeiten zusammenzählen
		//
		if (statisticData != null) {
			for (TimeStatisticData t : statisticData) {
				timeInMinutes += t.getTime().getTimeInMinutes();
			}
		}
		TimeData data = new TimeData();
		data.setTimeInMinutes(timeInMinutes);

		return data;
	}
}