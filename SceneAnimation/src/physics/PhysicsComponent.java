package physics;

import main.Component;
import maths.Mat4;
import maths.Vec2;

public class PhysicsComponent extends Component{

	
	private Vec2 velocity = new Vec2();
	private Vec2 acceleration = new Vec2();
	
	private float invMass;
	private float restitution;
	
	private float frictionConst;
	
	
	public PhysicsComponent(float mass, float restitution, float friction) {
		setMass(mass);
		this.frictionConst = friction;
		this.restitution = restitution;
	}
	public PhysicsComponent(float mass, float restitution) {
		this(mass, restitution, 0.01f);
	}
	public PhysicsComponent(float mass) {
		this(mass, 0.5f);
	}
	public PhysicsComponent() {
		this(10f);
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void update(float deltaTime) {
		Vec2 deltaAcceleration = acceleration.scale(deltaTime);
		
		velocity = velocity.add(deltaAcceleration);
		
		//friction
		velocity = velocity.add( velocity.scale(frictionConst).negative() );
		
		Vec2 deltaVelocity = velocity.scale(deltaTime);
		
		//System.out.println(deltaVelocity);
		
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
	public void addVelocity(Vec2 v) {
		this.velocity = velocity.add(v);
	}
	
	public Vec2 getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(Vec2 acceleration) {
		this.acceleration = acceleration;
	}
	
	public float getMass() {
		if (invMass == 0) {
			return 0;
		}
		return 1/invMass;
	}
	public float getInvMass() {
		return invMass;
	}
	public void setMass(float m) {
		if (m == 0)
			invMass = 0;
		else
			invMass = 1/m;
	}
	
	public float getRestitution() {
		return restitution;
	}
	public void setRestitution(float r) {
		restitution = r;
	}
	
	public float getFrictionConst() {
		return frictionConst;
	}
	public void setFrictionConst(float frictionConst) {
		this.frictionConst = frictionConst;
	}
	
	
	
	
}
