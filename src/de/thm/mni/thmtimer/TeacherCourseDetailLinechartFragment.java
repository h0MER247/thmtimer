package de.thm.mni.thmtimer;

import java.util.List;
import java.util.Random;

import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.LineChart;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class TeacherCourseDetailLinechartFragment extends Fragment implements LineChart.DataPointOnClickListener {
	
	private class ChartClickableData {
		
		public Category category;
		public Integer  week;
		public Float    investedTime;
	}
	
	private LineChart mLineChart;
	private Legend mLegend;
	private Boolean mShowAverage;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		mShowAverage = false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = (View)inflater.inflate(R.layout.teachercoursedetaillinechartfragment,
				                           container,
				                           false);
		
		mLineChart = (LineChart)view.findViewById(R.id.teachercoursedetail_historyChart);
		mLegend = (Legend)view.findViewById(R.id.teachercoursedetail_historyLegend);
		
		final Button averageOrTotal = (Button)view.findViewById(R.id.teachercoursedetail_btnAverageTotal);
		averageOrTotal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mShowAverage = !mShowAverage;
				
				averageOrTotal.setText(mShowAverage ? R.string.showTotal :
					                                  R.string.showAverage);
				
				updateChart();
			}
		});
		
		return view;
	}
	
	
	
	public void setGrabTouch(boolean enableGrab) {
		
		mLineChart.setGrabTouch(enableGrab);
	}
	
	public void updateChart() {
		
		int orientation = getResources().getConfiguration().orientation;
		
		
		// Je nach Drehung des Screens die Legende anpassen
		mLegend.setDrawSideBySide(orientation == Configuration.ORIENTATION_PORTRAIT);
		
		//
		// Im Moment einfach random Daten erzeugen
		//
		String[] labelsX = new String[30];
		String[] labelsY = new String[12 + 1];
		
		for(int i = 0; i < labelsX.length; i++)
			labelsX[i] = String.format("KW %d", 18 + i);
		
		for(int i = 0; i < labelsY.length; i++)
			labelsY[i] = String.format("%d h", i);
		
		List<Category> categorys = ModuleDAO.getTimeCategorys();
		
		
		Random rnd = new Random();
		
		for(Category category : categorys) {

			mLineChart.beginSeries();
			
			for(int i = 0;
					i < labelsX.length;
					i++) {

				ChartClickableData d = new ChartClickableData();
				d.category = category;
				d.week     = 18 + i;
				d.investedTime = rnd.nextFloat() * 12f;
				
				mLineChart.addValueToSeries(d.investedTime, d);
			}
			
			mLineChart.endSeries();
			mLegend.addLegendLabel(category.getName());
		}
		
		mLineChart.setLabels(labelsX, labelsY);
		mLineChart.setChartSize(7, labelsX.length, 9, labelsY.length);
		mLineChart.setDataPointOnClickListener(this);
	}

	@Override
	public void onDataPointClicked(Object data) {
		
		ChartClickableData myData = (ChartClickableData)data;
		
		TimeData t = new TimeData();
		t.setTimeInMinutes((int)(myData.investedTime * 60f));
		
		Toast.makeText(getActivity(),
				       String.format("Week: %d\nCategory: %s\nInvested Time: %s",
				    		         myData.week,
				    		         myData.category.getName(),
				    		         t.toString()),
				       Toast.LENGTH_LONG).show();
	}
}