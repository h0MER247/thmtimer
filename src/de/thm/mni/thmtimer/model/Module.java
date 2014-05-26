package de.thm.mni.thmtimer.model;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Module {
	private Long mId;
	private String mName;
	private Integer mCreditPoints;
	private String mDescription;
	private String mModuleNumber;
	private int mSWS;
	private String mRequirement;
	private String mTestingMethod;
	private String mFrequency;
	private List<Module> mPrerequisites;
	private String mResponsible;

	// ToDo: Mehr Informationen zu einem Modul hinzufügen!
	public Module(Long id, String name, String moduleNumber) {
		mId = id;
		mName = name;
		mModuleNumber = moduleNumber;
	
	}

	public Long getID() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public String getModuleNumber() {
		return mModuleNumber;
	}
	
	public Integer getCreditPoints() {
		return mCreditPoints;
	}

	public void setCreditPoints(int mCreditPoints) {
		this.mCreditPoints = mCreditPoints;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public int getSWS() {
		return mSWS;
	}

	public void setSWS(int mSWS) {
		this.mSWS = mSWS;
	}

	public String getRequirement() {
		return mRequirement;
	}

	public void setRequirement(String mRequirement) {
		this.mRequirement = mRequirement;
	}

	public String getTestingMethod() {
		return mTestingMethod;
	}

	public void setTestingMethod(String mTestingMethod) {
		this.mTestingMethod = mTestingMethod;
	}

	public String getFrequency() {
		return mFrequency;
	}

	public void setFrequency(String mFrequency) {
		this.mFrequency = mFrequency;
	}

	public List<Module> getPrerequisites() {
		return mPrerequisites;
	}

	public void setPrerequisites(List<Module> mPrerequisites) {
		this.mPrerequisites = mPrerequisites;

	}

	public String getResponsible() {
		return mResponsible;
	}

	public void setResponsible(String mResponsible) {
		this.mResponsible = mResponsible;
	}

	@Override
	public String toString() {
		return mName;
	}
}