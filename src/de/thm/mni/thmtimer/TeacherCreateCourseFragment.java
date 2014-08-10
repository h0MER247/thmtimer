package de.thm.mni.thmtimer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Module;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


public class TeacherCreateCourseFragment extends Fragment {

	private EditText mCourseName;
	private EditText mDescription;
	private DatePicker mStartDate;
	private Button mCreate;
	private Long mModuleID;
	
	
	public View onCreateView(LayoutInflater inflater,
			                 ViewGroup container,
			                 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.teachercreatecourse,
				                     container,
				                     false);
		
		mModuleID = getArguments().getLong("id", -1);
		
		
		// Get views
		mCourseName  = (EditText)view.findViewById(R.id.courseName);
		mDescription = (EditText)view.findViewById(R.id.description);
		mStartDate   = (DatePicker)view.findViewById(R.id.startDate);
		mCreate      = (Button)view.findViewById(R.id.create);
		
		mCourseName.setText(ModuleDAO.getModuleByID(mModuleID).getName());
		
		mCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String courseName = mCourseName.getText().toString();
				String description = mDescription.getText().toString();
				
				if(courseName.isEmpty()) {
					
					Toast.makeText(getActivity(),
							       R.string.createcourse_no_name,
							       Toast.LENGTH_LONG).show();
				}
				else if(description.isEmpty()) {
					
					// Leere Beschreibung sollte hoffentlich bald erlaubt sein:
					// https://scm.thm.de/redmine/issues/11182
					Toast.makeText(getActivity(),
							       R.string.createcourse_no_desc,
							       Toast.LENGTH_LONG).show();
				}
				else {
				
					Calendar calendar = new GregorianCalendar(mStartDate.getYear(),
							                                  mStartDate.getMonth(),
							                                  mStartDate.getDayOfMonth());
					
					EnterModuleActivity activity = (EnterModuleActivity)getActivity();
					
					activity.onCreateCourse(ModuleDAO.getTermList().get(0).getId(),
							                mModuleID,
							                courseName,
							                calendar.getTimeInMillis(),
							                description);
				}
			}
		});
		
		return view;
	}
	
	// Hack für Fehler #11167
	public void setModuleID(long id) {
		
		mModuleID = id;
		mCourseName.setText(ModuleDAO.getModuleByID(mModuleID).getName());
	}
}