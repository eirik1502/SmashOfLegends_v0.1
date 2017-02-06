package main;

import java.util.ArrayList;
import java.util.LinkedList;

import maths.Mat4;
import maths.Vec2;
import maths.Vec3;

public class SceneNode {

	private SceneNode parent;
	private boolean inSystem = false;
	private Engine engine;
	
	private ArrayList<SceneNode> children = new ArrayList<>();
	private LinkedList<SceneNode> waitListAddChild = new LinkedList<>();
	private LinkedList<SceneNode> waitListRemoveChild = new LinkedList<>();
	
	private ArrayList<Component> components = new ArrayList<>();
	private LinkedList<Component> waitListAddComponent = new LinkedList<>();
	private LinkedList<Component> waitListRemoveComponent = new LinkedList<>();
	
	//private boolean updateLock = false;
	
	private Mat4 transform;
	
	private float x, y, z;
	private float scaleX = 1, scaleY = 1, scaleZ = 1;
	private float rotationX, rotationY, rotationZ;

	
	
	public void start(SceneNode parent, Engine engine) {
		this.parent = parent;
		this.engine = engine;
		this.inSystem = true;
		transform = Mat4.identity();
		
		//start all components
		components.forEach(c -> c.superStart(this));
		
		//start all children
		children.forEach(c -> c.start(this, engine) );
	}
	
	public void update(float deltaTime) {
//		updateLock = true;
		
		for (Component c : components) {
			if (c.isActive())
				c.superUpdate(deltaTime);
		}
		
		Mat4 translate = Mat4.translate( new Vec3(x, y, z) );
		Mat4 rotate = Mat4.rotate(rotationZ);
		Mat4 scale = Mat4.scale(new Vec3(scaleX, scaleY, scaleZ));
		transform = translate.multiply( rotate.multiply( scale ) );
		
		for (SceneNode child : children) {
			child.update(deltaTime);
		}
		
		getEngine().incrementUpdatedObjects();
	}
	
	public void afterUpdate() {
		for (SceneNode child : children) {
			child.afterUpdate();
		}
		applyWaitChildLists();
	}
	
	public void render(Mat4 parentTransform) {
		//apply parent transform before this nodes transform
		Mat4 nodeTransform = parentTransform.multiply(this.transform);
		
		for (Component c : components) {
			if (c.isActive())
				c.superRender(nodeTransform);
		}
		
		for (SceneNode child : children) {
			child.render( nodeTransform );
		}
	}
	
	public void destroy() {
		for (SceneNode n : getChildren()) {
			n.destroy();
		}
		for (Component c : getComponents()) {
			removeComponent(c);
		}
		getParent().removeChild(this);
	}
	
	public void addChild(SceneNode child) {
		if (inSystem) {
			if (inSystem && getEngine().isUpdateLock()) {
				waitListAddChild.push(child);
				return;
			}
			child.start(this, engine);
		}
		children.add(child);
	}
	public void removeChild(SceneNode child) {
		if (getEngine().isUpdateLock()) {
			waitListRemoveChild.push(child);
			return;
		}
		children.remove(child);
	}
	
	public void addComponent(Component c) {
		if (inSystem && getEngine().isUpdateLock()) {
			waitListAddComponent.push(c);
			return;
		}
		components.add(c);
		
		if (inSystem) //if node has not been added to system, start when added
			c.superStart(this);
	}
	public void removeComponent(Component c) {
		if (getEngine().isUpdateLock()) {
			waitListRemoveComponent.push(c);
			return;
		}
		if (inSystem) //if node has not been added to system, it has never been started
			c.superEnd();
		components.remove(c);
	}
	public Component getComponent(Class<? extends Component> type) {
		for (Component c : getComponents()) {
			if (type.isInstance(c)) {
				return c;
			}
		}
		
		throw new IllegalStateException("Could not retrieve the desired component");
	}
	
	
	public SceneNode getParent() {
		return parent;
	}
	public Engine getEngine() {
		return engine;
	}
	
	
	public ArrayList<SceneNode> getChildren() {
		return children;
	}
	public ArrayList<Component> getComponents() {
		return components;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void addX(float dx) {
		setX(getX() + dx);
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void addY(float dy) {
		setY(getY() + dy);
	}
	
	public void setPosXY(Vec2 pos) {
		setX(pos.x);
		setY(pos.y);
	}
	
	public void addPosXY(Vec2 pos) {
		addX(pos.x);
		addY(pos.y);
	}
	public Vec2 getPosXY() {
		return new Vec2(getX(), getY());
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public void addZ(float dz) {
		setZ(getZ() + dz);
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}
	
	public float getGlobalRotationZ() {
		if (getParent() == null)
			return getRotationZ();
		
		return getParent().getGlobalRotationZ() + getRotationZ();
	}
	
	public float getGlobalX() {
		if (getParent() == null)
			return getX();
		
		return getParent().getGlobalX() + getX();
	}
	
	public float getGlobalY() {
		if (getParent() == null)
			return getY();
		
		return getParent().getGlobalY() + getY();
	}
	
	
	
	//private shit
	private void applyWaitChildLists() {
		if (getEngine().isUpdateLock()) return;
		
		//add/remove children
		while( !waitListAddChild.isEmpty() ) {
			addChild( waitListAddChild.poll() );
		}
		while( !waitListRemoveChild.isEmpty() ) {
			removeChild( waitListRemoveChild.poll() );
		}
		
		//add/remove components
		while( !waitListAddComponent.isEmpty() ) {
			addComponent( waitListAddComponent.poll() );
		}
		while( !waitListRemoveComponent.isEmpty() ) {
			removeComponent( waitListRemoveComponent.poll() );
		}
	}
}
