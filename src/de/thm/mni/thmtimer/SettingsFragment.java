package de.thm.mni.thmtimer;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class SettingsFragment extends DialogFragment implements OnClickListener {

	public static final String FILE_NAME = "SETTINGS";
	public static final String BOOL_USER = "settingUsername";
	private static Dialog dialog;
	private SharedPreferences settings;

	private CheckBox check;
	
	@Override	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle(R.string.settings);
		settings = getActivity().getSharedPreferences(FILE_NAME, 0);
		
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settingsfragment, container, false);

		// get views
		check = (CheckBox) view.findViewById(R.id.settingUsername);
		check.setChecked(settings.getBoolean(BOOL_USER, false));
		
		Button but = (Button) view.findViewById(R.id.saveSettings);
		but.setOnClickListener(this);
		
		return view;
	}	

	@Override
	public void onClick(View v) {
		//save settings
		settings.edit().putBoolean(BOOL_USER, check.isChecked()).commit();
		dialog.dismiss();
	}
}
