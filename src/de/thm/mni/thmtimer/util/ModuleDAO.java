package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.Expenditure;
import de.thm.thmtimer.entities.Module;
import de.thm.thmtimer.entities.User;
import de.thm.mni.thmtimer.R;
import de.thm.thmtimer.entities.Category;


public class ModuleDAO {
	
	private static User mUser;
	private static List<Course> mAllCourses;
	private static List<Course> mStudentCourses;
	private static List<Expenditure> mStudentExpenditures;
	private static List<Course> mTeacherCourses;
	private static List<Category> mTimeCategorys;
	private static List<Module> mModules;
	
	
	

	/*
	public static void getSurvivalPackageFromServer() {
		
		beginJob();
		getUserFromServer(0);
		getStudentCourseListFromServer(requestID);
		getTeacherCourseList();
		getTimeCategorys();
		getModules();
		
		
	}*/
	
	
	
	// --------- MODULE
	
	public static void getModulesFromServer(int requestID) {
		
		addJob(GET_MODULES, requestID);
	}
	
	public static List<Module> getModuleList() {
		
		return mModules;
	}
	
	public static Module getModuleByID(long id) {
		
		if(!isModulesInvalidated()) {
			
			for(Module m : mModules) {
				
				if(m.getId() == id)
					return m;
			}
		}
		
		return null;
	}
	
	private static boolean isModulesInvalidated() {
		
		return mModules == null;
	}
	
	public static void invalidateModules() {
		
		mModules = null;
	}
	
	

	// --------- USER
	
	public static void getUserFromServer(int requestID) {
		
		addJob(GET_USER, requestID);
	}
	
	public static User getUser() {
		
		return mUser;
	}

	public static boolean isUserInvalidated() {
		
		return mUser == null;
	}
	
	public static void invalidateUser() {
		
		mUser = null;
	}
	
	
	
	// --------- TIME CATEGORYS
	
	public static void getTimeCategorysFromServer(int requestID) {
		
		addJob(GET_TIMECATEGORYS, requestID);
	}
	
	public static Category getTimeCategoryByID(Long id) {
		
		if(!isTimeCategorysInvalid()) {
			
			for(Category c : mTimeCategorys) {
				
				if(c.getId() == id)
					return c;
			}
		}
		
		return null;
	}
	
	public static List<Category> getTimeCategorys() {
		
		return mTimeCategorys;
	}
	
	public static boolean isTimeCategorysInvalid() {
		
		return mTimeCategorys == null;
	}
		
	public static void invalidateTimeCategorys() {
		
		mTimeCategorys = null;
	}
	
	
	
	// --------- STUDENT COURSE LIST
	
	public static void addStudentToCourse(int requestID, Long courseID) {
		
		ADD_STUDENT_TO_COURSE.setParameter(courseID);
		addJob(ADD_STUDENT_TO_COURSE, requestID);
	}
	
	public static void getStudentCourseListFromServer(int requestID) {
		
		addJob(GET_STUDENTCOURSELIST, requestID);
	}
	
	public static List<Course> getStudentCourseList() {
		
		return mStudentCourses;
	}
	
	public static boolean isStudentCourseListInvalid() {
		
		return mStudentCourses == null;
	}
	
	public static void invalidateStudentCourseList() {
		
		mStudentCourses = null;
	}
	
	public static Course getStudentCourseByID(Long id) {
		
		if(!isStudentCourseListInvalid()) {
			
			for(Course c : mStudentCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}
	


	// --------- TEACHER COURSE LIST
	
	public static void getTeacherCourseListFromServer(int requestID) {
		
		addJob(GET_TEACHERCOURSELIST, requestID);
	}
	
	public static List<Course> getTeacherCourseList() {
		
		return mTeacherCourses;
	}
	
	public static Boolean isTeacherCourseListInvalid() {
		
		return mTeacherCourses == null;
	}
	
	public static void invalidateTeacherCourseList() {
		
		mTeacherCourses = null;
	}
	
	public static Course getTeacherCourseByID(Long id) {
		
		if(!isTeacherCourseListInvalid()) {
			
			for(Course c : mTeacherCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}
	
	
	
	// --------- ALL COURSE LIST 
	
	public static void getAllCourseListFromServer(int requestID) {
		
		addJob(GET_ALLCOURSELIST, requestID);
	}
	
	public static List<Course> getAllCourseList() {
		
		return mAllCourses;
	}
	
	public static Boolean isAllCourseListInvalid() {
		
		return mAllCourses == null;
	}
	
	public static void invalidateAllCourseList() {
		
		mAllCourses = null;
	}
	
	public static Course getAllCourseByID(Long id) {
		
		if(!isAllCourseListInvalid()) {
			
			for(Course c : mAllCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}	
	
	
	
	
	// --------- EXPENDITURES
	
	public static void getStudentExpendituresFromServer(int requestID) {

		addJob(GET_EXPENDITURES, requestID);
	}
	
	public static void postStudentExpenditureToServer(int requestID, Expenditure expenditure) {
		
		POST_EXPENDITURE.setParameter(expenditure);
		
		addJob(POST_EXPENDITURE, requestID);
	}
	
	public static Boolean isStudentExpendituresInvalid() {
		
		return mStudentExpenditures == null;
	}
	
	public static void invalidateStudentExpenditures() {
		
		mStudentExpenditures = null;
	}
	
	public static List<Expenditure> getStudentExpendituresByCourseID(Long courseID) {
		
		ArrayList<Expenditure> ret = new ArrayList<Expenditure>();
		
		if(!isStudentExpendituresInvalid()) {
			
			for(Expenditure e : mStudentExpenditures) {
				
				if(e.getCourse().getId() == courseID)
					ret.add(e);
			}
		}
		
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	//
	// --------------- Serverkommunikation
	//

	
	
	/**
	 * Server Operations
	 * 
	 * Hier alle Serveroperationen implementieren die wir benötigen !!!
	 */
	
	//
	// User Objekt laden
	//
	private static ServerOperation GET_USER = new ServerOperation() {
		
		@Override
		public boolean runIf() {
			
			return isUserInvalidated();
		}
		
		@Override
		public void run() {
			
			mUser = Connection.request("/users/" + Connection.getUsername(),
					                   HttpMethod.GET,
					                   User.class);
		}

		@Override
		public void setParameter(Object... parameter) {
			
		}

		@Override
		public int getDialogMessage() {
			
			return R.string.connection_loading;
		}
	};
	
	//
	// Zeitkategorien holen
	//
	private static ServerOperation GET_TIMECATEGORYS = new ServerOperation() {
		
		@Override
		public boolean runIf() {
			
			return isTimeCategorysInvalid();
		}
		
		@Override
		public void run() {
			
			Category[] categorys = Connection.request("/categories/",
					                                  HttpMethod.GET,
					                                  Category[].class);
			
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
	private static ServerOperation GET_STUDENTCOURSELIST = new ServerOperation()  {
		
		@Override
		public boolean runIf() {
			
			return isStudentCourseListInvalid();
		}
		
		@Override
		public void run() {
			
			Course[] Courses = Connection.request("/courses/user/" + Connection.getUsername(),
					                     HttpMethod.GET,
					                     Course[].class);

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
	private static ServerOperation GET_TEACHERCOURSELIST = new ServerOperation() {
		
		@Override
		public boolean runIf() {
			
			return isTeacherCourseListInvalid();
		}
		
		@Override
		public void run() {
			
			/*
			// TODO: Warten bis das Serverteam den Bug gefixed hat und die Liste abrufbar ist
			Course[] Courses = Connection.request("/courses/lecture/" + Connection.getUsername(),
					                                        HttpMethod.GET,
					                                        Course[].class);
			*/
			Course[] Courses = Connection.request("/courses/user/" + Connection.getUsername(),
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
	private static ServerOperation GET_ALLCOURSELIST = new ServerOperation() {
		
		@Override
		public boolean runIf() {
			
			return isAllCourseListInvalid();
		}
		
		@Override
		public void run() {
			
            Course[] courses = Connection.request("/courses/",
                                                  HttpMethod.GET,
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
	private static ServerOperation POST_EXPENDITURE = new ServerOperation() {
		
		private Expenditure mExpenditure;
		
		@Override
		public boolean runIf() {
			
			return true;
		}
		
		@Override
		public void run() {
			
			Expenditure expenditure = Connection.request("/expenditures/",
					                                     HttpMethod.POST,
					                                     mExpenditure,
					                                     Expenditure.class);
			
			if(mStudentExpenditures == null)
				mStudentExpenditures = new ArrayList<Expenditure>();
			
			mStudentExpenditures.add(expenditure);
		}

		@Override
		public void setParameter(Object... parameter) {
			
			mExpenditure = (Expenditure)parameter[0];
		}
		
		@Override
		public int getDialogMessage() {
			
			return R.string.connection_saving_expenditures;
		}
	};

	//
	// Liste mit Aufwänden vom Server holen
	//
	private static ServerOperation GET_EXPENDITURES = new ServerOperation() {
		
		@Override
		public boolean runIf() {
			
			return isStudentExpendituresInvalid();
		}
		
		@Override
		public void run() {
			
			Expenditure[] expenditures = Connection.request("/expenditures/",
					                                        HttpMethod.GET,
					                                        Expenditure[].class);
			
			if(mStudentExpenditures == null)
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
	private static ServerOperation GET_MODULES = new ServerOperation() {

		@Override
		public boolean runIf() {

			return isModulesInvalidated();
		}

		@Override
		public void run() {
			
			Module[] modules = Connection.request("/modules",
					                              HttpMethod.GET,
					                              Module[].class);
			
			mModules = Arrays.asList(modules);
		}

		@Override
		public void setParameter(Object... parameter) {
			
		}

		@Override
		public int getDialogMessage() {
			
			// TODO
			return R.string.connection_loading;
		}
		
		
	};
	
	//
	// Student in einen Kurs einschreiben
	//
	private static ServerOperation ADD_STUDENT_TO_COURSE = new ServerOperation() {
		
		private Long mCourseID;
		
		@Override
		public boolean runIf() {
			
			return true;
		}

		@Override
		public void run() {
			
			Connection.request("/courses/" + mCourseID.toString() + "/user/" + Connection.getUsername(),
					           HttpMethod.POST,
					           null);
			
			invalidateStudentCourseList();
		}

		@Override
		public void setParameter(Object... parameter) {

			mCourseID = (Long)parameter[0];
		}

		@Override
		public int getDialogMessage() {
			
			// TODO
			return R.string.connection_loading;
		}
	};
	
	

	
	
	
	
	
	
	
	
	
	private interface ServerOperation {
		
		public boolean runIf();
		
		public void    run();
		public void    setParameter(Object... parameter);
		
		public int     getDialogMessage();
	}
	
	private static class ServerJob {
		
		private ServerOperation mOperation;
		private int             mRequestID;
		private boolean         mOk;
		private String          mErrorMessage;
		
		public ServerJob(ServerOperation operation, int requestID) {
			
			mOperation = operation;
			mRequestID = requestID;
			
			mOk = true;
		}
		
		public ServerOperation getOperation() { 
			
			return mOperation;
		}
		
		public int getRequestID() {
			
			return mRequestID;
		}
		
		public void setErrorMessage(String msg) {
			
			mErrorMessage = msg;
			mOk = false;
		}
		
		public String getErrorMessage() {
			
			return mErrorMessage;
		}
	}
	
	private static ArrayList<ServerJob> mJobs;
	
	
	
	/**
	 * Neuen Job starten
	 */
	public static void beginJob() {
		
		mJobs = new ArrayList<ServerJob>();
	}
	
	
	/**
	 * Job hinzufügen
	 * 
	 * @param operation
	 *    Gewünschte Operation die ausgeführt werden soll
	 * @param requestID
	 *    Eine requestID welche im Fehlerfall benötigt wird. Sollte beim commiten
	 *    ein Fehler auftreten, wird in der Activity onDAOError() mit der entsprechenden
	 *    requestID aufgerufen, und man kann dann entsprechend auf den Fehler reagieren.
	 */
	private static void addJob(ServerOperation operation, int requestID) {
		
		if(mJobs == null)
			throw new IllegalArgumentException("You cant add to a job before you start one!");
		
		mJobs.add(new ServerJob(operation, requestID));
	}
	
	
	/**
	 * Jobs ausführen
	 * 
	 * @param ctx
	 *   Kontext (bei Activity: this, bei Fragment: getActivity())
	 * @param listener
	 *   Listener um Fehlermeldungen zu bekommen und benachrichtigt zu werden, wenn der
	 *   Job erfolgreich abgeschlossen wurde.
	 */
	public static void commitJob(final Context ctx, final ModuleDAOListener listener) {
		
		if(mJobs == null)
			throw new IllegalArgumentException("Cant commit an unstarted job");
		
		
		AsyncTask<ServerJob, ServerJob, Boolean> worker = new AsyncTask<ServerJob, ServerJob, Boolean>() {
			
			private ProgressDialog mDialog;
			
			@Override
			protected void onPreExecute() {
				
				mDialog = new ProgressDialog(ctx);
				
				mDialog.setCancelable(false);
				mDialog.setCanceledOnTouchOutside(false);
				mDialog.setIndeterminate(true);
			}

			@Override
			protected Boolean doInBackground(ServerJob... params) {
				
				for(ServerJob job : params) {
					
					ServerOperation op = job.getOperation();
					
					if(op.runIf()) {
						
						try {
							
							publishProgress(job);
							op.run();
						}
		                catch(final HttpClientErrorException e) {
		                	
		                	job.setErrorMessage(e.toString());
		                	publishProgress(job);
		                    return false;
		                }
		                catch(final ResourceAccessException e) {
		                	
		                	job.setErrorMessage(e.toString());
		                	publishProgress(job);
		                    return false;
		                }
					}
				}
				
				return true;
			}
			
	        @Override
	        protected void onProgressUpdate(ServerJob... values) {
	        	
	            super.onProgressUpdate(values);
	            
	            ServerJob job = values[0];
	            
	            if(job.mOk) {
	            	
	            	mDialog.setMessage(ctx.getResources().getString(job.getOperation().getDialogMessage()));
	            	mDialog.show();
	            }
	            else {
	            	
	            	if(listener != null) 
	            		listener.onDAOError(job.getRequestID(), job.getErrorMessage());
	            }
	        }
	        
			protected void onPostExecute(Boolean result) {
				
				if((mDialog != null) && mDialog.isShowing())
					mDialog.dismiss();
				
				mDialog = null;
				
				if(result && (listener != null))
					listener.onDAOFinished();
			}
		};
		
		worker.execute(mJobs.toArray(new ServerJob[mJobs.size()]));
	}
}