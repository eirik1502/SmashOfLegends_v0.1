package main;

import maths.Mat4;

public abstract class Component {

	private SceneNode owner;
	private boolean isActive;
	
	
	public void superStart(SceneNode parent) {
		this.owner = parent;
		isActive = true;
		start();
	}
	
	public void superEnd() {
		end();
	}
	public void superUpdate(float deltaTime) {
		update(deltaTime);
	}
	public void superRender(Mat4 transform) {
		render(transform);
	}
	
	public void deactivate() {
		isActive = false;
	}
	public void activate() {
		isActive = true;
	}
	public boolean isActive() {
		return isActive;
	}
	
	public SceneNode getOwner() {
		return owner;
	}
	public Engine getEngine() {
		return getOwner().getEngine();
	}
	public SceneNode getRoot() {
		return getEngine().getRoot();
	}
	
	
	
	public void rootAddChild(SceneNode child) {
		getRoot().addChild(child);
	}
	
	protected abstract void start();
	protected void end() {}; //just because I dont want to add it to all components right now
	protected abstract void update(float deltaTime);
	protected abstract void render(Mat4 transform);
}
