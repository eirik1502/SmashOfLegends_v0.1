package components;

import main.AbilityOwner;
import main.CharacterInputState;
import main.Component;
import main.Engine;
import main.SceneNode;
import main.Window;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;
import physics.PhysicsComponent;

public class CharacterComponent extends Component{

	
	private CharacterAbilityComponent ability1, ability2;	
	private CharacterAnimationComponent animationComp;
	private PhysicsComponent physicsComp;

	private CharacterInputState inputState = new CharacterInputState();
	
	private float moveAccel = 1000;
	
	
	public static final byte IDLE_STATE = 0, MOVE_STATE = 1, ABILITY_STATE = 2;
	private int currentState = IDLE_STATE;
	
	
	public void setInputState(CharacterInputState inputState) {
		this.inputState = inputState;
	}
	
	@Override
	protected void start() {
		animationComp = (CharacterAnimationComponent) super.getOwner().getComponent(CharacterAnimationComponent.class);
		physicsComp = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		ability1 = (BoomerangProjectileAbility) super.getOwner().getComponent(BoomerangProjectileAbility.class);
		ability2 = (LineProjectileAbility) super.getOwner().getComponent(LineProjectileAbility.class);
		
		physicsComp.setFrictionConst(0.1f);
	}
	@Override
	protected void update(float deltaTime) {
		SceneNode p = getOwner();
		Engine e = getEngine();
		
		updateWalk(p, e);
		updateRotation(p, e);
		updateAbilities(p, e);
	}
	
	private void updateWalk(SceneNode p, Engine e) {
		if (inState(ABILITY_STATE)) return;
		
		Vec2 currAccel = new Vec2();
		
		currAccel.x = (inputState.getMoveRight() ? 1f : 0f)
				- (inputState.getMoveLeft() ? 1f : 0f);
		
		currAccel.y = (inputState.getMoveDown() ? 1f : 0f)
				- (inputState.getMoveUp() ? 1f : 0f);
		
		currAccel = currAccel.scale( moveAccel );
		
		
		
		physicsComp.setAcceleration(currAccel);
		
		if (!currAccel.isNull()) {
			//physicsComp.setVelocity(currVelocity);
		}
		else if (inState(MOVE_STATE)) {
			setState(IDLE_STATE);
		}
	}
	private void updateRotation(SceneNode p, Engine e) {
		if (inState(ABILITY_STATE)) return;
		
		//set angle to mouse
		p.setRotationZ( TrigUtils.pointDirection(p.getX(), p.getY(), inputState.getMouseX(), inputState.getMouseY()) );
	}
	
	private void updateAbilities(SceneNode p, Engine e) {
		if (inState(ABILITY_STATE)) return;
		
		if (inputState.getAbility1()) {
			if (ability1.triggerAbility()) {
				if (ability1.hasAnimation())
					animationComp.startAnimation(ability1.getAnimationName());
				
				setState(ABILITY_STATE);
			}
		}
		else if (inputState.getAbility2()) {
			if (ability2.triggerAbility()) {
				if (ability2.hasAnimation())
					animationComp.startAnimation(ability2.getAnimationName());
				
				setState(ABILITY_STATE);
			}
		}
	}

	public void onAbilityEnd(CharacterAbilityComponent ability) {
		changeState(ABILITY_STATE, IDLE_STATE);
	}
	
	
	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}
	
	private void setState(byte state) {
		currentState = state;
	}
	private boolean changeState(byte fromState, byte toState) {
		if (inState(fromState)) {
			setState(toState);
			return true;
		}
		return false;
	}
	private int getState() {
		return currentState;
	}
	private boolean inState(byte state) {
		return currentState == state;
	}
}
