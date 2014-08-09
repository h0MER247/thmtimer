package de.thm.mni.thmtimer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import de.thm.thmtimer.entities.Category;

public class DurationPerWeek {
	
	public static class Duration {
		
		private Category mCategory;
		private Integer mDuration;
		
		
		@JsonProperty("category")
		public void setCategory(Category category) {
			
			mCategory = category;
		}
		
		@JsonProperty("duration")
		public void setDuration(Integer duration) {
			
			mDuration = duration;
		}
		
		
		public Category getCategory() {
			
			return mCategory;
		}
		
		public Integer getDuration() {
			
			return mDuration;
		}
	}
	
	private Integer mCalendarWeek;
	private List<Duration> mDurations;
	
	
	@JsonProperty("calendarWeek")
	public void setCalendarWeek(Integer calendarWeek) {
		
		mCalendarWeek = calendarWeek;
	}
	
	@JsonProperty("entries")
	public void setDurations(Duration[] durations) {
		
		mDurations = Arrays.asList(durations);
	}
	
	public void setDurations(List<Duration> durations) {
		
		mDurations = new ArrayList<Duration>(durations);
	}
	
	
	public Integer getCalendarWeek() {
		
		return mCalendarWeek;
	}	
	
	public List<Duration> getDurations() {
		
		return mDurations;
	}
}