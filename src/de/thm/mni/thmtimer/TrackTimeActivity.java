package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.TimeTracking;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TrackTimeActivity extends Activity {

	protected Module module;
	protected EditText timeEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracktimeactivity);
		
		Bundle extras = getIntent().getExtras();
		int moduleID = extras.getInt("module_id");
		this.module = StaticModuleData.findModule(moduleID);

		Button btnEnter = (Button) findViewById(R.id.enterTime);
		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				TimeTracking time = new TimeTracking("New TimeTracking entry");
				time.hours = Integer.parseInt(timeEdit.getText().toString());
				time.minutes = Integer.parseInt(timeEdit.getText().toString());
				module.addTimeTracking(time);
				Intent intent = new Intent(TrackTimeActivity.this, TimeTrackingActivity.class);
				startActivity(intent);
			}

		});
	}
}
