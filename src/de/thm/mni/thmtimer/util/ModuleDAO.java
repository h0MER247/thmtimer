package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
	
	
	
	//
	// --------- MODULE
	//
	
	public static void getModuleListFromServer(int requestID) {
		
		addJob(new GET_MODULES(), requestID);
	}
	
	public static List<Module> getModuleList() {
		
		return mModules;
	}
	
	public static Module getModuleByID(long id) {
		
		if(mModules != null) {
			
			for(Module m : mModules) {
				
				if(m.getId() == id)
					return m;
			}
		}
		
		return null;
	}
	
	public static void invalidateModules() {
		
		mModules = null;
	}
	
	

	//
	// --------- BENUTZER
	//
	
	public static void getUserFromServer(int requestID) {
		
		addJob(new GET_USER(), requestID);
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
		
		addJob(new GET_TIMECATEGORYS(), requestID);
	}
	
	public static List<Category> getTimeCategorys() {
		
		return mTimeCategorys;
	}
	
	public static Category getTimeCategoryByID(Long id) {
		
		if(mTimeCategorys != null) {
			
			for(Category c : mTimeCategorys) {
				
				if(c.getId() == id)
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
		
		ServerOperation op = new ADD_STUDENT_TO_COURSE();
		op.setParameter(courseID);
		
		addJob(op, requestID);
	}
	
	

	//
	// --------- KURSLISTE DES STUDENTEN ABFRAGEN
	//
	
	public static void getStudentCourseListFromServer(int requestID) {
		
		addJob(new GET_STUDENTCOURSELIST(), requestID);
	}
	
	public static List<Course> getStudentCourseList() {
		
		return mStudentCourses;
	}
	
	public static Course getStudentCourseByID(Long id) {
		
		if(mStudentCourses != null) {
			
			for(Course c : mStudentCourses) {
				
				if(c.getId() == id) {
					
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
		
		addJob(new GET_TEACHERCOURSELIST(), requestID);
	}
	
	public static List<Course> getTeacherCourseList() {
		
		return mTeacherCourses;
	}
	
	public static Course getTeacherCourseByID(Long id) {
		
		if(mTeacherCourses != null) {
			
			for(Course c : mTeacherCourses) {
				
				if(c.getId() == id) {
					
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
		
		addJob(new GET_FULLCOURSELIST(), requestID);
	}
	
	public static List<Course> getFullCourseList() {
		
		return mAllCourses;
	}
	
	public static void invalidateFullCourseList() {
		
		mAllCourses = null;
	}
	
	
	
	//
	// --------- AUFWÄNDE ABRUFEN / ABSPEICHERN
	//
	
	public static void getStudentExpendituresFromServer(int requestID) {

		addJob(new GET_ALL_EXPENDITURES(), requestID);
	}
	
	public static void postStudentExpenditureToServer(int requestID, Expenditure expenditure) {
		
		ServerOperation op = new POST_EXPENDITURE();
		op.setParameter(expenditure);
		
		addJob(op, requestID);
	}
	
	public static List<Expenditure> getStudentExpendituresByCourseID(Long courseID) {
		
		ArrayList<Expenditure> ret = new ArrayList<Expenditure>();
		
		if(mStudentExpenditures != null) {
			
			for(Expenditure e : mStudentExpenditures) {
				
				if(e.getCourse().getId() == courseID)
					ret.add(e);
			}
		}
		
		return ret;
	}
	
	public static void invalidateStudentExpenditures() {
		
		mStudentExpenditures = null;
	}
	
	
	
	

	
	/**
	 * =============================================================================================
	 * Server Operationen
	 * =============================================================================================
	 * 
	 * Hier alle Serveroperationen implementieren die wir benötigen !!!
	 */
	
	//
	// User laden
	//
	private static class GET_USER extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mUser == null;
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
			
			// TODO: Eigener String dafür
			return R.string.connection_loading;
		}
	};
	
	
	//
	// Zeitkategorien holen
	//
	private static class GET_TIMECATEGORYS extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mTimeCategorys == null;
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
	private static class GET_STUDENTCOURSELIST extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mStudentCourses == null;
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
	private static class GET_TEACHERCOURSELIST extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mTeacherCourses == null;
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
	private static class GET_FULLCOURSELIST extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mAllCourses == null;
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
	private static class POST_EXPENDITURE extends ServerOperation {
		
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
	private static class GET_ALL_EXPENDITURES extends ServerOperation {
		
		@Override
		public boolean runIf() {
			
			return mStudentExpenditures == null;
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
	private static class GET_MODULES extends ServerOperation {

		@Override
		public boolean runIf() {

			return mModules == null;
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
			
			//TODO: Eigener String dafür
			return R.string.connection_loading;
		}
	};
	
	
	//
	// Student in einen Kurs einschreiben
	//
	private static class ADD_STUDENT_TO_COURSE extends ServerOperation {
		
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
			
			//TODO: Eigener String dafür
			return R.string.connection_loading;
		}
	};
	
	
	
	// TODO: Mehr ServerOperations unterstützen
	

	// ---------------------------------------------------------------------------------------------
	
	
	
	private static abstract class ServerOperation {
		
		public abstract boolean runIf();
		
		public abstract void run();
		public abstract void setParameter(Object... parameter);
		
		public abstract int getDialogMessage();
	}
	
	private static class ServerJob {
		
		private ServerOperation mOperation;
		private int mRequestID;
		private boolean mOk;
		private String mErrorMessage;
		
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
	private static AsyncTask<ServerJob, ServerJob, Boolean> mJobWorker;
	
	
	/**
	 * Neuen Job starten
	 */
	public static void beginJob() {
		
		if((mJobs != null) && (mJobWorker != null)) {
			
			try {
				
				//
				// Der Job wird aus irgendwelchen Gründen noch verarbeitet. Wir warten einfach
				// bis er fertig ist.
				//
				mJobWorker.get();
			}
			catch (InterruptedException e) {
				
			}
			catch (ExecutionException e) {
				
			}
		}
		
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
			throw new IllegalArgumentException("Start a job with beginJob() first!");
		
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
			throw new IllegalArgumentException("Can't commit an unstarted job! Start one with beginJob().");
		
		if(mJobs.size() == 0)
			return;
		
		
		mJobWorker = new AsyncTask<ServerJob, ServerJob, Boolean>() {
			
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
	            	
	            	if(!mDialog.isShowing())
	            		mDialog.show();
	            }
	            else {
	            	
	            	if(listener != null) 
	            		listener.onDAOError(job.getRequestID(), job.getErrorMessage());
	            }
	        }
	        
			protected void onPostExecute(Boolean result) {
						
				mJobs = null;
				
				if(result && (listener != null))
					listener.onDAOFinished();
				
				if((mDialog != null) && mDialog.isShowing())
					mDialog.dismiss();
				
				mDialog = null;
			}
		};
		
		mJobWorker.execute(mJobs.toArray(new ServerJob[mJobs.size()]));
	}
}