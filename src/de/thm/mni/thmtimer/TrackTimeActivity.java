package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class TrackTimeActivity extends Activity implements TimePickerDialog.OnTimeSetListener {
	
	private EditText mDuration;
	private Spinner mCategory;
	private EditText mDescription;
	private Button mChooseButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		Bundle extras = getIntent().getExtras();

		
		// Textfeld für den Zeiteintrag
		mDuration = (EditText) findViewById(R.id.durationEntry);
		
		// Kategorie
		mCategory = (Spinner)findViewById(R.id.categoryEntry);
		mCategory.setAdapter(new ArrayAdapter<Category>(this,
				                                        android.R.layout.simple_spinner_dropdown_item,
				                                        ModuleDAO.getTimeCategorys()));
		
		// Beschreibung
		mDescription = (EditText)findViewById(R.id.descriptionEntry);

		// Button zum Eintragen in die Liste
		Button btnEnter = (Button)findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				TimeData duration = new TimeData();

				try {
					
					duration.parseString(mDuration.getText().toString());
				}
				catch(IllegalArgumentException e) {
					
					Toast.makeText(TrackTimeActivity.this,
							       getString(R.string.enter_time_error_format),
							       Toast.LENGTH_LONG).show();
					
					mDuration.requestFocus();
					return;
				}
				
				if(duration.getTimeInMinutes() == 0) {
					
					Toast.makeText(TrackTimeActivity.this,
							       getString(R.string.enter_time_error_zerotime),
							       Toast.LENGTH_LONG).show();
				
					mDuration.requestFocus();
					return;
				}
				
				// Der Server mag scheinbar Zeiten größer als 24h nicht...
				if(duration.getTimeInMinutes() > 24 * 60) {
					
					// TODO
					Toast.makeText(TrackTimeActivity.this,
							       "Zeit zu groß",
							       Toast.LENGTH_LONG).show();
					return;
				}
				
				
				Category category = (Category)mCategory.getSelectedItem();
						
				Intent result = new Intent();
				result.putExtra("category_id", category.getId());
				result.putExtra("duration", duration.getTimeInMinutes());
				result.putExtra("description", mDescription.getText().toString());
						
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		});

		// Button zum Wählen der Zeit
		mChooseButton = (Button) findViewById(R.id.chooseTime);
		mChooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				Integer hours = 0;
				Integer minutes = 0;
				
				try {

					TimeData duration = new TimeData();
					duration.parseString(mDuration.getText().toString());
					
					hours   = Math.min(duration.getTimeInMinutes() / 60, 23);
					minutes = Math.min(duration.getTimeInMinutes() - (hours * 60), 59);
				}
				catch(IllegalArgumentException e) {
					
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

			mDuration.setText(t.toString());
			mDuration.setEnabled(false);
			mChooseButton.setEnabled(false);
			
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
}