package de.thm.mni.thmtimer.junit;

import de.thm.mni.thmtimer.LoginActivity;
import de.thm.mni.thmtimer.R;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;

public class LoginActitvityTest extends ActivityUnitTestCase<LoginActivity> {

	private Intent mLaunchIntent;
	private Button mButton;
	private EditText mUser, mPassword;

	public LoginActitvityTest() {
		super(LoginActivity.class);
	}
	

	protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), LoginActivity.class);
        startActivity(mLaunchIntent, null, null);
        mButton = (Button) getActivity().findViewById(R.id.btn_login);
        mUser = (EditText) getActivity().findViewById(R.id.user);
        mPassword = (EditText) getActivity().findViewById(R.id.password);
    }
	
	public void testNextActivityWasLaunchedWithIntent() {
		assertNotNull("No Username", mUser);
		assertNotNull("No Username", mPassword);
	    mButton.performClick();
	    final Intent launchIntent = getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);  

	}


}
