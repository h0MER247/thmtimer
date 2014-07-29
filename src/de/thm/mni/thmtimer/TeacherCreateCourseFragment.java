package de.thm.mni.thmtimer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Module;
import android.annotation.SuppressLint;
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

	private EditText mCourseName, mDescription;
	DatePicker mStartDate;
	private Button mCreate;
	private String courseName, description;
	private Long mModuleID;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (View) inflater.inflate(R.layout.teachercreatecourse,
				container, false);

		// get views
		mCourseName = (EditText) view.findViewById(R.id.courseName);
		mDescription = (EditText) view.findViewById(R.id.description);
		mStartDate = (DatePicker) view.findViewById(R.id.startDate);
		mCreate = (Button) view.findViewById(R.id.crate);

		mModuleID = getArguments().getLong("id", -1);
		Module m = ModuleDAO.getModuleByID(mModuleID);
		// Module module = StaticModuleData.findModule(id);

		mCourseName.setText(m.getName());

		mCreate.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				courseName = mCourseName.getText().toString();
				description = mDescription.getText().toString();
				if (courseName.isEmpty()) {
					Toast.makeText(getActivity(),
							"Kursname darf nicht leer sein.",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (description.isEmpty()) {
					// Leere Beschreibung sollte hoffentlich bald erlaubt sein:
					// https://scm.thm.de/redmine/issues/11182
					Toast.makeText(getActivity(),
							"Kursbeschreibung darf nicht leer sein.",
							Toast.LENGTH_LONG).show();
					return;
				}
				Calendar calendar = new GregorianCalendar(mStartDate.getYear(), 
						mStartDate.getMonth(), mStartDate.getDayOfMonth());
				
				EnterModuleActivity activity = (EnterModuleActivity) getActivity();
				activity.onCreateCourse(ModuleDAO.getTermList().get(0).getId(),
						mModuleID, courseName, calendar.getTimeInMillis(), description);
			}
		});

		return view;
	}
}
