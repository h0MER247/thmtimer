package de.thm.mni.thmtimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import de.thm.mni.thmtimer.customviews.Legend;
import de.thm.mni.thmtimer.customviews.LineChart;
import de.thm.mni.thmtimer.model.DurationPerWeek;
import de.thm.mni.thmtimer.model.DurationPerWeek.Duration;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.util.ModuleDAO;
import de.thm.thmtimer.entities.Category;


public class TeacherCourseDetailLinechartFragment extends Fragment implements LineChart.DataPointOnClickListener {
	
	private LineChart mLineChart;
	private Legend mLegend;
	private Boolean mShowCategories;
	private Boolean mShowTotal;
	//private List<DurationPerWeek> mRandomData;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		
		mShowCategories = true;
		mShowTotal      = false;
		
		//if(mRandomData == null)
		//	createRandomData(3);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.teachercoursedetaillinechartfragment,
				                           container,
				                           false);
		
		mLineChart = (LineChart)view.findViewById(R.id.teachercoursedetail_historyChart);
		mLegend = (Legend)view.findViewById(R.id.teachercoursedetail_historyLegend);
		
		
		final Button showCategories = (Button)view.findViewById(R.id.teachercoursedetail_btnCategories);
		showCategories.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mShowCategories = !mShowCategories;
				
				showCategories.setText(mShowCategories ? getString(R.string.hideCategories) :
					                                     getString(R.string.showCategories));
				
				updateChart();
			}
		});
		
		
		final Button showTotal = (Button)view.findViewById(R.id.teachercoursedetail_btnTotal);
		showTotal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mShowTotal = !mShowTotal;
				
				showTotal.setText(mShowTotal ? getString(R.string.hideTotal) :
					                           getString(R.string.showTotal));
				
				updateChart();
			}
		});
		
		return view;
	}
	
	@Override
	public void onDataPointClicked(Object data) {
		
		Duration myData = (Duration)data;
		
		// Kategorie
		String category = myData.getCategory().getName();
		
		// Investierte Zeit
		TimeData invested = new TimeData();
		invested.setTimeInMinutes(myData.getDuration());
		
		// Toastnachricht anzeigen
		Toast.makeText(getActivity(),
				       String.format("Invested time in '%s':\n%s h",
				    		         category,
				    		         invested.toString()),
				       Toast.LENGTH_LONG).show();
	}
	
	
	/*
	private void createRandomData(Integer students) {
		
		//
		// Zufallsdaten generieren, bis Server die Daten liefern kann
		//
		mRandomData = new ArrayList<DurationPerWeek>();
		
		Random rnd = new Random();
		List<Category> categories = ModuleDAO.getTimeCategorys();

		for(int kw  = 14;
				kw <= 34;
				kw++) {
			
			DurationPerWeek data     = new DurationPerWeek();
			List<Duration> durations = new ArrayList<Duration>();
			
			
			for(Category c : categories) {
				
				Duration d = new Duration();
				d.setCategory(c);
				d.setDuration(rnd.nextInt(6 * 8 * 60 * students));
				
				durations.add(d);
			}
			
			data.setCalendarWeek(kw);
			data.setDurations(durations);
			
			
			mRandomData.add(data);
		}
	}
	*/
	
	
	public void setGrabTouch(boolean enableGrab) {
		
		mLineChart.setGrabTouch(enableGrab);
	}
	
	public void updateChart() {
		
		final int Y_VISIBLE_HOURS = 8;
		final List<DurationPerWeek> durationsPerWeek;
		final List<Category> categories;
		
			
		//
		// Statistikdaten lesen
		//
		/*
		if(ModuleDAO.getDurationPerWeek() == null) {
			
			//
			// Falls das Severteam es nicht schafft das bis Montag zu fixen,
			// werden halt Zufallsdaten angezeigt, damit wir was zum 
			// vorzeigen haben
			//
			durationsPerWeek = mRandomData;
		}
		else {
		*/
			durationsPerWeek = ModuleDAO.getDurationPerWeek();
		//}
		
		if(durationsPerWeek.size() == 0)
			return;
		
		
		//
		// Kategorien lesen
		//
		categories = new ArrayList<Category>(ModuleDAO.getTimeCategorys());
		
		final Category categoryTotal = new Category(categories.size(),     "TOT", "Total");
		final Category categoryAvg   = new Category(categories.size() + 1, "AVG", "Average");
		
		categories.add(categoryTotal);
		categories.add(categoryAvg);
		
		
		
		// Daten sortieren, da es (wieso auch immer) unsortiert vom Server kommt...
		
		// Zeitkategorien sortieren
		Collections.sort(categories, new Comparator<Category>() {
			
			@Override
			public int compare(Category lhs, Category rhs) {
				
				return (int)(lhs.getId() - rhs.getId());
			}
		});
		
		// Zeitstatistikeinträge nach Kalenderwoche sortieren
		Collections.sort(durationsPerWeek, new Comparator<DurationPerWeek>() {

			@Override
			public int compare(DurationPerWeek lhs, DurationPerWeek rhs) {
				
				return lhs.getCalendarWeek() - rhs.getCalendarWeek();
			}
		});
		
		// Kategorien der Zeitstatistikeinträge sortieren
		for(DurationPerWeek d : durationsPerWeek) {
			
			Collections.sort(d.getDurations(), new Comparator<Duration>() {

				@Override
				public int compare(Duration lhs, Duration rhs) {
					
					return (int)(lhs.getCategory().getId() - rhs.getCategory().getId());
				}
			});
		}		
		
				
		
		//
		// Legende mit Daten befüllen
		//
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			mLegend.setDrawSideBySide(true);
		else
			mLegend.setDrawSideBySide(false);
		
		mLegend.clearData();
		for(Category c : categories) {
			
			mLegend.addLegendLabel(c.getName());
		}
		
		
		//
		// Chart mit Daten befüllen
		//
		Integer maxTime   = Integer.MIN_VALUE;
		Integer weekStart = durationsPerWeek.get(0).getCalendarWeek();
		Integer weekEnd   = durationsPerWeek.get(durationsPerWeek.size() - 1).getCalendarWeek();
		Integer weekTotal;
		Integer weekAvg;
		
		
		mLineChart.beginChart(categories.size());
		
		for(DurationPerWeek week : durationsPerWeek) {
			
			//
			// Kategorien hinzufügen
			//
			for(Duration duration : week.getDurations()) {
				
				if(mShowCategories) {
					
					Long  index = duration.getCategory().getId() - 1;
					Float hours = duration.getDuration().floatValue() / 60f;
					
					mLineChart.addValueToSeries(index.intValue(),
							                    hours,
							                    duration);
				}
				
				if(!mShowTotal) {
					
					if(maxTime < duration.getDuration())
						maxTime = duration.getDuration();
				}
			}
			
			if(mShowTotal) {
				
				if(maxTime < week.getTotalDuration())
					maxTime = week.getTotalDuration();
			}
			
			weekTotal = week.getTotalDuration();
			weekAvg   = week.getAverageDuration();
			
			//
			// Gesamtzeit hinzufügen
			//
			if(mShowTotal) {
				
				Duration durationTotal = new Duration();
				durationTotal.setDuration(weekTotal);
				durationTotal.setCategory(categoryTotal);
				
				mLineChart.addValueToSeries(categories.size() - 2,
						                    weekTotal.floatValue() / 60f,
						                    durationTotal);
			}
			
			//
			// Durchschnitt hinzufügen
			//
			Duration durationAvg = new Duration();
			durationAvg.setDuration(weekAvg);
			durationAvg.setCategory(categoryAvg);
			
			mLineChart.addValueToSeries(categories.size() - 1,
					                    weekAvg.floatValue() / 60f,
					                    durationAvg);
		}
		
		mLineChart.endChart();
		
		
		//
		// Labels für die X-Achse erzeugen
		//
		ArrayList<String> labelsX = new ArrayList<String>();
		
		for(int i  = weekStart;
				i <= weekEnd;
				i++) {
			
			labelsX.add(String.format("KW %d", i));
		}
		
		mLineChart.setLabelsX(labelsX);
		mLineChart.setLabelsY("%d h");
		
		
		//
		// Chartgröße festlegen
		//
		maxTime = (int)Math.ceil(maxTime.doubleValue() / 60.0);
		
		if(maxTime > Y_VISIBLE_HOURS) {
			
			mLineChart.setChartSize(7, labelsX.size(), 1 + Y_VISIBLE_HOURS, 1 + maxTime);
			mLineChart.setScaleXY(1, maxTime / Y_VISIBLE_HOURS);
		}
		else {
			
			mLineChart.setChartSize(7, labelsX.size(), 1 + maxTime, 1 + maxTime);
			mLineChart.setScaleXY(1, 1);
		}
		
		
		//
		// DataPointOnClickListener setzen
		//
		mLineChart.setDataPointOnClickListener(this);
	}
}