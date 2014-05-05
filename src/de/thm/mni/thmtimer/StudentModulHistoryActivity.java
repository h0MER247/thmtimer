package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class StudentModulHistoryActivity extends ListActivity {

	Modul m1 = new Modul("C100", "Gundlagen der Informatik", 10, "Lernen");
	Modul m2 = new Modul("C101", "Betriebssyteme", 15, "Lernen");
	Modul m3 = new Modul("C102", "Softwaretechnik", 12, "Lernen");
	List<Modul> mModule = new ArrayList<Modul>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mModule.add(m1);
		mModule.add(m2);
		mModule.add(m3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		// This adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_module_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			/*
			Intent i = new Intent(this, newTask.class);
			startActivity(i);
			return true;  */
		
		}
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		ArrayAdapter<Modul> adapter = new ArrayAdapter<Modul>(this,
				android.R.layout.simple_list_item_1, mModule);
		setListAdapter(adapter);
	}

}
