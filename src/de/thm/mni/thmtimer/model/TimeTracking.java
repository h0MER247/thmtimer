package de.thm.mni.thmtimer.model;

public class TimeTracking {
	public String label;
	public int hours;
	public int minutes;

	public TimeTracking(String label) {
		this.label = label;
		this.hours = 1;
		this.minutes = 30;
	}

	@Override
	public String toString() {
		return label + " (" + hours + "h, " + minutes + "min)";
	}
}
