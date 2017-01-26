package components;

import main.Collideable;
import main.Component;
import main.PhShape;
import main.SceneNode;
import maths.Mat4;

public class CollisionComponent extends Component implements Collideable{

	
	private PhShape collisionShape;
	
	
	public CollisionComponent(PhShape collisionShape) {
		this.collisionShape = collisionShape;
	}
	
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update(float deltaTime) {
		SceneNode p = super.getOwner();
		collisionShape.setX(p.getGlobalX());
		collisionShape.setY(p.getGlobalY());
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhShape getPhShape() {
		return collisionShape;
	}

}
