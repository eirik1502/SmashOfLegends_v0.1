package main;

public class PhCircle extends PhShape{

	private float x, y, radius;
	
	public PhCircle( float cx, float cy, float radius) {
		this.x = cx;
		this.y = cy;
		this.radius = radius;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public PhRectangle getBoundingBox() {
		return new PhRectangle(x-radius, y-radius, 2*radius, 2*radius);
	}
}
