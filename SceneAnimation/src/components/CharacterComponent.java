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

	
	private CharacterAbilityComponent[] abilities;	
	private CharacterAnimationComponent animationComp;
	private PhysicsComponent physicsComp;

	private CharacterInputState inputState = new CharacterInputState();
	
	private float moveAccel = 30;//15;
	
	
	public static final byte IDLE_STATE = 0, MOVE_STATE = 1, ABILITY_STATE = 2;
	private int currentState = IDLE_STATE;
	
	
	public void setInputState(CharacterInputState inputState) {
		this.inputState = inputState;
	}
	
	@Override
	protected void start() {
		animationComp = (CharacterAnimationComponent) super.getOwner().getComponent(CharacterAnimationComponent.class);
		physicsComp = (PhysicsComponent) super.getOwner().getComponent(PhysicsComponent.class);
		
		abilities = new CharacterAbilityComponent[3];
		abilities[0] = (BoomerangProjectileAbility) super.getOwner().getComponent(BoomerangProjectileAbility.class);
		abilities[1] = (LineProjectileAbility) super.getOwner().getComponent(LineProjectileAbility.class);
		abilities[2] = (DashAbility) super.getOwner().getComponent(DashAbility.class);
		
		physicsComp.setFrictionConst(3f);
		physicsComp.setFrictionModel(PhysicsComponent.FRICTION_MODEL_VICIOUS);
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
		
		if (!currAccel.isNull()) {
			currAccel = currAccel.normalize().scale( moveAccel );
			physicsComp.addAcceleration(currAccel );
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
		
		
		for (int i = 0; i < abilities.length; i++) {
			if (inputState.getAbility(i)) {
				if (abilities[i].triggerAbility()) {
					
					if (abilities[i].hasAnimation())
						animationComp.startAnimation(abilities[i].getAnimationName());
					
					setState(ABILITY_STATE);
					break;
				}
			}
		}
		
	}

	public void onAbilityEnd(CharacterAbilityComponent ability) {
		changeState(ABILITY_STATE, IDLE_STATE);
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
