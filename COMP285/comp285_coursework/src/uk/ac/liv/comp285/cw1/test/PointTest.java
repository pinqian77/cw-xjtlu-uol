/**
 * 
 */
package uk.ac.liv.comp285.cw1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.liv.comp285.cw1.shapes.Point;

/**
 * @author Symmetric_QIAN
 *
 */
public class PointTest {
	// Declare points
	private Point point1 = null;
	private Point point2 = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Initialize point1 at (0, 0)
		point1 = new Point(0, 0);
		
		// Initialize point2 at (-1, 1)
		point2 = new Point(-1, 1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Release point1 by setting it to null
		point1 = null;
		
		// Release point2 by setting it to null
		point2 = null;
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Point#getX()}.
	 */
	@Test
	public void testGetX() {
		// Test getting X value of the point
		assertEquals(0, point1.getX(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Point#setX(float)}.
	 */
	@Test
	public void testSetX() {
		// Test setting X value of itself
		point1.setX(0);
		assertEquals(0, point1.getX(), 0);
		
		// Test setting X value of the point
		point1.setX(1);
		assertEquals(1, point1.getX(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Point#getY()}.
	 */
	@Test
	public void testGetY() {
		// Test getting Y value of the point
		assertEquals(0, point1.getY(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Point#setY(float)}.
	 */
	@Test
	public void testSetY() {
		// Test setting Y value of itself
		point1.setY(0);
		assertEquals(0, point1.getY(), 0);
		
		// Test setting Y value of the point
		point1.setY(1);
		assertEquals(1, point1.getY(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Point#rotate(uk.ac.liv.comp285.cw1.shapes.Point, double)}.
	 */
	@Test
	public void testRotate() {
		// Declare a point as origin
		Point origin = new Point(0, 0);
		
		// Declare a result point for testing
		Point res = null;
		
		// Test when origin is null
		res = point2.rotate(null, Math.PI);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when origin is itself
		res = point2.rotate(point2, Math.PI);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when angle is 0
		res = point2.rotate(origin, 0);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when angle is pi
		res = point2.rotate(origin, Math.PI);
		assertEquals(1, res.getX(), 0);
		assertEquals(-1, res.getY(), 0);
		
		// Test when angle is 2 * pi
		res = point2.rotate(origin, 2 * Math.PI);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when angle is -2 * pi
		res = point2.rotate(origin, -2 * Math.PI);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when angle is 4 * pi
		res = point2.rotate(origin, 4 * Math.PI);
		assertEquals(point2.getX(), res.getX(), 0);
		assertEquals(point2.getY(), res.getY(), 0);
		
		// Test when angle is  pi/4
		res = point2.rotate(origin, Math.PI / 4);
		assertEquals(-Math.sqrt(2), res.getX(), 0.0001);
		assertEquals(0, res.getY(), 0.0001);
		
		// Test when angle is -pi/4
		res = point2.rotate(origin, -Math.PI / 4);
		assertEquals(0, res.getX(), 0.0001);
		assertEquals(Math.sqrt(2), res.getY(), 0.0001);
		
	}

}
