package components;

import main.CharacterInputState;
import main.Component;
import main.SceneNode;
import maths.M;
import maths.Mat4;
import maths.Vec2;
import physics.PhysicsComponent;

public class CharacterAiComponent extends Component {

	
	private CharacterComponent characterComponent;
	private PhysicsComponent physicsComponent;
	
	//self made
	private DrawVecComponent drawVecComp;
	
	private SceneNode[] holes;
	
	private float mouseAngle = 0;
	
	boolean mvLeft = false;
	boolean mvRight = false;
	
	boolean mvUp = false;
	boolean mvDown = false;
	
	
	private float dirTimer = 0;
	
	private Vec2 dangerDrawVec = new Vec2();
	private Vec2 dangerCounterDrawVec = new Vec2();
	
	private float dangerCounter = 0;
	
	
	public CharacterAiComponent(SceneNode[] holes) {
		this.holes = holes;
	}
	
	@Override
	protected void start() {
		characterComponent = (CharacterComponent) super.getOwner().getComponent(CharacterComponent.class);
		physicsComponent = (PhysicsComponent) getOwner().getComponent(PhysicsComponent.class);
		drawVecComp = (DrawVecComponent) getOwner().getComponent(DrawVecComponent.class);
		
		drawVecComp.addVec(dangerDrawVec, 4, 128);
		drawVecComp.addVec(dangerCounterDrawVec, 4, 64);
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode owner = getOwner();
		float deltaMousePos = 0.1f;
		
		boolean ab1 = false, ab2 = false, ab3 = false;
		
		//calculate danger
		float holeConsiderRadius = 450f;
		float speedConsiderRadiusRatio = 0.8f;
		//float holeDangerRadius = 200f;
		
		Vec2 lrudRatio = getHoleDangerRatio(owner, holeConsiderRadius, speedConsiderRadiusRatio);
		
		
		float lrudRatioLen = lrudRatio.getLength();
		float lrudRatioDir = lrudRatio.getDirection();
		if (lrudRatioLen < 0.3f) {
			dangerCounter = 0;
		}
		else {
			dangerCounter += deltaTime*(lrudRatioLen*2);
		}
		
		ab1 = M.random() < 0.01f;
		ab2 = M.random() < 0.1f;
		
		mouseAngle += (1 - M.random()*2) * deltaMousePos;
		
		if (dangerCounter > 0.5f) {
			mouseAngle = lrudRatioDir;
		}
		//dash away from hole if I sense danger
		if (dangerCounter > 1) {
			mouseAngle = lrudRatioDir;
//			ab1 = false;
//			ab2 = false;
			ab3 = true;
		}
		
//		dangerDrawVec.setAs(lrudRatio);
//		dangerCounterDrawVec.y = -dangerCounter;
		
		
		dirTimer -= deltaTime;
		if (dirTimer <= 0) {
			
			//change vec only on move counter
			dangerDrawVec.setAs(lrudRatio);
			dangerCounterDrawVec.y = -dangerCounter;
			
			Vec2 lrudRatioScaled = lrudRatio.scale(2);
			
			float noActionRat = 0.5f	/2;
			float n = 1 - M.random()*2;
			n = n + lrudRatioScaled.x;
			
			mvLeft = n < 0 - noActionRat;
			mvRight = n > 0 + noActionRat;
			
			n = 1 - M.random()*2;
			n = n + lrudRatioScaled.y;
			
			mvUp = n < 0 - noActionRat;
			mvDown = n > 0 + noActionRat;
			
			//if close to hole, set timer shorter
			dirTimer = M.random()*0.7f * (1 -  0.5f*(lrudRatioLen ) );
		}
		
		characterComponent.setInputState(new CharacterInputState(
				getOwner().getX() + M.cos(mouseAngle)*5,
				getOwner().getY() + M.sin(mouseAngle)*5,
				mvLeft, mvRight, mvUp, mvDown,
				ab1, ab2, ab3, false
				));
		
	}
	
	private Vec2 getHoleDangerRatio(SceneNode owner, float holeDangerRadius, float speedConsiderRadiusRatio) {
		Vec2 lrudRatio = new Vec2();
		Vec2 velocity = physicsComponent.getVelocity();
		float maxVelocity = 10	*PhysicsComponent.METER;
		
		Vec2 ownerPos = new Vec2(owner.getX(), owner.getY());
		for (int i = 0; i < holes.length; i++) {
			SceneNode h = holes[i];
			Vec2 holePos = new Vec2( h.getX(), h.getY() );
			Vec2 distVec = holePos.subtract( ownerPos );
			
			float dist = distVec.getLength();
			if (dist > holeDangerRadius) continue;
			
			//update move ratios to avoid close holes
			lrudRatio = lrudRatio.add( distVec.normalize().scale(1 - dist/holeDangerRadius).negative() );
			if (dist < holeDangerRadius*speedConsiderRadiusRatio) {
				float velocityTowardHole = velocity.dotProduct(distVec);
				lrudRatio = lrudRatio.scale(M.max(1, 1 + velocityTowardHole/maxVelocity ));
			}
		}
		
		return lrudRatio;
	}
}
