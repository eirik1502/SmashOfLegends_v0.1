package components;

import main.Component;
import main.SceneNode;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;
import physics.PhysicsComponent;

public class MoveBoomerangComponent extends Component{

	private PhysicsComponent physicsComponent;
	
	private Vec2 startSpeed;
	private Vec2 accel;
	private float time;
	
	private float timer = 0;
	
	
	public MoveBoomerangComponent(float startDirection, float turnTime, float turnRadius) {
		
		float startSpeedLen = (2*turnRadius / turnTime) /PhysicsComponent.METER;
		float accelLen = - 2*turnRadius/(turnTime*turnTime) /PhysicsComponent.METER;
		
		startSpeed = Vec2.newLenDir(startSpeedLen, startDirection);
		accel = Vec2.newLenDir(accelLen, startSpeed.getDirection());
		
		time = turnTime*2 - 0.01f;
		timer = time;
	}
	
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent) getOwner().getComponent(PhysicsComponent.class);
		
		physicsComponent.addImpulse(startSpeed);
	}

	@Override
	protected void update(float deltaTime) {
		timer -= deltaTime;
		if (timer <= 0) {
			getOwner().destroy();
		}
		
		physicsComponent.addAcceleration(accel);
	}

}
