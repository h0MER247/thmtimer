package de.thm.mni.thmtimer.model;

import org.codehaus.jackson.annotate.JsonProperty;

import de.thm.thmtimer.entities.Category;

public class DurationPerWeek {
	
	public static class Durations {
		
		private Category mCategory;
		private Integer mDuration;
		
		@JsonProperty("category")
		public Category getCategory() {
			
			return mCategory;
		}
		
		@JsonProperty("category")
		public void setCategory(Category category) {
			
			mCategory = category;
		}
		
		
		@JsonProperty("duration")
		public Integer getDuration() {
			
			return mDuration;
		}
		
		@JsonProperty("duration")
		public void setDuration(Integer duration) {
			
			mDuration = duration;
		}
	}
	
	private Integer mCalendarWeek;
	private Durations[] mDurations;
	
	
	@JsonProperty("calendarWeek")
	public Integer getCalendarWeek() {
		
		return mCalendarWeek;
	}
	
	@JsonProperty("calendarWeek")
	public void setCalendarWeek(Integer calendarWeek) {
		
		mCalendarWeek = calendarWeek;
	}
	
	
	@JsonProperty("entries")
	public Durations[] getDurations() {
		
		return mDurations;
	}
	
	@JsonProperty("entries")
	public void setDurations(Durations[] durations) {
		
		mDurations = durations;
	}
}