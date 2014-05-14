package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

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
				
				Intent intent = new Intent(LoginActivity.this, ModuleListActivity.class);
				startActivity(intent);
			}
		});
	}
}
