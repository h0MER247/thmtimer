package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeTracking;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class TrackTimeActivity extends Activity {
	private Long mCourseID;
	private EditText mTimeEdit;
	private Spinner mUsageSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		mCourseID = extras.getLong("course_id");
		
		mUsageSpinner = (Spinner) findViewById(R.id.usageSpinner);
		mUsageSpinner.setAdapter(new ArrayAdapter<TimeCategory>(this, android.R.layout.simple_spinner_dropdown_item,
				StaticModuleData.getTimeCategorys()));

		mTimeEdit = (EditText) findViewById(R.id.timeEntry);
		mTimeEdit.setHint(getString(R.string.enter_time_format));
		
		if(extras.containsKey("stopped_time")) {
			
			TimeData t = new TimeData();
			t.setTimeInMinutes(extras.getInt("stopped_time"));
			
			mTimeEdit.setText(t.toString());
			mTimeEdit.setEnabled(false);
		}

		Button btnEnter = (Button) findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				TimeData time = new TimeData();

				if (time.parseString(mTimeEdit.getText().toString())) {
					TimeCategory category = (TimeCategory) mUsageSpinner.getSelectedItem();

					TimeTracking data = new TimeTracking(-1l, category.getID(), "Gelernt", time);
					StaticModuleData.getStudentData().addTimeTracking(mCourseID, data);
					
					setResult(Activity.RESULT_OK);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.enter_time_error), Toast.LENGTH_LONG).show();
				}
			}
		});
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
}
