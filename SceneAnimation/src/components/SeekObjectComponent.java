package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import main.SceneNode;
import maths.Vec2;

public class SeekObjectComponent extends SeekPointComponent{

	
	private ArrayList<SceneNode> objects;
	
	
	public SeekObjectComponent(SceneNode object, float accel) {
		super(object.getX(), object.getY(), accel);
		
		objects = new ArrayList<>(1);
		objects.add(object);
	}
	public SeekObjectComponent(float accel, SceneNode...objects) {
		super(accel, Arrays.stream(objects).map(o -> o.getPosXY()).toArray(size -> new Vec2[size]) );
		
		this.objects = new ArrayList<>( Arrays.asList(objects) );
	}
	
	@Override
	public void update(float deltaTime) {
		int i = 0;
		for (SceneNode o : objects) {
			super.setPoint(i++, o.getPosXY());
		}
		
		super.update(deltaTime);
	}
	
}
