package components;

import main.Component;
import main.SceneNode;
import maths.Mat4;
import maths.TrigUtils;

public class MoveLineComponent extends Component {

	
	private float deltaX;
	private float deltaY;
	
	
	public MoveLineComponent(float direction, float speed) {
		deltaX = TrigUtils.cos(direction) * speed;
		deltaY = TrigUtils.sin(direction) * speed;
	}
	
	@Override
	protected void start() {
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode p = getOwner();
		p.setX(p.getX() + deltaX);
		p.setY(p.getY() + deltaY);
	}

	@Override
	protected void render(Mat4 transform) {
	}

}
