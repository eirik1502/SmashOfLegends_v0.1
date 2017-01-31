package physics;

import java.util.ArrayList;
import java.util.List;

import main.SceneNode;
import maths.M;
import maths.Vec2;

public class PhysicsEngine {

	
    private ArrayList<CollisionComponent> collideables = new ArrayList<>();
    
    
    /**
     * Object has to have a PhysicsComponent
     * @param c
     */
	public void addCollideable(CollisionComponent c) {
		collideables.add(c);
		System.out.println("Number of collideable objects: " + collideables.size());
	}
	public void removeCollideable(CollisionComponent c) {
		collideables.remove(c);
	}
	
	public void resolve() {
		simpleResolve();
	}
	
	/**
	 * resolve collision objects by moving them appart
	 */
	private void simpleResolve() {
		for (int i = 0; i < collideables.size(); i++) {
			for (int j = i; j < collideables.size(); j++) {
				CollisionComponent c1 = collideables.get(i);
				CollisionComponent c2 = collideables.get(j);
				
				if (c1 == c2) continue; //do not resolve collisions between the same object
				
				CollisionData data = isCollision(c1, c2);
				
				if (data.isCollision) {
					resolveCollision(c1, c2, data); //Should not apply impulse before all collisions are resolved.
				}
			}
		}
	}
	
//	public SceneNode collide(Class<?> type) {
//		for (Collideable c : collideables) {
//			if (type.isInstance(c)) {
//				return (SceneNode) c;
//			}
//		}
//		return null;
//	}
	
	public static void resolveCollision(CollisionComponent c1, CollisionComponent c2, CollisionData data) {
		PhysicsComponent p1 = c1.getPhysicsComponent();
		PhysicsComponent p2 = c2.getPhysicsComponent();
		float invM1 = p1.getInvMass();
		float invM2 = p2.getInvMass();
		
		Vec2 relVelocity = p2.getVelocity().subtract(p1.getVelocity());
		float velAlongNormal = relVelocity.dotProduct(data.normal);
		
		if (velAlongNormal > 0) { //do not resolve collision if objects are moving apart
			return;
		}
		
		float restitution = M.min(p1.getRestitution(), p2.getRestitution());
		
		float impulseLength = -(1 + restitution) * velAlongNormal;
		impulseLength /= invM1 + invM2;
		
		// Apply impulse
		Vec2 impulse = data.normal.scale(impulseLength);
		p1.addVelocity( impulse.scale( invM1 ).negative() );
		p2.addVelocity( impulse.scale( invM2 ) );
		
		positionalCorrection(p1, p2, data);
	}
	
	/**
	 * Corrects for floating point error to eliminate sinking of objects
	 * @param p1
	 * @param p2
	 * @param data
	 */
	private static void positionalCorrection(PhysicsComponent p1, PhysicsComponent p2, CollisionData data)
	{
		SceneNode o1 = p1.getOwner();
		SceneNode o2 = p2.getOwner();
		
		float percent = 0.2f; // usually 20% to 80%
		float slop = 0.01f; // usually 0.01 to 0.1
		Vec2 correction = data.normal.scale(percent * (M.max(data.penetration - slop, 0.0f) / (p1.getInvMass() + p2.getInvMass()) ) );
		o1.addPosXY( correction.scale(p1.getInvMass()).negative() );
		o2.addPosXY( correction.scale(p2.getInvMass()) );
	}
	

	public static CollisionData isCollision(CollisionComponent c1, CollisionComponent c2) {
		return isCollision(c1.getPhShape(), c2.getPhShape());
	}
	
	public static CollisionData isCollision(PhShape shape1, PhShape shape2) {
		if (shape1 instanceof PhCircle && shape2 instanceof PhCircle) {
			return isCollision((PhCircle)shape1, (PhCircle)shape2);
		}
		else if (shape1 instanceof PhCircle && shape2 instanceof PhRectangle) {
			CollisionData d = isCollision((PhRectangle)shape2, (PhCircle)shape1);
			return new CollisionData(d.isCollision, d.normal.negative(), d.penetration); //flip normal here
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhCircle) {
			return isCollision((PhRectangle)shape1, (PhCircle)shape2);
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhRectangle) {
			return isCollision((PhRectangle)shape1, (PhRectangle)shape2);
		}
		throw new UnsupportedOperationException("Cannot check collision between given shapes");
	}
	
	public static CollisionData isCollision(PhCircle circ1, PhCircle circ2) {
		Vec2 pos1 = circ1.getPos();
		Vec2 pos2 = circ2.getPos();
		float r1 = circ1.getRadius();
		float r2 = circ2.getRadius();
		
		float maxDist = r1 + r2;
		float maxDistSquared = M.pow2(maxDist);
		
		Vec2 distVec = pos2.subtract(pos1);
		
		if (distVec.getLengthSquared() >= maxDistSquared) return new CollisionData(false);
		
		float dist = distVec.getLength();
		
		//set default values if circles on same pos
		float penetration = r1;
		Vec2 normal = new Vec2(1, 0);
		
		if (dist != 0) {
			penetration = maxDist - dist;
			normal = distVec.scale(1/dist); //optimized normalize
		}
		
		return new CollisionData(true, normal, penetration);
	}
	public static CollisionData isCollision(PhRectangle rect1, PhRectangle rect2) {
		Vec2 distVec = rect2.getPos().subtract(rect1.getPos());
		
		// Calculate half extents along x axis for each object
		float rect1HExtentX = rect1.getWidth() / 2.0f;
		float rect2HExtentX = rect2.getWidth() / 2.0f;
		
		// Calculate overlap on x axis
		float xOverlap = rect1HExtentX + rect2HExtentX - M.abs( distVec.x );
		
		Vec2 normal = new Vec2();
		float penetration = 0;
		boolean isCollision = false;
		// SAT test on x axis
		if(xOverlap > 0)
		{
			// Calculate half extents along y axis for each object
			float rect1HExtentY = rect1.getHeight() / 2.0f;
			float rect2HExtentY = rect2.getHeight() / 2.0f;
			
			// Calculate overlap on y axis
			float yOverlap = rect1HExtentY + rect2HExtentY - M.abs( distVec.y );
			
			// SAT test on y axis
			if(yOverlap > 0)
			{
				isCollision = true;
				
				// Find out which axis is axis of least penetration
				if(xOverlap < yOverlap) {
					// Point towards B knowing that n points from A to B
					if(distVec.x < 0)
						normal = new Vec2( -1, 0 );
					else
						normal = new Vec2( 1, 0 );
					
					penetration = xOverlap;
				}
				else {
					// Point toward B knowing that n points from A to B
					if(distVec.y < 0)
						normal = new Vec2( 0, -1 );
					else
						normal = new Vec2( 0, 1 );
					
					penetration = yOverlap;
				}
			}
		}
		
		return new CollisionData(isCollision, normal, penetration);
	}
	public static CollisionData isCollision(PhRectangle rect, PhCircle circ) {
		// Vector from A to B
		Vec2 distVec = circ.getPos().subtract(rect.getPos());
		
		// Calculate half extents along each axis
		float rectHalfExtentX = rect.getWidth() / 2;
		float rectHalfExtentY = rect.getHeight() / 2;
		
		// Closest point on rect to center of circ
		Vec2 closestRectPoint = new Vec2(	M.clamp(distVec.x, -rectHalfExtentX, rectHalfExtentX),
											M.clamp(distVec.y, -rectHalfExtentY, rectHalfExtentY) );
		
		boolean circInsideRect = false;
		
		// Circle is inside the AABB, so we need to clamp the circle's center
		// to the closest edge
		if(distVec.equals(closestRectPoint)) {
			//if the clamping above did not make closestRectPoint different from distVec, circ center inside rect
			circInsideRect = true;
		
			// Find closest axis
			//if(M.abs( distVec.x ) > M.abs( distVec.y ) ) {
			if(M.abs( distVec.x/rect.getWidth() ) > M.abs( distVec.y/rect.getHeight() ) ) {
				//not entirely right. If rectangles width != height it might clamp to wrong axis.
				//  ---> fixed by dividing by width and height of rectangle
				// Clamp to rect edge in x direction
				if(closestRectPoint.x > 0)
					closestRectPoint.x = rectHalfExtentX;
				else
					closestRectPoint.x = -rectHalfExtentX;
			}
			else { //clamp to rect edge in y direction
				if(closestRectPoint.y > 0)
					closestRectPoint.y = rectHalfExtentY;
				else
					closestRectPoint.y = -rectHalfExtentY;
			}
		}
		
		Vec2 rectCircVec = distVec.subtract( closestRectPoint );
		float rectCircDistSquared = rectCircVec.getLengthSquared();
		float circRadius = circ.getRadius();
		
		// Can now determine if there is a collision
		if(rectCircDistSquared > M.pow2(circRadius) && !circInsideRect) {
			return new CollisionData(false, new Vec2(), 0);
		}
		
		// Avoided sqrt if no collision is found
		float rectCircDist = M.sqrt(rectCircDistSquared);
		
		Vec2 normal = rectCircVec.scale(1/rectCircDist);
		float penetration = circRadius - rectCircDist;
		
		// Flip normal if circ inside rect
		if(circInsideRect) {
			normal = normal.negative();
			//penetration = rectCircDist; <--- this seems right, but works better without
		}
		
		return new CollisionData(true, normal, penetration);
	}
	
	public static boolean isCollisionSimple(PhCircle circ1, PhCircle circ2) {
		float x1 = circ1.getX();
		float y1 = circ1.getY();
		float x2 = circ2.getX();
		float y2 = circ2.getY();
		
		Vec2 pos1 = circ1.getPos();
		Vec2 pos2 = circ2.getPos();
		float r1 = circ1.getRadius();
		float r2 = circ2.getRadius();
		
		float maxDist = r1 + r2;
		float maxDistSquared = M.pow2(maxDist);
		
		Vec2 distVec = pos2.subtract(pos1);
		
		return distVec.getLengthSquared() < maxDistSquared;
	}
	public static boolean isCollisionSimple(PhCircle circ1, PhRectangle rect2) {
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
		if (!isCollisionSimple(rect2, circ1.getBoundingBox())){
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
	public static boolean isCollisionSimple(PhRectangle rect1, PhRectangle rect2) {
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
