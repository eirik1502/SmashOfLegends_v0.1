package components;

import main.CharacterInputState;
import main.Component;
import main.Engine;
import main.SceneNode;
import main.Window;
import maths.Mat4;
import maths.TrigUtils;

public class ControllableComponent extends Component {


	private CharacterComponent characterComponent;
	
	
	@Override
	protected void start() {
		characterComponent = (CharacterComponent) super.getOwner().getComponent(CharacterComponent.class);
	}
	
	@Override
	protected void update(float deltaTime) {
		Engine e = super.getEngine();
		CharacterInputState newInputState = new CharacterInputState(
				e.getMouseX(), e.getMouseY(),
				e.isKeyboardPressed(Window.KEY_A),
				e.isKeyboardPressed(Window.KEY_D),
				e.isKeyboardPressed(Window.KEY_W),
				e.isKeyboardPressed(Window.KEY_S),
				//e.isKeyboardPressed(Window.KEY_D),
				e.isMousePressed(Window.MOUSE_BUTTON_1),
				e.isMousePressed(Window.MOUSE_BUTTON_2),
				e.isKeyboardPressed(Window.KEY_E),
				e.isKeyboardPressed(Window.KEY_Q)
				);
		characterComponent.setInputState(newInputState);
	}

	@Override
	protected void render(Mat4 transform) {

	}


}
