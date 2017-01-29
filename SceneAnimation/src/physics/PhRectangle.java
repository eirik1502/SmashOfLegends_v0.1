package physics;

import maths.Vec2;

public class PhRectangle extends PhShape{

	private Vec2 pos, size;
	
	
	public PhRectangle( Vec2 pos, Vec2 size) {
		this.pos = pos;
		this.size = size;
	}
	public PhRectangle( float x, float y, float width, float height ) {
		this( new Vec2(x, y), new Vec2(width, height) );
	}

	
	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 npos) {
		pos = npos;
	}
	public void setPos(float x, float y) {
		setX(x);
		setY(y);
	}
	
	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = size;
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
	
	public float getWidth() {
		return size.x;
	}
	public void setWidth(float width) {
		this.size.x = width;
	}
	
	public float getHeight() {
		return size.y;
	}
	public void setHeight(float height) {
		this.size.y = height;
	}

	public PhRectangle getBoundingBox() {
		return this;
	}
	
}
