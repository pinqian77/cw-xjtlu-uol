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
public class RegularPolygonTest {
	
	// Declare a regular polygon with even sides
	private RegularPolygon polygon_4;
	// Declare a regular polygon with odd sides
	private RegularPolygon polygon_5;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Initialize a regular polygon which has 4 sides with center (0,0) and radius 10
		polygon_4 = new RegularPolygon(4, new Point(0,0), 10);
		
		// Initialize a regular polygon which has 5 sides with center (0,0) and radius 10
		polygon_5 = new RegularPolygon(5, new Point(0,0), 10);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Release polygon by setting it to null
		polygon_4 = null;
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.RegularPolygon#getArea()}.
	 */
	@Test
	public void testGetArea() {
		// 1. Test 4 sides
		// Test computing the polygon's area
		assertEquals(200, polygon_4.getArea(), 0);
		
		// 1.2 Test computing the circle area when rotation
		polygon_4.setRotation(new Point(0, 0), Math.PI);
		// In this case, no change to the area
		assertEquals(200, polygon_4.getArea(), 0);
		
		// 1.3 Test computing the area after scaling
		// 1.3.1 Same origin
		polygon_4.setScale(new Point(0, 0), 2, 2);
		assertEquals(800, polygon_4.getArea(), 0);
		// 1.3.2 Other origin
		polygon_4.setScale(new Point(2, 0), 2, 2);
		assertEquals(800, polygon_4.getArea(), 0);
		//1.3.3 Negative scale factors
		polygon_4.setScale(new Point(2, 0), -2, 2);
		assertEquals(800, polygon_4.getArea(), 0);
		// 1.3.4 Shrink
		polygon_4.setScale(new Point(2, 0), 0.5, 0.5);
		assertEquals(50, polygon_4.getArea(), 0);
	
		// 2. Test 5 sides
		// 2.1 Test computing the polygon's area
		float expected_area = (float)(5 * 0.5 * Math.pow(10, 2) * Math.sin(2 * Math.PI / 5));
		assertEquals(expected_area, polygon_5.getArea(), 0.0001);
		
		// 2.2 Test computing the circle area when rotation
		polygon_5.setRotation(new Point(0, 0), Math.PI);
		// In this case, no change to the area
		assertEquals(expected_area, polygon_5.getArea(), 0.0001);
		
		// 2.3 Test computing the area after scaling
		// 2.3.1 Same origin
		polygon_5.setScale(new Point(0, 0), 2, 2);
		assertEquals(951.0565, polygon_5.getArea(), 0.0001);
		// 2.3.2 Other origin
		polygon_5.setScale(new Point(2, 0), 2, 2);
		assertEquals(951.0565, polygon_5.getArea(), 0.0001);
		// 2.3.3 Negative scale factors
		polygon_5.setScale(new Point(2, 0), -2, 2);
		assertEquals(951.0565, polygon_5.getArea(), 0.0001);
		// 2.3.4 Shrink
		polygon_5.setScale(new Point(2, 0), 0.5, 0.5);
		assertEquals(59.4410, polygon_5.getArea(), 0.0001);
		
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.RegularPolygon#getLowerLeftPoint()}.
	 */
	@Test
	public void testGetLowerLeftPoint() {
		// 1. Test 4 sides
		// 1.1 Test getting lower left point without rotation
		assertEquals(-10, polygon_4.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-10, polygon_4.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 1.2 Test getting lower left point with rotation
		// Create a point as rotation origin
		Point origin = new Point(0,0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			polygon_4.setRotation(origin, Math.PI/4 * i);
			// Declare and initialize min_X and min_Y as the position of the lower left point
			float min_X = Float.MAX_VALUE, min_Y = Float.MAX_VALUE;
			// Compare to find the lower left point
			for(Point v: polygon_4.vertices) {
				min_X = Math.min(v.rotate(origin, Math.PI/4 * i).getX(), min_X);
				min_Y = Math.min(v.rotate(origin, Math.PI/4 * i).getY(), min_Y);
			}
			
			// Test the lower left point obtained by rotation
			assertEquals(min_X, polygon_4.getLowerLeftPoint().getX(), 0);
			assertEquals(min_Y, polygon_4.getLowerLeftPoint().getY(), 0);
		}
		
		
		// 1.3 Test getting lower left point with scaling
		// 1.3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		polygon_4.setScale(new Point(0, 0), 2, 2);
		assertEquals(-20, polygon_4.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-20, polygon_4.getLowerLeftPoint().getY(), 0.0001);
		
		// 1.3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		polygon_4.setScale(new Point(2, 1), 2, 2);
		assertEquals(-22, polygon_4.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-21, polygon_4.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 1.4 Test when rotation angle is 0 and scale factors are 1
		polygon_4.setRotation(new Point(0, 0), 0);
		polygon_4.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(-10, polygon_4.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-10, polygon_4.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 2. Test 5 sides
		// 2.1 Test getting lower left point without rotation
		// Declare and initialize res_X and res_Y as the position of the lower left point
		float res_X = Float.MAX_VALUE, res_Y = Float.MAX_VALUE;
		// Compare to find the lower left point
		for(Point v: polygon_5.vertices) {
			res_X = Math.min(v.getX(), res_X);
			res_Y = Math.min(v.getY(), res_Y);
		}
		// Test the lower left point
		assertEquals(res_X, polygon_5.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(res_Y, polygon_5.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 2.2 Test getting lower left point with rotation
		// Create a point as rotation origin
		origin = new Point(0,0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			polygon_5.setRotation(origin, Math.PI/4 * i);
			// Declare and initialize min_X and min_Y as the position of the lower left point
			float min_X = Float.MAX_VALUE, min_Y = Float.MAX_VALUE;
			// Compare to find the lower left point
			for(Point v: polygon_5.vertices) {
				min_X = Math.min(v.rotate(origin, Math.PI/4 * i).getX(), min_X);
				min_Y = Math.min(v.rotate(origin, Math.PI/4 * i).getY(), min_Y);
			}
			
			// Test the lower left point obtained by rotation
			assertEquals(min_X, polygon_5.getLowerLeftPoint().getX(), 0);
			assertEquals(min_Y, polygon_5.getLowerLeftPoint().getY(), 0);
		}
		
		
		// 2.3 Test getting upper right point with scaling
		// 2.3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		polygon_5.setScale(new Point(0, 0), 2, 2);
		assertEquals(-16.1803, polygon_5.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-19.0211, polygon_5.getLowerLeftPoint().getY(), 0.0001);
		
		// 2.3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		polygon_5.setScale(new Point(2, 1), 2, 2);
		assertEquals(-18.1803, polygon_5.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-20.0211, polygon_5.getLowerLeftPoint().getY(), 0.0001);
		
		
		// 2.4. Test when rotation angle is 0 and scale factors are 1
		polygon_5.setRotation(new Point(0, 0), 0);
		polygon_5.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(res_X, polygon_5.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(res_Y, polygon_5.getLowerLeftPoint().getY(), 0.0001);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.RegularPolygon#getUpperRightPoint()}.
	 */
	@Test
	public void testGetUpperRightPoint() {
		// 1. Test 4 sides
		// 1.1 Test getting upper right point without rotation
		assertEquals(10, polygon_4.getUpperRightPoint().getX(), 0.0001);
		assertEquals(10, polygon_4.getUpperRightPoint().getY(), 0.0001);
		
		// 1.2 Test getting upper right point with rotation
		// Create a point as rotation origin
		Point origin = new Point(0,0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			polygon_4.setRotation(origin, Math.PI/4 * i);
			
			// Declare and initialize max_X and max_Y as the position of the upper right point
			float max_X = Float.MIN_VALUE, max_Y = Float.MIN_VALUE;
			
			// Compare to find the upper right point
			for(Point v: polygon_4.vertices) {
				max_X = Math.max(v.rotate(origin, Math.PI/4 * i).getX(), max_X);
				max_Y = Math.max(v.rotate(origin, Math.PI/4 * i).getY(), max_Y);
			}
			
			// Test the upper right point obtained by rotation
			assertEquals(max_X, polygon_4.getUpperRightPoint().getX(), 0);
			assertEquals(max_Y, polygon_4.getUpperRightPoint().getY(), 0);
		}
		
		// 1.3 Test getting upper right point with scaling
		// 1.3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		polygon_4.setScale(new Point(0, 0), 2, 2);
		assertEquals(20, polygon_4.getUpperRightPoint().getX(), 0.0001);
		assertEquals(20, polygon_4.getUpperRightPoint().getY(), 0.0001);
		
		// 1.3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		polygon_4.setScale(new Point(2, 1), 2, 2);
		assertEquals(18, polygon_4.getUpperRightPoint().getX(), 0.0001);
		assertEquals(19, polygon_4.getUpperRightPoint().getY(), 0.0001);
		
		
		// 1.4. Test when rotation angle is 0 and scale factors are 1
		polygon_4.setRotation(new Point(0, 0), 0);
		polygon_4.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(10, polygon_4.getUpperRightPoint().getX(), 0.0001);
		assertEquals(10, polygon_4.getUpperRightPoint().getY(), 0.0001);
		
		
		// 2. Test 5 sides
		// 2.1 Test getting upper right point without rotation
		// Declare and initialize res_X and res_Y as the position of the lower left point
		float res_X = Float.MIN_VALUE, res_Y = Float.MIN_VALUE;
		// Compare to find the upper right point
		for(Point v: polygon_5.vertices) {
			res_X = Math.max(v.getX(), res_X);
			res_Y = Math.max(v.getY(), res_Y);
		}
		// Test the upper right point
		assertEquals(res_X, polygon_5.getUpperRightPoint().getX(), 0.0001);
		assertEquals(res_Y, polygon_5.getUpperRightPoint().getY(), 0.0001);
		
		
		// 2.2 Test getting upper right point with rotation
		// Create a point as rotation origin
		origin = new Point(0,0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			polygon_5.setRotation(origin, Math.PI/4 * i);
			// Declare and initialize max_X and max_Y as the position of the upper right point
			float max_X = Float.MIN_VALUE, max_Y = Float.MIN_VALUE;
			// Compare to find the upper right point
			for(Point v: polygon_5.vertices) {
				max_X = Math.max(v.rotate(origin, Math.PI/4 * i).getX(), max_X);
				max_Y = Math.max(v.rotate(origin, Math.PI/4 * i).getY(), max_Y);
			}
			
			// Test the upper right point obtained by rotation
			assertEquals(max_X, polygon_5.getUpperRightPoint().getX(), 0);
			assertEquals(max_Y, polygon_5.getUpperRightPoint().getY(), 0);
		}
		
		
		// 2.3 Test getting upper right point with scaling
		// 2.3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		polygon_5.setScale(new Point(0, 0), 2, 2);
		assertEquals(20, polygon_5.getUpperRightPoint().getX(), 0.0001);
		assertEquals(19.0211, polygon_5.getUpperRightPoint().getY(), 0.0001);
		
		// 2.3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		polygon_5.setScale(new Point(2, 1), 2, 2);
		assertEquals(18, polygon_5.getUpperRightPoint().getX(), 0.0001);
		assertEquals(18.0211, polygon_5.getUpperRightPoint().getY(), 0.0001);
		
		
		// 2.4. Test when rotation angle is 0 and scale factors are 1
		polygon_5.setRotation(new Point(0, 0), 0);
		polygon_5.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(res_X, polygon_5.getUpperRightPoint().getX(), 0.0001);
		assertEquals(res_Y, polygon_5.getUpperRightPoint().getY(), 0.0001);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.Shape#doesCollide(uk.ac.liv.comp285.cw1.IShape)}.
	 */
	@Test
	public void testDoesCollide() {
		// 1. Test 4 sides
		// 1.1 Inside or overlap with the polygon
		assertTrue(polygon_4.doesCollide(new Rectangle(0, 0, 10, 10)));
		assertTrue(polygon_4.doesCollide(new Circle(0, 0, 10)));
		assertTrue(polygon_4.doesCollide(new RegularPolygon(4, new Point(0,0), 10)));
		
		// 1.2 Outside but touch the polygon
		assertTrue(polygon_4.doesCollide(new Rectangle(0, 10, 10, 10)));
		assertTrue(polygon_4.doesCollide(new Circle(0, 20, 10)));
		assertTrue(polygon_4.doesCollide(new RegularPolygon(4, new Point(0,20), 10)));
		
		// 1.3 Outside the polygon
		assertFalse(polygon_4.doesCollide(new Rectangle(0, 11, 10, 10)));
		assertFalse(polygon_4.doesCollide(new Circle(0, 20, 9)));
		assertFalse(polygon_4.doesCollide(new RegularPolygon(4, new Point(0,21), 10)));
		
		// 2. Test 5 sides
		// 2.1 Inside or overlap with the polygon
		assertTrue(polygon_5.doesCollide(new Rectangle(0, 0, 10, 10)));
		assertTrue(polygon_5.doesCollide(new Circle(0, 0, 10)));
		assertTrue(polygon_5.doesCollide(new RegularPolygon(5, new Point(0,0), 10)));
		
		// 2.2 Outside but touch the polygon
		assertTrue(polygon_5.doesCollide(new Rectangle(0, 9, 10, 10)));
		assertTrue(polygon_5.doesCollide(new Circle(0, 10, 10)));
		assertTrue(polygon_5.doesCollide(new RegularPolygon(5, new Point(0,10), 10)));
		
		// 2.3 Outside the polygon
		assertFalse(polygon_5.doesCollide(new Rectangle(0, 10, 10, 10)));
		assertFalse(polygon_5.doesCollide(new Circle(0, 20, 9)));
		assertFalse(polygon_5.doesCollide(new RegularPolygon(5, new Point(0,21), 10)));
	}

}
