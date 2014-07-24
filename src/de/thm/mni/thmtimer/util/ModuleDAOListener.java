package de.thm.mni.thmtimer.util;

public interface ModuleDAOListener {

	public void onDAOFinished();
	public void onDAOError(int requestID, String errorMessage);
}