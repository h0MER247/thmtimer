package de.thm.mni.thmtimer;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import de.thm.mni.thmtimer.util.Connection;
import de.thm.mni.thmtimer.util.AbstractAsyncActivity;
import de.thm.mni.thmtimer.util.StaticModuleData;
import de.thm.thmtimer.entities.User;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AbstractAsyncActivity {

	private SharedPreferences sharedPref;
	private static String username_key = "lastUserName";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loginactivity);
		this.sharedPref = getPreferences(Context.MODE_PRIVATE);

		// ONLY FOR STATIC DATA
		StaticModuleData.fillData();

		// Restore last username
		if (getSharedPreferences(SettingsFragment.FILE_NAME, 0).getBoolean(
				SettingsFragment.BOOL_USER, false)) {
			EditText editText = (EditText) findViewById(R.id.user);
			String lastUserName = sharedPref.getString(username_key, "");
			if (!lastUserName.isEmpty()) {
				editText.setText(lastUserName);
				findViewById(R.id.password).requestFocus();
			}
		}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.loginactivity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			new SettingsFragment().show(ft, "settingsDialog");
			return true;
		default:
			return false;
		}
	}

	private void saveUserName(String username) {
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(username_key, username);
		editor.commit();
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
			try {
				return Connection.request("/users/" + Connection.username,
						HttpMethod.GET, User.class);
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
			if (result != null) {
				saveUserName(result.getThmUsername());
				displayResponse(String.format(
						getString(R.string.login_greeting),
						result.getFirstName()));
				Intent intent = new Intent(LoginActivity.this,
						ModuleListActivity.class);
				startActivity(intent);
			} else {
				displayResponse(this.errormessage);
			}
		}

	}

}
