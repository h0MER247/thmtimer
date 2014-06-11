package de.thm.mni.thmtimer;

import java.util.ArrayList;

import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeStatisticData;
import de.thm.mni.thmtimer.util.StaticModuleData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class TeacherCourseDetailActivity extends FragmentActivity {
	private Long mCourseID;
	private TeacherData mData;
	private ArrayList<TimeStatisticData> mTimeStatistics;
	private Course mCourse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_teachercoursedetail);

		// Referenzen auf die GUI Items
		TextView courseName = (TextView) findViewById(R.id.teachercoursedetail_txtCourseName);
		ExpandableListView detailList = (ExpandableListView) findViewById(R.id.teachercoursedetail_lstDetails);

		// Kursstatistikdaten holen
		mCourseID = getIntent().getExtras().getLong("course_id");

		mData = StaticModuleData.getTeacherData();
		mCourse = StaticModuleData.findCourse(mCourseID);
		mTimeStatistics = mData.getTimeStatistic(mCourseID);

		// GUI Items belegen
		courseName.setText(mCourse.getName());
		detailList.setAdapter(new MyExpandableListAdapter(getApplicationContext()));
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

	private class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private Context mContext;

		public MyExpandableListAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition < 3 ? 1 : 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			View v = null;

			switch (groupPosition) {

			// Anzahl Studenten in diesem Kurs
			case 0:
				v = LayoutInflater.from(mContext)
						.inflate(R.layout.listitem_teachercoursedetail_students, parent, false);

				TextView studentCount = (TextView) v.findViewById(R.id.teachercoursedetail_txtStudentCount);

				studentCount.setText(mContext.getText(R.string.students) + ": " + mCourse.getStudentCount().toString());
				break;

			// Zeitverteilung und PieChart
			case 1:
				v = LayoutInflater.from(mContext).inflate(R.layout.listitem_teachercoursedetail_investedtimes, parent,
						false);

				TextView categorys = (TextView) v.findViewById(R.id.teachercoursedetail_txtCategorys);
				TextView timesInvested = (TextView) v.findViewById(R.id.teachercoursedetail_txtTimesInvested);
				PieChart pieChart = (PieChart) v.findViewById(R.id.teachercoursedetail_pieChart);
				PieChartLegend pieChartLegend = (PieChartLegend) v
						.findViewById(R.id.teachercoursedetail_pieChartLegend);

				// Ermitteln wieviel Zeit insgesamt in diesen Kurs investiert
				// wurde
				Integer totalMinutes = mData.getTimeInvestedTotal(mCourseID).getTimeInMinutes();

				// Den Text f�r die Kategorien und investierten Zeiten erstellen
				String categorysText = "";
				String timesInvestedText = "";

				for (TimeStatisticData t : mTimeStatistics) {

					// Zeit und Kategorie holen
					TimeData td = t.getTime();
					TimeCategory category = StaticModuleData.findTimeCategory(t.getCategoryID());

					//
					// Text f�r die Kategorien und investierten Zeiten erstellen
					//
					if (categorysText.length() != 0)
						categorysText += "\n";
					categorysText += category.getDescription();

					if (timesInvestedText.length() != 0)
						timesInvestedText += "\n";
					timesInvestedText += t.getTime() + " "
							+ String.format("(%04.1f%%)", ((100.0 / totalMinutes) * td.getTimeInMinutes()));

					//
					// Tortenst�ck hinzuf�gen
					//
					pieChart.addValue((float) td.getTimeInMinutes());
					pieChartLegend.addLabel(category.getDescription());
				}

				categorys.setText(categorysText);
				timesInvested.setText(timesInvestedText);
				break;

			case 2: // Tempor�r
				TextView tmp = new TextView(mContext);
				tmp.setText("ToDo");

				v = tmp;
				break;
			}

			return v;
		}

		@Override
		public int getGroupCount() {
			return 3;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_teachercoursedetail_header, parent, false);

			TextView header = (TextView) v.findViewById(R.id.teachercoursedetail_txtParentHeader);

			switch (groupPosition) {

			case 0:
				header.setText(mContext.getText(R.string.inscribedstudents));
				break;

			case 1:
				header.setText(mContext.getText(R.string.timedistribution));
				break;

			case 2:
				header.setText(mContext.getText(R.string.superfeature));
				break;
			}

			return v;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}
	}
}