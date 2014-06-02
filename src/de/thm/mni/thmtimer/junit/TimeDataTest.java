package de.thm.mni.thmtimer.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import de.thm.mni.thmtimer.model.TimeData;

public class TimeDataTest {

	@Test
	public void testConstructor() {
		TimeData t = new TimeData("2:01");
		assertEquals(2, (int) t.getHours());
		assertEquals(1, (int) t.getMinutes());

		t = new TimeData();
		assertEquals(0, (int) t.getHours());
		assertEquals(0, (int) t.getMinutes());
	}

	@Test
	public void testParseString() {
		TimeData t = new TimeData();
		boolean result = t.parseString("xzy");
		assertFalse(result);

		result = t.parseString("1:30");
		assertTrue(result);
	}

	@Test
	public void testSetTimeInMinutes() {
		TimeData t = new TimeData();
		t.setTimeInMinutes(950);

		assertEquals(15, (int) t.getHours());
		assertEquals(50, (int) t.getMinutes());
	}

	@Test
	public void testSetTimeInSeconds() {
		TimeData t = new TimeData();
		t.setTimeInSeconds(123456);

		// Wird auf Minuten gerundet
		assertEquals(123420, (int) t.getTimeInSeconds());
		assertEquals(34, (int) t.getHours());
		assertEquals(17, (int) t.getMinutes());
	}

	@Test
	public void testCompareTo() {
		TimeData t1 = new TimeData();
		TimeData t2 = new TimeData();

		assertEquals(0, t1.compareTo(t2));

		t1.setTimeInMinutes(60);
		t2.setTimeInMinutes(120);
		assertEquals(-60, t1.compareTo(t2));

		t1.setTimeInMinutes(90);
		t2.setTimeInMinutes(60);
		assertEquals(30, t1.compareTo(t2));
	}
}
