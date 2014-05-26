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

	private static Dialog mDialog;
	private TextView mModuleName, mModuleNumber, mCreditPoints, mTeacher, mContent, mDescription, mExpenditure,
			mRequirement, mTestingMethod;
	private Button mBtnEnter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = super.onCreateDialog(savedInstanceState);
		mDialog.setTitle(R.string.module_details);
		return mDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.moduldetailfragment, container, false);

		// get views
		mModuleName = (TextView) view.findViewById(R.id.name);
		mModuleNumber = (TextView) view.findViewById(R.id.number);
		mCreditPoints = (TextView) view.findViewById(R.id.cp);
		mTeacher = (TextView) view.findViewById(R.id.teacher);
		mDescription = (TextView) view.findViewById(R.id.discription);
		mContent = (TextView) view.findViewById(R.id.content);
		mExpenditure = (TextView) view.findViewById(R.id.expenditure);
		mRequirement = (TextView) view.findViewById(R.id.requirement);
		mTestingMethod = (TextView) view.findViewById(R.id.testingMethod);
		mBtnEnter = (Button) view.findViewById(R.id.enter);
		mBtnEnter.setOnClickListener(this);

		Long id = getArguments().getLong("id", -1);
		Module m = StaticModuleData.findModule(id);

		mModuleName.setText(m.getName());
		mModuleNumber.setText(m.getModuleNumber());
		mCreditPoints.setText(m.getCreditPoints().toString());
		// teacher.setText(m.getTeacher());
		mTeacher.setText(m.getResponsible());
		mDescription.setText(m.getDescription());
		mContent.setText("Inhalt"); // Was ist das?
		mExpenditure.setText(Integer.toString((m.getCreditPoints() * 30)) + " Stunden");
		mRequirement.setText("2 anerkannte Hausuebungen");
		mTestingMethod.setText("Klausur");

		return view;
	}

	@Override
	public void onClick(View arg0) {
		enterModule();
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog.dismiss();
	}

	private void enterModule() {
		// send Information to Server

	}

}
