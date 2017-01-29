package components;

import main.Component;
import main.SceneNode;
import maths.Mat4;
import maths.TrigUtils;
import maths.Vec2;
import physics.PhysicsComponent;


/**
 * Not working
 * @author Eirik
 *
 */
public class SeekPointComponent extends Component {

	private PhysicsComponent physicsComponent;
	
	private Vec2 point;
	private float accel;
	
	
	public SeekPointComponent(Vec2 point, float accel) {
		this.point = point;
		this.accel = accel;
	}
	public SeekPointComponent(float x, float y, float accel) {
		this(new Vec2(x, y), accel );
	}
	
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent)super.getOwner().getComponent(PhysicsComponent.class);
		
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode o = super.getOwner();
		PhysicsComponent pc = physicsComponent;
		
		Vec2 homeDir = Vec2.createLendir(1.0f, TrigUtils.pointDirection(o.getX(), o.getY(), point.x, point.y));
		physicsComponent.setAcceleration(homeDir.scale(accel));
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}

}
