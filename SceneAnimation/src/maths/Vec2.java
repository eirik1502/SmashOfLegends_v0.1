package maths;

public class Vec2 {

	public float x, y;

	public Vec2() {
		x = 0.0f;
		y = 0.0f;
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLendir(float length, float direction) {
		x = (float)Math.cos(direction)*length;
		y = (float)Math.sin(direction)*length;
	}
	

	public Vec2 add(Vec2 vec) {
		return new Vec2(x + vec.x, y + vec.y);
	}
	public Vec2 subtract(Vec2 vec) {
		return new Vec2(x - vec.x, y - vec.y);
	}
	public float dotProduct(Vec2 vec) {
		return x*vec.x + y*vec.y;
	}
	
	public Vec2 scale(float s) {
		return new Vec2(x*s, y*s);
	}
	
	public Vec2 negative() {
		return new Vec2(-x, -y);
	}
	
	public Vec2 normalize() {
		float len = getLength();
		return new Vec2(x/len, y/len);
	}
	
	public float getLength() {
		return (float)Math.sqrt(x*x + y*y);
	}
	public float getDirection() {
		return (float)Math.atan2(y, x);
	}
	
	public boolean isNull() {
		return x == 0 && y == 0;
	}
	
	
	public String toString() {
		return "[Vec2; x="+x+" y="+y+"]";
	}
}
