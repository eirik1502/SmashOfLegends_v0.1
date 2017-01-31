package physics;

import main.Component;
import main.SceneNode;
import maths.Mat4;

public class CollisionComponent extends Component{

	
	private PhysicsComponent physicsComponent;
	
	
	private PhShape collisionShape;
	
	
	public CollisionComponent(PhShape collisionShape) {
		this.collisionShape = collisionShape;
	}
	
	
	@Override
	protected void start() {
		SceneNode owner = getOwner();
		physicsComponent = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		super.getEngine().addCollideable(this);
		
		collisionShape.setX(owner.getX());
		collisionShape.setY(owner.getY());
	}
	@Override
	protected void end() {
		getEngine().removeCollideable(this);
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode p = super.getOwner();
		collisionShape.setX(p.getGlobalX());
		collisionShape.setY(p.getGlobalY());
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}

	public PhShape getPhShape() {
		return collisionShape;
	}
	
	public PhysicsComponent getPhysicsComponent() {
		return physicsComponent;
	}

}
