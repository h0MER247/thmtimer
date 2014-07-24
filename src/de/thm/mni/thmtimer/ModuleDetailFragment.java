package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Module;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ModuleDetailFragment extends Fragment {
	
	private TextView mModuleName, mModuleNumber, mCreditPoints, mResponsible,
			mDescription, mExpenditure, mRequirement, mTestingMethod, mSWS,
			mFrequency, mPrereq;
	private Button mBtnEnter;
	private Long mModuleID;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		mModuleID = getArguments().getLong("id", -1);
		
		
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
		mPrereq = (TextView) view.findViewById(R.id.prerequisites);
		mBtnEnter = (Button) view.findViewById(R.id.enter);
		mBtnEnter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO
				Toast.makeText(getActivity(),
						       "TODO: Liste mit allen Kursen zu diesem Modul wo man sich dann wirklich einschreiben kann",
						       Toast.LENGTH_LONG).show();
			}
		});
		
		
		Module m = ModuleDAO.getModuleByID(mModuleID);
		
		mModuleName.setText(m.getName());
		mModuleNumber.setText(m.getNumber());
		mCreditPoints.setText(String.valueOf(m.getCreditPoints()));
		
		mExpenditure.setText(String.format("%d %s",
				                           m.getCreditPoints() * 30,
				                           getString(R.string.hours)));
		
		/*
		 * Im Moment auskommentiert, da vom Server nicht vorgesehen / implementiert
		 * 
		mDescription.setText(c.getDescription()); 
		mResponsible.setText(m.getResponsible());
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
		*/
		
		return view;
	}
}