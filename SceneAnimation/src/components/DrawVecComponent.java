package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import main.Component;
import main.SceneNode;
import main.VertexArray;
import maths.Mat4;
import maths.Vec2;
import physics.PhysicsComponent;
import utils.VertexArrayUtils;

public class DrawVecComponent extends Component {

	
	private HashMap<Vec2, SceneNode> vecNodes = new HashMap<>();
	
	
	public DrawVecComponent() {
		
	}
	
	public void addVec(Vec2 v, float width, float scale) {
		vecNodes.put(v, createVecNode(width, scale) );
	}
	public void addVecMeter(Vec2 v, float width) {
		addVec(v, width, PhysicsComponent.METER);
	}
	public void addVecQuarterMeter(Vec2 v, float width) {
		addVec(v, width, 0.25f * PhysicsComponent.METER);
	}
	public void removeVec(Vec2 v) {
		vecNodes.remove(v);
	}
	
	private SceneNode createVecNode(float width, float scale) {
		VertexArray vao = VertexArrayUtils.createRectangle(scale, width);
		SceneNode n = new SceneNode();
		n.addComponent(new RenderComponent(vao, 0, width/2) );
		
		return n;
	}
	
	@Override
	protected void start() {
		for (SceneNode v : vecNodes.values())
			getOwner().addChild(v);
	}
	@Override
	protected void end() {
//		for (Map.Entry<Vec2, SceneNode> entry : vecNodes.entrySet()) {
//			entry.getValue().destroy();
//		}
	}
	
	@Override
	protected void update(float deltaTime) {
		for (Map.Entry<Vec2, SceneNode> entry : vecNodes.entrySet()) {
			Vec2 v = entry.getKey();
			SceneNode vn = entry.getValue();
			
			vn.setScaleX(v.getLength());
			vn.setRotationZ(v.getDirection() - getOwner().getGlobalRotationZ());
		}
	}

}
