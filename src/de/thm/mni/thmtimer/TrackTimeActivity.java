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
	
	private EditText mTimeEntry;
	private Spinner mUsageSpinner;
	private Button mChooseButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		Bundle extras = getIntent().getExtras();
		
		
		// Kategorie
		mUsageSpinner = (Spinner) findViewById(R.id.usageSpinner);
		mUsageSpinner.setAdapter(new ArrayAdapter<Category>(this,
				                                            android.R.layout.simple_spinner_dropdown_item,
				                                            ModuleDAO.getTimeCategorys()));
		
		// Textfeld für den Zeiteintrag
		mTimeEntry = (EditText) findViewById(R.id.timeEntry);

		// Button zum Eintragen in die Liste
		Button btnEnter = (Button) findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				TimeData time = new TimeData();

				try {
					
					time.parseString(mTimeEntry.getText().toString());
					
					if(time.getTimeInMinutes() > 0) {
						
						Category category = (Category) mUsageSpinner.getSelectedItem();
						
						Intent result = new Intent();
						result.putExtra("category_id", category.getId());
						result.putExtra("time", time.getTimeInMinutes());
						
						setResult(Activity.RESULT_OK, result);
						finish();
					}
					else {
						
						Toast.makeText(getApplicationContext(),
								       getString(R.string.enter_time_error_zerotime),
								       Toast.LENGTH_LONG).show();
					}
				}
				catch(IllegalArgumentException e) {
					
					Toast.makeText(getApplicationContext(),
							       getString(R.string.enter_time_error_format),
							       Toast.LENGTH_LONG).show();
				}
			}
		});

		// Button zum Wählen der Zeit
		mChooseButton = (Button) findViewById(R.id.chooseTime);
		mChooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				TimeData time = new TimeData();
				time.parseString(mTimeEntry.getText().toString());

				TimePickerDialog picker = new TimePickerDialog(TrackTimeActivity.this,
						                                       TrackTimeActivity.this,
						                                       Math.min(time.getTimeInHours(), 23),
						                                       Math.min(time.getTimeInMinutes(), 59),
						                                       true);
				
				picker.setTitle(R.string.enter_time_choose_header);
				picker.show();
			}
		});

		// Zeitdaten von der Stoppuhr
		if(extras.containsKey("stopped_time")) {
			
			TimeData t = new TimeData();
			t.setTimeInMinutes(extras.getInt("stopped_time"));

			mTimeEntry.setText(t.toString());
			mTimeEntry.setEnabled(false);
			mChooseButton.setEnabled(false);
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
		
		mTimeEntry.setText(time.toString());
	}
}