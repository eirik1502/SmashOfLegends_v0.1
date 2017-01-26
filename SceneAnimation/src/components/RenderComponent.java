package components;

import main.Component;
import main.VertexArray;
import maths.Mat4;
import maths.Vec3;

public class RenderComponent extends Component {

	private VertexArray vao;
	private Vec3 centerTranslate;
	
	
	public RenderComponent(VertexArray vao, float centerX, float centerY) {
		centerTranslate = new Vec3(-centerX, -centerY, 0f);
		this.vao = vao;
	}
	public RenderComponent(VertexArray vao) {
		this(vao, 0, 0);
	}
	
	@Override
	protected void start() {
		
	}

	@Override
	protected void update(float deltaTime) {
		
	}

	@Override
	protected void render(Mat4 transform) {
		//add transform for center position
		Mat4 finalTransform = transform.multiply( Mat4.translate( centerTranslate ) );
		getEngine().renderVertexArray(vao, finalTransform);
	}

	
}
