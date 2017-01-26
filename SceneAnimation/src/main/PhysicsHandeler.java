package main;

import java.util.ArrayList;
import java.util.List;

public class PhysicsHandeler {


	public static boolean isCollision(Collideable coll1, Collideable coll2) {
		return isCollision(coll1.getPhShape(), coll2.getPhShape());
	}
	
	public static boolean isCollision( PhShape shape1, PhShape shape2 ) {
		if (shape1 instanceof PhCircle && shape2 instanceof PhCircle) {
			return isCollision((PhCircle)shape1, (PhCircle)shape2);
		}
		else if (shape1 instanceof PhCircle && shape2 instanceof PhRectangle) {
			return isCollision((PhCircle)shape1, (PhRectangle)shape2);
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhCircle) {
			return isCollision((PhCircle)shape2, (PhRectangle)shape1);
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhRectangle) {
			return isCollision((PhRectangle)shape1, (PhRectangle)shape2);
		}
		throw new UnsupportedOperationException("Cannot check collision between given shapes");
	}
	
	public static boolean isCollision(PhCircle circ1, PhCircle circ2) {
		float r1 = circ1.getRadius();
		float x1 = circ1.getX();
		float y1 = circ1.getY();
		float r2 = circ2.getRadius();
		float x2 = circ2.getX();
		float y2 = circ2.getY();
		
		float maxDistSquared = r1 + r2;
		float distSquared = (float)( Math.pow((float)(y2-y1), 2) + Math.pow((float)(x2-x1), 2) );
		return distSquared < maxDistSquared;
	}
	public static boolean isCollision(PhCircle circ1, PhRectangle rect2) {
		//throw new UnsupportedOperationException("Circ-rect collision not supported yet");
		float r1 = circ1.getRadius();
		float cx = circ1.getX();
		float cy = circ1.getY();
		float x2 = rect2.getX();
		float y2 = rect2.getY();
		float w2 = rect2.getWidth();
		float h2 = rect2.getHeight();
		
//		float bx = cx-r1;
//		float by = cy-r1;
//		float bWidth = 2*r1;
//		float bHeigth = 2*r1;
//		if (!(isCollision(rect2, new PhRectangle(bx, by, bWidth, bHeigth)))){
//			return false;
//		}
		if (!isCollision(rect2, circ1.getBoundingBox())){
			return false;
		}
		float r2 = r1*r1;
		if (y2>cy){
			if (x2>cx){
				return (r2>(x2-cx)*(x2-cx) + (y2-cy)*(y2-cy));
			}
			else if (cx>x2+w2){
				return (r2>(x2+w2-cx)*(x2+w2-cx) + (y2-cy)*(y2-cy));
			}
		}
		else if (y2+h2<cy) {
			if (x2>cx){
				return (r2>(x2-cx)*(x2-cx) + (y2+h2-cy)*(y2+h2-cy));
			}
			else if (cx>x2+w2){
				return (r2>(x2+w2-cx)*(x2+w2-cx) + (y2+h2-cy)*(y2+h2-cy));
			}
		}
		return true;
		
	}
	public static boolean isCollision(PhRectangle rect1, PhRectangle rect2) {
		float x1 = rect1.getX();
		float y1 = rect1.getY();
		float w1 = rect1.getWidth();
		float h1 = rect1.getHeight();
		float x2 = rect2.getX();
		float y2 = rect2.getY();
		float w2 = rect2.getWidth();
		float h2 = rect2.getHeight();
		
		return ( (x2 < x1+w1 && x2+w2 > x1) && (y2 < y1+h1 && y2+h2 >y1) );
	}
}
