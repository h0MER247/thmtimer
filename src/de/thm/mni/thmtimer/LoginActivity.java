package de.thm.mni.thmtimer;


import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import de.thm.mni.thmtimer.util.Connection;
import de.thm.mni.thmtimer.util.AbstractAsyncActivity;
import de.thm.mni.thmtimer.util.StaticModuleData;
import de.thm.thmtimer.entities.User;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AbstractAsyncActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loginactivity);

		// ONLY FOR STATIC DATA
		StaticModuleData.fillData();

		Button btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				new FetchSecuredResourceTask().execute();
			}
		});
	}

	private void displayResponse(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private class FetchSecuredResourceTask extends AsyncTask<Void, Void, User> {
		
		private String errormessage;

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog();

			// build the message object
			EditText editText = (EditText) findViewById(R.id.user);
			Connection.username = editText.getText().toString();

			editText = (EditText) findViewById(R.id.password);
			Connection.password = editText.getText().toString();
		}

		@Override
		protected User doInBackground(Void... params) {
			try{
				return Connection.request("/users/" + Connection.username, HttpMethod.GET, User.class);	
			} catch (HttpClientErrorException e) {
				this.errormessage = getString(R.string.login_failed);
				Log.e(TAG, e.getLocalizedMessage(), e);
				return null;
			} catch (ResourceAccessException e) {
				this.errormessage = getString(R.string.connection_unreachable);
				Log.e(TAG, e.getLocalizedMessage(), e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(User result) {
			dismissProgressDialog();
			if (result != null){
				displayResponse(String.format(getString(R.string.login_greeting), result.getFirstName()));
				Intent intent = new Intent(LoginActivity.this, ModuleListActivity.class);
				startActivity(intent);
			} else {
				displayResponse(this.errormessage);
			}
		}

	}

}
