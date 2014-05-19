package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.TimeTracking;

public class StaticModuleData {

	public static List<Module> data;

	public static void fillData() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(2014, 4, 1);
		data = new ArrayList<Module>();
		Module module;
		module = new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014",
				6);
		module.addTimeTracking(new TimeTracking("Gelernt"));
		module.addTimeTracking(new TimeTracking("Film geschaut"));
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);

		module = new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", 6);
		module.addTimeTracking(new TimeTracking("Nicht gelernt"));
		module.addTimeTracking(new TimeTracking("Skript nachgelesen"));
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);

		module = new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", 6);
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
		
		module = new Module(1, "Programmieren interaktiver Systeme", 42, "Prof. Dr. Franzen", "Sommersemester 2013", 6);
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
				
		module = new Module(4, "Rechnernetze und ihre Anwendung", 23, "Prof. Dr. Schmitt", "Sommersemester 2014", 6);
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
				
		module = new Module(5, "Softwaretechnik", 42, "Prof. Dr. Renz", "Sommersemester 2014", 6);
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
		
		module = new Module(7, "Datenbanken", 42, "Prof. Dr. Renz", "Sommersemester 2014", 6);
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
		
		module = new Module(6, "Android-Praktikum", 23, "Prof. Dr. Süß", "Sommersemester 2014", 9);
		module.addTimeTracking(new TimeTracking("Gecodet"));
		module.addTimeTracking(new TimeTracking("Von der Android-API geträumt"));
		module.setStartDate(new Date(calendar.getTimeInMillis()));
		data.add(module);
	}

	public static Module findModule(long id) {
		for (Module m : data) {
			if (m.getID() == id) {
				return m;
			}
		}
		throw new IllegalArgumentException("Module " + id + " not found");
	}
}
