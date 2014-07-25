package de.thm.mni.thmtimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;


public class TrackTimeActivity extends Activity implements TimePickerDialog.OnTimeSetListener {
	
	// Dauer
	private EditText mDuration;
	private TimeData mDurationData;
	
	// Startzeit
	private EditText mStartTime;
	private Date mStartTimeData;
	private SimpleDateFormat mStartTimeFormat;
	
	// Kategorie
	private Spinner mCategory;
	private List<Category> mCategorys;
	
	// Beschreibung
	private EditText mDescription;
	private Button mChooseButton;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		
		mCategorys = ModuleDAO.getTimeCategorys();
		mDurationData = new TimeData();
		
		
		mStartTimeFormat = (SimpleDateFormat)SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
				                                                                  SimpleDateFormat.SHORT);
		mStartTimeFormat.setLenient(true);
		
		
		// Textfeld für den Zeiteintrag
		mDuration = (EditText)findViewById(R.id.durationEntry);
		
		// Startzeit
		mStartTime = (EditText)findViewById(R.id.startTimeEntry);
		mStartTime.setHint(mStartTimeFormat.toLocalizedPattern());
		
		// Kategorie
		mCategory = (Spinner)findViewById(R.id.categoryEntry);
		mCategory.setAdapter(new CategoryAdapter(this,
				                                 R.layout.categorydropdownitem,
				                                 mCategorys));
		
		// Beschreibung
		mDescription = (EditText)findViewById(R.id.descriptionEntry);

		// Button zum Eintragen in die Liste
		Button btnEnter = (Button)findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(!isDurationValid()) {
					
					Toast.makeText(TrackTimeActivity.this,
						           getString(R.string.enter_time_duration_invalid),
						           Toast.LENGTH_LONG).show();
					
					mDuration.requestFocus();
					return;
				}
				
				if(mStartTime.length() == 0)
					generateStartTime();
				
				if(!isStartTimeValid()) {
					
					Toast.makeText(TrackTimeActivity.this,
							       getString(R.string.enter_time_starttime_invalid_format),
							       Toast.LENGTH_LONG).show();
					
					mStartTime.requestFocus();
					return;
				}
				
				if(!isStartTimeConsistent()) {
					
					Toast.makeText(TrackTimeActivity.this,
						           getString(R.string.enter_time_starttime_invalid_consistency),
						           Toast.LENGTH_LONG).show();
					
					mStartTime.requestFocus();
					return;
				}
				
				
				Category category = (Category)mCategory.getSelectedItem();
						
				Intent result = new Intent();
				result.putExtra("duration", mDurationData.getTimeInMinutes());
				result.putExtra("start_time", mStartTimeData.getTime());
				result.putExtra("category_id", category.getId());
				result.putExtra("description", mDescription.getText().toString());
				
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		});

		// Button zum Wählen der Zeit
		mChooseButton = (Button) findViewById(R.id.chooseDuration);
		mChooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				Integer hours = 0;
				Integer minutes = 0;
				
				if(isDurationValid()) {
					
					hours   = Math.min(mDurationData.getTimeInMinutes() / 60, 23);
					minutes = Math.min(mDurationData.getTimeInMinutes() - (hours * 60), 59);
				}
				
				TimePickerDialog picker = new TimePickerDialog(TrackTimeActivity.this,
						                                       TrackTimeActivity.this,
						                                       hours,
						                                       minutes,
						                                       true);
				
				picker.setTitle(R.string.enter_time_choose_header);
				picker.show();
			}
		});

		// Zeitdaten von der Stoppuhr
		if(extras.containsKey("stopped_time")) {
			
			TimeData t = new TimeData();
			t.setTimeInMinutes(extras.getInt("stopped_time"));

			Date d = new Date(extras.getLong("start_time"));
			mStartTime.setText(mStartTimeFormat.format(d));
			
			mDuration.setText(t.toString());
			mDuration.setEnabled(false);
			mStartTime.setEnabled(false);
			mChooseButton.setVisibility(View.INVISIBLE);
			
			mDescription.requestFocus();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:
			finish();
			return true;
			
		default:
			return false;
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		TimeData time = new TimeData();
		time.setTimeInMinutes((hourOfDay * 60) + minute);
		
		mDuration.setText(time.toString());
	}
	
	
	
	private boolean isDurationValid() {
		
		try {
		
			mDurationData.parseString(mDuration.getText().toString());
		}
		catch(IllegalArgumentException e) {} 
		
		return mDurationData.getTimeInMinutes() > 0 &&
			   mDurationData.getTimeInMinutes() < 24 * 60;
	}
	
	private boolean isStartTimeValid() {
		
		try {
			
			mStartTimeData = mStartTimeFormat.parse(mStartTime.getText().toString());
		}
		catch(ParseException e) {}
		
		return mStartTimeData != null;
	}
	
	private boolean isStartTimeConsistent() {
		
		Date now = new Date();
		
		// Der Server nimmt Expenditures nicht an, die dieses Kriterium nicht erfüllen!
		return mStartTimeData.getTime() + (60000l * mDurationData.getTimeInMinutes()) <= now.getTime();
	}
	
	private void generateStartTime() {
		
		Date t = new Date();
		t.setTime(t.getTime() - (60000l * mDurationData.getTimeInMinutes()));
		
		mStartTime.setText(mStartTimeFormat.format(t));
	}
	
	
	
	/**
	 * 
	 *
	 */
	private class CategoryAdapter extends ArrayAdapter<Category> {
		
		public CategoryAdapter(Context context,
				               int resource,
				               List<Category> objects) {
			
			super(context, resource, objects);
		}
		
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			
			return getView(position, convertView, parent);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			if(convertView == null) {
				
				convertView = getLayoutInflater().inflate(R.layout.categorydropdownitem,
						                                  parent,
						                                  false);
			}
			
			TextView category = (TextView)convertView.findViewById(R.id.categorySpinnerText);
			
			// Setze Kategorienamen
			category.setText(getItem(position).getName());
			
			
			return convertView;
		}
	}
}