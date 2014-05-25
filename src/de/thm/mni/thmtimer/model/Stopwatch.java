package de.thm.mni.thmtimer.model;

import java.util.Date;

public class Stopwatch {
	
	public interface AsyncStopwatchListener {
		
		public void onStopwatchStart();
		public void onStopwatchStop(Integer stoppedTime);
		
		public void onServerError(String message);
	}
	
	private AsyncStopwatchListener mListener;
	private Boolean mIsRunning;
	private Date mStartTime; // (Das ist nur da, weil im Moment kein Server verfügbar ist...)
	
	
	public Stopwatch() {
		
		mIsRunning = false;
	}
	
	
	
	public void setListener(AsyncStopwatchListener listener) {
		
		mListener = listener;
	}
	
	public void start() {
		
		//
		// ToDo: Serverkommunikation
		//
		mIsRunning = true;
		mStartTime = new Date();
		
		mListener.onStopwatchStart();
	}
	
	public void stop() {

		//
		// ToDo: Serverkommunikation
		//
		mIsRunning = false;
		
		Date now = new Date();
		Long delta = now.getTime() - mStartTime.getTime();
		
		Integer minutes = (int)Math.max(1l, delta / 60000l);
		mListener.onStopwatchStop(minutes); 
	}
	
	public Boolean isRunning() {
		
		return mIsRunning;
	}
}