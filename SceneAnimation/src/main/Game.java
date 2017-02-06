package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import components.BoomerangProjectileAbility;
import components.CharacterAnimationComponent;
import components.CharacterComponent;
import components.ControllableComponent;
import components.GameControlComponent;
import components.LineProjectileAbility;
import components.OrbitComponent;
import components.RenderComponent;
import components.SeekPointComponent;
import components.ShootComponent;
import maths.M;
import maths.Mat4;
import physics.NaturalCollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
import physics.PhysicsComponent;
import physics.PhysicsEngine;
import utils.ShaderUtils;
import utils.VertexArrayUtils;

public class Game {
	
	public static float WINDOW_WIDTH = 1600;
	public static float WINDOW_HEIGHT = 900;
	
	public static float BOARD_WIDTH = 1600;
	public static float BOARD_HEIGHT = 900;
	
	private Engine engine;
	
	private SceneNode root;
	
	private boolean restarting = false;
	
	
	public static void main(String[] args) {
		while(true) {
			Game game = new Game();
			game.init();
			if (game.start() == 2) {
				continue;
			}
			break;
		}
	}
	
	public void init() {
		engine = new Engine();
		
		engine.setWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		engine.setWindowTitle("Barrarrarararrrrara");
		
		root = new SceneNode();
		
		engine.setRoot(root);
		
		engine.init();
		engine.enableUserInput();
		
		root.addComponent(new GameControlComponent(this) );
		
	}
	
	public int start() {

		int code =  engine.start();
		if (restarting) {
			restarting = false;
			return 2;
		}
		return code;
	}
	
	public void restart() {
		restarting = true;
		engine.stop();
	}
	
	public void stop() {
		engine.stop();
	}
}
