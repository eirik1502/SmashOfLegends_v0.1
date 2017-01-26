package main;

import maths.Mat4;

public abstract class Component {

	private SceneNode owner;
	
	
	public void superStart(SceneNode parent) {
		this.owner = parent;
		start();
	}
	
	public void superUpdate(float deltaTime) {
		update(deltaTime);
		
	}
	
	public void superRender(Mat4 transform) {
		render(transform);
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
	protected abstract void update(float deltaTime);
	protected abstract void render(Mat4 transform);
}
