package components;

import main.Component;
import maths.Mat4;

public class RotationComponent extends Component {

	
	private float roundsPerSec;
	
	
	public RotationComponent(float roundsPerSec) {
		this.roundsPerSec = roundsPerSec;
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void update(float deltaTime) {
		getOwner().setRotationZ( getOwner().getRotationZ() + deltaTime * roundsPerSec );

	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub

	}

}
