package components;

import main.SceneNode;
import main.VertexArray;
import utils.VertexArrayUtils;

public class LineProjectileAbility extends CharacterAbilityComponent {

	
	private float bulletRadius;
	private float bulletSpeed;
	
	
	public LineProjectileAbility(String animationName, float cooldown, float duration, float effectStart, float bulletSpeed, float bulletRadius) {
		super(animationName, cooldown, duration, effectStart);
		
		this.bulletRadius = bulletRadius;
		this.bulletSpeed = bulletSpeed;
	}

	@Override
	protected boolean onTrigger() {
		System.out.println("Ability triggered");
		return true;
	}
	
	@Override
	protected boolean onEffect() {
		createProjectile();
		return false;
	}

	private void createProjectile() {
		VertexArray vao = VertexArrayUtils.createRectangle(bulletRadius*2, bulletRadius*2);
		float direction = getOwner().getGlobalRotationZ();
		float sx = getOwner().getGlobalX();
		float sy = getOwner().getGlobalY();
		
		SceneNode b = new SceneNode();
		b.addComponent(new RenderComponent(vao, bulletRadius, bulletRadius));
		b.addComponent(new MoveLineComponent(direction, bulletSpeed) );

		b.setX(sx);
		b.setY(sy);
		
		super.rootAddChild(b);
	}

	@Override
	protected boolean onEnd() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
