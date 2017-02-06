package physics;

import java.util.HashMap;

import main.Component;
import main.SceneNode;

public class CustomCollisionComponent extends Component implements CollisionComponent {

	private PhysicsComponent physicsComponent;
	
	private PhShape collisionShape;
	
	private String name;
	private HashMap<String, CustomCollisionResolvement> resolvements = new HashMap<>();
	
	
	public CustomCollisionComponent(String name, PhShape collisionShape) {
		this.name = name;
		this.collisionShape = collisionShape;
	}
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		super.getEngine().addCustomCollideable(this);
		
		updatePhShapePos();
	}
	@Override
	protected void end() {
		getEngine().removeCustomCollideable(this);
	}

	public void addResolvement(String objectName, CustomCollisionResolvement resolvement) {
		resolvements.put(objectName, resolvement);
	}
	
	public void resolve(CustomCollisionComponent other) {
		String oname = other.getName();
		if (resolvements.containsKey(oname)) {
			resolvements.get(oname).onCollision(this, other);
		}
	}
	
	public String getName() {
		return name;
	}
	
	
	@Override
	protected void update(float deltaTime) {
		updatePhShapePos();
	}
	private void updatePhShapePos() {
		SceneNode p = super.getOwner();
		collisionShape.setX(p.getGlobalX());
		collisionShape.setY(p.getGlobalY());
	}
	
	@Override
	public PhShape getPhShape() {
		return collisionShape;
	}
	
	@Override
	public PhysicsComponent getPhysicsComponent() {
		return physicsComponent;
	}

}
