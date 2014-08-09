package de.thm.mni.thmtimer.model;

import org.codehaus.jackson.annotate.JsonProperty;

import de.thm.thmtimer.entities.Category;

public class DurationPerCategory {
		
	private Category mCategory;
	private Integer mDuration;
	
	
	@JsonProperty("key")
	public Category getCategory() {
		
	    return mCategory;
	}
	
	@JsonProperty("key")
	public void setCategory(Category category) {
		
		mCategory = category;
	}
	
	@JsonProperty("value")
	public Integer getDuration() {
		
		return mDuration;		
	}
	
	@JsonProperty("value")
	public void setDuration(Integer duration) {
		
		mDuration = duration;		
	}
}