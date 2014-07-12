package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpMethod;

import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.Module;
import de.thm.mni.thmtimer.model.CourseModel;
import de.thm.mni.thmtimer.model.StudentData;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Expenditure;

public class ModuleDAO {
	private static List<Module> mModules;
	private static List<CourseModel> mStudentCourses;
	private static List<CourseModel> mTeacherCourses;
	private static List<Category> mTimeCategorys;
	private static StudentData mStudentData;
	private static TeacherData mTeacherData;

	public static StudentData getStudentData() {
		return mStudentData;
	}

	public static TeacherData getTeacherData() {
		return mTeacherData;
	}

	public static List<Category> getTimeCategorys() {
		return mTimeCategorys;
	}

	public static List<Module> getModuleList() {
		/*
		 * Error: liefert einzelnes Modul, keine Liste
		 * 
		 * mModules = Connection.request("/modules", HttpMethod.GET,
		 * Module.class);
		 */
		return mModules;
	}

	public static Module findModule(Long moduleID) {
		return Connection.request("/modules/moduleID?" + moduleID, HttpMethod.GET, Module.class);
	}

	public static List<CourseModel> getStudentCourseList() {
		if (mStudentCourses == null) {
			CourseModel[] c = Connection.request("/courses/user/" + Connection.username, HttpMethod.GET,
					CourseModel[].class);
			mStudentCourses = Arrays.asList(c);
		}
		return mStudentCourses;
	}

	public static void invalidateStudentCourseList() {
		mStudentCourses = null;
	}

	public static List<Long> getStudentCourseIDs() {
		if(mStudentCourses == null)
			throw new IllegalStateException("Studentcourses not yet loaded.");
		List<Long> ids = new ArrayList<Long>();
		for (Course c : mStudentCourses) {
			ids.add(c.getId());
		}
		return ids;
	}

	public static CourseModel findStudentCourse(Long courseID) {
		for (CourseModel c : mStudentCourses) {
			if (c.getId() == courseID)
				return c;
		}
		throw new IllegalArgumentException("Course with ID " + courseID + " not found");
	}
	
	public static List<Long> getTeacherCourseIDs() {
		if(mTeacherCourses == null)
			throw new IllegalStateException("Teachercourses not yet loaded.");
		List<Long> ids = new ArrayList<Long>();
		for (CourseModel c : mTeacherCourses) {
			ids.add(c.getId());
		}
		return ids;
	}
	
	public static CourseModel findTeacherCourse(Long courseID) {
		for (CourseModel c : mTeacherCourses) {
			if (c.getId() == courseID)
				return c;
		}
		throw new IllegalArgumentException("Course with ID " + courseID + " not found");
	}

	public static Category findTimeCategory(Long categoryID) {
		return Connection.request("/categories/id?" + categoryID, HttpMethod.GET, Category.class);
	}

	// Fetch one course by id. Id must be passed by URL.
	public static Course getCoursesById(int id) {
		return Connection.request("/courses/id?" + id, HttpMethod.GET, Course.class);
	}

	// Add a user to the course with id in URL.
	public static void addUserToCourse(int id, String username) {
		Connection.request("/courses/" + id + "/user/" + username, HttpMethod.POST, null);
	}

	// Delete a user from the course with id in URL.
	public static void deleteUserFromCourse(int id, String username) {
		Connection.request("/courses/" + id + "/user/" + username, HttpMethod.DELETE, null);
	}

	/*
	 * Search courses with the specified values. If no course with this values
	 * exists an empty List is returned. If a parameter is null the value is
	 * ignored in search.
	 * 
	 * public static List<Course> getCourses(String courseName, int termID, int
	 * moduleID, String moduleName, int facultyId, boolean isClosed){ mCourse =
	 * Connection.request("/courses"+ moduleID", HttpMethod.GET, Course.class);
	 * return mCourses;
	 * 
	 * }
	 */

	// Expenditure:

	// Fetch a list of all expenditure entries of the active user in DB
	public static Expenditure getExpenditures() {
		return Connection.request("/expenditures", HttpMethod.GET, Expenditure.class);
	}

	// Fetch all expenditure entries by course id belongs to the active user
	public static Expenditure getExpendituresByCourse(int id) {
		return Connection.request("/expenditures/course/" + id, HttpMethod.GET, Expenditure.class);
	}

	// Delete a expenditure entry by id
	public static void deleteExpenditure(int id) {
		Connection.request("/expenditures/course/" + id, HttpMethod.DELETE, null);
	}
}
