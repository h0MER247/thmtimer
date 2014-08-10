package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpMethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.DurationPerCategory;
import de.thm.mni.thmtimer.model.DurationPerWeek;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.Expenditure;
import de.thm.thmtimer.entities.Module;
import de.thm.thmtimer.entities.Term;
import de.thm.thmtimer.entities.User;

public class ModuleDAO {

	private static User mUser = null;
	private static List<Course> mAllCourses = null;
	private static List<Course> mStudentCourses = null;
	private static List<Expenditure> mStudentExpenditures = null;
	private static List<Course> mTeacherCourses = null;
	private static List<Category> mTimeCategorys = null;
	private static List<Module> mModules = null;
	private static List<Term> mTerms = null;
	private static List<DurationPerCategory> mDurationPerCategory = null;
	private static List<DurationPerWeek> mDurationPerWeek = null;
	
	
	//
	// --------- ZEIT JE KATEGORIE PRO WOCHE
	//
	public static void getDurationPerWeekFromServer(int requestID, Long courseID) {
		
		ServerOperation op = new GET_DURATION_PER_WEEK(requestID);
		op.setParameter(courseID);
		
		addJob(op);
	}
	
	public static List<DurationPerWeek> getDurationPerWeek() {
		
		return mDurationPerWeek;
	}
	
	public static void invalidateDurationPerWeek() {
		
		mDurationPerWeek = null;
	}
	
	//
	// --------- ZEIT JE KATEGORIE
	//

	public static void getDurationPerCategoryFromServer(int requestID,
			Long courseID) {

		ServerOperation op = new GET_DURATION_PER_CATEGORY(requestID);
		op.setParameter(courseID);

		addJob(op);
	}

	public static List<DurationPerCategory> getDurationPerCategory() {

		return mDurationPerCategory;
	}

	public static void invalidateDurationPerCategory() {

		mDurationPerCategory = null;
	}

	//
	// --------- MODULE
	//

	public static void getModuleListFromServer(int requestID) {

		addJob(new GET_MODULES(requestID));
	}

	public static List<Module> getModuleList() {

		return mModules;
	}

	public static Module getModuleByID(long id) {

		if (mModules != null) {

			for (Module m : mModules) {

				if (m.getId() == id)
					return m;
			}
		}

		return null;
	}

	public static void invalidateModules() {

		mModules = null;
	}
	
	//
	// --------- TERMS
	//
	
	public static void getTermListFromServer(int requestID) {

		addJob(new GET_TERMS(requestID));
	}
	
	public static List<Term> getTermList() {

		return mTerms;
	}

	//
	// --------- BENUTZER
	//

	public static void getUserFromServer(int requestID) {

		addJob(new GET_USER(requestID));
	}

	public static User getUser() {

		return mUser;
	}

	public static void invalidateUser() {

		mUser = null;
	}

	//
	// --------- ZEIT KATEGORIEN
	//

	public static void getTimeCategorysFromServer(int requestID) {

		addJob(new GET_TIMECATEGORYS(requestID));
	}

	public static List<Category> getTimeCategorys() {

		return mTimeCategorys;
	}

	public static Category getTimeCategoryByID(Long id) {

		if (mTimeCategorys != null) {

			for (Category c : mTimeCategorys) {

				if (c.getId() == id)
					return c;
			}
		}

		return null;
	}

	public static void invalidateTimeCategorys() {

		mTimeCategorys = null;
	}

	//
	// --------- STUDENTEN IN EINEN KURS EINSCHREIBEN
	//

	public static void addStudentToCourse(int requestID, Long courseID) {

		ServerOperation op = new ADD_STUDENT_TO_COURSE(requestID);
		op.setParameter(courseID);

		addJob(op);
	}

	//
	// --------- NEUEN KURS ERSTELLEN
	//

	public static void addCourse(int requestID, Long termID, Long moduleID,
			String name, Long startDate, String description) {

		ServerOperation op = new ADD_COURSE(requestID);
		op.setParameter(termID, moduleID, name, startDate, description);

		addJob(op);
	}

	//
	// --------- KURSLISTE DES STUDENTEN ABFRAGEN
	//

	public static void getStudentCourseListFromServer(int requestID) {

		addJob(new GET_STUDENTCOURSELIST(requestID));
	}

	public static List<Course> getStudentCourseList() {

		return mStudentCourses;
	}

	public static Course getStudentCourseByID(Long id) {

		if (mStudentCourses != null) {

			for (Course c : mStudentCourses) {

				if (c.getId() == id) {

					return c;
				}
			}
		}

		return null;
	}

	public static void invalidateStudentCourseList() {

		mStudentCourses = null;
	}

	//
	// --------- KURSLISTE DES DOZENTEN ABFRAGEN
	//

	public static void getTeacherCourseListFromServer(int requestID) {

		addJob(new GET_TEACHERCOURSELIST(requestID));
	}

	public static List<Course> getTeacherCourseList() {

		return mTeacherCourses;
	}

	public static Course getTeacherCourseByID(Long id) {

		if (mTeacherCourses != null) {

			for (Course c : mTeacherCourses) {

				if (c.getId() == id) {

					return c;
				}
			}
		}

		return null;
	}

	public static void invalidateTeacherCourseList() {

		mTeacherCourses = null;
	}

	//
	// --------- KURSLISTE ALLER VERFÜGBAREN KURSE ABFRAGEN
	//

	public static void getFullCourseListFromServer(int requestID) {

		addJob(new GET_FULLCOURSELIST(requestID));
	}

	public static List<Course> getFullCourseList() {

		return mAllCourses;
	}

	public static Course searchCourseByID(Long courseID) {

		if (mAllCourses != null) {

			for (Course c : mAllCourses) {

				if (c.getId() == courseID)
					return c;
			}
		}

		return null;
	}

	public static void invalidateFullCourseList() {

		mAllCourses = null;
	}

	//
	// --------- AUFWÄNDE ABRUFEN / ABSPEICHERN / LÖSCHEN
	//

	public static void getStudentExpendituresFromServer(int requestID) {

		addJob(new GET_ALL_EXPENDITURES(requestID));
	}

	public static void postStudentExpenditureToServer(int requestID,
			Expenditure expenditure) {

		ServerOperation op = new POST_EXPENDITURE(requestID);
		op.setParameter(expenditure);

		addJob(op);
	}

	public static void putStudentExpenditureToServer(int requestID,
			Expenditure expenditure) {

		ServerOperation op = new PUT_EXPENDITURE(requestID);
		op.setParameter(expenditure);

		addJob(op);
	}
	
	public static void deleteStudentExpenditureFromServer(int requestID, Long expenditureID) {
		
		ServerOperation op = new DELETE_EXPENDITURE(requestID);
		op.setParameter(expenditureID);
		
		addJob(op);
	}

	public static List<Expenditure> getStudentExpendituresByCourseID(
			Long courseID) {

		ArrayList<Expenditure> ret = new ArrayList<Expenditure>();

		if (mStudentExpenditures != null) {

			for (Expenditure e : mStudentExpenditures) {

				if (e.getCourse().getId() == courseID)
					ret.add(e);
			}
		}

		return ret;
	}

	public static Expenditure getStudentExpenditureByID(Long expenditureID) {

		if (mStudentExpenditures != null) {

			for (Expenditure e : mStudentExpenditures) {

				if (e.getId() == expenditureID)
					return e;
			}
		}

		return null;
	}

	public static void invalidateStudentExpenditures() {

		mStudentExpenditures = null;
	}

	/**
	 * ========================================================================
	 * ===================== Server Operationen
	 * ==================================
	 * ===========================================================
	 * 
	 * Hier alle Serveroperationen implementieren die wir benötigen !!!
	 */

	//
	// User laden
	//
	private static class GET_USER extends ServerOperation {

		public GET_USER(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mUser == null;
		}

		@Override
		public void run() {

			mUser = Connection.request("/users/" + Connection.getUsername(),
					HttpMethod.GET, User.class);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_user;
		}
	};

	//
	// Zeitkategorien holen
	//
	private static class GET_TIMECATEGORYS extends ServerOperation {

		public GET_TIMECATEGORYS(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mTimeCategorys == null;
		}

		@Override
		public void run() {

			Category[] categorys = Connection.request("/categories/",
					HttpMethod.GET, Category[].class);

			mTimeCategorys = Arrays.asList(categorys);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_categories;
		}
	};

	//
	// Kursliste für die Rolle Student laden
	//
	private static class GET_STUDENTCOURSELIST extends ServerOperation {

		public GET_STUDENTCOURSELIST(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mStudentCourses == null;
		}

		@Override
		public void run() {

			Course[] Courses = Connection.request(
					"/courses/user/" + Connection.getUsername(),
					HttpMethod.GET, Course[].class);

			mStudentCourses = Arrays.asList(Courses);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_courses;
		}
	};

	//
	// Kursliste für die Rolle Dozent laden
	//
	private static class GET_TEACHERCOURSELIST extends ServerOperation {

		public GET_TEACHERCOURSELIST(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return (mTeacherCourses == null) && ((mUser != null) && mUser.isLecteur());
		}

		@Override
		public void run() {
			
			Course[] Courses = Connection.request("/courses/lecturer/" + Connection.getUsername(),
					                              HttpMethod.GET,
					                              Course[].class);
			
			mTeacherCourses = Arrays.asList(Courses);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_courses;
		}
	};

	//
	// Alle Kurse vom Server laden (für die Suche)
	//
	private static class GET_FULLCOURSELIST extends ServerOperation {

		public GET_FULLCOURSELIST(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mAllCourses == null;
		}

		@Override
		public void run() {

			Course[] courses = Connection.request("/courses/", HttpMethod.GET,
					Course[].class);

			mAllCourses = Arrays.asList(courses);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_courses;
		}
	};

	//
	// Aufwandseintrag zum Server schicken
	//
	private static class POST_EXPENDITURE extends ServerOperation {

		private Expenditure mExpenditure;

		public POST_EXPENDITURE(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return true;
		}

		@Override
		public void run() {

			Expenditure expenditure = Connection.request("/expenditures/",
					HttpMethod.POST, mExpenditure, Expenditure.class);

			if (mStudentExpenditures == null)
				mStudentExpenditures = new ArrayList<Expenditure>();

			mStudentExpenditures.add(expenditure);
		}

		@Override
		public void setParameter(Object... parameter) {

			mExpenditure = (Expenditure) parameter[0];
		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_saving_expenditures;
		}
	};

	//
	// Aufwandseintrag editieren
	//
	private static class PUT_EXPENDITURE extends ServerOperation {

		private Expenditure mExpenditure;

		public PUT_EXPENDITURE(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return true;
		}

		@Override
		public void run() {

			Expenditure expenditure = Connection.request("/expenditures/"
					+ mExpenditure.getId(), HttpMethod.PUT, mExpenditure,
					Expenditure.class);

			// Dieses Expenditure in unserer lokalen Liste editieren
			for (int i = 0; i < mStudentExpenditures.size(); i++) {

				Expenditure e = mStudentExpenditures.get(i);

				if (e.getId() == expenditure.getId()) {

					mStudentExpenditures.set(i, expenditure);
					break;
				}
			}
		}

		@Override
		public void setParameter(Object... parameter) {

			mExpenditure = (Expenditure) parameter[0];
		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_saving_expenditures;
		}
	}
	
	//
	// Aufwandseintrag löschen
	//
	private static class DELETE_EXPENDITURE extends ServerOperation {

		private Long mExpenditureID;
		
		public DELETE_EXPENDITURE(int requestID) {
			
			super(requestID);
		}
		
		@Override
		public boolean runIf() {
			
			return true;
		}

		@Override
		public void run() {
			
			Connection.request("/expenditures/" + mExpenditureID.toString(),
					           HttpMethod.DELETE,
					           null);
			
			// Dieses Expenditure aus der lokalen Liste löschen
			for(int i = 0;
					i < mStudentExpenditures.size();
					i++) {
				
				Expenditure e = mStudentExpenditures.get(i);
				
				if(e.getId() == mExpenditureID) {
					
					mStudentExpenditures.remove(i);
					break;
				}
			}
		}

		@Override
		public void setParameter(Object... parameter) {
			
			mExpenditureID = (Long)parameter[0];
		}

		@Override
		public int getDialogMessage() {
			
			return R.string.connection_deleting_expenditure;
		}
	}
	

	//
	// Liste mit Aufwänden vom Server holen
	//
	private static class GET_ALL_EXPENDITURES extends ServerOperation {

		public GET_ALL_EXPENDITURES(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mStudentExpenditures == null;
		}

		@Override
		public void run() {

			Expenditure[] expenditures = Connection.request("/expenditures/",
					HttpMethod.GET, Expenditure[].class);

			if (mStudentExpenditures == null)
				mStudentExpenditures = new ArrayList<Expenditure>();

			mStudentExpenditures.addAll(Arrays.asList(expenditures));
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_expenditures;
		}
	};

	//
	// Module vom Server holen
	//
	private static class GET_MODULES extends ServerOperation {

		public GET_MODULES(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mModules == null;
		}

		@Override
		public void run() {

			Module[] modules = Connection.request("/modules", HttpMethod.GET,
					Module[].class);

			mModules = Arrays.asList(modules);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_modules;
		}
	};

	//
	// Terms vom Server holen
	//
	private static class GET_TERMS extends ServerOperation {

		public GET_TERMS(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mTerms == null;
		}

		@Override
		public void run() {

			Term[] terms = Connection.request("/terms", HttpMethod.GET,
					Term[].class);

			mTerms = Arrays.asList(terms);
		}

		@Override
		public void setParameter(Object... parameter) {

		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_terms;
		}
	};

	//
	// Student in einen Kurs einschreiben
	//
	private static class ADD_COURSE extends ServerOperation {

		private Long mTermID, mModuleID, mStartDate;
		private String mName, mDescription;

		public ADD_COURSE(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return true;
		}

		@Override
		public void run() {
			Course c = new Course();
			c.setDescription(mDescription);
			c.setIsClosed(false);
			for (Term t : mTerms) {
				if (t.getId() == mTermID) {
					c.setTerm(t);
				}
			}
			for (Module m : mModules) {
				if (m.getId() == mModuleID) {
					c.setModule(m);
				}
			}
			List<User> l = new ArrayList<User>();
			l.add(mUser);
			c.setLecturer(l);
			c.setUsers(null);
			c.setName(mName);
			c.setStart(new Date(mStartDate));
			/*JSONObject course = new JSONObject();
			try {
				course.put("description", mDescription);
				course.put("start", mStartDate);
				for (Term t : mTerms) {
					if (t.getId() == mTermID) {
						JSONObject term = new JSONObject();
						term.put("id", t.getId());
						term.put("type", t.getType());
						term.put("year", t.getYear());
						term.put("start", t.getStart().getTime());
						term.put("end", t.getEnd().getTime());
						course.put("term", term);
					}
				}
				for (Module m : mModules) {
					if (m.getId() == mModuleID) {
						JSONObject module = new JSONObject();
						module.put("id", m.getId());
						module.put("number", m.getNumber());
						module.put("shortcut", m.getShortcut());
						module.put("name", m.getName());
						module.put("creditPoints", m.getCreditPoints());
						Faculty f = m.getFaculty();
						JSONObject faculty = new JSONObject();
						faculty.put("id", f.getId());
						faculty.put("number", f.getNumber());
						faculty.put("shortcut", f.getShortcut());
						faculty.put("name", f.getName());
						module.put("faculty", faculty);
						course.put("module", module);
					}
				}
				course.put("isClosed", false);
				JSONArray lecturers = new JSONArray();
				JSONObject lecturer = new JSONObject();
				lecturer.put("thmUsername", mUser.getThmUsername());
				lecturer.put("firstName", mUser.getFirstName());
				lecturer.put("lastName", mUser.getLastName());
				lecturer.put("lecteur", mUser.isLecteur());
				lecturer.put("admin", mUser.isAdmin());
				lecturers.put(lecturer);
				course.put("lecturer", lecturers);
				JSONArray users = new JSONArray();
				JSONObject user = new JSONObject();
				user.put("thmUsername", null);
				user.put("firstName", null);
				user.put("lastName", null);
				user.put("lecteur", false);
				user.put("admin", false);
				users.put(user);
				course.put("users", users);
				course.put("name", mName);
			} catch (JSONException e) {
				e.printStackTrace();
			}*/

			Connection.request("/courses", HttpMethod.POST, c, Course.class);

			invalidateTeacherCourseList();
		}

		@Override
		public void setParameter(Object... parameter) {

			mTermID = (Long) parameter[0];
			mModuleID = (Long) parameter[1];
			mName = (String) parameter[2];
			mStartDate = (Long) parameter[3];
			mDescription = (String) parameter[4];
		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_create_course;
		}
	};

	//
	// Student in einen Kurs einschreiben
	//
	private static class ADD_STUDENT_TO_COURSE extends ServerOperation {

		private Long mCourseID;

		public ADD_STUDENT_TO_COURSE(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return true;
		}

		@Override
		public void run() {

			Connection.request("/courses/" + mCourseID.toString() + "/user/"
					+ Connection.getUsername(), HttpMethod.POST, null);

			invalidateStudentCourseList();
		}

		@Override
		public void setParameter(Object... parameter) {

			mCourseID = (Long) parameter[0];
		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_join_course;
		}
	};

	//
	// Zeiten pro Kategorie anfordern
	//

	private static class GET_DURATION_PER_CATEGORY extends ServerOperation {

		private Long mCourseID;

		public GET_DURATION_PER_CATEGORY(int requestID) {

			super(requestID);
		}

		@Override
		public boolean runIf() {

			return mDurationPerCategory == null;
		}

		@Override
		public void run() {

			DurationPerCategory[] durations = Connection.request(
					"/statistics/category/" + mCourseID.toString(),
					HttpMethod.GET, DurationPerCategory[].class);

			mDurationPerCategory = Arrays.asList(durations);
		}

		@Override
		public void setParameter(Object... parameter) {

			mCourseID = (Long) parameter[0];
		}

		@Override
		public int getDialogMessage() {

			return R.string.connection_loading_statistics;
		}
	};
	
	
	//
	// Zeiten je Kategorie pro Woche anfordern
	
	private static class GET_DURATION_PER_WEEK extends ServerOperation {

		private Long mCourseID;
		
		public GET_DURATION_PER_WEEK(int requestID) {
			
			super(requestID);
		}
		
		
		@Override
		public boolean runIf() {

			return mDurationPerWeek == null;
		}

		@Override
		public void run() {
			
			DurationPerWeek[] durations = Connection.request("/statistics/week/" + mCourseID.toString(),
					                                         HttpMethod.GET,
					                                         DurationPerWeek[].class);
			
			mDurationPerWeek = Arrays.asList(durations);
		}
		
		@Override
		public void setParameter(Object... parameter) {

			mCourseID = (Long)parameter[0];
		}

		@Override
		public int getDialogMessage() {
			
			return R.string.connection_loading_statistics;
		}
	};
	
	
	
	

	// TODO: Mehr ServerOperations unterstützen

	// ---------------------------------------------------------------------------------------------

	private static abstract class ServerOperation {

		private int mRequestID;
		private String mErrorMessage;
		private boolean mFailed;

		public ServerOperation(Integer requestID) {

			mRequestID = requestID;

			mErrorMessage = "";
			mFailed = false;
		}

		public void setFailed(String error) {

			mErrorMessage = error;
			mFailed = true;
		}

		public boolean hasFailed() {

			return mFailed;
		}

		public String getErrorMessage() {

			return mErrorMessage;
		}

		public int getRequestID() {

			return mRequestID;
		}

		/* Hier zurückgeben, was gelten muss damit run() aufgerufen wird */
		public abstract boolean runIf();

		/* Hier Logik implementieren, welche mit dem Server kommuniziert, etc. */
		public abstract void run();

		/* Hier Parameter holen, die für run() benötigt werden */
		public abstract void setParameter(Object... parameter);

		/* Hier die Meldung für das ProgressDialog als R.string.xyz zurückgeben */
		public abstract int getDialogMessage();
	}

	private static ArrayList<ServerOperation> mJobs;
	private static WorkerTask mJobWorker;

	/**
	 * Neuen Job starten
	 */
	public static void beginJob() {

		if (mJobs != null)
			throw new IllegalArgumentException("A job is still pending!");

		mJobs = new ArrayList<ServerOperation>();
	}

	/**
	 * Job hinzufügen
	 * 
	 * @param operation
	 *            Gewünschte Operation die ausgeführt werden soll
	 */
	private static void addJob(ServerOperation operation) {

		if (mJobs == null)
			throw new IllegalArgumentException(
					"Start a job with beginJob() first!");

		mJobs.add(operation);
	}

	/**
	 * Jobs ausführen
	 * 
	 * @param ctx
	 *            Kontext (bei Activity: this, bei Fragment: getActivity())
	 * @param listener
	 *            Listener um Fehlermeldungen zu bekommen und benachrichtigt zu
	 *            werden, wenn der Job erfolgreich abgeschlossen wurde.
	 */
	public static void commitJob(Context context, ModuleDAOListener listener) {

		if (mJobs == null)
			throw new IllegalArgumentException(
					"Can't commit an unstarted job! Start one with beginJob().");

		if (context == null)
			throw new IllegalArgumentException(
					"ModuleDAO needs a context to display the progressdialog! (The context is 'this' on activitys and 'getActivity()' on fragments)");

		if (listener == null)
			throw new IllegalArgumentException("Give ModuleDAO a listener!");

		if (mJobs.size() > 0) {

			mJobWorker = new WorkerTask(context, listener);
			mJobWorker
					.execute(mJobs.toArray(new ServerOperation[mJobs.size()]));
		}
	}

	/**
	 * Neuen Kontext für den Job bereitstellen
	 * 
	 * @param ctx
	 *            Kontext (bei Activity: this, bei Fragment: getActivity())
	 * @param listener
	 *            Listener um Fehlermeldungen zu bekommen und benachrichtigt zu
	 *            werden, wenn der Job erfolgreich abgeschlossen wurde.
	 */
	public static void setNewContext(Context context, ModuleDAOListener listener) {

		if (listener == null)
			throw new IllegalArgumentException("Give ModuleDAO a listener!");

		if ((mJobs != null) && (mJobWorker != null)) {

			mJobWorker.setNewContext(context, listener);
		}
	}

	/**
	 * WorkerTask: Führt alle Jobs im Hintergrund aus und zeigt dabei einen
	 * ProgressDialog an
	 * 
	 */
	private static class WorkerTask extends
			AsyncTask<ServerOperation, ServerOperation, Boolean> {

		private Context mContext, mNewContext;
		private ModuleDAOListener mListener, mNewListener;
		private Boolean mContextChanged;
		private ProgressDialog mDialog;

		public WorkerTask(Context context, ModuleDAOListener listener) {

			mContext = context;
			mListener = listener;

			mContextChanged = false;
		}

		@Override
		protected void onPreExecute() {

			createProgressDialog();
		}

		@Override
		protected Boolean doInBackground(ServerOperation... params) {

			for (ServerOperation op : params) {

				if (op.runIf()) {

					try {

						publishProgress(op);
						op.run();
					} catch (Exception e) {

						op.setFailed(e.toString());
						publishProgress(op);

						return false;
					}
				}
			}

			return true;
		}

		@Override
		protected void onProgressUpdate(ServerOperation... values) {

			super.onProgressUpdate(values);

			ServerOperation op = values[0];

			checkContext();

			if (op.hasFailed()) {

				if (mListener != null)
					mListener.onDAOError(op.getRequestID(),
							op.getErrorMessage());
			} else {

				mDialog.setMessage(mContext.getResources().getString(
						op.getDialogMessage()));

				if (!mDialog.isShowing())
					mDialog.show();
			}
		}

		protected void onPostExecute(Boolean result) {

			mJobs = null;

			checkContext();

			if (result && (mListener != null))
				mListener.onDAOFinished();

			if ((mDialog != null) && mDialog.isShowing())
				mDialog.dismiss();

			mDialog = null;
		}

		public void setNewContext(Context context, ModuleDAOListener listener) {

			mNewContext = context;
			mNewListener = listener;

			mContextChanged = true;
		}

		private void checkContext() {

			if (mContextChanged) {

				mContext = mNewContext;
				mListener = mNewListener;

				if (getStatus() == AsyncTask.Status.RUNNING)
					createProgressDialog();

				mContextChanged = false;
			}
		}

		private void createProgressDialog() {

			mDialog = new ProgressDialog(mContext);

			mDialog.setCancelable(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setIndeterminate(true);
		}
	}
}