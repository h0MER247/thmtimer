package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Random;

import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.LineChart;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TeacherCourseDetailLinechartFragment extends Fragment {

	private LineChart mLineChart;
	private Legend mLegend;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.teachercoursedetaillinechartfragment,
				                           container,
				                           false);
		
		mLineChart = (LineChart)view.findViewById(R.id.teachercoursedetail_historyChart);
		mLegend = (Legend)view.findViewById(R.id.teachercoursedetail_historyLegend);
		
		
		Button categoryToggle = (Button)view.findViewById(R.id.teachercoursedetail_btnCategoryToggle);
		categoryToggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO
			}
		});
		
		return view;
	}
	
	
	
	public void setGrabTouch(boolean enableGrab) {
		
		mLineChart.setGrabTouch(enableGrab);
	}
	
	public void updateChart() {
		
		//
		// Im Moment einfach random Daten erzeugen
		//
		String[] labelsX = new String[12];
		String[] labelsY = new String[13];
		
		for(int i = 0; i < 12; i++)
			labelsX[i] = String.format("KW %d", 18 + i);
		
		for(int i = 0; i < 13; i++)
			labelsY[i] = String.format("%d h", i);
		
		
		ArrayList<Float> data;
		Random rnd = new Random();
		
		for(long id = 1;
				 id < 3;
				 id++) {
			
			Category category = ModuleDAO.getTimeCategoryByID(id);
			data = new ArrayList<Float>();
			
			for(int i = 0;
					i <= 12;
					i++) {
				
				data.add(rnd.nextFloat() * 12f);
			}
			
			mLineChart.addChartSeries(data);
			mLegend.addLegendLabel(category.getName());
		}
		
		mLineChart.setLabels(labelsX, labelsY);
		mLineChart.setChartSize(7, 12, 9, 13);
	}
}