package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ModuleDetailFragment extends DialogFragment implements
OnClickListener {

	private static Dialog dialog;
	private TextView mModulName, mModulNumber, mCP, mTeacher, mContent;
	private Button mEnter;

	@Override	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle(R.string.modul_details);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.moduldetailfragment, container, false);

		// get views
		mModulName = (TextView) view.findViewById(R.id.name);
		mModulNumber = (TextView) view.findViewById(R.id.number);
		mCP = (TextView) view.findViewById(R.id.cp);
		mTeacher = (TextView) view.findViewById(R.id.teacher);
		mContent = (TextView) view.findViewById(R.id.content);		
		mEnter = (Button) view.findViewById(R.id.enter);
		mEnter.setOnClickListener(this);

		Long id = getArguments().getLong("id", -1);
		Module m = StaticModuleData.findModule(id);

		mModulName.setText(m.getName());
		mModulNumber.setText("C1231");
		mCP.setText("6");
		mTeacher.setText(m.getTeacher());
		mContent.setText("Inhalt");

		return view;
	}	


	@Override
	public void onClick(View arg0) {
		enterModul();
		//dialog.dismiss();		
	}

	private void enterModul() {
		// send Information to Server

	}

}
