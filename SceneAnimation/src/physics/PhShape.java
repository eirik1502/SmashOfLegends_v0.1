package physics;



public abstract class PhShape {

	
	public abstract PhRectangle getBoundingBox();
	
	public abstract void setX(float x);
	public abstract void setY(float y);
	
	public abstract PhShapes getShape();
	public int getShapeNumber() {
		return getShape().number;
	}
	
}
