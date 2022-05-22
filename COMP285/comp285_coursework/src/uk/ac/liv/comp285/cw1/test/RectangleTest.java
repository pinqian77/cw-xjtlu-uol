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
public class RectangleTest {

	// Declare a rectangle
	private Rectangle rectangle;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Initialize a rectangle, 
		// whose width is 2, height is 1 and lower left hand point is (0, 0) 
		rectangle = new Rectangle(0, 0, 2, 1);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Release rectangle by setting it to null
		rectangle = null;
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Rectangle#getArea()}.
	 */
	@Test
	public void testGetArea() {
		// 1. Test computing the rectangle's area
		assertEquals(2, rectangle.getArea(), 0);
		
		// 2. Test computing the rectangle's area after scaling
		// 2.1 Scale origin is null
		rectangle.setScale(null, 2, 2);
		// In this case, no change to the area
		assertEquals(2, rectangle.getArea(), 0);
		
		
		// 2.2 Scale factor is 1
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the area
		assertEquals(2, rectangle.getArea(), 0);
		
		
		// 2.3 Valid shrink scale 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0.5, 0.5);
		// In this case, width and height are both shrink to 1/4
		assertEquals(0.5, rectangle.getArea(), 0);
		
		
		// 2.4 Valid expand scale
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 2, 2);
		// In this case, width and height are both doubled 
		assertEquals(8, rectangle.getArea(), 0);
		
		
		// 2.5 Scale factor is 0
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0, 2);
		// In this case, width and height are both doubled 
		assertEquals(0, rectangle.getArea(), 0);
		
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 2, 0);
		// In this case, width and height are both doubled 
		assertEquals(0, rectangle.getArea(), 0);
		
		
		// 2.6 Scale origin is not itself
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(20, 10), 2, 2);
		// In this case, width and height are both doubled 
		assertEquals(8, rectangle.getArea(), 0);
		
		// 2.6 Scale factor is negative
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), -2, -2);
		// In this case, width and height are both doubled 
		assertEquals(8, rectangle.getArea(), 0);
		
		// 2.6 Scale factor is negative
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), -2, 2);
		// In this case, width and height are both doubled 
		assertEquals(8, rectangle.getArea(), 0);
		
		// 3. Test computing the rectangle's area when rotation angle is 0 and scale factors are 1
		rectangle.setRotation(new Point(0, 0), 0);
		rectangle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the area
		assertEquals(2, rectangle.getArea(), 0);
		
		// 4. Test computing the rectangle's area when rotation
		rectangle.setRotation(new Point(0, 0), Math.PI);
		// In this case, no change to the area
		assertEquals(2, rectangle.getArea(), 0);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Rectangle#getLowerLeftPoint()}.
	 */
	@Test
	public void testGetLowerLeftPoint() {
		// 1. Test getting lower left point without rotation
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0);
		
		// 2. Test getting lower left point with rotation
		// Create a point as rotation origin
		Point rotationOrigin = new Point(0, 0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			rectangle.setRotation(rotationOrigin, Math.PI/4 * i);
			
			// Declare and initialize min_X and min_Y as the position of the lower left point
			float min_X = Float.MAX_VALUE, min_Y = Float.MAX_VALUE;
			
			// Compare to find the lower left point
			for(Point v: rectangle.vertices) {
				min_X = Math.min(v.rotate(rotationOrigin, Math.PI/4 * i).getX(), min_X);
				min_Y = Math.min(v.rotate(rotationOrigin, Math.PI/4 * i).getY(), min_Y);
			}
			
			// Test the lower left point obtained by rotation
			assertEquals(min_X, rectangle.getLowerLeftPoint().getX(), 0);
			assertEquals(min_Y, rectangle.getLowerLeftPoint().getY(), 0);
		}
		
		// 3. Test getting lower left point with scaling
		// 3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 2, 2);
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		// 3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(2, 1), 2, 2);
		assertEquals(-2, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-1, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		// 3.3 Valid shrink scale 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0.5, 0.5);
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		// 3.4 Factor is 0 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0, 2);
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		rectangle.setScale(new Point(0, 0), 2, 0);
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		// 3.5 Factor is negative 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(2, 1), -2, 2);
		assertEquals(2, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(-1, rectangle.getLowerLeftPoint().getY(), 0.0001);
		
		// 4. Test when rotation angle is 0 and scale factors are 1
		rectangle.setRotation(new Point(0, 0), 0);
		rectangle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(0, rectangle.getLowerLeftPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getLowerLeftPoint().getY(), 0.0001);
	}

	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.shapes.Rectangle#getUpperRightPoint()}.
	 */
	@Test
	public void testGetUpperRightPoint() {
		// 1. Test getting upper right point without rotation
		assertEquals(2, rectangle.getUpperRightPoint().getX(), 0);
		assertEquals(1, rectangle.getUpperRightPoint().getY(), 0);
		
		// 2. Test getting upper right point with rotation
		
		// Create a point as rotation origin
		Point origin = new Point(0,0);
		for(int i = 0; i <= 8; i++) {			
			// For each loop, rotate pi/4 degree
			rectangle.setRotation(origin, Math.PI/4 * i);
			
			// Declare and initialize max_X and max_Y as the position of the upper right point
			float max_X = Float.MIN_VALUE, max_Y = Float.MIN_VALUE;
			
			// Compare to find the upper right hand point
			for(Point v: rectangle.vertices) {
				max_X = Math.max(v.rotate(origin, Math.PI/4 * i).getX(), max_X);
				max_Y = Math.max(v.rotate(origin, Math.PI/4 * i).getY(), max_Y);
			}
			
			// Test the lower left point obtained by rotation
			assertEquals(max_X, rectangle.getUpperRightPoint().getX(), 0);
			assertEquals(max_Y, rectangle.getUpperRightPoint().getY(), 0);
		}
		
		// 3. Test getting upper right point with scaling
		// 3.1 Expand with origin (0, 0)
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 2, 2);
		assertEquals(4, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(2, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		// 3.2 Expand with origin (2, 1)
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(2, 1), 2, 2);
		assertEquals(2, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(1, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		// 3.3 Valid shrink scale 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0.5, 0.5);
		assertEquals(1, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(0.5, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		// 3.4 Factor is 0 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(0, 0), 0, 2);
		assertEquals(0, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(2, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		rectangle.setScale(new Point(0, 0), 2, 0);
		assertEquals(4, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(0, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		// 3.5 Factor is negative 
		// Create a point as scale origin and set scale
		rectangle.setScale(new Point(2, 1), -2, 2);
		assertEquals(6, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(1, rectangle.getUpperRightPoint().getY(), 0.0001);
		
		
		// 4. Test when rotation angle is 0 and scale factors are 1
		rectangle.setRotation(new Point(0, 0), 0);
		rectangle.setScale(new Point(0, 0), 1, 1);
		// In this case, no change to the point
		assertEquals(2, rectangle.getUpperRightPoint().getX(), 0.0001);
		assertEquals(1, rectangle.getUpperRightPoint().getY(), 0.0001);
	}
	
	/**
	 * Test method for {@link uk.ac.liv.comp285.cw1.Shape#doesCollide(uk.ac.liv.comp285.cw1.IShape)}.
	 */
	@Test
	public void testDoesCollide() {
		// 1. Inside or overlap with the rectangle
		assertTrue(rectangle.doesCollide(new Rectangle(0, 0, 1, 1)));
		assertTrue(rectangle.doesCollide(new Circle(1, 1, 1)));
		assertTrue(rectangle.doesCollide(new RegularPolygon(4, new Point(1, 1), 1)));
		
		// 2. Outside but touch the rectangle
		assertTrue(rectangle.doesCollide(new Rectangle(0, 1, 2, 1)));
		assertTrue(rectangle.doesCollide(new Circle(0, 2, 1)));
		assertTrue(rectangle.doesCollide(new RegularPolygon(4, new Point(0, 2), 1)));
		
		// 3. Outside the rectangle
		assertFalse(rectangle.doesCollide(new Rectangle(0, 2, 2, 1)));
		assertFalse(rectangle.doesCollide(new Circle(0, 3, 1)));
		assertFalse(rectangle.doesCollide(new RegularPolygon(4, new Point(10, 10), 1)));
	}
}
