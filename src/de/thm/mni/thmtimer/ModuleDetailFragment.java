package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ModuleDetailFragment extends DialogFragment implements
		OnClickListener {

	private static Dialog mDialog;
	private TextView mModuleName, mModuleNumber, mCreditPoints, mResponsible,
			mDescription, mExpenditure, mRequirement, mTestingMethod, mSWS,
			mFrequency, mPrereq;
	private Button mBtnEnter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = super.onCreateDialog(savedInstanceState);
		mDialog.setTitle(R.string.module_details);
		return mDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.moduldetailfragment, container,
				false);

		// get views
		mModuleName = (TextView) view.findViewById(R.id.name);
		mModuleNumber = (TextView) view.findViewById(R.id.number);
		mCreditPoints = (TextView) view.findViewById(R.id.cp);
		mResponsible = (TextView) view.findViewById(R.id.responsible);
		mDescription = (TextView) view.findViewById(R.id.description);
		mExpenditure = (TextView) view.findViewById(R.id.expenditure);
		mRequirement = (TextView) view.findViewById(R.id.requirement);
		mTestingMethod = (TextView) view.findViewById(R.id.testingMethod);
		mSWS = (TextView) view.findViewById(R.id.sws);
		mFrequency = (TextView) view.findViewById(R.id.frequency);
		mPrereq = (TextView) view.findViewById(R.id.prerequisites);
		mBtnEnter = (Button) view.findViewById(R.id.enter);
		mBtnEnter.setOnClickListener(this);

		Long id = getArguments().getLong("id", -1);
		Module m = StaticModuleData.findModule(id);

		mModuleName.setText(m.getName());
		mModuleNumber.setText(m.getModuleNumber());
		mCreditPoints.setText(m.getCreditPoints().toString());
		mResponsible.setText(m.getResponsible());
		mDescription.setText(m.getDescription());
		mExpenditure.setText(Integer.toString((m.getCreditPoints() * 30)) + " "
				+ getString(R.string.hours));
		mRequirement.setText(m.getRequirement());
		mTestingMethod.setText(m.getTestingMethod());
		mSWS.setText(Integer.toString(m.getSWS()));
		mFrequency.setText(m.getFrequency());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < m.getPrerequisites().size(); i++) {
			sb.append("&#8226; " + m.getPrerequisites().get(i).getName());
			if(i < m.getPrerequisites().size()-1) {
				sb.append("<br />");
			}
		}
		mPrereq.setText(Html.fromHtml(sb.toString()));

		return view;
	}

	@Override
	public void onClick(View arg0) {
		enterModule();
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	private void enterModule() {
		// send Information to Server

	}

}
