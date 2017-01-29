package physics;

import maths.Vec2;

public class PhCircle extends PhShape{

	private Vec2 pos;
	private float radius;
	
	
	public PhCircle(Vec2 pos, float radius) {
		this.pos = pos;
		this.radius = radius;
	}
	public PhCircle( float cx, float cy, float radius) {
		this(new Vec2(cx, cy), radius);
	}
	public PhCircle(float radius) {
		this(new Vec2(), radius);
	}

	public Vec2 getPos() {
		return pos;
	}
	
	public float getX() {
		return pos.x;
	}

	public void setX(float x) {
		this.pos.x = x;
	}

	public float getY() {
		return pos.y;
	}

	public void setY(float y) {
		this.pos.y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public PhRectangle getBoundingBox() {
		return new PhRectangle(pos.x-radius, pos.y-radius, 2*radius, 2*radius);
	}
}
