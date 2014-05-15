package de.thm.mni.thmtimer.junit;

import de.thm.mni.thmtimer.LoginActivity;
import de.thm.mni.thmtimer.R;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

public class LoginActitvityTest extends ActivityUnitTestCase<LoginActivity> {

	private LoginActivity mActivity;
	private Intent mLaunchIntent;
	private Button mButton;

	public LoginActitvityTest(Class<LoginActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}	
	
	protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), LoginActivity.class);
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton =
                (Button) getActivity()
                .findViewById(R.id.btn_login);
    }
	
	public void testNextActivityWasLaunchedWithIntent() {
	    startActivity(mLaunchIntent, null, null);
	    final Button launchNextButton =
	            (Button) getActivity()
	            .findViewById(R.id.btn_login);
	    launchNextButton.performClick();

	    final Intent launchIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertTrue(isFinishCalled());

	}


}




