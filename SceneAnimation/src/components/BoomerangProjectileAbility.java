package components;

import main.SceneNode;
import main.VertexArray;
import maths.TrigUtils;
import maths.Vec2;
import physics.NaturalCollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
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
		
		Vec2 startPos = new Vec2(sx, sy).add(Vec2.newLenDir(16, direction));
		b.setPosXY(startPos);
		
		
		b.addComponent(new NaturalCollisionComponent(new PhCircle(bulletRadius)));
		b.addComponent(new PhysicsComponent(60*3).setDrawVectors(true) );
		
		b.addComponent(new DrawVecComponent() );
		
		super.rootAddChild(b);
	}

	@Override
	protected boolean onEnd() {
		// TODO Auto-generated method stub
		return false;
	}

}
