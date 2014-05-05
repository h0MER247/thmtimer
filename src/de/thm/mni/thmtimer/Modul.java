package de.thm.mni.thmtimer;

public class Modul {
	
	private String moduleNumber, moduleName, task;
	private double time;
	private long id;
	
	
	public Modul(String moduleNumber, String moduleName, double time, String task) {
		super();
		this.moduleNumber = moduleNumber;
		this.moduleName = moduleName;
		this.time = time;
		this.task = task;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getModuleNumber() {
		return moduleNumber;
	}
	public void setModuleNumber(String moduleNumber) {
		this.moduleNumber = moduleNumber;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String toString(){
		return (moduleNumber + " " + moduleName + " " + time + "h" + " ("  + task + ")");
	}
	
}
