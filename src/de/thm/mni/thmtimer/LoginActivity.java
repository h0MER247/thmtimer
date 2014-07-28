package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.util.Connection;
import de.thm.mni.thmtimer.util.ModuleDAOListener;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.User;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends Activity implements ModuleDAOListener {
	
	private final String TAG = LoginActivity.class.getSimpleName();
	
	private static final String SETTINGS_USERNAME = "lastUserName";
	private static final String SETTINGS_PASSWORD = "lastPassword";
	private static final String SETTINGS_REMEMBERME = "rememberMe";
	
	private final int DAO_REQUEST_USER = 0;
	private final int DAO_REQUEST_STUDENTCOURSELIST = 1;
	private final int DAO_REQUEST_TEACHERCOURSELIST = 2;
	private final int DAO_REQUEST_TIMECATEGORYS = 3;
	private final int DAO_REQUEST_EXPENDITURES = 4;
	private final int DAO_REQUEST_MODULELIST = 5;
	private final int DAO_REQUEST_FULL_COURSELIST = 6;
	
	private SharedPreferences mSharedPref;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ModuleDAO.setNewContext(this, this);
		
		this.setContentView(R.layout.loginactivity);
		
		mSharedPref = getSharedPreferences(SettingsFragment.FILE_NAME,
				                           Context.MODE_PRIVATE);
		
		Button btnLogin       = (Button)  findViewById(R.id.btn_login);
		EditText editUser     = (EditText)findViewById(R.id.user);
		EditText editPassword = (EditText)findViewById(R.id.password);
		CheckBox rememberMe   = (CheckBox)findViewById(R.id.remember_me);
		
		
		// Set rememberMe checkbox depending on saved settings 
		rememberMe.setChecked(mSharedPref.getBoolean(SETTINGS_REMEMBERME, false));

		// Restore last username
		if(mSharedPref.getBoolean(SettingsFragment.BOOL_USER, false) ||
		   mSharedPref.getBoolean(SETTINGS_REMEMBERME, false)) {
			
			String lastUserName = mSharedPref.getString(SETTINGS_USERNAME, "");
			
			if(!lastUserName.isEmpty()) {
				
				editUser.setText(lastUserName);
				findViewById(R.id.password).requestFocus();
			}
		}
		
		// Restore password
		if(mSharedPref.getBoolean(SETTINGS_REMEMBERME, false)) {
			
			String password = mSharedPref.getString(SETTINGS_PASSWORD, "");
			
			if(!password.isEmpty()) {
			
				editPassword.setText(password);
			}
		}
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				doLogin();
			}
		});
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
	
	
	
	@Override
	public void onDAOError(int requestID, String message) {
		
		switch(requestID) {
		
		case DAO_REQUEST_USER:
			Toast.makeText(this,
					       String.format("Fehler beim Laden des Users: %s", message),
					       Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_STUDENTCOURSELIST:
			Toast.makeText(this,
				       	   String.format("Fehler beim Laden der Studentenkursliste: %s", message),
				       	   Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_TEACHERCOURSELIST:
			Toast.makeText(this,
			       	      String.format("Fehler beim Laden der Dozentenkursliste: %s", message),
			       	      Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_TIMECATEGORYS:
			Toast.makeText(this,
		       	           String.format("Fehler beim Laden der Zeitkategorien: %s", message),
		       	           Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_EXPENDITURES:
			Toast.makeText(this,
	       	               String.format("Fehler beim Laden der Aufwände: %s", message),
	       	               Toast.LENGTH_LONG).show();
			break;
			
		case DAO_REQUEST_MODULELIST:
			Toast.makeText(this,
    	                   String.format("Fehler beim Laden der Modulliste: %s", message),
    	                   Toast.LENGTH_LONG).show();
			
		case DAO_REQUEST_FULL_COURSELIST:
			Toast.makeText(this,
    	                   String.format("Fehler beim Laden der gesamten Kursliste: %s", message),
    	                   Toast.LENGTH_LONG).show();
			break;
		}
		
		// TODO: Was jetzt?
	}
	
	@Override
	public void onDAOFinished() {
		
		User user = ModuleDAO.getUser();
		
		
		CheckBox rememberMe = (CheckBox)findViewById(R.id.remember_me);
		
		// Save username & password
		SharedPreferences.Editor editor = mSharedPref.edit();
		
		editor.putBoolean(SETTINGS_REMEMBERME, rememberMe.isChecked());
        editor.putString(SETTINGS_USERNAME, Connection.getUsername());
		if(rememberMe.isChecked())
			editor.putString(SETTINGS_PASSWORD, Connection.getPassword());
		
		editor.commit();
		
		// Show greetings
		Toast.makeText(this,
				       String.format(getString(R.string.login_greeting),
				    		                   user.getFirstName()),
				       Toast.LENGTH_LONG).show();
		
		// Open Modulelist
		Intent intent = new Intent(LoginActivity.this,
				                   ModuleListActivity.class);
		startActivity(intent);
	}
	
	
	
	private void doLogin() {
		
		// Benutzername und Passwort setzen
		EditText editUsername = (EditText)findViewById(R.id.user);
		EditText editPassword = (EditText)findViewById(R.id.password);
		
		String username = editUsername.getText().toString();
		String password = editPassword.getText().toString();
		
		
		if(!(username.isEmpty() || password.isEmpty())) {
			
			Connection.setUsername(username);			
			Connection.setPassword(password);
			
			ModuleDAO.invalidateDurationPerCategory();
			ModuleDAO.invalidateFullCourseList();
			ModuleDAO.invalidateModules();
			ModuleDAO.invalidateStudentCourseList();
			ModuleDAO.invalidateStudentExpenditures();
			ModuleDAO.invalidateTeacherCourseList();
			ModuleDAO.invalidateTimeCategorys();
			ModuleDAO.invalidateUser();
			
			//
			// Alle Ressourcen anfordern, die wir benötigen
			//
			ModuleDAO.beginJob();
			ModuleDAO.getUserFromServer(DAO_REQUEST_USER);
			ModuleDAO.getFullCourseListFromServer(DAO_REQUEST_FULL_COURSELIST);
			ModuleDAO.getStudentCourseListFromServer(DAO_REQUEST_STUDENTCOURSELIST);
			ModuleDAO.getTeacherCourseListFromServer(DAO_REQUEST_TEACHERCOURSELIST);
			ModuleDAO.getModuleListFromServer(DAO_REQUEST_MODULELIST);
			ModuleDAO.getStudentExpendituresFromServer(DAO_REQUEST_EXPENDITURES);
			ModuleDAO.getTimeCategorysFromServer(DAO_REQUEST_TIMECATEGORYS);
			ModuleDAO.commitJob(this, this);
		}
	}
}