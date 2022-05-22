package uk.ac.liv.comp285.cw1.shapes;

import java.awt.Graphics;

import uk.ac.liv.comp285.cw1.Shape;

public class Circle extends Shape {
	public Circle(float x, float y,float radius) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	private float x,y,radius;	// x,y are centre of the circle

	@Override
	public float getArea() {
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, return original area
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			return((float)(Math.PI * radius * radius));
		}
		// Else return the scaled area
		// Declare and initialize res_r as the radius of the scaled circle
		float res_r;

		// Get result radius
		res_r = (float)(radius * Math.abs(getScaleX()));
		
		return((float)(Math.PI * res_r * res_r));
	}

	@Override
	public Point getLowerLeftPoint() {
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, process rotation
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			// Get result center of the circle
			Point res_center = new Point(x,y).rotate(rotationOrigin, angle);
			// Return the lower left point of the result circle
			return new Point(res_center.getX() - radius, res_center.getY() - radius);
		}
		
		// Else process scale
		// Declare and initialize res_X, res_Y, and res_r as the position of the scaled center and radius
		float res_X, res_Y, res_r;
		
		res_X = (float)((x - scaleOrigin.getX()) * getScaleX() + scaleOrigin.getX());
		res_Y = (float)((y - scaleOrigin.getY()) * getScaleY() + scaleOrigin.getY());
		res_r = (float)(radius * getScaleX());
		
		return new Point(res_X - res_r, res_Y - res_r);
	}

	@Override
	public Point getUpperRightPoint() {
		// Get scale origin
		Point scaleOrigin = getScaleOrigin();
		
		// If scale origin is null or scale factors are 1, process rotation
		if (scaleOrigin == null || (getScaleX() == 1 && getScaleY() == 1)) {
			// Get result center of the circle
			Point res_center = new Point(x,y).rotate(rotationOrigin, angle);
			// Return the upper right point of the result circle
			return new Point(res_center.getX() + radius, res_center.getY() + radius);
		}
		
		// Else process scale
		// Declare and initialize res_X, res_Y, and res_r as the position of the scaled center and radius
		float res_X, res_Y, res_r;
		
		res_X = (float)((x - scaleOrigin.getX()) * getScaleX() + scaleOrigin.getX());
		res_Y = (float)((y - scaleOrigin.getY()) * getScaleY() + scaleOrigin.getY());
		res_r = (float)(radius * getScaleX());
		
		return new Point(res_X + res_r, res_Y + res_r);
	}

	@Override
	public void render(Graphics g) {
		g.drawArc((int)(x-radius),(int)(y-radius), (int)(radius*2), (int)(radius*2),0,360);
	}

	
	

}
