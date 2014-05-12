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

public class ModuleDetailFragment extends DialogFragment implements OnClickListener {

	private static Dialog dialog;
	private TextView moduleName, moduleNumber, creditPoints, teacher, content;
	private Button btnEnter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		dialog = super.onCreateDialog(savedInstanceState);
		dialog.setTitle(R.string.module_details);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.moduldetailfragment, container, false);

		// get views
		moduleName = (TextView) view.findViewById(R.id.name);
		moduleNumber = (TextView) view.findViewById(R.id.number);
		creditPoints = (TextView) view.findViewById(R.id.cp);
		teacher = (TextView) view.findViewById(R.id.teacher);
		content = (TextView) view.findViewById(R.id.content);
		btnEnter = (Button) view.findViewById(R.id.enter);
		btnEnter.setOnClickListener(this);

		Long id = getArguments().getLong("id", -1);
		Module m = StaticModuleData.findModule(id);

		moduleName.setText(m.getName());
		moduleNumber.setText("C1231");
		creditPoints.setText("6");
		teacher.setText(m.getTeacher());
		content.setText("Inhalt");

		return view;
	}

	@Override
	public void onClick(View arg0) {
		enterModule();
		if(dialog!=null) {
			dialog.dismiss();
		}
	}

	private void enterModule() {
		// send Information to Server

	}

}
