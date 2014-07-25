package de.thm.mni.thmtimer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchDialog extends DialogFragment {
	
	public interface StopwatchListener {
		
		public void onStoppedTime(Date startTime, Integer timeInMinutes);
	}
	
	private StopwatchListener mListener;
	private TextView mTimeView;
	private Timer mTimer;
	private Long mTimeMemory;
	private Long mTimeStart;
	private Boolean mRunning;
	private Boolean mStarted;
	private Date mStartTime;
	
	
	public StopwatchDialog() {
		
		super();

		setCancelable(false);
		
		mTimeMemory = 0l;
		mRunning = false;
		mStarted = false;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.stopwatchfragment, null);
		Button b;
		
		
		// Dialogtitel
		getDialog().setTitle(getString(R.string.stopwatch_header));
		
		// Zeitanzeige
		mTimeView = (TextView)v.findViewById(R.id.stopwatch_txtTime);
		mTimeView.setText(getString(R.string.stopwatch_zerotime));
		
		// Start / Pause Button
		b = (Button)v.findViewById(R.id.stopwatch_btnStart);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!mRunning)
					startTimer();
				else
					stopTimer();
				
				Button b = (Button)v;
				b.setText(mRunning ? getString(R.string.stopwatch_action_pause) :
					                 getString(R.string.stopwatch_action_resume));
			}
		});
		
		// Abbruch Button
		b = (Button)v.findViewById(R.id.stopwatch_btnAbort);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mStarted) {
				
					AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
						
					alert.setIcon(android.R.drawable.ic_dialog_alert);
					alert.setTitle(R.string.stopwatch_abort_header);
					alert.setMessage(getString(R.string.stopwatch_abort_message));
					
					alert.setButton(DialogInterface.BUTTON_POSITIVE,
							        getString(R.string.stopwatch_abort_positive),
							        new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							stopTimer();
							dismiss();
						}
					});
					
					alert.setButton(DialogInterface.BUTTON_NEGATIVE,
							        getString(R.string.stopwatch_abort_negative),
							        new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					
					alert.show();
				}
				else {
					
					//
					// Zeiterfassung lief noch nicht: Das Fenster kann ohne
					// Nachfrage geschlossen werden
					//
					dismiss();
				}
			}	
		});
		
		//
		// Stop Button
		//
		b = (Button)v.findViewById(R.id.stopwatch_btnStop);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mStarted) {
				
					stopTimer();
				
					Integer timeInMinutes = Math.max(1, (int)(getTime() / 60l));
					
					mListener.onStoppedTime(mStartTime, timeInMinutes);
					dismiss();
				}
			}
		});
		
		return v;
	}
	
	
	private void startTimer() {
		
		if(!mStarted)
			mStartTime = new Date();
		
		mTimeStart = System.currentTimeMillis();
		
		mRunning = true;
		mStarted = true;
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				if(mRunning) {
					
					onUpdateUI();
				}
			}
			
		}, 1000l, 1000l);
	}
	
	private void stopTimer() {
		
		mTimeMemory = getTime();
			
		mRunning = false;
		mTimer.cancel();
	}
	
	
	
	private void onUpdateUI() {
		
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				Long time, hours, minutes, seconds;
				
				time = getTime();
				hours = time / 3600l;
				time -= hours * 3600l;
				minutes = time / 60l;
				time -= minutes * 60l;
				seconds = time;
				
				mTimeView.setText(String.format("%02d:%02d:%02d",
						                        hours,
						                        minutes,
						                        seconds));
			}
		});
	}
	
	private Long getTime() {
		
		return mTimeMemory + ((System.currentTimeMillis() - mTimeStart) / 1000l);
	}
	
	public void setListener(StopwatchListener listener) {
		
		mListener = listener;
	}
}