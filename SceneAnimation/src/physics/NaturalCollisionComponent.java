package physics;

import main.Component;
import main.SceneNode;
import maths.Mat4;

public class NaturalCollisionComponent extends Component implements CollisionComponent {


	private PhysicsComponent physicsComponent;
	
	
	private PhShape collisionShape;
	
	
	public NaturalCollisionComponent(PhShape collisionShape) {
		this.collisionShape = collisionShape;
	}
	
	
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		super.getEngine().addNaturalCollideable(this);
		
		updatePhShapePos();
	}
	@Override
	protected void end() {
		getEngine().removeNaturalCollideable(this);
	}

	@Override
	protected void update(float deltaTime) {
		updatePhShapePos();
	}

	@Override
	public PhShape getPhShape() {
		return collisionShape;
	}
	
	private void updatePhShapePos() {
		SceneNode p = super.getOwner();
		collisionShape.setX(p.getGlobalX());
		collisionShape.setY(p.getGlobalY());
	}
	
	@Override
	public PhysicsComponent getPhysicsComponent() {
		return physicsComponent;
	}

}
