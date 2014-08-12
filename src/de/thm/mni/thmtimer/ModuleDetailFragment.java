package de.thm.mni.thmtimer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Module;


public class ModuleDetailFragment extends Fragment {
	
	private TextView mModuleName, mModuleNumber, mCreditPoints, mResponsible,
			mDescription, mExpenditure, mRequirement, mTestingMethod, mSWS,
			mFrequency;
	private Button mBtnEnter;
	private Long mCourseID;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		mCourseID = getArguments().getLong("id", -1);
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.moduldetailfragment,
				                     container,
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
		mBtnEnter = (Button) view.findViewById(R.id.enter);
		mBtnEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EnterModuleActivity activity = (EnterModuleActivity)getActivity();
				
				activity.onEnterCourse(mCourseID);
			}
		});
		
		Module m = ModuleDAO.searchCourseByID(mCourseID).getModule();
		
		mModuleName.setText(m.getName());
		mModuleNumber.setText(m.getNumber());
		mCreditPoints.setText(String.valueOf(m.getCreditPoints()));
		mDescription.setText(m.getDescription());
		mResponsible.setText(m.getResponsible());
		mExpenditure.setText(String.format("%d %s",
				                           m.getCreditPoints() * 30,
				                           getString(R.string.hours)));
		mRequirement.setText(m.getRequirement());
		mTestingMethod.setText(m.getTestingMethod());
		if (m.getSemesterHours() != null)
			mSWS.setText(Integer.toString(m.getSemesterHours()));
		mFrequency.setText(m.getFrequency());
		
		return view;
	}
}