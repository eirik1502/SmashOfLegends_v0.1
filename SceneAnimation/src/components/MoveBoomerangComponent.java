package components;

import main.Component;
import main.SceneNode;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;
import physics.PhysicsComponent;

public class MoveBoomerangComponent extends Component{

	private PhysicsComponent physicsComponent;
	
	private Vec2 speed;
	private Vec2 accel;
	
	private SceneNode owner;
	
	
	public MoveBoomerangComponent(float startDirection, float turnTime, float turnRadius) {
		
		float startSpeed = 2*turnRadius / turnTime;
		float accelLen = - 2*turnRadius/(turnTime*turnTime);
		
		speed = new Vec2();
		speed.setLendir(startSpeed, startDirection);
		
		accel = new Vec2();
		accel.setLendir(accelLen, speed.getDirection());
		//System.out.println("Acceleration: "+ accelLen+"\nv0: " + startSpeed);
	}
	
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent) getOwner().getComponent(PhysicsComponent.class);
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode p = getOwner();
		
		speed = speed.add(accel.scale(deltaTime));
		//Vec2 scaledSpeed = speed.scale(deltaTime);
		
		physicsComponent.setVelocity(speed);
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
