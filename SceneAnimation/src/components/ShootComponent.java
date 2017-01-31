package components;

import main.Component;
import main.SceneNode;
import main.VertexArray;
import maths.Mat4;
import utils.VertexArrayUtils;

public class ShootComponent extends Component {

	
	private float reloadTime;
	private float bulletRadius;
	private float bulletSpeed;
	
	
	private float reloadTimer = 0;
	
	
	
	public ShootComponent(float reloadTime, float bulletSpeed, float bulletRadius) {
		this.reloadTime = reloadTime;
		this.bulletSpeed = bulletSpeed;
		this.bulletRadius = bulletRadius;
	}
	public ShootComponent() {
		this(0.5f, 20, 5);
	}
	
	@Override
	protected void start() {
	}
	
	
	public float getReloadTime() {
		return reloadTime;
	}
	public float getReloadTimer() {
		return reloadTimer;
	}
	public void setReloadTimer(float time) {
		reloadTimer = time;
	}
	public void addReloadTimer(float deltaTime) {
		reloadTimer += deltaTime;
	}
	public boolean canShoot() {
		return reloadTimer <= 0;
	}
	public boolean shoot() {
		//shoot
		if (canShoot()) {
			createBullet();
			setReloadTimer(reloadTime);
			return true;
		}
		return false;
	}
	
	private void createBullet() {
		//System.out.println("creating bullet");
		
		VertexArray vao = VertexArrayUtils.createRectangle(bulletRadius*2, bulletRadius*2);
		float direction = getOwner().getGlobalRotationZ();
		float sx = getOwner().getGlobalX();
		float sy = getOwner().getGlobalY();
		
		SceneNode b = new SceneNode();
		b.addComponent(new RenderComponent(vao, bulletRadius, bulletRadius));
		b.addComponent(new MoveLineComponent(direction, bulletSpeed, 3) );
		b.setX(sx);
		b.setY(sy);
		
		super.rootAddChild(b);
	}

	@Override
	protected void update(float deltaTime) {
		if (getReloadTimer() > 0) {
			//System.out.println(reloadTimer);
			addReloadTimer( - reloadTime*deltaTime );
		}
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
