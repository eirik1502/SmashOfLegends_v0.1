package physics;

import maths.Vec2;

public class CollisionData {

	public final Vec2 normal;
	public final float penetration;
	
	public final boolean isCollision;
	
	
	public CollisionData(boolean isCollision, Vec2 normal, float penetration) {
		this.isCollision = isCollision;
		this.normal = normal;
		this.penetration = penetration;
	}
	public CollisionData(boolean isCollision) {
		this(isCollision, null, 0);
	}
}
