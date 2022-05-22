package uk.ac.liv.comp285.cw1.shapes;

import java.awt.Graphics;

import uk.ac.liv.comp285.cw1.Shape;

public class Rectangle extends Shape {
	
	// Declare an array to store vertices of the rectangle
	public Point[] vertices;
	
	public Rectangle(float x, float y, float width, float height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		// Initialize the array with size 4
		vertices = new Point[4];
		
		// Store vertices to the array
		vertices[0] = new Point(x, y);
		vertices[1] = new Point(x + width, y);
		vertices[2] = new Point(x, y + height);
		vertices[3] = new Point(x + width, y + height);
		
	}

	private float x,y,width,height;

	@Override
	public float getArea() {
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, return original area
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			return(width * height);
		}
		
		// Else return the scaled area
		return (float)(width * height * Math.abs(getScaleX()) * Math.abs(getScaleY()));
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
		Point p1=new Point(x,y).rotate(rotationOrigin, angle);
		Point p2=new Point(x+width,y).rotate(rotationOrigin, angle);
		Point p3=new Point(x+width,y+height).rotate(rotationOrigin, angle);
		Point p4=new Point(x,y+height).rotate(rotationOrigin, angle);
		int y1=(int)(g.getClipRect().height-p1.getY());
		int y2=(int)(g.getClipRect().height-p2.getY());
		int y3=(int)(g.getClipRect().height-p3.getY());
		int y4=(int)(g.getClipRect().height-p4.getY());
		
		g.drawLine((int)p1.getX(),(int) y1,(int)p2.getX(),y2);
		g.drawLine((int)p2.getX(),y2,(int)p3.getX(),y3);
		g.drawLine((int)p3.getX(),(int) y3,(int)p4.getX(),y4);
		g.drawLine((int)p4.getX(),y4,(int)p1.getX(),y1);
		
	}
	
	

}
