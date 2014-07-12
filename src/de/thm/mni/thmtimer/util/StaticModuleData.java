package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.List;

import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.StudentData;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeStatisticData;
import de.thm.mni.thmtimer.model.TimeTracking;

public class StaticModuleData {

	private static List<Module> mModules;
	private static List<TimeCategory> mTimeCategorys;

	private static StudentData mStudentData;
	private static TeacherData mTeacherData;

	public static void fillData() {
		createModules();
		createTimeCategorys();

		createStudentData();
		createTeacherData();
	}

	// Modul Metainformationen
	private static void createModules() {
		if (mModules == null) {
			ArrayList<Module> prereq = new ArrayList<Module>();
			mModules = new ArrayList<Module>();
			Module m = new Module(0l, "Objektorientierte Programmierung", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(1l, "Compilerbau", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(2l, "Lineare Algebra", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(3l, "Programmieren interaktiver Systeme", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(4l, "Rechnernetze und ihre Anwendung", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(5l, "Datenbanken", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(6l, "Betriebssysteme", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
			prereq.add(m);
			
			m = new Module(7l, "Softwaretechnik Praktikum", "MN1007");
			m.setFrequency("Jedes Semester");
			m.setPrerequisites(prereq);
			m.setRequirement("3 Hausübungen");
			m.setResponsible("Prof. Dr. Franzen");
			m.setSWS(6);
			m.setTestingMethod("Klausur");
			m.setCreditPoints(6);
			m.setDescription("Dieses Modul führt in die Programmierung interaktiver" +
					" Desktop-Anwendungen ein, bei denen auf eine Datenbank zugegriffen" +
					" wird und die entsprechend softwareergonomischer Standards gestaltet" +
					" werden.");
			mModules.add(m);
		}
	}

	// Kategorien für Zeiterfassungen
	private static void createTimeCategorys() {

		if (mTimeCategorys == null) {

			mTimeCategorys = new ArrayList<TimeCategory>();

			mTimeCategorys.add(new TimeCategory(0l, "Vorlesung"));
			mTimeCategorys.add(new TimeCategory(1l, "Praktikum"));
			mTimeCategorys.add(new TimeCategory(2l, "Hausarbeit"));
			mTimeCategorys.add(new TimeCategory(3l, "Vorbereitung"));
			mTimeCategorys.add(new TimeCategory(4l, "Sonstiges"));
		}
	}

	private static void createStudentData() {

		if (mStudentData == null) {

			mStudentData = new StudentData();

			// Eingeschrieben in:
			mStudentData.addCourse(0l); // OOP
			mStudentData.addCourse(2l); // Compilerbau
			mStudentData.addCourse(5l); // Rechnernetze
			mStudentData.addCourse(6l); // Datenbanken
			mStudentData.addCourse(9l); // SWT-P (Kurs Volkmer)

			// Timetrackingdaten f�r:
			// OOP
			mStudentData.addTimeTracking(0l, // KursID (so wie sie in der
												// Datenbank stehen w�rde)
					new TimeTracking(0l, // TimeTrackingID (so wie sie in der
											// Datenbank stehen w�rde) -> Um
											// Zeiten evtl. zu bearbeiten,
											// l�schen, etc...
							0l, // CategoryID (so wie sie in der Datenbank
								// stehen w�rde) -> Ordnet diesem Tracking eine
								// Kategorie zu (Hausaufgaben, Praktika,
								// Vorlesung, etc...)
							"War heute in der Vorlesung", // Eigener Text f�r
															// dieses Tracking
							parseTimeData("1:30"))); // Zeitangabe (hier 1 Stunde
													// 30 Minuten)

			mStudentData.addTimeTracking(0l, new TimeTracking(1l, 0l, "War in der Vorlesung", parseTimeData("1:31")));
			mStudentData.addTimeTracking(0l, new TimeTracking(2l, 1l, "War im Praktikum... War ganz gut", parseTimeData(
					"0:46")));
			mStudentData.addTimeTracking(0l, new TimeTracking(3l, 2l, "Hausübung 1 fertig", parseTimeData("2:12")));
			mStudentData.addTimeTracking(0l, new TimeTracking(4l, 4l, "Geträumt von Android", parseTimeData("7:42")));
			mStudentData.addTimeTracking(0l, new TimeTracking(13l, 4l, "Semester verpennt", parseTimeData("100:00")));
			
			// Compilerbau
			mStudentData.addTimeTracking(2l, new TimeTracking(5l, 0l, "War in der Vorlesung, Compilerbau ist top...",
					parseTimeData("1:31")));
			mStudentData.addTimeTracking(2l, new TimeTracking(6l, 1l, "War im Praktikum... Es l�uft...", parseTimeData(
					"1:36")));
			mStudentData.addTimeTracking(2l, new TimeTracking(7l, 2l, "Hausübung 1 angefangen", parseTimeData("1:12")));
			mStudentData.addTimeTracking(2l, new TimeTracking(8l, 2l, "Hausübung 1 fertiggestellt",
					parseTimeData("2:51")));
			mStudentData.addTimeTracking(2l, new TimeTracking(13l, 4l, "Semester verpennt", parseTimeData("87:00")));

			// Rechnernetze
			mStudentData.addTimeTracking(5l, new TimeTracking(9l, 0l, "Vorlesung gut, Prof. Dr. Müller ist super",
					parseTimeData("1:33")));
			mStudentData.addTimeTracking(5l, new TimeTracking(10l, 1l, "War im Praktikum... Es läuft...", parseTimeData(
					"1:36")));
			mStudentData.addTimeTracking(5l, new TimeTracking(11l, 2l, "Hausübung 1 angefangen", parseTimeData("1:12")));
			mStudentData.addTimeTracking(5l, new TimeTracking(12l, 2l, "Hausübung 1 fertiggestellt", parseTimeData(
					"2:51")));
			mStudentData.addTimeTracking(5l, new TimeTracking(13l, 4l, "In der Mensa gegessen", parseTimeData("0:12")));
			mStudentData.addTimeTracking(5l, new TimeTracking(14l, 4l, "Semester verpennt", parseTimeData("80:00")));

			
			// Datenbanken
			mStudentData.addTimeTracking(6l, new TimeTracking(14l, 0l, "Vorlesung gehabt", parseTimeData("1:30")));
			mStudentData.addTimeTracking(6l, new TimeTracking(15l, 2l, "Hausübung 1 ist endlich fertig", parseTimeData(
					"4:22")));
			mStudentData.addTimeTracking(6l, new TimeTracking(13l, 4l, "Semester verpennt", parseTimeData("80:00")));

			// SWT-P (Kurs Volkmer)
			mStudentData.addTimeTracking(9l, new TimeTracking(16l, 1l, "Praktika Stunde gehabt", parseTimeData("3:00")));
			mStudentData.addTimeTracking(9l, new TimeTracking(17l, 4l, "Besprechung", parseTimeData("1:22")));
			mStudentData.addTimeTracking(9l, new TimeTracking(13l, 4l, "Semester verpennt", parseTimeData("91:00")));
		}
	}
	
	private static TimeData parseTimeData(String time){
		TimeData t = new TimeData();
		t.parseString(time);
		return t;
	}

	private static void createTeacherData() {
		if (mTeacherData == null) {
			mTeacherData = new TeacherData();

			// Als Dozent halten wir:
			mTeacherData.addCourse(2l); // Compilerbau
			mTeacherData.addCourse(7l); // Betriebssysteme

			// Zeitstatistikdaten f�r
			// Compilerbau
			mTeacherData.addTimeStatistic(2l, // KursID (so wie sie in der
												// Datenbank stehen w�rde)
					new TimeStatisticData(0l, // KategorieID (so wie sie in der
												// Datenbank stehen w�rde)
							parseTimeData("14:13"))); // Gesamtzeit aller
														// Studenten innerhalb
														// dieser Kategorie

			mTeacherData.addTimeStatistic(2l, new TimeStatisticData(1l, parseTimeData("3:12")));
			mTeacherData.addTimeStatistic(2l, new TimeStatisticData(2l, parseTimeData("1:12")));
			mTeacherData.addTimeStatistic(2l, new TimeStatisticData(3l, parseTimeData("2:12")));
			mTeacherData.addTimeStatistic(2l, new TimeStatisticData(4l, parseTimeData("4:12")));

			// Betriebssysteme
			mTeacherData.addTimeStatistic(7l, new TimeStatisticData(0l, parseTimeData("12:12")));
			mTeacherData.addTimeStatistic(7l, new TimeStatisticData(1l, parseTimeData("4:42")));
			mTeacherData.addTimeStatistic(7l, new TimeStatisticData(2l, parseTimeData("5:32")));
			mTeacherData.addTimeStatistic(7l, new TimeStatisticData(3l, parseTimeData("7:22")));
			mTeacherData.addTimeStatistic(7l, new TimeStatisticData(4l, parseTimeData("8:12")));
		}
	}

	public static StudentData getStudentData() {
		return mStudentData;
	}

	public static TeacherData getTeacherData() {
		return mTeacherData;
	}

	public static List<TimeCategory> getTimeCategorys() {
		return mTimeCategorys;
	}


	public static List<Module> getModuleList() {
		return mModules;
	}

	public static Module findModule(Long moduleID) {
		for (Module x : mModules) {
			if (x.getID() == moduleID) {
				return x;
			}
		}
		return null;
	}

	public static TimeCategory findTimeCategory(Long categoryID) {
		for (TimeCategory x : mTimeCategorys) {
			if (x.getID() == categoryID) {
				return x;
			}
		}
		return null;
	}
}
