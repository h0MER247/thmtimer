package de.thm.mni.thmtimer.util;


public interface AbstractAsyncView {

	public void showProgressDialog(int stringRes);
	public void dismissProgressDialog();
	
	public void onDAOError(int requestID, String message);
	public void onDAOFinished();
}
