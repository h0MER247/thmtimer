package de.thm.mni.thmtimer;

import java.util.List;

import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.PieChart;
import de.thm.mni.thmtimer.model.DurationPerCategory;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TeacherCourseDetailPiechartFragment extends Fragment {
	
	private PieChart mPieChart;
	private Legend mLegend;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.teachercoursedetailpiechartfragment,
				                           container,
				                           false);
		
		mPieChart = (PieChart)view.findViewById(R.id.teachercoursedetail_pieChart);
		mLegend = (Legend)(Legend)view.findViewById(R.id.teachercoursedetail_pieChartLegend);
		
		return view;
	}
	
	
	public void updateChart() {
		
		final List<DurationPerCategory> durations = ModuleDAO.getDurationPerCategory();
		
		if(durations != null) {
		
			// Ermitteln wieviel Zeit insgesamt in diesen Kurs investiert wurde
			Integer totalMinutes = 0;
			
			for(DurationPerCategory duration : durations) {
				
				totalMinutes += duration.getDuration();
			}
			
			//
			// Pie Chart mit den Daten befüllen
			//
			TimeData timeData = new TimeData();
			
			for(DurationPerCategory d : durations) {
				
				Integer  duration = d.getDuration();
				Category category = d.getCategory();
				
				mPieChart.addValue(duration.floatValue());
				
				timeData.setTimeInMinutes(duration);
				mLegend.addLegendLabel(String.format("%s: %sh (%04.1f%%)",
	                                                category.getName(),
	                                                timeData.toString(),
	                                                (100.0f / totalMinutes) * duration));
			}
		}
	}
}