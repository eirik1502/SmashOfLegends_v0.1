package components;

import maths.Vec2;
import physics.PhysicsComponent;

public class DashAbility extends CharacterAbilityComponent {

	
	private PhysicsComponent phComp;
	
	private float dashAccelLen;
	private float dashAccelDir;
	
	
	public DashAbility(String animationName, float cooldown, float duration, float effectStart, float dashAccel) {
		super(animationName, cooldown, duration, effectStart);
		this.dashAccelLen = dashAccel;
	}
	
	@Override
	public void start() {
		super.start();
		
		phComp = (PhysicsComponent) getOwner().getComponent(PhysicsComponent.class);
	}
	

	@Override
	protected boolean onTrigger() {
		dashAccelDir = getOwner().getGlobalRotationZ();
		return true;
	}

	@Override
	protected boolean onEffect() {
		//System.out.println("DASH!");
		phComp.addImpulse(Vec2.newLenDir(dashAccelLen, dashAccelDir));
		return true;
	}

	@Override
	protected boolean onEnd() {
		return true;
	}

	
	
	
	
	
	

}
