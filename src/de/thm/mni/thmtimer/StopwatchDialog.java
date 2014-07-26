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
		
		public void onStoppedTime(Long startTime, Integer timeInSeconds);
	}
	
	private StopwatchListener mListener;
	private TextView mTimeView;
	private Timer mTimer;
	private Integer mTime;
	private Boolean mRunning;
	private Boolean mStarted;
	private Date mStartTime;
	
	
	public StopwatchDialog() {
		
		super();

		setCancelable(false);
		
		mTime = 0;
		
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
					
					showAbortDialog(R.string.stopwatch_abort_message);
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
				
					if(mTime < 60) {
						
						showAbortDialog(R.string.stopwatch_stopped_less_than_one_minute);
					}
					else {
						
						stopTimer();
						
						mListener.onStoppedTime(mStartTime.getTime(), mTime);
						dismiss();
					}
				}
			}
		});
		
		return v;
	}
	
	
	private void startTimer() {
		
		if(!mStarted)
			mStartTime = new Date();
		
		mRunning = true;
		mStarted = true;
		
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				if(mRunning) {
					
					mTime++;
					onUpdateUI();
				}
			}
			
		}, 1000l, 1000l);
	}
	
	private void stopTimer() {
		
		mRunning = false;
		mTimer.cancel();
	}
	
	
	private void showAbortDialog(int stringID) {
		
		AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
		
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setTitle(R.string.stopwatch_abort_header);
		alert.setMessage(getString(stringID));
		
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
	
	private void onUpdateUI() {
		
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				Integer time, hours, minutes, seconds;
				
				time = mTime;
				hours = time / 3600;
				time -= hours * 3600;
				minutes = time / 60;
				time -= minutes * 60;
				seconds = time;
				
				mTimeView.setText(String.format("%02d:%02d:%02d",
						                        hours,
						                        minutes,
						                        seconds));
			}
		});
	}
	
	public void setListener(StopwatchListener listener) {
		
		mListener = listener;
	}
}