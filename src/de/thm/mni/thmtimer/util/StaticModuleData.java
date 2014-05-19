package de.thm.mni.thmtimer.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.thm.mni.thmtimer.model.Course;
import de.thm.mni.thmtimer.model.Module;
import de.thm.mni.thmtimer.model.StudentData;
import de.thm.mni.thmtimer.model.TeacherData;
import de.thm.mni.thmtimer.model.TimeData;
import de.thm.mni.thmtimer.model.TimeCategory;
import de.thm.mni.thmtimer.model.TimeStatisticData;
import de.thm.mni.thmtimer.model.TimeTracking;



public class StaticModuleData {

	private static ArrayList<Module> m_modules;
	private static ArrayList<Course> m_courses;
	private static ArrayList<TimeCategory> m_timeCategorys;

	private static StudentData m_studentData;
	private static TeacherData m_teacherData;



	public static void fillData() {

		createModules();
		createCourseList();
		createTimeCategorys();


		createStudentData();
		createTeacherData();
		
		for(Course c:m_courses) {
			findModule(c.getModuleID()).addCourse(c);
		}
	}



	// Modul Metainformationen
	private static void createModules() {

		if(m_modules == null) {

			m_modules = new ArrayList<Module>();

			m_modules.add(new Module(0l,                                 // ModulID (so wie sie in der Serverdatenbank stehen w�rde)
					"Objektorientierte Programmierung", // Modulbezeichnung
					"MN1007",							 // Modulnummer
					6,									 // Anzahl Creditpoints
					360,								 // Zu investierende Zeit in dieses Modul in Stunden
					"Lorem Ipsum.."));					 // Modulbeschreibung (laut Modulhandbuch)

			m_modules.add(new Module(1l, "Lineare Algebra", "MN1007", 6, 200, "Blubb"));
			m_modules.add(new Module(2l, "Compilerbau", "MN1007", 6, 200, "Abcdefg"));
			m_modules.add(new Module(3l, "Programmieren interaktiver Systeme", "MN1007", 6, 400, "TestBlubb"));
			m_modules.add(new Module(4l, "Rechnernetze und ihre Anwendung", "MN1007", 6, 300, "BlaBlubb"));
			m_modules.add(new Module(5l, "Datenbanken", "MN1007", 6, 150, "Lalelu"));
			m_modules.add(new Module(6l, "Betriebssysteme", "MN1007", 6, 180, "Hier ne tolle Beschreibung"));
			m_modules.add(new Module(7l, "Softwaretechnik Praktikum", "MN1007", 6, 190, "Ganz tolle Beschreibung hier"));
		}
	}

	// Liste aller Kurse die in diesem Semester stattfinden
	private static void createCourseList() {

		if(m_courses == null) {

			m_courses = new ArrayList<Course>();

			// OOP
			m_courses.add(new Course(0l,									// KursID (so wie sie in der Serverdatenbank stehen w�rde)
					0l,									// ModulID (so wie sie in der Serverdatenbank stehen w�rde) -> "Verbindet" den Kurs mit dem dazugeh�rigen Modul
					"Objektorientierte Programmierung",	// Kursbezeichnung (Der Server sollte uns die Modulbezeichnung geben, wenn nur ein Kurs vorhanden ist)
					"Prof. Dr. Letschert", 				// Dozent dieses Kurses
					131));									// Eingeschriebene Studenten

			// Lineare Algebra
			m_courses.add(new Course(1l, 1l, "Lineare Algebra", "Prof. Dr. Just", 85));

			// Compilerbau
			m_courses.add(new Course(2l, 2l, "Compilerbau", "Prof. Dr. Geisse", 32));

			// Programmieren interaktiver Systeme 
			m_courses.add(new Course(3l, 3l, "Programmieren interaktiver Systeme - Kurs A", "Prof. Dr. Franzen", 60));
			m_courses.add(new Course(4l, 3l, "Programmieren interaktiver Systeme - Kurs B", "Prof. Dr. Lauwerth", 60));

			// Rechnernetze und ihre Anwendung
			m_courses.add(new Course(5l, 4l, "Rechnernetze und ihre Anwendung", "Prof. Dr. M�ller", 61));

			// Datenbanken
			m_courses.add(new Course(6l, 5l, "Datenbanken", "Prof. Dr. Renz", 53));

			// Betriebssysteme
			m_courses.add(new Course(7l, 6l, "Betriebssysteme", "Prof. Dr. Geisse", 44));

			// Softwaretechnik Praktikum
			m_courses.add(new Course(9l, 7l, "Softwaretechnik Praktikum - THMTimer Projekt", "Herr Volkmer", 20));
			m_courses.add(new Course(10l, 7l, "Softwaretechnik Praktikum - Patientenverwaltung", "Herr Mustermann", 24));
			m_courses.add(new Course(11l, 7l, "Softwaretechnik Praktikum - Rezeptdatenbank", "Frau Musterfrau", 16));
		}
	}

	// Kategorien f�r Zeiterfassungen
	private static void createTimeCategorys() {

		if(m_timeCategorys == null) {

			m_timeCategorys = new ArrayList<TimeCategory>();

			m_timeCategorys.add(new TimeCategory(0l,			// KategorieID (so wie sie in der Serverdatenbank stehen w�rde)
					"Vorlesung"));	// Bezeichnung f�r diese Kategorie

			m_timeCategorys.add(new TimeCategory(1l, "Praktika"));
			m_timeCategorys.add(new TimeCategory(2l, "Hausarbeit"));
			m_timeCategorys.add(new TimeCategory(3l, "Vorbereitung"));
			m_timeCategorys.add(new TimeCategory(4l, "Sonstiges"));
		}
	}



	private static void createStudentData() {

		if(m_studentData == null) {

			m_studentData = new StudentData();

			// Eingeschrieben in:
			m_studentData.addCourse(0l); // OOP
			m_studentData.addCourse(2l); // Compilerbau
			m_studentData.addCourse(5l); // Rechnernetze
			m_studentData.addCourse(6l); // Datenbanken
			m_studentData.addCourse(9l); // SWT-P (Kurs Volkmer)

			// Timetrackingdaten f�r:
			// OOP
			m_studentData.addTimeTracking(0l,												// KursID (so wie sie in der Datenbank stehen w�rde)
					new TimeTracking(0l,								// TimeTrackingID (so wie sie in der Datenbank stehen w�rde) -> Um Zeiten evtl. zu bearbeiten, l�schen, etc...
							0l,								// CategoryID (so wie sie in der Datenbank stehen w�rde) -> Ordnet diesem Tracking eine Kategorie zu (Hausaufgaben, Praktika, Vorlesung, etc...)
							"War heute in der Vorlesung",	// Eigener Text f�r dieses Tracking
							new TimeData("1:30")));			// Zeitangabe (hier 1 Stunde 30 Minuten)

			m_studentData.addTimeTracking(0l, new TimeTracking(1l, 0l, "War in der Vorlesung", new TimeData("1:31")));
			m_studentData.addTimeTracking(0l, new TimeTracking(2l, 1l, "War im Praktikum... War ganz gut", new TimeData("0:46")));
			m_studentData.addTimeTracking(0l, new TimeTracking(3l, 2l, "Haus�bung 1 fertig", new TimeData("2:12")));
			m_studentData.addTimeTracking(0l, new TimeTracking(4l, 4l, "Getr�umt von Android", new TimeData("7:42")));

			// Compilerbau
			m_studentData.addTimeTracking(2l, new TimeTracking(5l, 0l, "War in der Vorlesung, Compilerbau ist top...", new TimeData("1:31")));
			m_studentData.addTimeTracking(2l, new TimeTracking(6l, 1l, "War im Praktikum... Es l�uft...", new TimeData("1:36")));
			m_studentData.addTimeTracking(2l, new TimeTracking(7l, 2l, "Haus�bung 1 angefangen", new TimeData("1:12")));
			m_studentData.addTimeTracking(2l, new TimeTracking(8l, 2l, "Haus�bung 1 fertiggestellt", new TimeData("2:51")));

			// Rechnernetze
			m_studentData.addTimeTracking(5l, new TimeTracking(9l, 0l, "Vorlesung gut, Prof. Dr. M�ller ist super", new TimeData("1:33")));
			m_studentData.addTimeTracking(5l, new TimeTracking(10l, 1l, "War im Praktikum... Es l�uft...", new TimeData("1:36")));
			m_studentData.addTimeTracking(5l, new TimeTracking(11l, 2l, "Haus�bung 1 angefangen", new TimeData("1:12")));
			m_studentData.addTimeTracking(5l, new TimeTracking(12l, 2l, "Haus�bung 1 fertiggestellt", new TimeData("2:51")));
			m_studentData.addTimeTracking(5l, new TimeTracking(13l, 4l, "In der Mensa gegessen", new TimeData("0:12")));

			// Datenbanken
			m_studentData.addTimeTracking(6l, new TimeTracking(14l, 0l, "Vorlesung gehabt", new TimeData("1:30")));
			m_studentData.addTimeTracking(6l, new TimeTracking(15l, 2l, "Haus�bung 1 ist endlich fertig", new TimeData("4:22")));

			// SWT-P (Kurs Volkmer)
			m_studentData.addTimeTracking(9l, new TimeTracking(16l, 1l, "Praktika Stunde gehabt", new TimeData("3:00")));
			m_studentData.addTimeTracking(9l, new TimeTracking(17l, 4l, "Besprechung", new TimeData("1:22")));
		}
	}

	private static void createTeacherData() {

		if(m_teacherData == null) {

			m_teacherData = new TeacherData();

			// Als Dozent halten wir:
			m_teacherData.addCourse(2l); // Compilerbau
			m_teacherData.addCourse(7l); // Betriebssysteme

			// Zeitstatistikdaten f�r
			// Compilerbau
			m_teacherData.addTimeStatistic(2l,												// KursID (so wie sie in der Datenbank stehen w�rde)
					new TimeStatisticData(0l,						// KategorieID (so wie sie in der Datenbank stehen w�rde)
							new TimeData("14:13")));	// Gesamtzeit aller Studenten innerhalb dieser Kategorie

			m_teacherData.addTimeStatistic(2l, new TimeStatisticData(1l, new TimeData("3:12")));
			m_teacherData.addTimeStatistic(2l, new TimeStatisticData(2l, new TimeData("1:12")));
			m_teacherData.addTimeStatistic(2l, new TimeStatisticData(3l, new TimeData("2:12")));
			m_teacherData.addTimeStatistic(2l, new TimeStatisticData(4l, new TimeData("4:12")));

			// Betriebssysteme
			m_teacherData.addTimeStatistic(7l, new TimeStatisticData(0l, new TimeData("12:12")));
			m_teacherData.addTimeStatistic(7l, new TimeStatisticData(1l, new TimeData("4:42")));
			m_teacherData.addTimeStatistic(7l, new TimeStatisticData(2l, new TimeData("5:32")));
			m_teacherData.addTimeStatistic(7l, new TimeStatisticData(3l, new TimeData("7:22")));
			m_teacherData.addTimeStatistic(7l, new TimeStatisticData(4l, new TimeData("8:12")));
		}
	}



	public static StudentData getStudentData() {

		return m_studentData;
	}

	public static TeacherData getTeacherData() {

		return m_teacherData;
	}

	public static ArrayList<TimeCategory> getTimeCategorys() {

		return m_timeCategorys;
	}

	public static ArrayList<Course> getCourseList() {

		return m_courses;
	}

	public static ArrayList<Module> getModuleList() {

		return m_modules;
	}



	public static Module findModule(Long moduleID) {

		for(Module x : m_modules) {

			if(x.getID() == moduleID) {

				return x;
			}
		}

		return null;
	}

	public static Course findCourse(Long courseID) {

		for(Course x : m_courses) {

			if(x.getID() == courseID) {

				return x;
			}
		}

		return null;
	}

	public static TimeCategory findTimeCategory(Long categoryID) {

		for(TimeCategory x : m_timeCategorys) {

			if(x.getID() == categoryID) {

				return x;
			}
		}

		return null;
	}
}
