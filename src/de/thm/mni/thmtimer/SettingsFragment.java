package de.thm.mni.thmtimer;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsFragment extends DialogFragment implements OnClickListener {

	public static final String FILE_NAME = "settings";
	public static final String BOOL_USER = "settingUsername";
	public static final String HOURS_RED = "settingHoursRed";
	public static final String HOURS_YELLOW = "settingHoursYellow";
	public static final String HOURS_GREEN = "settingHoursGreen";
	public static final int VAL_HOURS_RED = -20;
	public static final int VAL_HOURS_GREEN = 20;
	public static final int VAL_HOURS_YELLOW = 0;
	private static Dialog mDialog;
	private SharedPreferences mSettings;

	private CheckBox mCheckBox;
	private EditText mEditTextGreen;
	private EditText mEditTextYellow;
	private EditText mEditTextRed;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = super.onCreateDialog(savedInstanceState);
		mDialog.setTitle(R.string.settings);
		mSettings = getActivity().getSharedPreferences(FILE_NAME, 0);

		return mDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settingsfragment, container,
				false);

		// get views
		mCheckBox = (CheckBox) view.findViewById(R.id.settingUsername);
		mCheckBox.setChecked(mSettings.getBoolean(BOOL_USER, false));

		mEditTextGreen = (EditText) view.findViewById(R.id.settingHoursGreen);
		mEditTextGreen.setText(Integer.toString(mSettings.getInt(HOURS_GREEN,
				VAL_HOURS_GREEN)));

		mEditTextYellow = (EditText) view.findViewById(R.id.settingHoursYellow);
		mEditTextYellow.setText(Integer.toString(mSettings.getInt(HOURS_YELLOW,
				VAL_HOURS_YELLOW)));

		mEditTextRed = (EditText) view.findViewById(R.id.settingHoursRed);
		mEditTextRed.setText(Integer.toString(mSettings.getInt(HOURS_RED,
				VAL_HOURS_RED)));

		Button but = (Button) view.findViewById(R.id.saveSettings);
		but.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		// save settings
		int green = Integer.parseInt(mEditTextGreen.getText().toString());
		int yellow = Integer.parseInt(mEditTextYellow.getText().toString());
		int red = Integer.parseInt(mEditTextRed.getText().toString());
		if(green>yellow&&yellow>red) {
			Editor edit = mSettings.edit();
			edit.putBoolean(BOOL_USER, mCheckBox.isChecked());
			edit.putInt(HOURS_GREEN, green);
			edit.putInt(HOURS_YELLOW, yellow);
			edit.putInt(HOURS_RED, red);
			edit.commit();
			mDialog.dismiss();
		}
		else {
			Toast.makeText(getActivity(), R.string.colorAlert,
					Toast.LENGTH_SHORT).show();
		}
	}
}
