package uk.ac.liv.comp285.cw1.shapes;

import java.awt.Graphics;

import uk.ac.liv.comp285.cw1.Shape;

public class RegularPolygon extends Shape {
	
	// Declare an array to store vertices of the regular polygon
	public Point[] vertices;
	
	public RegularPolygon(int sides, Point centre, float radius) {
		super();
		this.sides = sides;
		this.centre = centre;
		this.radius = radius;
		
		// Initialize the array with size equals to the number of sizes
		vertices = new Point[sides];
		
		for(int i = 0; i < sides; i++) {
			// Get x and y of the vertices of the polygon
			float x = (float)(centre.getX() + Math.cos(2 * Math.PI/sides * i) * radius);
			float y = (float)(centre.getY() + Math.sin(2 * Math.PI/sides * i) * radius);
			
			// Store vertices to the array
			vertices[i] = new Point(x, y);
		}
	}
	/**
	 * How many sides to the polygon
	 */
	private int sides=0;
	/**
	 * Centre of polygon, this represents the centre of the smallest circle which would contain the polygon 
	 */
	private Point centre;
	/**
	 * Size of the polygon defined as the radius of the centre of the smallest circle which would contain the polygon
	 */
	
	private float radius;
	
	@Override
	public float getArea() {
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, return original area
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			// Compute the area by dividing the polygon into triangles
			return (float)(sides * 0.5 * Math.pow(radius, 2) * Math.sin(2 * Math.PI / sides));
		}
		
		// Else return the scaled area
		
		// Circle-Like scale
		// Declare and initialize res_r as the radius of the scaled circle
		float res_r = (float)(radius * Math.abs(getScaleX()));

		// Compute the scaled area
		return (float)(sides * 0.5 * Math.pow(res_r, 2) * Math.sin(2 * Math.PI / sides));
	}
	
	@Override
	public Point getLowerLeftPoint() {
		// Declare and initialize min_X and min_Y as the position of the lower left point
		float min_X = Float.MAX_VALUE, min_Y = Float.MAX_VALUE;
		
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, process rotation
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {			
			// Compare to find the lower left hand point
			for(Point v: vertices) {
				min_X = Math.min(v.rotate(rotationOrigin, angle).getX(), min_X);
				min_Y = Math.min(v.rotate(rotationOrigin, angle).getY(), min_Y);
			}
			// Return the lower left hand point
			return new Point(min_X, min_Y);
		}
		
		// Else process scale					
		// Declare and initialize res_X and res_Y as the position of the scaled position
		float res_X, res_Y;
		
		// Iterate the vertices to get scaled points
		for(int i = 0; i < vertices.length; i++) {
			res_X = (float)((vertices[i].getX() - scaleOrigin.getX()) * getScaleX() + scaleOrigin.getX());
			res_Y = (float)((vertices[i].getY() - scaleOrigin.getY()) * getScaleY() + scaleOrigin.getY());
			
			// Compare to find the lower left hand point
			min_X = Math.min(res_X, min_X);
			min_Y = Math.min(res_Y, min_Y);
		}
		
		// Return the lower left hand point
		return new Point(min_X, min_Y);	
	}
	
	@Override
	public Point getUpperRightPoint() {
		// Declare and initialize max_X and max_Y as the position of the upper right point
		float max_X = Float.MIN_VALUE, max_Y = Float.MIN_VALUE;
		
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
				
		// If scale origin is null or scale factors are 1, process rotation
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			// Compare to find the upper right hand point
			for(Point v: vertices) {
				max_X = Math.max(v.rotate(rotationOrigin, angle).getX(), max_X);
				max_Y = Math.max(v.rotate(rotationOrigin, angle).getY(), max_Y);
			}
			// Return the upper right hand point
			return new Point(max_X, max_Y);
		}
		
		// Else process scale
		// Declare and initialize res_X and res_Y as the position of the scaled position
		float res_X, res_Y;
		
		// Iterate the vertices to get scaled points
		for(int i = 0; i < vertices.length; i++) {
			res_X = (float)((vertices[i].getX() - scaleOrigin.getX()) * getScaleX() + scaleOrigin.getX());
			res_Y = (float)((vertices[i].getY() - scaleOrigin.getY()) * getScaleY() + scaleOrigin.getY());
			
			// Compare to find the upper right hand point
			max_X = Math.max(res_X, max_X);
			max_Y = Math.max(res_Y, max_Y);
		}

		// Return the upper right hand point
		return new Point(max_X, max_Y);
	}
	
	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		for(int i = 0; i < vertices.length - 1; i++) {
			Point cur = vertices[i].rotate(rotationOrigin, angle);
			Point next = vertices[i+1].rotate(rotationOrigin, angle);
			int y_cur = (int)(g.getClipRect().height - cur.getY());
			int y_next = (int)(g.getClipRect().height - next.getY());
			g.drawLine((int)cur.getX(), (int)y_cur, (int)next.getX(), (int) y_next);
		}
		
		// Last point, connect it with the first point
		Point cur = vertices[vertices.length - 1].rotate(rotationOrigin, angle);
		Point next = vertices[0].rotate(rotationOrigin, angle);
		int y_cur = (int)(g.getClipRect().height - cur.getY());
		int y_next = (int)(g.getClipRect().height - next.getY());
		g.drawLine((int)cur.getX(), (int)y_cur, (int)next.getX(), (int) y_next);
	}
	
	

}
