package components;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	private ArrayList<Vec2> points = new ArrayList<>(2);
	private float accel;
	
	
	public SeekPointComponent(Vec2 point, float accel) {
		points.add(point);
		this.accel = accel;
	}
	public SeekPointComponent(float x, float y, float accel) {
		this(new Vec2(x, y), accel );
	}
	public SeekPointComponent(float accel, Vec2...points) {
		this.points.addAll(Arrays.asList(points));
		this.accel = accel;
	}
	
	@Override
	protected void start() {
		physicsComponent = (PhysicsComponent)super.getOwner().getComponent(PhysicsComponent.class);
		
	}
	
	public void setPoint(int pointi, Vec2 point) {
		points.set(pointi, point);
	}
	public void setPoint(Vec2 point) {
		setPoint(0, point);
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode o = super.getOwner();
		PhysicsComponent pc = physicsComponent;
		
		Vec2 ownerPos = o.getPosXY();
		Vec2 shortestDist = Vec2.newMaxValue();
		for (Vec2 p : points) {
			Vec2 newDist = p.subtract( ownerPos );
			if (newDist.getLengthSquared() < shortestDist.getLengthSquared()) {
				shortestDist = newDist;
			}
		}
		
		shortestDist.setLength(accel);
		physicsComponent.addAcceleration(shortestDist);
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}

}
