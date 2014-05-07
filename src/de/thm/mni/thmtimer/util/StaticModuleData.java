package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.model.Module;

public class StaticModuleData {

	public static List<Module> data;
	
	public static void fillData() {
		data = new ArrayList<Module>();
		data.add(new Module(0, "Objektorientierte Programmierung", 46, "Prof. Dr. Letschert", "Sommersemester 2014", "14h", false));
		data.add(new Module(2, "Lineare Algebra", 42, "Prof. Dr. Just", "Sommersemester 2014", "16h", false));
		data.add(new Module(3, "Compilerbau", 23, "Prof. Dr. Geisse", "Sommersemester 2014", "23h", true));
		data.add(new Module(1, "Programmieren interaktiver Systeme", 42, "Prof. Dr. Franzen", "Sommersemester 2013", "20h", false));
		data.add(new Module(4, "Rechnernetze und ihre Anwendung", 23, "Prof. Dr. Schmitt", "Sommersemester 2014", "23h", true));
		data.add(new Module(5, "Softwaretechnik", 42, "Prof. Dr. Renz", "Sommersemester 2014", "30h", false));
		data.add(new Module(7, "Datenbanken", 42, "Prof. Dr. Renz", "Sommersemester 2014", "16h", false));
		data.add(new Module(6, "Android-Praktikum", 23, "Prof. Dr. Süß", "Sommersemester 2014", "23h", true));
	}
	
	public static Module findModule(long id) {
		for(Module m:data) {
			if(m.getID()==id) {
				return m;
			}
		}
		return null;
	}
}
