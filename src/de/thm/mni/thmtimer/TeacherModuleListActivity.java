package de.thm.mni.thmtimer;

import java.util.ArrayList;

import de.thm.mni.thmtimer.model.ModuleData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TeacherModuleListActivity extends Activity {
	
	private class StudentModuleListAdapter extends ArrayAdapter<ModuleData> {

		public StudentModuleListAdapter() {
			
			super(TeacherModuleListActivity.this, R.layout.studentmodulelistitem, m_moduleListStudent); 
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null) {
				
				convertView = getLayoutInflater().inflate(R.layout.studentmodulelistitem, parent, false);
			}
			
			
			final ModuleData module = m_moduleListStudent.get(position);
			
			TextView name     = (TextView) convertView.findViewById(R.id.txtModuleName);
			TextView teacher  = (TextView) convertView.findViewById(R.id.txtStudentCount);
			TextView semester = (TextView) convertView.findViewById(R.id.txtSemester);
			TextView time     = (TextView) convertView.findViewById(R.id.txtTimeInvested);
			Button   action   = (Button)   convertView.findViewById(R.id.btnAction);
			
			name.setText(module.getName());
			teacher.setText(module.getTeacher());
			semester.setText(module.getSemester());
			time.setText(getString(R.string.timeInvested) + module.getTimeInvested());
			action.setText(module.getTimeLogRunning() ? R.string.stop : R.string.start);
			
			
			return convertView;
		}
	}
	
	private class TeacherModuleListAdapter extends ArrayAdapter<ModuleData> {

		public TeacherModuleListAdapter() {
			
			super(TeacherModuleListActivity.this, R.layout.teachermodulelistitem, m_moduleListTeacher); 
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null) {
				
				convertView = getLayoutInflater().inflate(R.layout.teachermodulelistitem, parent, false);
			}
			
			
			final ModuleData module = m_moduleListTeacher.get(position);
			
			TextView name         = (TextView) convertView.findViewById(R.id.txtModuleName);
			TextView studentCount = (TextView) convertView.findViewById(R.id.txtStudentCount);
			TextView semester     = (TextView) convertView.findViewById(R.id.txtSemester);
			TextView time         = (TextView) convertView.findViewById(R.id.txtTimeInvested);
			Button   action   =     (Button)   convertView.findViewById(R.id.btnAction);
			
			name.setText(module.getName());
			studentCount.setText(getString(R.string.studentCount) + String.valueOf(module.getStudentCount()));
			semester.setText(module.getSemester());
			time.setText(getString(R.string.timeInvested) + module.getTimeInvested());
			action.setText(module.getTimeLogRunning() ? R.string.stop : R.string.start);
			
			
			return convertView;
		}
	}
	
	private ArrayList<ModuleData> m_moduleListTeacher;
	private ArrayList<ModuleData> m_moduleListStudent;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.teachermodulelistactivity);
		
		
		setupTabHost();
		createSomeModuleData();
		createListViewTeacher();
		createListViewStudent();
	}
	
	
	private void setupTabHost() {
		
		TabHost tabHost;
		TabSpec tabSpec;
		
		tabHost = (TabHost)findViewById(R.id.tabhost);
		tabHost.setup();
		
		tabSpec = tabHost.newTabSpec("teacher");
		tabSpec.setContent(R.id.tabTeacher);
		tabSpec.setIndicator(getString(R.string.tab1));
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("student");
		tabSpec.setContent(R.id.tabStudent);
		tabSpec.setIndicator(getString(R.string.tab2));
		tabHost.addTab(tabSpec);
	}
	
	private void createSomeModuleData() {
		
		m_moduleListTeacher = new ArrayList<ModuleData>();
		m_moduleListStudent = new ArrayList<ModuleData>();
		
		m_moduleListStudent.add(new ModuleData(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h 3m", false));
		m_moduleListStudent.add(new ModuleData(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h 4m", false));
		m_moduleListStudent.add(new ModuleData(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h 12m", true));
		
		m_moduleListTeacher.add(new ModuleData(0, "Grundlagen der Informatik", 123, "Prof. Dr. Kneisel", "Sommersemester 2014", "15h 4m",  true));
		m_moduleListTeacher.add(new ModuleData(0, "Internetbasierte Systeme",  123, "Prof. Dr. Kneisel", "Sommersemester 2014", "16h 23m", false));
	}
	
	private void createListViewStudent() {
		
		ListView listView = (ListView)this.findViewById(R.id.lstStudentModules);
		
		listView.setAdapter(new StudentModuleListAdapter());
	}
	
	private void createListViewTeacher() {
		
		ListView listView = (ListView)this.findViewById(R.id.lstTeacherModules);
		
		listView.setAdapter(new TeacherModuleListAdapter());
	}
}