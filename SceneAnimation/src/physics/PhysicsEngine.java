package physics;

import java.util.ArrayList;
import java.util.List;

import main.SceneNode;
import maths.M;
import maths.Vec2;

public class PhysicsEngine {

	
    private ArrayList<NaturalCollisionComponent> collideables = new ArrayList<>();
    private ArrayList<CustomCollisionComponent> customCollideables = new ArrayList<>();
    
    
    /**
     * Object has to have a PhysicsComponent
     * @param c
     */
	public void addCollideable(NaturalCollisionComponent c) {
		collideables.add(c);
		//System.out.println("Number of collideable objects: " + collideables.size());
	}
	public void removeCollideable(NaturalCollisionComponent c) {
		collideables.remove(c);
	}
	
	public void addCustomCollideable(CustomCollisionComponent c) {
		customCollideables.add(c);
		//System.out.println("Number of collideable objects: " + collideables.size());
	}
	public void removeCustomCollideable(CustomCollisionComponent c) {
		customCollideables.remove(c);
	}
	
	/**
	 * resolve collision objects by moving them appart
	 */
	public void resolveAll() {
		resolveAllNatural();
		resolveAllCustom();
	}
	
	private void resolveAllCustom() {
		for (int i = 0; i < customCollideables.size(); i++) {
			for (int j = i; j < customCollideables.size(); j++) {
				CustomCollisionComponent c1 = customCollideables.get(i);
				CustomCollisionComponent c2 = customCollideables.get(j);
				
				if (c1 == c2) continue; //do not resolve collisions between the same object
				
				CollisionData data = new CollisionData(c1, c2);
				if ( isCollision(data) ) {
					c1.resolve(c2);
					c2.resolve(c1);
				}
			}
		}
	}
	
	private void resolveAllNatural() {
		for (int i = 0; i < collideables.size(); i++) {
			for (int j = i; j < collideables.size(); j++) {
				NaturalCollisionComponent c1 = collideables.get(i);
				NaturalCollisionComponent c2 = collideables.get(j);
				
				if (c1 == c2) continue; //do not resolve collisions between the same object
				
				CollisionData data = new CollisionData(c1, c2);
				if ( isCollision(data) ) {
					resolveNaturalCollision(data); //Should not apply impulse before all collisions are resolved.
				}
			}
		}
	}
	
	/**
	 * data input must contain normal and penetration data
	 * @param data
	 */
	private void resolveNaturalCollision(CollisionData data) {
		PhysicsComponent p1 = data.c1.getPhysicsComponent();
		PhysicsComponent p2 = data.c2.getPhysicsComponent();
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
//		p1.addVelocity( impulse.scale( invM1 ).negative() );
//		p2.addVelocity( impulse.scale( invM2 ) );
		p1.addImpulse( impulse.scale( invM1 ).negative() );
		p2.addImpulse( impulse.scale( invM2 ) );
		
		positionalCorrection(data);
	}

	/**
	 * Corrects for floating point error to eliminate sinking of objects
	 * @param p1
	 * @param p2
	 * @param data
	 */
	private void positionalCorrection(CollisionData data)
	{
		PhysicsComponent p1 = data.c1.getPhysicsComponent();
		PhysicsComponent p2 = data.c2.getPhysicsComponent();
		
		SceneNode o1 = p1.getOwner();
		SceneNode o2 = p2.getOwner();
		
		float percent = 0.2f; // usually 20% to 80%
		float slop = 0.01f; // usually 0.01 to 0.1
		
		Vec2 correction = data.normal.scale(percent * (M.max(data.penetration - slop, 0.0f) / (p1.getInvMass() + p2.getInvMass()) ) );
		o1.addPosXY( correction.scale(p1.getInvMass()).negative() );
		o2.addPosXY( correction.scale(p2.getInvMass()) );
//		p1.addTemporaryVelocity( correction.scale(p1.getInvMass()).negative() );
//		p2.addTemporaryVelocity( correction.scale(p2.getInvMass()) );
	}

	
	private boolean isCollision(CollisionData data) {
		
		PhShape shape1 = data.c1.getPhShape();
		PhShape shape2 = data.c2.getPhShape();
		
		if (shape1 instanceof PhCircle && shape2 instanceof PhCircle) {
			return isCollisionCircCirc(data);
		}
		else if (shape1 instanceof PhCircle && shape2 instanceof PhRectangle) {
			return isCollisionCircRect(data);
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhCircle) {
			return isCollisionRectCirc(data);
		}
		else if (shape1 instanceof PhRectangle && shape2 instanceof PhRectangle) {
			return isCollisionRectRect(data);
		}
		throw new UnsupportedOperationException("Cannot check collision between given shapes");
	}
	
	private boolean isCollisionCircCirc(CollisionData data) {
		PhCircle circ1 = (PhCircle)data.c1.getPhShape();
		PhCircle circ2 = (PhCircle)data.c2.getPhShape();
		
		Vec2 pos1 = circ1.getPos();
		Vec2 pos2 = circ2.getPos();
		float r1 = circ1.getRadius();
		float r2 = circ2.getRadius();
		
		float maxDist = r1 + r2;
		float maxDistSquared = M.pow2(maxDist);
		
		Vec2 distVec = pos2.subtract(pos1);
		
		if (distVec.getLengthSquared() >= maxDistSquared) {
			return false;
		}
		
		float dist = distVec.getLength();
		
		if (dist != 0) {
			data.penetration = maxDist - dist;
			data.normal = distVec.scale(1/dist); //optimized normalize
			return true;
		}
		
		//set default values if circles on same pos
		data.penetration = r1;
		data.normal = new Vec2(1, 0);
		return true;
	}
	private boolean isCollisionRectRect(CollisionData data) {
		PhRectangle rect1 = (PhRectangle)data.c1.getPhShape();
		PhRectangle rect2 = (PhRectangle)data.c2.getPhShape();
		
		Vec2 distVec = rect2.getPos().subtract(rect1.getPos());
		
		// Calculate half extents along x axis for each object
		float rect1HExtentX = rect1.getWidth() / 2.0f;
		float rect2HExtentX = rect2.getWidth() / 2.0f;
		
		// Calculate overlap on x axis
		float xOverlap = rect1HExtentX + rect2HExtentX - M.abs( distVec.x );
		
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
				// Find out which axis is axis of least penetration
				if(xOverlap < yOverlap) {
					// Point towards B knowing that n points from A to B
					if(distVec.x < 0)
						data.normal = new Vec2( -1, 0 );
					else
						data.normal =  new Vec2( 1, 0 );
					
					data.penetration = xOverlap;
					return true;
				}
				else {
					// Point toward B knowing that n points from A to B
					if(distVec.y < 0)
						data.normal = new Vec2( 0, -1 );
					else
						data.normal = new Vec2( 0, 1 );
					
					data.penetration = yOverlap;
					return true;
				}
			}
		}
		
		return false;
	}
	private boolean isCollisionCircRect(CollisionData data) {
		CollisionComponent oc1 = data.c1;
		data.c1 = data.c2;
		data.c2 = oc1;
		return isCollisionRectCirc(data);
	}
	private boolean isCollisionRectCirc(CollisionData data) {
		PhRectangle rect = (PhRectangle)data.c1.getPhShape();
		PhCircle circ = (PhCircle)data.c2.getPhShape();
		
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
			return false;
		}
		
		// Avoided sqrt if no collision is found
		float rectCircDist = M.sqrt(rectCircDistSquared);
		
		
		if (rectCircDist == 0) {
			return false;
		}
		
		Vec2 normal = rectCircVec.scale(1/rectCircDist);
		float penetration = circRadius - rectCircDist;
		
		// Flip normal if circ inside rect
		if(circInsideRect) {
			normal = normal.negative();
			//penetration = rectCircDist; <--- this seems right, but works better without
		}
	
		data.normal = normal;
		data.penetration = penetration;
		return true;
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
