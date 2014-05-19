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

	private Long courseID;
	//private Module module;
	private EditText timeEdit;
	private Spinner usageSpinner;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		this.courseID = extras.getLong("course_id");
		//this.module = StaticModuleData.findModule(moduleID);

		usageSpinner = (Spinner) findViewById(R.id.usageSpinner);
		usageSpinner.setAdapter(new ArrayAdapter<TimeCategory>(this, android.R.layout.simple_spinner_dropdown_item, StaticModuleData.getTimeCategorys()));
		
		timeEdit = (EditText) findViewById(R.id.timeEntry);
		timeEdit.setHint("HH:MM:SS");
		
		
		
		Button btnEnter = (Button) findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				TimeData time = new TimeData();
				
				if(time.parseString(timeEdit.getText().toString())) {
					
					TimeCategory category = (TimeCategory)usageSpinner.getSelectedItem();
					
					TimeTracking data = new TimeTracking(-1l, category.getID(), "Gelernt", time);
					StaticModuleData.getStudentData().addTimeTracking(courseID, data);
					
					setResult(Activity.RESULT_OK);
					finish();
				}
				else {
					
					Toast.makeText(getApplicationContext(), "Zeitangabe in HH:MM oder HH:MM:SS!", Toast.LENGTH_LONG).show();
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
