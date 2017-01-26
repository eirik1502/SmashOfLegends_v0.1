package components;

import java.util.HashMap;

import main.CharacterAnimation;
import main.CharacterAnimationState;
import main.Component;
import main.NodeAnimationState;
import main.SceneNode;
import maths.Mat4;

public class CharacterAnimationComponent extends Component{

	
	private HashMap<String, CharacterAnimation> animations = new HashMap<>();
	private float statesPerSec;
	
	private int headi = 0, lUpperArmi = 1, rUpperArmi = 2, lLowerArmi = 3, rLowerArmi = 4;
	private SceneNode[] characterParts = new SceneNode[5];
	
	private CharacterAnimationState nullState = new CharacterAnimationState();
	
	private boolean running = false;
	private float timeElapsed = 0;
	private CharacterAnimation currentAnimation = null;
	
	
	public CharacterAnimationComponent(float statesPerSec, SceneNode head, SceneNode lUpperArm, SceneNode rUpperArm, SceneNode lLowerArm, SceneNode rLowerArm) {
		this.statesPerSec = statesPerSec;
		
		this.characterParts[0] = head;
		this.characterParts[1] = lUpperArm;
		this.characterParts[2] = rUpperArm;
		this.characterParts[3] = lLowerArm;
		this.characterParts[4] = rLowerArm;
	}
	
	public void addAnimation(String name, CharacterAnimation animation) {
		animations.put(name, animation);
	}
	
	public void startAnimation(String name) {
		if (!animations.containsKey(name)) throw new IllegalStateException("animation does not exist");
		
		currentAnimation = animations.get(name);
		
		timeElapsed = 0;
		running = true;
	}
	
	public void endAnimation() {
		running = false;
		setCharacterState(nullState);
		currentAnimation = null;
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update(float deltaTime) {
		if (!running) return;
		
		timeElapsed += statesPerSec*deltaTime;
		if (timeElapsed >= currentAnimation.getStateCount()) {
			endAnimation();
			return;
		}
		
		int animIndex = (int)Math.floor(timeElapsed);
		CharacterAnimationState animState = currentAnimation.getState(animIndex);
		
		setCharacterState(animState);
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}
	
	private void setCharacterState(CharacterAnimationState animState) {
		NodeAnimationState[] nodeStates = animState.getNodeStates();
		for (int i = 0; i < 5; i++) {
			setNodeState(characterParts[i], nodeStates[i]);
		}
	}
	private void setNodeState(SceneNode node, NodeAnimationState animNodeState) {
		node.setX(animNodeState.getX());
		node.setY(animNodeState.getY());
		node.setRotationZ(animNodeState.getRotationZ());
	}
}
