package main;

import java.util.ArrayList;
import java.util.LinkedList;

import maths.Mat4;
import maths.Vec3;

public class SceneNode {

	private SceneNode parent;
	private boolean inSystem = false;
	private Engine engine;
	private ArrayList<SceneNode> children = new ArrayList<>();
	private ArrayList<Component> components = new ArrayList<>();
	private LinkedList<SceneNode> waitListAddChild = new LinkedList<>();
	private LinkedList<SceneNode> waitListRemoveChild = new LinkedList<>();
	
	private boolean updateLock = false;
	
	private Mat4 transform;
	
	private float x, y, z;
	private float scaleX, scaleY, scaleZ;
	private float rotationX, rotationY, rotationZ;

	
	
	public void start(SceneNode parent, Engine engine) {
		this.parent = parent;
		this.engine = engine;
		this.inSystem = true;
		transform = Mat4.identity();
		
		//start all components
		components.forEach(c -> c.superStart(this));
	}
	
	public void update(float deltaTime) {
		updateLock = true;
		
		
		components.forEach(c -> c.superUpdate(deltaTime));
		
		//update transform matrix
		transform = Mat4.translate( new Vec3(x, y, z) ).multiply( Mat4.rotate(rotationZ) ); //*scale
		
		children.forEach(c -> c.update(deltaTime));
		
		
		updateLock = false;
		applyWaitChildLists();
	}
	
	public void render(Mat4 parentTransform) {
		//apply parent transform before this nodes transform
		Mat4 nodeTransform = parentTransform.multiply(this.transform);
		
		components.forEach(c -> c.superRender(nodeTransform));
		
		children.forEach(c -> c.render( nodeTransform ));
	}
	
	
	public void destroy() {
		for (SceneNode n : getChildren()) {
			n.destroy();
			removeChild(n);
		}
		for (Component c : getComponents()) {
			removeComponent(c);
		}
		getParent().removeChild(this);
	}
	
	public void addChild(SceneNode child) {
		if (updateLock) {
			waitListAddChild.push(child);
			return;
		}
		children.add(child);
		child.start(this, engine);
	}
	public void removeChild(SceneNode child) {
		if (updateLock) {
			waitListRemoveChild.push(child);
			return;
		}
		children.remove(child);
	}
	
	public void addComponent(Component c) {
		components.add(c);
		
		if (inSystem) //if node has not been added to system, start when added
			c.superStart(this);
	}
	public void removeComponent(Component c) {
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
		if (updateLock) return;
		
		while( !waitListAddChild.isEmpty() ) {
			addChild( waitListAddChild.poll() );
		}
		while( !waitListRemoveChild.isEmpty() ) {
			removeChild( waitListRemoveChild.poll() );
		}
	}
}
