package physics;

import components.DrawVecComponent;
import main.Component;
import maths.Mat4;
import maths.Vec2;

public class PhysicsComponent extends Component{

	public static final float METER = 32.0f;
	public static final float gravityAcceleration = 9.81f;
	
	public static final int FRICTION_MODEL_COULOMB = 0,
							FRICTION_MODEL_VICIOUS = 1; //normal force * friction const
	
	private int frictionModel = FRICTION_MODEL_COULOMB;
	
	private Vec2 temporaryVelocity = new Vec2();
	private Vec2 velocity = new Vec2();
	private Vec2 impulse = new Vec2();
	private Vec2 acceleration = new Vec2();

	private float invMass;
	private float restitution;
	
	private float frictionConst;
	
	private boolean drawVectors = false;
	private DrawVecComponent drawVecComp;
	private float drawVecWidth = 2;
	private Vec2 velocityDraw = new Vec2();
	private Vec2 accelerationDraw = new Vec2();
	private Vec2 frictionDraw = new Vec2();
	
	
	public PhysicsComponent(float mass, float restitution, float friction) {
		setMass(mass);
		this.frictionConst = friction;
		this.restitution = restitution;
	}
	public PhysicsComponent(float mass, float restitution) {
		this(mass, restitution, 0.5f);
	}
	public PhysicsComponent(float mass) {
		this(mass, 0.5f);
	}
	public PhysicsComponent() {
		this(10f);
	}
	
	@Override
	protected void start() {
		if (drawVectors)  {
			drawVecComp = (DrawVecComponent) getOwner().getComponent(DrawVecComponent.class);
			drawVecComp.addVecQuarterMeter(velocityDraw, drawVecWidth);
			drawVecComp.addVecQuarterMeter(accelerationDraw, drawVecWidth);
			drawVecComp.addVecQuarterMeter(frictionDraw, drawVecWidth);
		}
		
	}
	@Override
	protected void update(float deltaTime) {
		
		Vec2 friction = getFriction();
		
		//draw vectors
		if (drawVectors) {
			accelerationDraw.setAs(acceleration);
			velocityDraw.setAs(velocity);
			frictionDraw.setAs(friction);
		}
		
		addAcceleration(friction);
		
		Vec2 deltaAcceleration = acceleration.scale(deltaTime);
		
		velocity = velocity.add(deltaAcceleration).add(impulse);

		Vec2 deltaVelocity = velocity.scale(deltaTime).add(temporaryVelocity);
		
		//System.out.println(deltaVelocity);
		
		super.getOwner().addX(deltaVelocity.x * METER);
		super.getOwner().addY(deltaVelocity.y * METER);
		
		resetTemporaryVelocity();
		resetImpulse();
		resetAcceleration();
	}
	private Vec2 getFriction() {
		Vec2 fricAccel = new Vec2();
		if (frictionModel == FRICTION_MODEL_COULOMB) {
			fricAccel = getVelocity().normalize().scale(gravityAcceleration*frictionConst).negative();
		}
		else if (frictionModel == FRICTION_MODEL_VICIOUS) {
			fricAccel = velocity.scale(frictionConst).negative();
		}
		return fricAccel;
	}
	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}
	
	public PhysicsComponent setDrawVectors(boolean value) {
		this.drawVectors = value;
		return this;
	}
	
	public void setFrictionModel(int model) {
		this.frictionModel = model;
	}
	
	public void addTemporaryVelocity(Vec2 v) {
		temporaryVelocity.setAs(v);
	}
	private void resetTemporaryVelocity() {
		temporaryVelocity.setZero();
	}
	
	public Vec2 getVelocity() {
		return velocity;
	}
//	private void setVelocity(Vec2 velocity) {
//		this.velocity = velocity;
//	}
//	private void addVelocity(Vec2 v) {
//		this.velocity = velocity.add(v);
//	}
	
	private void resetImpulse() {
		impulse.x = impulse.y = 0;
	}
	public void addImpulse(Vec2 v) {
		impulse = impulse.add(v);
	}
	
	public Vec2 getAcceleration() {
		return acceleration;
	}
//	private void setAcceleration(Vec2 acceleration) {
//		this.acceleration = acceleration;
//	}
	private void resetAcceleration() {
		acceleration.x = acceleration.y = 0;
	}
	public void addAcceleration(Vec2 acceleration) {
		this.acceleration = this.acceleration.add( acceleration );
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
