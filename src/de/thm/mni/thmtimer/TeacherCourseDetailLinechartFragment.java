package de.thm.mni.thmtimer;

import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.LineChart;
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
	
	
	public void updateChart() {
		
		/*
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
		*/
		
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
