package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeTracking;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TeacherCreateCourseActivity extends FragmentActivity{

	private EditText mModuleName, mModuleNumber, mCreditPoints, mDescription, mExpenditure,
	mRequirement, mTestingMethod, mFrequency;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teachercreatecourse);
		
		// get views
		mModuleName = (EditText) findViewById(R.id.name);
		mModuleNumber = (EditText)findViewById(R.id.number);
		mCreditPoints = (EditText) findViewById(R.id.cp);		
		mDescription = (EditText) findViewById(R.id.discription);
		mExpenditure = (EditText)findViewById(R.id.expenditure);
		mRequirement = (EditText)findViewById(R.id.requirement);
		mTestingMethod = (EditText)findViewById(R.id.testingMethod);
		mFrequency = (EditText)findViewById(R.id.frequency);

	}
	
	
	
	public void onButtonClick(View button) {
		Long id = null;
		String name = mModuleName.getText().toString();
		String number = mModuleNumber.getText().toString();
		Module module = new Module(id, name, number);
		module.setCreditPoints((Integer.parseInt(mCreditPoints.getText().toString())));
		module.setDescription(mDescription.getText().toString());
		module.setSWS(Integer.parseInt(mExpenditure.getText().toString()));
		module.setRequirement(mRequirement.getText().toString());
		module.setTestingMethod(mTestingMethod.getText().toString());
		module.setFrequency(mFrequency.getText().toString());
		
		StaticModuleData.getModuleList().add(module);
		
		setResult(Activity.RESULT_OK);
		finish();
		
	}
		
	


}
