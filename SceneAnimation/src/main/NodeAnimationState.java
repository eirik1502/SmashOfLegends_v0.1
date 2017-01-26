package main;

public class NodeAnimationState {

	private float x, y;
	private float rotationZ;
	
	public NodeAnimationState(float x, float y, float rotationZ) {
		setData(x, y, rotationZ);
	}
	
	public NodeAnimationState(float[] stateData) {
		if (stateData.length != 3) throw new IllegalStateException("NodeAnimationState not given data of length 3");
		setData(stateData[0], stateData[1], stateData[2]);
	}
	
	private void setData(float x, float y, float rotationZ) {
		this.x = x;
		this.y = y;
		this.rotationZ = rotationZ;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getRotationZ() {
		return rotationZ;
	}
	
	
	
	public String toString() {
		return "[NodeAnimationState; x="+x+", y="+y+", rotZ="+rotationZ+"]";
	}
}
