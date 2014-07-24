package de.thm.mni.thmtimer;

import java.util.ArrayList;

import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.LineChart;
import de.thm.mni.thmtimer.customviews.PieChart;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.mni.thmtimer.util.ModuleDAOListener;
import de.thm.thmtimer.entities.Category;
import de.thm.thmtimer.entities.Course;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;


public class TeacherCourseDetailActivity extends Activity implements ModuleDAOListener {
	
	private final String TAG = TeacherCourseDetailActivity.class.getSimpleName();
	
	//private final int DAO_REQUEST_DURATION_PER_CATEGORY = 0; // TODO
	//private final int DAO_REQUEST_DURATIONS_PER_WEEK = 1; // TODO
	
	private Long mCourseID;
	private Course mCourse;
	private ArrayList<Category> mTimeCategorys;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_teachercoursedetail);
		
		
		if(mTimeCategorys == null)
			mTimeCategorys = new ArrayList<Category>();
		
		
		mCourseID = getIntent().getExtras().getLong("course_id");
		mCourse   = ModuleDAO.getTeacherCourseByID(mCourseID);
		
		
		mTimeCategorys.clear();
		mTimeCategorys.addAll(ModuleDAO.getTimeCategorys());
		
		
		// Kursname setzen
		TextView courseName = (TextView)findViewById(R.id.teachercoursedetail_txtCourseName);
		courseName.setText(mCourse.getName());
		
		// Eingeschriebene Studenten setzen
		TextView enrolledStudents = (TextView)findViewById(R.id.teachercoursedetail_txtEnrolledStudents);

		enrolledStudents.setText(String.format("%s: %d",
				                               getText(R.string.students),
                                               mCourse.getUsers().size()));
		
		//
		// TODO: ViewPager verwenden
		//
		// Tabs für investierte Zeiten und Historie initialisieren
		TabHost tabHost = (TabHost) findViewById(R.id.teachercoursedetail_tabHost);
		TabSpec tab1;
		TabSpec tab2;
		
		tabHost.setup();
		
		tab1 = tabHost.newTabSpec("tab1");
		tab1.setContent(R.id.teachercoursedetail_tabInvestedTimes);
		tab1.setIndicator("Invested Times");
		tabHost.addTab(tab1);
		
		tab2 = tabHost.newTabSpec("tab2");
		tab2.setContent(R.id.teachercoursedetail_tabHistory);
		tab2.setIndicator("History");
		tabHost.addTab(tab2);
		
		// Alle Statistikdaten frisch anfordern
		//ModuleDAO.beginJob();
		// TODO: Statistikdaten holen
		//ModuleDAO.commitJob(this, this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}

		return false;
	}
	

	
	@Override
	public void onDAOError(int requestID, String message) {
		
		//switch(requestID) {
		//
		//}
	}
	
	@Override
	public void onDAOFinished() {
		
		setupInvestedTimes();
		setupHistory();
	}
	
	
	
	private void setupInvestedTimes() {
		
		PieChart pieChart       = (PieChart)findViewById(R.id.teachercoursedetail_pieChart);
		Legend   pieChartLegend = (Legend)  findViewById(R.id.teachercoursedetail_pieChartLegend);
		
		
		// Ermitteln wieviel Zeit insgesamt in diesen Kurs investiert wurde
		/*
		Integer totalMinutes = mData.getTimeInvestedTotal(mCourseID).getTimeInMinutes();
		
		for(TimeStatisticData t : mTimeStatistics) {

			// Zeit und Kategorie holen
			TimeData td = t.getTime();
			TimeCategory category = StaticModuleData.findTimeCategory(t.getCategoryID());
			
			// Zeitwert zum Tortenstück hinzufügen
			pieChart.addValue((float) td.getTimeInMinutes());
			
			// Kategorie zur Legende hinzufügen
			pieChartLegend.addLegendLabel(String.format("%s: %sh (%04.1f%%)",
					                                    category.getDescription(),
					                                    t.getTime().toString(),
					                                    (100.0f / totalMinutes) * td.getTimeInMinutes()));
		}
		*/
	}
	
	
	//
	// TODO: Serverteam ein Ticket schreiben, da ihre Daten unzureichend sind. Es wird nur die Zeit
	//       pro Woche insgesamt geliefert, und nicht Zeit pro Woche je Kategorie. :(
	//
	private void setupHistory() {
		
		LineChart historyChart   = (LineChart)findViewById(R.id.teachercoursedetail_historyChart);
		Legend    historyLegend  = (Legend)   findViewById(R.id.teachercoursedetail_historyLegend);
		Button    categoryToggle = (Button)   findViewById(R.id.teachercoursedetail_btnCategoryToggle);
		
		
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			
			historyLegend.setDrawSideBySide(false);
		}
		else {
			
			historyLegend.setDrawSideBySide(true);
			
			categoryToggle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Toast.makeText(getApplication(), "ToDo: Möglichkeit einzelne Kategorien ein-/auszublenden", Toast.LENGTH_LONG).show();
				}
			});
		}
		
		
		//
		// Im Moment einfach random Daten erzeugen
		//
		/*
		String[] labelsX = new String[12];
		String[] labelsY = new String[13];
		
		for(int i = 0; i < 12; i++)
			labelsX[i] = String.format("KW %d", 18 + i);
		
		for(int i = 0; i < 13; i++)
			labelsY[i] = String.format("%d h", i);
		
		
		ArrayList<Float> data;
		Random rnd = new Random();
		
		for(long id = 0;
				 id < 5;
				 id++) {
			
			TimeCategory category = StaticModuleData.findTimeCategory(id);
			data = new ArrayList<Float>();
			
			for(int i = 0;
					i < 12;
					i++) {
				
				data.add(rnd.nextFloat() * 12f);
			}
			
			historyChart.addChartSeries(data);
			historyLegend.addLegendLabel(category.getDescription());
		}
		
		historyChart.setLabels(labelsX, labelsY);
		historyChart.setChartSize(7, 12, 9, 13);
		*/
	}
}