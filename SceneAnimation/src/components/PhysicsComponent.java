package components;

import main.Component;
import maths.Mat4;
import maths.Vec2;

public class PhysicsComponent extends Component{

	
	private Vec2 velocity = new Vec2();
	private Vec2 acceleration = new Vec2();
	
	float frictionConst = 0.2f;
	
	
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void update(float deltaTime) {
		Vec2 deltaAcceleration = acceleration.scale(deltaTime);
		
		velocity = velocity.add(deltaAcceleration);
		
		//friction
		//velocity = velocity.add( velocity.scale(frictionConst).negative() );
		
		Vec2 deltaVelocity = velocity.scale(deltaTime);
		
		System.out.println(deltaVelocity);
		
		super.getOwner().addX(deltaVelocity.x);
		super.getOwner().addY(deltaVelocity.y);
	}
	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}
	public Vec2 getVelocity() {
		return velocity;
	}
	public void setVelocity(Vec2 velocity) {
		this.velocity = velocity;
	}
	public Vec2 getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(Vec2 acceleration) {
		this.acceleration = acceleration;
	}
	public float getFrictionConst() {
		return frictionConst;
	}
	public void setFrictionConst(float frictionConst) {
		this.frictionConst = frictionConst;
	}
	
	
	
	
}
