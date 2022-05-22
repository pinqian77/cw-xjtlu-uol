package uk.ac.liv.comp285.cw1.test;

import uk.ac.liv.comp285.cw1.CanvasFrame;
import uk.ac.liv.comp285.cw1.shapes.Circle;
import uk.ac.liv.comp285.cw1.shapes.Point;
import uk.ac.liv.comp285.cw1.shapes.Rectangle;
import uk.ac.liv.comp285.cw1.shapes.RegularPolygon;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CanvasFrame.showShapes();
		
		
		System.out.print("Checking out shapes");
		
		Rectangle rectangle=new Rectangle(400,400,200,200);
		
		rectangle.setRotation(new Point(0,0),0.1);
		
		Circle circle=new Circle(190.1f,200,80);
		
		
		System.out.println("Collides is "+rectangle.doesCollide(circle));
		
		
		int x=-50;
		int y=50;
		
		double tan=y/x;
		
		System.out.println("Arc tan is "+Math.atan(tan)*(360/(Math.PI*2)));
		
		
		RegularPolygon polygon = new RegularPolygon(4, new Point(-2,0), 1);
//		System.out.println("X: "+ polygon.vertices[0].getX());
//		System.out.println("Y: "+ polygon.vertices[0].getY());
//		System.out.println("X: "+ polygon.vertices[1].getX());
//		System.out.println("Y: "+ polygon.vertices[1].getY());
//		System.out.println("X: "+ polygon.vertices[2].getX());
//		System.out.println("Y: "+ polygon.vertices[2].getY());
//		System.out.println("X: "+ polygon.vertices[3].getX());
//		System.out.println("Y: "+ polygon.vertices[3].getY());
		
//		System.out.println("upper right X: "+ polygon.getUpperRightPoint().getX());
//		System.out.println("upper right y: "+ polygon.getUpperRightPoint().getY());
//		System.out.println("lower left X: "+ polygon.getLowerLeftPoint().getX());
//		System.out.println("lower left y: "+ polygon.getLowerLeftPoint().getY());
		
		
//		Point origin = new Point(100, 100);
//		rectangle.setScale(origin, 2, 2);
		
//		Rectangle r=new Rectangle(-1,-1,1,1);
//		System.out.println("upper right X: "+ r.getUpperRightPoint().getX());
//		System.out.println("upper right y: "+ r.getUpperRightPoint().getY());
//		System.out.println("lower left X: "+ r.getLowerLeftPoint().getX());
//		System.out.println("lower left y: "+ r.getLowerLeftPoint().getY());
		
				
	}

}
