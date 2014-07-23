package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.os.AsyncTask;
import de.thm.thmtimer.entities.Course;
import de.thm.thmtimer.entities.Expenditure;
import de.thm.thmtimer.entities.User;
import de.thm.mni.thmtimer.R;
import de.thm.thmtimer.entities.Category;


public class ModuleDAO {
	
	private static User mUser;
	private static List<Course> mAllCourses;
	private static List<Course> mStudentCourses;
	private static List<Expenditure> mStudentExpenditures;
	private static List<Course> mTeacherCourses;
	private static List<Category>    mTimeCategorys;
	
	
	

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
	
	
	
	
	public static void getExpendituresFromServer(int requestID) {

		addJob(GET_EXPENDITURES, requestID);
	}
	
	public static void postExpenditureToServer(int requestID, Expenditure expenditure) {
		
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
	
	public interface ServerOperation {
		
		public void run();
		public void setParameter(Object... parameter);
		public int  getDialogMessage();
	}
	
	
	/**
	 * Server Operations
	 * 
	 * Hier alle Serveroperationen implementieren die wir benötigen !!!
	 */
	public static ServerOperation GET_USER = new ServerOperation() {
		
		@Override
		public void run() {
			
			if(!isUserInvalidated())
				return;
			
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
	
	public static ServerOperation GET_TIMECATEGORYS = new ServerOperation() {
		
		@Override
		public void run() {
			
			if(!isTimeCategorysInvalid())
				return;
			
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
	
	public static ServerOperation GET_STUDENTCOURSELIST = new ServerOperation()  {
		
		@Override
		public void run() {
			
			if(!isStudentCourseListInvalid())
				return;
			
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
	
	public static ServerOperation GET_TEACHERCOURSELIST = new ServerOperation() {
		
		@Override
		public void run() {
			
			if(!isTeacherCourseListInvalid())
				return;
			
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
	
	public static ServerOperation GET_ALLCOURSELIST = new ServerOperation() {
		
		@Override
		public void run() {
			
            if(!isAllCourseListInvalid())
                return;
            
            Course[] courses = Connection.request("/courses/" + Connection.getUsername(),
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
	
	public static ServerOperation POST_EXPENDITURE = new ServerOperation() {
		
		Expenditure mExpenditure;
		
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

	public static ServerOperation GET_EXPENDITURES = new ServerOperation() {
		
		@Override
		public void run() {
			
			if(!isStudentExpendituresInvalid())
				return;
			
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
	
	
	
	
	
	/**
	 * Hier passiert die Magic :)
	 * 
	 * @param currentActivityOrFragment
	 *    Referenz auf die Aktivität oder das Fragment in welchem man diese Funktion aufruft.
	 *    
	 * @param op
	 *    GET-Operation, die am Server durchgeführt werden soll
	 *    
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
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
		
		public ServerOperation getOperation() { return mOperation; }
		public int getRequestID() { return mRequestID; }
		public void setErrorMessage(String msg) { mErrorMessage = msg; mOk = false; }
		public String getErrorMessage() { return mErrorMessage; }
	}
	
	private static ArrayList<ServerJob> mJobs;
	
	
	
	
	public static void beginJob() {
		
		mJobs = new ArrayList<ServerJob>();
	}
	
	private static void addJob(ServerOperation operation, int requestID) {
		
		if(mJobs == null)
			throw new IllegalArgumentException("You cant add to a job before you start one!");
		
		mJobs.add(new ServerJob(operation, requestID));
	}
	
	public static void commitJob(final AbstractAsyncView yourActivityOrView) {
		
		if(mJobs == null)
			throw new IllegalArgumentException("Cant commit an unstarted job");
		
		new AsyncTask<ServerJob, ServerJob, Boolean>() {
			
			private String mErrorMessage;
			
			@Override
			protected Boolean doInBackground(ServerJob... params) {
				
				for(ServerJob job : params) {
					
					int requestID      = job.getRequestID();
					ServerOperation op = job.getOperation();
					
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
				
				return true;
			}
			
	        @Override
	        protected void onProgressUpdate(ServerJob... values) {
	        	
	            super.onProgressUpdate(values);
	            
	            yourActivityOrView.dismissProgressDialog();
	            
	            
	            ServerJob job = values[0];
	            
	            if(job.mOk)
	            	yourActivityOrView.showProgressDialog(job.getOperation().getDialogMessage());
	            else
	            	yourActivityOrView.onDAOError(job.getRequestID(), job.getErrorMessage());
	            
	        }
	        
			protected void onPostExecute(Boolean result) {
				
				yourActivityOrView.dismissProgressDialog();
				
				if(result)
					yourActivityOrView.onDAOFinished();
			}
			
		}.execute(mJobs.toArray(new ServerJob[mJobs.size()]));
	}
}