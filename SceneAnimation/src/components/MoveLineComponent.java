package components;

import main.Component;
import main.SceneNode;
import maths.M;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;
import physics.PhysicsComponent;

public class MoveLineComponent extends Component {

	private PhysicsComponent physicsComp;
	
	private Vec2 initVelocity;
	
	
	public MoveLineComponent(float direction, float speed) {
		initVelocity = Vec2.createLendir(speed, direction);
	}
	
	@Override
	protected void start() {
		physicsComp = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		physicsComp.addVelocity(initVelocity);
	}

	@Override
	protected void update(float deltaTime) {
//		SceneNode p = getOwner();
//		p.setX(p.getX() + deltaX);
//		p.setY(p.getY() + deltaY);
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
