package uk.ac.liv.comp285.cw1;

import uk.ac.liv.comp285.cw1.shapes.Point;

public abstract class Shape implements IShape {
	
	protected double angle;
	protected Point rotationOrigin;
	private Point scaleOrigin;
	private double scaleX;
	private double scaleY;

	public Shape() {
		if (CanvasFrame.getPanelCanvas()!=null) {
			CanvasFrame.getPanelCanvas().addShape(this);
		}
	}
	
	

	public boolean doesCollide(IShape shape) {
		Point lowerLeft1=shape.getLowerLeftPoint();
		Point upperRight1=shape.getUpperRightPoint();
		Point lowerLeft2=getLowerLeftPoint();
		Point upperRight2=getUpperRightPoint();
		
//		final float THRESHOLD = (float)0.000001;
//		if (Math.abs(upperRight1.getX()-lowerLeft2.getX())<THRESHOLD || 
//			Math.abs(lowerLeft1.getX()-upperRight2.getX())<THRESHOLD ||
//			Math.abs(upperRight1.getY()-lowerLeft2.getY())<THRESHOLD ||
//			Math.abs(lowerLeft1.getY()-upperRight2.getY())<THRESHOLD) {
//			return (true); // two shapes touch each other 
//		}
		
		
		if (upperRight1.getX()<lowerLeft2.getX()) {
			return(false);		// no over lap horizontal
		}
		if (lowerLeft1.getX()>upperRight2.getX()) {
			return(false);		// no over lap  horizontal
		}
		if (upperRight1.getY()<lowerLeft2.getY()) {
			return(false);		// no over lap vertical
		}
		if (lowerLeft1.getY()>upperRight2.getY()) {
			return(false);		// no over lap vertical
		}
		return(true);
	}
	
	@Override
	public void setRotation(Point origin, double angle) {
		this.angle=angle;
		this.rotationOrigin=origin;
		
	}
	
	@Override
	public void setScale(Point p,double scaleX,double scaleY) {
		this.scaleOrigin=p;
		this.scaleX=scaleX;
		this.scaleY=scaleY;
	}



	public Point getScaleOrigin() {
		return scaleOrigin;
	}



	public double getScaleX() {
		return scaleX;
	}



	public double getScaleY() {
		return scaleY;
	}
	
	
}
