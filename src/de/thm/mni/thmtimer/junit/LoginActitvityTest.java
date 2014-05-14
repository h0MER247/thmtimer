package de.thm.mni.thmtimer.junit;

import de.thm.mni.thmtimer.LoginActivity;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

public class LoginActitvityTest extends ActivityUnitTestCase<LoginActivity> {

	private LoginActivity mActivity;
	private Button mButton;

	public LoginActitvityTest(Class<LoginActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	


	protected void setUp() throws Exception {        
		super.setUp();        
		mActivity = this.getActivity();         
		mButton = (Button) mActivity.findViewById(de.thm.mni.thmtimer.R.id.btn_login);  
	}

	public void testPreconditions() {   
		assertNotNull(mButton);		//if mButton exists?
	}

}




