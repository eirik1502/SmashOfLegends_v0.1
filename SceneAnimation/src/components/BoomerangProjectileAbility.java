package components;

import main.SceneNode;
import main.VertexArray;
import physics.CollisionComponent;
import physics.PhCircle;
import physics.PhysicsComponent;
import utils.VertexArrayUtils;

public class BoomerangProjectileAbility extends CharacterAbilityComponent {

	
	private float bulletRadius;
	
	private float bulletTurnTime;
	private float bulletTurnRadius;
	
	
	public BoomerangProjectileAbility(String animationName, float cooldown, float duration, float effectStart, float bulletRadius, float bulletTurnTime, float bulletTurnRadius ) {
		super(animationName, cooldown, duration, effectStart);
		
		this.bulletRadius = bulletRadius;
		this.bulletTurnTime = bulletTurnTime;
		this.bulletTurnRadius = bulletTurnRadius;
	}

	@Override
	protected boolean onTrigger() {
		//System.out.println("Ability triggered");
		return true;
	}
	
	@Override
	protected boolean onEffect() {
		createProjectile();
		return true;
	}

	private void createProjectile() {
		VertexArray vao = VertexArrayUtils.createRectangle(bulletRadius*2, bulletRadius*2);
		float direction = getOwner().getGlobalRotationZ();
		float sx = getOwner().getGlobalX();
		float sy = getOwner().getGlobalY();
		
		SceneNode b = new SceneNode();
		b.addComponent(new RenderComponent(vao, bulletRadius, bulletRadius));
		
		b.addComponent(new MoveBoomerangComponent(direction, bulletTurnTime, bulletTurnRadius));
		b.setX(sx);
		b.setY(sy);
		
		b.addComponent(new CollisionComponent(new PhCircle(16f)));
		b.addComponent(new PhysicsComponent(60) );
		
		super.rootAddChild(b);
	}

	@Override
	protected boolean onEnd() {
		// TODO Auto-generated method stub
		return false;
	}

}
