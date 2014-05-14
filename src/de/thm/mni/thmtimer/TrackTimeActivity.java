package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.TimeTracking;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class TrackTimeActivity extends Activity {

	protected Module module;
	protected EditText timeEdit;
	protected Spinner usageSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracktimeactivity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		long moduleID = extras.getLong("module_id");
		this.module = StaticModuleData.findModule(moduleID);

		usageSpinner = (Spinner) findViewById(R.id.usageSpinner);
		timeEdit = (EditText) findViewById(R.id.timeEntry);
		Button btnEnter = (Button) findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int minutes;
				try {
					minutes = Integer.parseInt(timeEdit.getText().toString());
				} catch (NumberFormatException e) {
					setResult(Activity.RESULT_CANCELED);
					finish();
					return;
				}
				TimeTracking time = new TimeTracking(usageSpinner.getSelectedItem().toString(), minutes);
				module.addTimeTracking(time);
				setResult(Activity.RESULT_OK);
				finish();
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
