package components;

import java.util.ArrayList;

import main.CharacterAnimation;
import main.Component;
import main.Game;
import main.SceneNode;
import main.VertexArray;
import main.Window;
import maths.M;
import maths.Mat4;
import physics.CollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
import physics.PhysicsComponent;
import utils.VertexArrayUtils;

public class GameControlComponent extends Component {

	private Game game;
	
	private ArrayList<SceneNode> enemies = new ArrayList<>();
	
	
	private float timeSinceEnemySeekSwitch = 0;
	
	
	public GameControlComponent(Game game) {
		this.game = game;
	}
	
	@Override
	protected void start() {
		createInitialObjects();
		this.switchEnemiesSeekComponent();
	}

	@Override
	protected void update(float deltaTime) {
		float timeBetweenSwitch = 0.3f;
		
		timeSinceEnemySeekSwitch += deltaTime;
		if (getEngine().isKeyboardPressed(Window.KEY_E) && timeSinceEnemySeekSwitch > timeBetweenSwitch) {
			System.out.println("Change enemy seek state");
			switchEnemiesSeekComponent();
			timeSinceEnemySeekSwitch = 0;
		}
	}

	@Override
	protected void render(Mat4 transform) {
		// TODO Auto-generated method stub

	}
	
	private void switchEnemiesSeekComponent() {
		for (SceneNode e : enemies) {
			SeekPointComponent spc = (SeekPointComponent) e.getComponent(SeekPointComponent.class);
			if (spc.isActive()) {
				spc.deactivate();
			}
			else {
				spc.activate();
			}
		}
	}
	
	private void createInitialObjects() {
		SceneNode root = getRoot();
		float WINDOW_WIDTH = Game.WINDOW_WIDTH;
		float WINDOW_HEIGHT = Game.WINDOW_HEIGHT;
		
		SceneNode p = createPlayer(root, 100, 100);
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				SceneNode e = createEnemy(root , 100*i+100, 100*j + 100, 10 + M.random()*90);
				enemies.add(e);
			}
		}
		
		createWall(root, WINDOW_WIDTH/2, 32, WINDOW_WIDTH, 64);
		createWall(root, WINDOW_WIDTH/2, WINDOW_HEIGHT-32, WINDOW_WIDTH, 64);
		
		createWall(root, 32, WINDOW_HEIGHT/2, 64, WINDOW_HEIGHT-128);
		createWall(root, WINDOW_WIDTH-32, WINDOW_HEIGHT/2, 64, WINDOW_HEIGHT-128);
		
		createWall(root, WINDOW_WIDTH/2, WINDOW_HEIGHT/2, 200, 200);
	}
	
	private SceneNode createEnemy(SceneNode root, float x, float y, float mass) {
		float width = 16 + 32 * mass/90;
		float height = 16 + 32 * mass/90;
		VertexArray vaoEnemy = VertexArrayUtils.createRectangle(width, height);
		
		SceneNode nEnemy = new SceneNode();
		
		nEnemy.addComponent(new RenderComponent(vaoEnemy, width/2, height/2));
		nEnemy.addComponent(new SeekPointComponent(Game.WINDOW_WIDTH/2, Game.WINDOW_HEIGHT/2, 50) );
	
		nEnemy.addComponent(new CollisionComponent(new PhRectangle(width, height)));
		nEnemy.addComponent(new PhysicsComponent(mass, 0.5f, 0.0005f) );
		
		nEnemy.setX(x);
		nEnemy.setY(y);
		root.addChild(nEnemy);
		
		return nEnemy;
	}
	
	//public Collideable collideEnemy
	private SceneNode createWall(SceneNode root, float x, float y, float width, float height) {
		VertexArray vao = VertexArrayUtils.createRectangle(width, height);
		
		SceneNode n = new SceneNode();
		
		n.addComponent(new RenderComponent(vao, width/2, height/2));
		
		n.addComponent(new CollisionComponent(new PhRectangle(width, height)));
		n.addComponent(new PhysicsComponent(0, 0.5f, 0.5f) );
		//nEnemy.addComponent(new SeekPositionComponent(x, y));
		
		n.setX(x);
		n.setY(y);
		root.addChild(n);
		
		return n;
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
		nCharacter.addComponent(new LineProjectileAbility("dab", 0.2f, 0.1f, 0.08f, 800, 10) );
		
		nCharacter.addComponent(new CharacterComponent() );
		nCharacter.addComponent(new PhysicsComponent(80, 0.2f) );
		nCharacter.addComponent(new CollisionComponent(new PhCircle(16)));
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
