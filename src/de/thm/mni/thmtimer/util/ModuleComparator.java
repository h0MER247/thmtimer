package de.thm.mni.thmtimer.util;

import java.util.Comparator;

import de.thm.mni.thmtimer.model.Module;

public class ModuleComparator implements Comparator<Module> {

	@Override
	public int compare(Module lhs, Module rhs) {
		return lhs.getName().compareTo(rhs.getName());
	}

}
