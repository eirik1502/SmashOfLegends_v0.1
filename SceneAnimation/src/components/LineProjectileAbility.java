package components;

import main.SceneNode;
import main.VertexArray;
import maths.TrigUtils;
import physics.NaturalCollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
import physics.PhysicsComponent;
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
		return true;
	}
	
	@Override
	protected boolean onEffect() {
		createProjectile();
		return false;
	}

	private void createProjectile() {
		//VertexArray vao = VertexArrayUtils.createRectangle(bulletRadius*2, bulletRadius*2);
		VertexArray vao = VertexArrayUtils.createCircle(bulletRadius, 8);
		float direction = getOwner().getGlobalRotationZ();
		float sx = getOwner().getGlobalX();
		float sy = getOwner().getGlobalY();
		
		float ownerRadius = 16;
		
		SceneNode b = new SceneNode();
		b.setX(sx + TrigUtils.lengthdirX(ownerRadius, direction));
		b.setY(sy + TrigUtils.lengthdirY(ownerRadius, direction));
		
		b.addComponent(new RenderComponent(vao));
		b.addComponent(new MoveLineComponent(direction, bulletSpeed, 2) );
		
		b.addComponent(new NaturalCollisionComponent(new PhCircle(bulletRadius)));
		b.addComponent(new PhysicsComponent(40, 0.99f, 0.0001f).setDrawVectors(true) );
		
		b.addComponent(new DrawVecComponent() );
		
		super.rootAddChild(b);
	}

	@Override
	protected boolean onEnd() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
