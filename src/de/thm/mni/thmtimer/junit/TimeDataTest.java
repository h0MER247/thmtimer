package de.thm.mni.thmtimer.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import de.thm.mni.thmtimer.model.TimeData;

public class TimeDataTest {

	@Test
	public void testConstructor() {
		TimeData t = new TimeData();
		assertEquals(0, t.getTimeInHours());
		assertEquals(0, t.getTimeInMinutes());
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

		assertEquals(15, t.getTimeInHours());
		assertEquals(950, t.getTimeInMinutes());
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
