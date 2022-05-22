/**
 * 
 */
package uk.ac.liv.comp285.cw1.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.liv.comp285.cw1.shapes.Circle;
import uk.ac.liv.comp285.cw1.shapes.Point;
import uk.ac.liv.comp285.cw1.shapes.Rectangle;
import uk.ac.liv.comp285.cw1.shapes.RegularPolygon;

/**
 * @author Symmetric_QIAN
 *
 */
public class CircleTest {

	// Declare a circle
	private Circle circle;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Initialize a circle whose center is (1, 1) and radius is 1
		circle = new Circle(1, 1, 1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Release the circle by setting it to null
		circle = null;
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Circle#getArea()}.
	 */
	@Test
	public void testGetArea() {		
		// 1. Test computing the circle's area
		assertEquals((float) Math.PI * 1 * 1, circle.getArea(), 0);
		
		// 2. Test computing the circle's area after scaling
		circle.setScale(new Point(1, 1), 2, 2);
		assertEquals((float) Math.PI * 2 * 2, circle.getArea(), 0);
		
		// 3. Test computing the circle's area after scaling with different origin
		circle.setScale(new Point(0, 0), 2, 2);
		assertEquals((float) Math.PI * 2 * 2, circle.getArea(), 0);
		
		// 4. Test computing the circle's area after scaling with factor is 0
		circle.setScale(new Point(0, 0), 0, 2);
		assertEquals(0, circle.getArea(), 0.0001);
		
		// 5. Test computing the rectangle's area when rotation angle is 0 and scale factors are 1
		circle.setRotation(new Point(0, 0), 0);
		circle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the area
		assertEquals(Math.PI * 1 * 1, circle.getArea(), 0.0001);
		
		// 6. Test computing the circle area when rotation
		circle.setScale(null, 0, 0);
		circle.setRotation(new Point(0, 0), Math.PI);
		// In this case, no change to the area
		assertEquals((float) Math.PI * 1 * 1, circle.getArea(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Circle#getLowerLeftPoint()}.
	 */
	@Test
	public void testGetLowerLeftPoint() {
		// 1. Test getting lower left point without rotation
		assertEquals(0, circle.getLowerLeftPoint().getX(), 0);
		assertEquals(0, circle.getLowerLeftPoint().getY(), 0);
		
		
		// 2. Test getting lower left point with rotation when the origin is the center
		// Create a point as rotation origin
		Point origin = new Point(1, 1);
		
		// Rotate when the rotation origin is its center
		circle.setRotation(origin, Math.PI);
		
		// In this case, the lower left hand point should be equal to the bounding rectangle's lower left hand point
		assertEquals(0, circle.getLowerLeftPoint().getX(), 0);
		assertEquals(0, circle.getLowerLeftPoint().getY(), 0);
		
		
		// 3. Test getting lower left point with rotation when the origin is not the center
		// Declare and initialize the original center of the circle
		Point center = new Point(1, 1);
		
		// Set a point as rotation origin
	    origin = new Point(0, 0);
	    
	    // For each loop, rotate pi/4 degree
		for(int i = 0; i <= 8; i++) {
			circle.setRotation(origin, Math.PI/4 * i);
			
			// Get result center of the circle
			Point res_center = center.rotate(origin, Math.PI/4 * i);
			
			// In this case, the result should be equal to the bounding rectangle's lower left hand point
			assertEquals(res_center.getX() - 1, circle.getLowerLeftPoint().getX(), 0);
			assertEquals(res_center.getY() - 1, circle.getLowerLeftPoint().getY(), 0);
		}
		
		
		// 4. Test getting lower left point with scaling
		// 4.1 Expand with origin (1, 1)
		// Create a point as scale origin and set scale
		circle.setScale(new Point(1, 1), 2, 2);
		assertEquals(-1, circle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-1, circle.getLowerLeftPoint().getY(), 0.0001);
		
		// 4.2 Expand with origin (2, 2)
		// Create a point as scale origin and set scale
		circle.setScale(new Point(2, 2), 2, 2);
		assertEquals(-2, circle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-2, circle.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 5. Test when rotation angle is 0 and scale factors are 1
		circle.setRotation(new Point(0, 0), 0);
		circle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(0, circle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, circle.getLowerLeftPoint().getY(), 0.0001);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Circle#getUpperRightPoint()}.
	 */
	@Test
	public void testGetUpperRightPoint() {
		// 1. Test getting upper right point without rotation
		assertEquals(2, circle.getUpperRightPoint().getX(), 0);
		assertEquals(2, circle.getUpperRightPoint().getY(), 0);
		
		
		// 2. Test getting upper right point with rotation when the origin is the center
		// Create a point as rotation origin
		Point origin = new Point(1, 1);
		// Rotate when the rotation origin is its center
		circle.setRotation(origin, Math.PI);
		// In this case, the lower left hand point should be equal to the bounding rectangle's upper right hand point
		assertEquals(2, circle.getUpperRightPoint().getX(), 0);
		assertEquals(2, circle.getUpperRightPoint().getY(), 0);
		
		
		// 3. Test getting upper right point with rotation when the origin is not the center
		// Declare and initialize the original center of the circle
		Point center = new Point(1, 1);
		// Set a point as rotation origin
	    origin = new Point(0, 0);
	    // For each loop, rotate pi/4 degree
		for(int i = 0; i <= 8; i++) {
			circle.setRotation(origin, Math.PI/4 * i);
			
			// Get result center of the circle
			Point res_center = center.rotate(origin, Math.PI/4 * i);
			
			// In this case, the result should be equal to the bounding rectangle's upper right hand point
			assertEquals(res_center.getX() + 1, circle.getUpperRightPoint().getX(), 0);
			assertEquals(res_center.getY() + 1, circle.getUpperRightPoint().getY(), 0);
		}
		
		
		// 4. Test getting upper right point with scaling
		// 4.1 Expand with origin (1, 1)
		// Create a point as scale origin and set scale
		circle.setScale(new Point(1, 1), 2, 2);
		assertEquals(3, circle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(3, circle.getUpperRightPoint().getY(), 0.0001);
		
		// 4.2 Expand with origin (2, 2)
		// Create a point as scale origin and set scale
		circle.setScale(new Point(2, 2), 2, 2);
		assertEquals(2, circle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(2, circle.getUpperRightPoint().getY(), 0.0001);
		
		
		// 5. Test when rotation angle is 0 and scale factors are 1
		circle.setRotation(new Point(0, 0), 0);
		circle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(2, circle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(2, circle.getUpperRightPoint().getY(), 0.0001);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.Shape#doesCollide(uk.ac.liv.comp285.cw1.IShape)}.
	 */
	@Test
	public void testDoesCollide() {
		// 1. Inside or overlap with the circle
		assertTrue(circle.doesCollide(new Rectangle(0, 0, 1, 1)));
		assertTrue(circle.doesCollide(new Circle(1, 1, 1)));
		assertTrue(circle.doesCollide(new RegularPolygon(4, new Point(0, 0), 1)));
		
		// 2. Outside but touch the circle
		assertTrue(circle.doesCollide(new Rectangle(0, 2, 1, 1)));
		assertTrue(circle.doesCollide(new Circle(1, 3, 1)));
		assertTrue(circle.doesCollide(new RegularPolygon(4, new Point(-1, 1), 1)));
		
		// 3. Outside the circle
		assertFalse(circle.doesCollide(new Rectangle(0, 3, 2, 1)));
		assertFalse(circle.doesCollide(new Circle(0, 4, 1)));
		assertFalse(circle.doesCollide(new RegularPolygon(4, new Point(10, 10), 1)));
	}

}
