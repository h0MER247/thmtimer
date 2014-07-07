package de.thm.mni.thmtimer.util;

import java.util.List;

import org.springframework.http.HttpMethod;
import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.StudentData;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Expenditure;

public class ModuleDAO {
	
	private static Module mModule;
	private static Course mCourse;
	private static List<Module> mModules;
	private static List<Course> mCourses;
	private static List<TimeCategory> mTimeCategorys;
	private static StudentData mStudentData;
	private static TeacherData mTeacherData;
	
	public static StudentData getStudentData() {
		return mStudentData;
	}

	public static TeacherData getTeacherData() {
		return mTeacherData;
	}

	public static List<TimeCategory> getTimeCategorys() {
		return mTimeCategorys;
	}

	public static List<Course> getCourseList() {
		return mCourses;
	}

	public static List<Module> getModuleList() {
		/* Error: liefert einzelnes Modul, keine Liste
		 * 
		mModules = Connection.request("/modules",
					HttpMethod.GET, Module.class); */
		return mModules;  
	}

	public static Module findModule(Long moduleID) {
		return Connection.request("/modules/moduleID?" + moduleID,
				HttpMethod.GET, Module.class);
	}

	public static Course findCourse(Long courseID) {
		return Connection.request("/courses/courseID",
				HttpMethod.GET, Course.class);
	}

	public static Category findTimeCategory(Long categoryID) {
		return Connection.request("/categories/id?" + categoryID, HttpMethod.GET, Category.class );
	}
	
	//Course:
	
	//Fetch courses by thm username. Id must be passed by URL.
	public static List<Course> getCoursesByUsername(String username){
		/* Response Model sollte eine Liste sein
		mCourses = Connection.request("/courses/user/" + username, HttpMethod.GET , Course.class);		
		 */
		return mCourses;
	}
	
	//Fetch one course by id. Id must be passed by URL.
	public static Course getCoursesById(int id){
		mCourse = Connection.request("/courses/id?" + id, HttpMethod.GET , Course.class);		
		return mCourse;
	}
	
	//Add a user to the course with id in URL.
	public static void addUserToCourse(int id, String username){
		Connection.request("/courses/" + id +"/user/" + username,HttpMethod.POST, null);
	}
	
	//Delete a user from the course with id in URL.
	public static void deleteUserFromCourse(int id, String username){
		Connection.request("/courses/" + id +"/user/" + username,HttpMethod.DELETE, null);
	}
	
	/*
	 * Search courses with the specified values. 
	 * If no course with this values exists an empty List is returned. 
	 * If a parameter is null the value is ignored in search.
	 
	public static List<Course> getCourses(String courseName, int termID, int moduleID, String moduleName, int facultyId, boolean isClosed){
		mCourse = Connection.request("/courses"+ moduleID",
				HttpMethod.GET, Course.class);
		return mCourses;
		
	} */
	
	 
	
	//Expenditure:
	
	//Fetch a list of all expenditure entries of the active user in DB
	public static Expenditure getExpenditures(){
		return Connection.request("/expenditures", HttpMethod.GET , Expenditure.class);
	}
	
	//Fetch all expenditure entries by course id belongs to the active user
		public static Expenditure getExpendituresByCourse(int id){
			return Connection.request("/expenditures/course/" + id, HttpMethod.GET , Expenditure.class);
		}
	
	//Delete a expenditure entry by id
	public static void deleteExpenditure(int id){
		Connection.request("/expenditures/course/" + id, HttpMethod.DELETE , null);
	}
	
	

}
