package physics;

import maths.Vec2;

public class CollisionData {

	//public enum CollisionType {CIRC_CIRC, RECT_RECT, RECT_CIRC, CIRC_RECT};
	
	public CollisionComponent c1, c2;
	
	public Vec2 normal;
	public float penetration;
	
	
	public CollisionData(CollisionComponent c1, CollisionComponent c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
//	public void setNormal(Vec2 normal) {
//		this.normal = normal;
//	}
//	public void setPenetration(float penetration) {
//		this.penetration = penetration;
//	}
	
	
	@Override
	public String toString() {
		return "[CollisionData; normal="+normal+" penetration="+penetration+"]";
	}
}
