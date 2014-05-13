package de.thm.mni.thmtimer.model;

public class TimeTracking {
	public String label;
	public int minutes;

	public TimeTracking(String label, int minutes) {
		this.label = label;
		this.minutes = minutes;
	}

	/** For example data generation */
	public TimeTracking(String label) {
		this.label = label;
		this.minutes = 30;
	}

	@Override
	public String toString() {
		return label + " (" + minutes + "min)";
	}
}
