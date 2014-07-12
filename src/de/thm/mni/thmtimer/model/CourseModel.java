package de.thm.mni.thmtimer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.thm.thmtimer.entities.Course;

public class CourseModel extends Course {

	
	/** Dummy */
	public int getStudentCount() {
		return 10;
	}
	
	/** Dummy until implemented by Serverteam (Bug #11005)*/
	public Date getStartDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d;
		try {
			d = sdf.parse("01/04/2014");
		} catch (ParseException e) {
			return null;
		}
		return d;
	}
	
	public long getModuleID() {
		return 1;
	}

	public String getTeacher() {
		return "Teacher";
	}
	
}
