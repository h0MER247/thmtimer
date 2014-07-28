package de.thm.mni.thmtimer;

import java.text.SimpleDateFormat;

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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TeacherCreateCourseFragment extends Fragment {

	private TextView mName;
	private EditText mCourseName, mDescription, mStartDate;
	private Button mCreate;
	private String courseName, description, sdate;
	private Long mModuleID;
	private java.util.Date date;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = (View) inflater.inflate(R.layout.teachercreatecourse,
				container, false);

		// get views
		mName = (TextView) view.findViewById(R.id.name);
		mCourseName = (EditText) view.findViewById(R.id.courseName);
		mDescription = (EditText) view.findViewById(R.id.description);
		mStartDate = (EditText) view.findViewById(R.id.startDate);
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
				sdate = mStartDate.getText().toString();
				SimpleDateFormat sdfToDate = new SimpleDateFormat(
						"dd.MM.yyyy");
				try {
					date = sdfToDate.parse(sdate);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				if(courseName.equals("")||courseName==null)
					return;
				if(description.equals("")||description==null)
					return;
				if(date==null)
					return;
				EnterModuleActivity activity = (EnterModuleActivity) getActivity();
				ModuleDAO.getTermList().get(0).getId();
				activity.onCreateCourse(ModuleDAO.getTermList().get(0).getId(),
						mModuleID, courseName, date.getTime(), description);
			}
		});

		return view;
	}
}
