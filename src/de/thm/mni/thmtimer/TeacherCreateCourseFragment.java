package de.thm.mni.thmtimer;

import java.text.SimpleDateFormat;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.util.StaticModuleData;
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

public class TeacherCreateCourseFragment extends Fragment{

	private TextView mName;
	private EditText mCourseName, mTeacher, mStartDate;
	private Button mCreate;
	private String courseName, teacher, sdate;
	private Long id;
	private java.util.Date date;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.teachercreatecourse, container, false);

		// get views
		mName = (TextView) view.findViewById(R.id.name);
		mCourseName = (EditText)view.findViewById(R.id.courseName);
		mTeacher = (EditText)view.findViewById(R.id.teacher);
		mStartDate = (EditText)view.findViewById(R.id.startDate);
		mCreate = (Button)view.findViewById(R.id.crate);

		id = getArguments().getLong("id", -1);
		Module module = StaticModuleData.findModule(id);	

		mCourseName.setText(module.getName());		

		mCreate.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				courseName = mCourseName.getText().toString();
				teacher = mTeacher.getText().toString();
				sdate = mStartDate.getText().toString();
				SimpleDateFormat sdfToDate = new SimpleDateFormat(
						"dd.MM.yyyy HH:mm:ss");
				try {
					date = sdfToDate.parse(sdate);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}

				// TODO: Add server callback
				/*Course course = new Course(100l, id, courseName, teacher, 0);
				course.setStartDate(date);
				StaticModuleData.getCourseList().add(course);*/
			}
		});


		return view;

	}	


}
