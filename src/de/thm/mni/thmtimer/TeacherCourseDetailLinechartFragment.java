package de.thm.mni.thmtimer;

import java.util.ArrayList;
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
		
		public ChartClickableData(Category category, Integer week, Float investedTime) {
			
			this.category = category;
			this.week = week;
			this.investedTime = investedTime;
		}
	}
	
	private LineChart mLineChart;
	private Legend mLegend;
	private Boolean mShowAverage;
	private ArrayList<ArrayList<ChartClickableData>> mRandomData;
	
	
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
				
				averageOrTotal.setText(mShowAverage ? R.string.hideAverage :
					                                  R.string.showAverage);
				
				updateChart();
			}
		});
		
		Button selectCategories = (Button)view.findViewById(R.id.teachercoursedetail_btnSelectCategories);
		selectCategories.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
			}
		});
		
		return view;
	}
	
	
	
	@Override
	public void onDataPointClicked(Object data) {
		
		ChartClickableData myData = (ChartClickableData)data;

		String week, category, time;
		

		// Week String
		week = String.format("KW %d", myData.week);
		
		// Category String
		if(myData.category == null)
			category = "Average of all categories";
		else
			category = myData.category.getName();
		
		// Time String
		TimeData t = new TimeData();
		t.setTimeInMinutes((int)(myData.investedTime * 60f));
		time = String.format("Invested Time: %s", t.toString());
		
		//
		// Daten anzeigen
		//
		Toast.makeText(getActivity(),
				       String.format("%s\n%s\n%s", week, category, time),
				       Toast.LENGTH_LONG).show();
	}
	
	
	public void createRandomData() {
		
		final int STUDENT_COUNT = 35;
		
		/*
		 * Zufallsdaten generieren, bis Server die Daten liefern kann
		 */
		mRandomData = new ArrayList<ArrayList<ChartClickableData>>();
		
		Random rnd = new Random();
		List<Category> categories = ModuleDAO.getTimeCategorys();
		
		for(Category c : categories) {
			
			ArrayList<ChartClickableData> randomData = new ArrayList<ChartClickableData>();
			
			for(int kw = 0;
					kw < 54;
					kw++) {
				
				// 7 Tage pro Woche, 12 Studen maximal pro Student... Whatever ^^
				randomData.add(new ChartClickableData(c, kw, rnd.nextFloat() * (7f * 12f * STUDENT_COUNT)));
			}
			
			mRandomData.add(randomData);
		}
	}
	
	
	
	public void setGrabTouch(boolean enableGrab) {
		
		mLineChart.setGrabTouch(enableGrab);
	}
	
	// TODO: Anpassen sobald echte Daten vom Server vorliegen
	public Float getAverageFromWeek(int week) {
		
		Float valueTotal = 0f;
		int   numValues  = 0;
		
		for(ArrayList<ChartClickableData> data : mRandomData) {
			
			valueTotal += data.get(week).investedTime;
			
			numValues++;
		}
		
		return valueTotal / numValues;
	}
	
	
	public void updateChart() {
		
		int orientation = getResources().getConfiguration().orientation;
		
		// Daten löschen
		mLegend.clearData();
		mLineChart.clearData();
		
		// Legende konfigurieren
		mLegend.setDrawSideBySide(orientation == Configuration.ORIENTATION_PORTRAIT);
		
		
		//
		// Chart und Legende füllen
		//
		int maxTime = 0;
		int maxWeek = 0;
		
		for(ArrayList<ChartClickableData> randomData : mRandomData) {
			
			if(randomData.size() > maxWeek)
				maxWeek = randomData.size();
			
			// Legende mit Kategorie füllen
			mLegend.addLegendLabel(randomData.get(0).category.getName());
			
			//
			// Linechart aufbauen
			//
			
			mLineChart.beginSeries();
			for(ChartClickableData data : randomData) {
				
				mLineChart.addValueToSeries(data.investedTime, data);
				
				if(data.investedTime > maxTime)
					maxTime = data.investedTime.intValue();
			}
			mLineChart.endSeries();
		}
		
		//
		// Durchschnitt anzeigen
		//
		if(mShowAverage) {

			mLineChart.beginSeries();
			
			for(int kw = 0;
					kw < 54;
					kw++) {
				
				ChartClickableData x = new ChartClickableData(null, kw, getAverageFromWeek(kw));
				
				mLineChart.addValueToSeries(x.investedTime, x);
			}
			
			mLineChart.endSeries();
			mLegend.addLegendLabel(getString(R.string.average));
		}
		
		//
		// Labels erzeugen
		//
		String labelsX[] = new String[maxWeek];
		String labelsY[] = new String[maxTime];
		
		for(int i = 0;
				i < maxWeek;
				i++) {
			
			labelsX[i] = String.format("KW %d", i);
		}
		for(int i = 0;
				i < maxTime;
				i++) {
			
			labelsY[i] = String.format("%d h", i);
		}
		
		mLineChart.setLabels(labelsX, labelsY);
		
		
		//
		// Chartgröße festlegen
		//
		mLineChart.setChartSize(7, labelsX.length, 12, labelsY.length);
		mLineChart.setScaleXY(1, labelsY.length / 12);
		
		
		//
		// DataPointOnClickListener setzen
		//
		mLineChart.setDataPointOnClickListener(this);
	}
}