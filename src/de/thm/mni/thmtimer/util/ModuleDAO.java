package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import de.thm.thmtimer.entities.Expenditure;
import de.thm.thmtimer.entities.User;
import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.model.CourseModel;
import de.thm.thmtimer.entities.Category;


public class ModuleDAO {
	
	private static Integer mJobSize = -1;
	
	private static User mUser;
	private static List<CourseModel> mAllCourses;
	private static List<CourseModel> mStudentCourses;
	private static List<Expenditure> mStudentExpenditures;
	private static List<CourseModel> mTeacherCourses;
	private static List<Category>    mTimeCategorys;
	
	
	
	/*
	 * Gibt die Anzahl der vom DAO gewünschten Ressourcen an, sind dann alle "loadXYZFromServer" Anfragen
	 * erfolgreich fertiggestellt wird onDAOFinished() aufgerufen.
	 */
	public static void setJobSize(int size) {
		
		synchronized(mJobSize) {
		
			mJobSize = size;
		}
	}
	public static int getJobSize() {
		
		synchronized(mJobSize) {
			
			return mJobSize;
		}
	}
	public static boolean hasFinishedJob() {
		
		return mJobSize == 0;
	}
	
	
	public static void loadUserFromServer(AbstractAsyncView ctx, int daoRequestID) {
		
		DoServerOperation(ctx, daoRequestID, R.string.connection_loading, new Runnable() {
			
			@Override
			public void run() {
				
				if(!isUserInvalidated())
					return;
				
				mUser = Connection.request("/users/" + Connection.getUsername(),
						                   HttpMethod.GET,
						                   User.class);
			}
		});
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
	
	
	

	
	public static void loadTimeCategorysFromServer(AbstractAsyncView ctx, int daoRequestID) {
		
		DoServerOperation(ctx, daoRequestID, R.string.connection_loading_categories, new Runnable() {
			
			@Override
			public void run() {
				
				if(!isTimeCategorysInvalid())
					return;
				
				Category[] categorys = Connection.request("/categories/",
						                                  HttpMethod.GET,
						                                  Category[].class);
				
				mTimeCategorys = Arrays.asList(categorys);
			}
		});
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
	
	
	
	
	public static void loadStudentCourseListFromServer(AbstractAsyncView ctx, int daoRequestID) {
		
		DoServerOperation(ctx, daoRequestID, R.string.connection_loading_courses, new Runnable() {
			
			@Override
			public void run() {
				
				if(!isStudentCourseListInvalid())
					return;
				
				CourseModel[] courseModels = Connection.request("/courses/user/" + Connection.getUsername(),
						                     HttpMethod.GET,
						                     CourseModel[].class);

				mStudentCourses = Arrays.asList(courseModels);
			}
		});
	}
	
	
	public static List<CourseModel> getStudentCourseList() {
		
		return mStudentCourses;
	}
	
	public static boolean isStudentCourseListInvalid() {
		
		return mStudentCourses == null;
	}
	
	public static void invalidateStudentCourseList() {
		
		mStudentCourses = null;
	}
	
	public static CourseModel getStudentCourseByID(Long id) {
		
		if(!isStudentCourseListInvalid()) {
			
			for(CourseModel c : mStudentCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}
	
	
	
	

	public static void loadTeacherCourseListFromServer(AbstractAsyncView ctx, int daoRequestID) {

		DoServerOperation(ctx, daoRequestID, R.string.connection_loading_courses, new Runnable() {
			
			@Override
			public void run() {
				
				if(!isTeacherCourseListInvalid())
					return;
				
				/*
				CourseModel[] courseModels = Connection.request("/courses/lecture/" + Connection.getUsername(),
						                                        HttpMethod.GET,
						                                        CourseModel[].class);
				*/
				CourseModel[] courseModels = Connection.request("/courses/user/" + Connection.getUsername(),
						                                        HttpMethod.GET,
						                                        CourseModel[].class);
				
				mTeacherCourses = Arrays.asList(courseModels);
			}
		});
	}
	
	public static List<CourseModel> getTeacherCourseList() {
		
		return mTeacherCourses;
	}
	
	public static Boolean isTeacherCourseListInvalid() {
		
		return mTeacherCourses == null;
	}
	
	public static void invalidateTeacherCourseList() {
		
		mTeacherCourses = null;
	}
	
	public static CourseModel getTeacherCourseByID(Long id) {
		
		if(!isTeacherCourseListInvalid()) {
			
			for(CourseModel c : mTeacherCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}
	
	
	
	
	
	/**
	 * Lädt alle verfügbaren Kurse vom Server
	 * 
	 * @param ctx
	 *   Activity oder Fragment
	 * @param id
	 *   RequestID welche bei onDAOSuccess() oder onDAOError() zurückgegeben wird
	 */
	public static void loadAllCourseListFromServer(AbstractAsyncView ctx, int daoRequestID) {

		DoServerOperation(ctx, daoRequestID, R.string.connection_loading_courses, new Runnable() {
			
			@Override
			public void run() {
				
				if(!isAllCourseListInvalid())
					return;
				
				CourseModel[] courseModels = Connection.request("/courses/",
						                                        HttpMethod.GET,
						                                        CourseModel[].class);
				
				mAllCourses = Arrays.asList(courseModels);
			}
		});
	}
	
	public static List<CourseModel> getAllCourseList() {
		
		return mAllCourses;
	}
	
	public static Boolean isAllCourseListInvalid() {
		
		return mAllCourses == null;
	}
	
	public static void invalidateAllCourseList() {
		
		mAllCourses = null;
	}
	
	public static CourseModel getAllCourseByID(Long id) {
		
		if(!isAllCourseListInvalid()) {
			
			for(CourseModel c : mAllCourses) {
				
				if(c.getId() == id) {
					
					return c;
				}
			}
		}
		
		return null;
	}	
	
	
	
	
	
	
	public static void postStudentExpenditureToServer(AbstractAsyncView ctx, int daoRequestID, final Expenditure expenditureData) {
		
		DoServerOperation(ctx, daoRequestID, R.string.connection_saving_expenditures, new Runnable() {
			
			@Override
			public void run() {
				
				Expenditure expenditure = Connection.request("/expenditures/",
						                                     HttpMethod.POST,
						                                     expenditureData,
						                                     Expenditure.class);
				
				if(mStudentExpenditures == null)
					mStudentExpenditures = new ArrayList<Expenditure>();
				
				mStudentExpenditures.add(expenditure);
			}
		});
	}
	
	public static void loadStudentExpendituresFromServer(AbstractAsyncView ctx, int daoRequestID) {
		
		DoServerOperation(ctx, daoRequestID, R.string.connection_loading_expenditures, new Runnable() {
			
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
		});
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
	 * Holt Daten vom Server
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
	private static void DoServerOperation(final AbstractAsyncView view,
			                              final int requestID,
			                              final int dialogMessage,
			                              final Runnable operation) {
		
		// Task initialisieren
		AsyncTask<Void, Void, Boolean> at = new AsyncTask<Void, Void, Boolean>() {
			
			private String mErrorMessage = "Unknown Error";
					
			@Override
			protected void onPreExecute() {
				
				Log.d("LOG", "Showing Progessdialog with string ID" + dialogMessage);
				
				if(view != null)
					view.showProgressDialog(dialogMessage);
			}
				
			@Override
			protected Boolean doInBackground(Void... params) {
					
				try {
				
					operation.run();
					return true;
				}
				catch(HttpClientErrorException e) {
					
					mErrorMessage = e.toString();
					return false;
				}
				catch(ResourceAccessException e) {
						
					mErrorMessage = e.toString();
					return false;
				}
			}
				
			protected void onPostExecute(Boolean result) {
				
				int jobSize = getJobSize();
				
				if(result) {
					
					if(jobSize > 0) {
						
						jobSize--;
						setJobSize(jobSize);
					}
					
					if(view != null)
						view.onDAOSuccess(requestID);
					
					if((jobSize == 0) && (view != null))
						view.onDAOFinished();
				}
				else {
					
					if(view != null)
						view.onDAOError(requestID, mErrorMessage);
					
					setJobSize(-1);
				}
				
				if(view != null)
					view.dismissProgressDialog();
			}
		};
		
		// Task ausführen
		at.execute();
	}
}