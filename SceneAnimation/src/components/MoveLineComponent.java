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
	private float time;
	
	private float timer = 0;
	
	
	public MoveLineComponent(float direction, float speed, float time) {
		
		initVelocity = Vec2.newLenDir(speed, direction);
		this.time = time;
	}
	
	@Override 
	protected void start() {
		physicsComp = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		physicsComp.addImpulse(initVelocity);
	}

	@Override
	protected void update(float deltaTime) {
		timer += deltaTime;
		if (timer >= time) {
			getOwner().destroy();
		}
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
