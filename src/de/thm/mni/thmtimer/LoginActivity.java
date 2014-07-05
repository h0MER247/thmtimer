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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AbstractAsyncActivity {

	private SharedPreferences sharedPref;
	private static final String SETTINGS_USERNAME = "lastUserName";
	private static final String SETTINGS_PASSWORD = "lastPassword";
	private static final String SETTINGS_REMEMBERME = "rememberMe";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loginactivity);
		this.sharedPref = getSharedPreferences(SettingsFragment.FILE_NAME,
				Context.MODE_PRIVATE);

		Button btnLogin = (Button) findViewById(R.id.btn_login);
		EditText editUser = (EditText) findViewById(R.id.user);
		EditText editPassword = (EditText) findViewById(R.id.password);
		CheckBox rememberMe = (CheckBox) findViewById(R.id.remember_me);

		// ONLY FOR STATIC DATA
		StaticModuleData.fillData();

		rememberMe.setChecked(sharedPref.getBoolean(SETTINGS_REMEMBERME, false));

		// Restore last username
		if (sharedPref.getBoolean(SettingsFragment.BOOL_USER, false) ||
				sharedPref.getBoolean(SETTINGS_REMEMBERME, false)) {
			String lastUserName = sharedPref.getString(SETTINGS_USERNAME, "");
			if (!lastUserName.isEmpty()) {
				editUser.setText(lastUserName);
				findViewById(R.id.password).requestFocus();
			}
		}

		// Remember login
		String password = sharedPref.getString(SETTINGS_PASSWORD, "");
		if (sharedPref.getBoolean(SETTINGS_REMEMBERME, false) && !password.isEmpty()) {
			editPassword.setText(password);
			new LoginTask().execute();
		}

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new LoginTask().execute();
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

	private class LoginTask extends AsyncTask<Void, Void, User> {

		private String errormessage;

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog();

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
				// Save username & password
				EditText pw = (EditText) findViewById(R.id.password);
				CheckBox rememberme = (CheckBox) findViewById(R.id.remember_me);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putBoolean(SETTINGS_REMEMBERME, rememberme.isChecked());
				editor.putString(SETTINGS_USERNAME, result.getThmUsername());
				if (rememberme.isChecked())
					editor.putString(SETTINGS_PASSWORD, pw.getText().toString());
				editor.commit();

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
