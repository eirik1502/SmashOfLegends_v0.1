package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import components.BoomerangProjectileAbility;
import components.CharacterAnimationComponent;
import components.CharacterComponent;
import components.CollisionComponent;
import components.ControllableComponent;
import components.LineProjectileAbility;
import components.OrbitComponent;
import components.PhysicsComponent;
import components.RenderComponent;
import components.ShootComponent;
import maths.Mat4;
import utils.ShaderUtils;
import utils.VertexArrayUtils;

public class Game {
	
	private static float WINDOW_WIDTH = 1600;
	private static float WINDOW_HEIGHT = 900;
	
	private static float BOARD_WIDTH = 1600;
	private static float BOARD_HEIGHT = 900;
	
	private Engine engine;
	
	
	private SceneNode root;
	
	private SceneNode square;
	
	
	public static void main(String[] args) {
		Game game = new Game();
		game.init();
		game.start();
	}
	
	public void init() {
		engine = new Engine();
		engine.setWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		engine.setWindowTitle("Barrarrarararrrrara");
		
		root = new SceneNode();
		engine.setRoot(root);
		
		engine.init();
		engine.enableUserInput();
		
		createPlayer(root, 100, 100);
		
		SceneNode e = createEnemy(root, 800, 800);
		
		engine.assignCollideable((CollisionComponent)e.getComponent(CollisionComponent.class));
		
//		float[] animData = {0,0,0, 0,0,0, 0,0,0, 0,0,0, 0,0,0,
//							0,0,0, 0,0,0, 0,0,0, 8,0,0, 8,0,0};
//		CharacterAnimation anim = new CharacterAnimation(animData);
//		
//		System.out.println(anim);
//		
//		System.out.println(anim.getState(1));
	}
	
	public void start() {
		float FPS = 60;
		
		engine.start();
	}
	
	//public Collideable collideEnemy
	
	private SceneNode createEnemy(SceneNode root, float x, float y) {
		VertexArray vaoEnemy = VertexArrayUtils.createRectangle(32, 32);
		
		SceneNode nEnemy = new SceneNode();
		
		nEnemy.addComponent(new RenderComponent(vaoEnemy, 16, 16));
		
		nEnemy.setX(x);
		nEnemy.setY(y);
		root.addChild(nEnemy);
		
		return nEnemy;
	}
	
	private SceneNode createPlayer(SceneNode root, float x, float y) {
		VertexArray vaoHead = VertexArrayUtils.createRectangle(32, 32);
		VertexArray vaoLupperArm = VertexArrayUtils.createRectangle(32, 16);
		VertexArray vaoRupperArm = VertexArrayUtils.createRectangle(32, 16);
		VertexArray vaoLlowerArm = VertexArrayUtils.createRectangle(32, 12);
		VertexArray vaoRlowerArm = VertexArrayUtils.createRectangle(32, 12);
		VertexArray vaoLhandStar = VertexArrayUtils.createRectangle(8, 8);
		VertexArray vaoRhandStar = VertexArrayUtils.createRectangle(8, 8);
		
		SceneNode nCharacter = new SceneNode(); //not rendered
		SceneNode nHead = new SceneNode();
		SceneNode nLshoulder = new SceneNode(); //not rendered
		SceneNode nRshoulder = new SceneNode(); //not rendered
		SceneNode nLupperArm = new SceneNode();
		SceneNode nRupperArm = new SceneNode();
		SceneNode nLalbow = new SceneNode(); //not rendered
		SceneNode nRalbow = new SceneNode(); //not rendered
		SceneNode nLlowerArm = new SceneNode();
		SceneNode nRlowerArm = new SceneNode();
		SceneNode nLhand = new SceneNode(); //not rendered
		SceneNode nRhand = new SceneNode(); //not rendered
		SceneNode nLhandStar = new SceneNode();
		SceneNode nRhandStar = new SceneNode();

		//render components
		nHead.addComponent(new RenderComponent(vaoHead, 16, 16));
		nLupperArm.addComponent(new RenderComponent(vaoLupperArm, 8, 16) );
		nRupperArm.addComponent(new RenderComponent(vaoRupperArm, 8, 0) );
		nLlowerArm.addComponent(new RenderComponent(vaoLlowerArm, 8, 12) );
		nRlowerArm.addComponent(new RenderComponent(vaoRlowerArm, 8, 0) );
		nLhandStar.addComponent(new RenderComponent(vaoLhandStar, 4, 4) );
		nRhandStar.addComponent(new RenderComponent(vaoRhandStar, 4, 4) );
		
		//joint placement
		nLshoulder.setY(-20);
		nRshoulder.setY(20);
		nLalbow.setX(36);
		nRalbow.setX(36);
		nLhand.setX(36);
		nRhand.setX(36);
		
		nLhandStar.addComponent(new OrbitComponent(16.0f, -6.28f));
		nRhandStar.addComponent(new OrbitComponent(16.0f, 5.28f));
		
//		nLlowerArm.addComponent(new OrbitComponent(4.0f, 4f));
//		nRlowerArm.addComponent(new OrbitComponent(4.0f, 3.5f));

//		nHead.addComponent(new OrbitComponent(3.0f, 6.28f*0.5f));
		
		//position sub nodes
//		n2.setX(32);
//		n3.setX(32);
//		n4.setX(32);
//		n5.setX(32);
//		n6.setX(32);
		
		//animation
		float[] waveAnimData = {
				0,0,0.2f,	0,0,-0.1f,	0,0,0.1f,	8,0,0,		8,0,0,
				0,0,0.4f,	0,0,-0.2f,	0,0,0.2f,	16,0,0,		16,0,0,
				0,0,0.6f,	0,0,-0.3f,	0,0,0.4f,	24,0,0,	24,0,0,
				0,0,0.8f,	0,0,-0.4f,	0,0,0.4f,	32,0,0,	32,0,0,
				0,0,1.0f,	0,0,-0.3f,	0,0,0.4f,	24,0,0,	24,0,0,
				0,0,1.2f,	0,0,-0.2f,	0,0,0.2f,	16,0,0,		16,0,0,
				0,0,1.4f,	0,0,-0.1f,	0,0,0.1f,	8,0,0,		8,0,0
			};
//		float[] dabAnimData = {
//				0,0,0.2f,	0,0,-0.2f,	0,0,0.2f,	0,0,0,		0,0,-0.4f,
//				2,5,0.4f,	0,0,-0.4f,	0,0,0.4f,	0,0,0,		0,0,-0.8f,
//				4,10,0.6f,	0,0,-0.6f,	0,0,0.6f,	0,0,0,		-4,-4,-1.2f,
//				8,15,0.8f,	0,0,-1.0f,	0,0,0.8f,	0,0,0,		-8,-8,-1.6f,
//				12,20,1.0f,	0,0,-1.4f,	0,0,1.2f,	0,0,0,		-12,-12,-2.4f,
//				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,
//				
//				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,
//				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,
//
//				12,20,1.0f,	0,0,-1.4f,	0,0,1.2f,	0,0,0,		-12,-12,-2.4f,
//				4,10,0.6f,	0,0,-0.6f,	0,0,0.6f,	0,0,0,		-4,-4,-1.2f,
//				0,0,0.2f,	0,0,-0.2f,	0,0,0.2f,	0,0,0,		0,0,-0.4f,
//		};
		float[] dabAnimData = {
				
				0,0,0.2f,	0,0,-0.2f,	0,0,0.2f,	0,0,0,		0,0,-0.4f,
				4,10,0.6f,	0,0,-0.6f,	0,0,0.6f,	0,0,0,		-4,-4,-1.2f,
				12,20,1.0f,	0,0,-1.4f,	0,0,1.2f,	0,0,0,		-12,-12,-2.4f,
				
				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,
				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,

				16,25,1.2f,	0,0,-1.5f,	0,0,1.2f,	0,0,0,		-16,-16,-2.4f,
				12,20,1.0f,	0,0,-1.4f,	0,0,1.2f,	0,0,0,		-12,-12,-2.4f,
				8,15,0.8f,	0,0,-1.0f,	0,0,0.8f,	0,0,0,		-8,-8,-1.6f,
				4,10,0.6f,	0,0,-0.6f,	0,0,0.6f,	0,0,0,		-4,-4,-1.2f,
				2,5,0.4f,	0,0,-0.4f,	0,0,0.4f,	0,0,0,		0,0,-0.8f,
				0,0,0.2f,	0,0,-0.2f,	0,0,0.2f,	0,0,0,		0,0,-0.4f,
		};

		CharacterAnimationComponent animComp = new CharacterAnimationComponent(30, nHead, nLupperArm, nRupperArm, nLlowerArm, nRlowerArm);
		animComp.addAnimation("wave", new CharacterAnimation(waveAnimData));
		animComp.addAnimation("dab", new CharacterAnimation(dabAnimData));
		nCharacter.addComponent(animComp );

		nCharacter.addComponent(new BoomerangProjectileAbility("wave", 1f, 0.24f, 0.1f, 15, 0.6f, 500) );
		nCharacter.addComponent(new LineProjectileAbility("dab", 0.2f, 0.1f, 0.08f, 10, 10) );
		
		nCharacter.addComponent(new CharacterComponent() );
		nCharacter.addComponent(new PhysicsComponent() );
		nCharacter.addComponent(new ControllableComponent() ); //input delayed 1 frame
		
		nCharacter.setX(x);
		nCharacter.setY(y);
		
		//ordering
		root.addChild(nCharacter);
		nCharacter.addChild(nHead);
		nCharacter.addChild(nLshoulder);
		nCharacter.addChild(nRshoulder);
		nLshoulder.addChild(nLupperArm);
		nRshoulder.addChild(nRupperArm);
		nLupperArm.addChild(nLalbow);
		nRupperArm.addChild(nRalbow);
		nLalbow.addChild(nLlowerArm);
		nRalbow.addChild(nRlowerArm);
		nLlowerArm.addChild(nLhand);
		nRlowerArm.addChild(nRhand);
		nLhand.addChild(nLhandStar);
		nRhand.addChild(nRhandStar);
		
		return nCharacter;
	}

}
