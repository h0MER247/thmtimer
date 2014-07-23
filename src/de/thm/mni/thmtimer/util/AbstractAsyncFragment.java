package de.thm.mni.thmtimer.util;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;


public abstract class AbstractAsyncFragment extends Fragment implements AbstractAsyncView {
	
	private ProgressDialog progressDialog;
	private boolean destroyed = false;
	

	// ***************************************
	// Activity methods
	// ***************************************
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		destroyed = true;
	}
	
	/* Fix #11128 */
	@Override
	public void onPause() {
		
		super.onPause();
		dismissProgressDialog();
	}



	// ***************************************
	// Public methods
	// ***************************************
	public void showProgressDialog(int stringRes) {
		
		showProgressDialog(getString(stringRes));
	}
	
	public void showProgressDialog(CharSequence message) {
		
		if(progressDialog == null) {
			
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}

		/* Fix #11128 */
		if(!destroyed) {
			
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}

	public void dismissProgressDialog() {
		
		if(progressDialog != null && !destroyed) {
			
			progressDialog.dismiss();
		}
	}
}