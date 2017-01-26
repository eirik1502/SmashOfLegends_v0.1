package components;

import main.Component;
import main.SceneNode;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;

public class MoveBoomerangComponent extends Component{

	
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
		System.out.println("Acceleration: "+ accelLen+"\nv0: " + startSpeed);
	}
	
	@Override
	protected void start() {
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode p = getOwner();
		
		speed = speed.add(accel.scale(deltaTime));
		Vec2 scaledSpeed = speed.scale(deltaTime);
		
		p.setX(p.getX() + scaledSpeed.x);
		p.setY(p.getY() + scaledSpeed.y);
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
