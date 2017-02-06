package components;

import main.AbilityOwner;
import main.Component;
import maths.Mat4;

public abstract class CharacterAbilityComponent extends Component{

	
	private CharacterComponent owner;
	
	private float statCooldown,
					duration,
					effectStart,
					freezeDuration;
	
	private String animationName;
	
	
	private float currCooldown;
	
	public static final byte READY_STATE = 0, STARTLAG_STATE = 1, ENDLAG_STATE = 2, COOLDOWN_STATE = 3;
	private byte currentState = READY_STATE;
	
	private boolean timerActive = false;
	private float timer;
	
	
	public CharacterAbilityComponent(String animationName, float cooldown, float duration, float effectStart, float freezeDuration) {
		this.animationName = animationName;
		this.statCooldown = cooldown;
		this.duration = duration;
		this.effectStart = effectStart;
		this.freezeDuration = freezeDuration;
	}
	public CharacterAbilityComponent(float cooldown, float duration, float effectStart, float freezeDuration) {
		this(null, cooldown, duration, effectStart, freezeDuration);
	}
	public CharacterAbilityComponent(String animationName, float cooldown, float duration, float effectStart) {
		this(animationName, cooldown, duration, effectStart, 0f);
	}
	public CharacterAbilityComponent(float cooldown, float duration, float effectStart) {
		this(cooldown, duration, effectStart, 0f);
	}
	public CharacterAbilityComponent(float cooldown) {
		this(cooldown, 0.23f, 0.1f, 0.2f);
	}
	
	@Override
	protected void start() {
		owner = (CharacterComponent) super.getOwner().getComponent(CharacterComponent.class);
	}
	
	public boolean triggerAbility() {
		if (currentState == READY_STATE) {
			if (onTrigger()) {
				//start timer
				startTimer();
				currentState = STARTLAG_STATE;
				
				return true;
			}
		}
		return false;
	}
	private void triggerEffect() {
		onEffect();
	}
	private void endAbility() {
		onEnd();
		resetTimer();
		applyCooldown();
		
		owner.onAbilityEnd(this);
	}
	
	public boolean hasAnimation() {
		return animationName != null;
	}
	public String getAnimationName() {
		return animationName;
	}
	
	public void applyCooldown() {
		currCooldown = statCooldown;
	}
	private void cooldownFinished() {
		currentState = READY_STATE;
	}
	
	protected abstract boolean onTrigger();
	protected abstract boolean onEffect();
	protected abstract boolean onEnd();
	

	@Override
	protected void update(float deltaTime) {
		if (timerActive()) {
			incrementTimer(deltaTime);
			
			switch(currentState) {
			case STARTLAG_STATE:
				
				if (getTimerTime() >= effectStart) {
					triggerEffect();
					currentState = ENDLAG_STATE;
				}
				break;
				
			case ENDLAG_STATE:
				
				if (getTimerTime() >= duration) {
					endAbility();
					currentState = COOLDOWN_STATE;
				}
				break;
			}
			
		}
		if (currCooldown > 0) {
			currCooldown -= deltaTime;
			
			if (currCooldown <= 0) {
				cooldownFinished();
			}
		}
		
		
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}
	
	private void startTimer() {
		timerActive = true;
	}
	private void stopTimer() {
		timerActive = false;
	}
	private void setTimer(float time) {
		timer = time;
	}
	private void resetTimer() {
		stopTimer();
		setTimer(0f);
	}
	private float getTimerTime() {
		return timer;
	}
	private boolean timerActive() {
		return timerActive;
	}
	private void incrementTimer(float deltaTime) {
		timer += deltaTime;
	}
}
