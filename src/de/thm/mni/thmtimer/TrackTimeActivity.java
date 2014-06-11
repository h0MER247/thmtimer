package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeTracking;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class TrackTimeActivity extends Activity implements TimePickerDialog.OnTimeSetListener {

	private Long mCourseID;
	private EditText mTimeEntry;
	private Spinner mUsageSpinner;
	private Button mChooseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		mCourseID = extras.getLong("course_id");

		// Kategorie
		mUsageSpinner = (Spinner) findViewById(R.id.usageSpinner);
		mUsageSpinner.setAdapter(new ArrayAdapter<TimeCategory>(this, android.R.layout.simple_spinner_dropdown_item,
				StaticModuleData.getTimeCategorys()));

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
				} catch (IllegalArgumentException e) {
					Toast.makeText(getApplicationContext(), getString(R.string.enter_time_error_format),
							Toast.LENGTH_LONG).show();
					return;
				}
				if (time.getTimeInMinutes() <= 0) {
					Toast.makeText(getApplicationContext(), getString(R.string.enter_time_error_zerotime),
							Toast.LENGTH_LONG).show();
					return;
				}
				TimeCategory category = (TimeCategory) mUsageSpinner.getSelectedItem();

				TimeTracking data = new TimeTracking(-1l, category.getID(), "Gelernt", time);
				StaticModuleData.getStudentData().addTimeTracking(mCourseID, data);

				setResult(Activity.RESULT_OK);
				finish();
			}
		});

		// Button zum Wählen der Zeit
		mChooseButton = (Button) findViewById(R.id.chooseTime);
		mChooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				TimeData time = new TimeData();
				time.parseString(mTimeEntry.getText().toString());

				TimePickerDialog picker = new TimePickerDialog(TrackTimeActivity.this, TrackTimeActivity.this, Math
						.min(time.getTimeInHours(), 23), Math.min(time.getTimeInMinutes(), 59), true);

				picker.setTitle(R.string.enter_time_choose_header);
				picker.show();
			}
		});

		// Zeitdaten von der Stoppuhr
		if (extras.containsKey("stopped_time")) {

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
		time.setTimeInHours(hourOfDay);
		time.setTimeInMinutes(minute);

		mTimeEntry.setText(time.toString());
	}
}