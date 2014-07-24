package de.thm.mni.thmtimer.junit;

import org.junit.Before;

import de.thm.mni.thmtimer.R;
import de.thm.mni.thmtimer.TrackTimeActivity;
import de.thm.mni.thmtimer.model.TimeData;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TrackTimeActivityTest extends ActivityUnitTestCase<TrackTimeActivity> {
	
	private Intent mLaunchIntent;
	private Spinner mUsageSpinner;
	private Button mButton;
	TimeData time = new TimeData();
	long mCourseID = 1;
	

	public TrackTimeActivityTest() {
		super(TrackTimeActivity.class);
	}

	@Before
	public void setUp() throws Exception {	
		super.setUp();
		mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), TrackTimeActivity.class);
        startActivity(mLaunchIntent, null, null);
        mUsageSpinner = (Spinner) getActivity().findViewById(R.id.usageSpinner);
        mButton = (Button) getActivity().findViewById(R.id.enterTime);  
	}
	
	public void testSpinner(){
		assertNotNull("No Spinner input",mUsageSpinner);
	}
	
	public void testEnterTime(){
		mButton.performClick();
		//TimeCategory category = (TimeCategory) mUsageSpinner.getSelectedItem();
		//TimeTracking data = new TimeTracking(-1l, category.getID(), "Gelernt", time);
		//StaticModuleData.getStudentData().addTimeTracking(mCourseID, data);
	}	

}
