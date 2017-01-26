package components;

import main.Component;
import maths.Mat4;
import maths.TrigUtils;

public class OrbitComponent extends Component {

	private float orbitRadius;
	private float angleSpeed;
	
	private float currAngle = 0;
	
	
	public OrbitComponent(float radius, float angleSpeed) {
		this.orbitRadius = radius;
		this.angleSpeed = angleSpeed;
	}
	
	@Override
	protected void start() {
		
	}

	@Override
	protected void update(float deltaTime) {
		currAngle += angleSpeed*deltaTime;
		getOwner().setX(TrigUtils.cos(currAngle) * orbitRadius);
		getOwner().setY(TrigUtils.sin(currAngle) * orbitRadius);
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}

}
