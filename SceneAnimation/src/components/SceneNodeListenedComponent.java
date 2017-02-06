package components;

import main.Component;
import main.SceneNodeEndListener;
import main.SceneNodeRenderListener;
import main.SceneNodeStartListener;
import main.SceneNodeUpdateListener;
import maths.Mat4;

public class SceneNodeListenedComponent extends Component {

	
	private SceneNodeStartListener startListener;
	private SceneNodeEndListener endListener;
	private SceneNodeUpdateListener updateListener;
	private SceneNodeRenderListener renderListener;
	
	
	public SceneNodeListenedComponent() {
		
	}
	
	public void assignStartListener(SceneNodeStartListener listener) {
		this.startListener = listener;
	}
	public void assignEndListener(SceneNodeEndListener listener) {
		this.endListener = listener;
	}
	public void assignUpdateListener(SceneNodeUpdateListener listener) {
		this.updateListener = listener;
	}
	public void assignRenderListener(SceneNodeRenderListener listener) {
		this.renderListener = listener;
	}
	
	@Override
	protected void start() {
		if (startListener != null) {
			startListener.onStart();
		}
	}
	
	@Override
	protected void end() {
		if (endListener != null) {
			endListener.onEnd();
		}
	}
	
	@Override
	protected void update(float deltaTime) {
		if (updateListener != null) {
			updateListener.onUpdate(deltaTime);
		}
	}
	
	@Override
	protected void render(Mat4 transform) {
		if (renderListener != null) {
			renderListener.onRender(transform);
		}
	}

}
