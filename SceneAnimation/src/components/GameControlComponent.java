package components;


import java.util.ArrayList;

import main.CharacterAnimation;
import main.Component;
import main.Game;
import main.SceneNode;
import main.VertexArray;
import main.Window;
import maths.M;
import maths.Vec2;
import physics.CustomCollisionComponent;
import physics.CustomCollisionResolvement;
import physics.NaturalCollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
import physics.PhysicsComponent;
import utils.VertexArrayUtils;

public class GameControlComponent extends Component {

	private Game game;
	
	private float totalTimeElapsed = 0;
	
	
	private float displayTimer = 0;
	
	private SceneNode[] players;
	
	private ArrayList<SceneNode> holes = new ArrayList<>();
	
	
	public GameControlComponent(Game game) {
		this.game = game;
	}
	
	@Override
	protected void start() {
		createInitialObjects();
		
		SceneNodeListenedComponent listenedComp = new SceneNodeListenedComponent();
		listenedComp.assignEndListener(() -> game.restart() );
		players[0].addComponent(listenedComp);
		//this.switchEnemiesSeekComponent();
		
		getRoot().addComponent(new SpawnEnemiesComponent());
	}
	
	public SceneNode getPlayer(int i) {
		return players[i];
	}
	public SceneNode[] getPlayers() {
		return players;
	}

	@Override
	protected void update(float deltaTime) {
		totalTimeElapsed += deltaTime;
		
		
		displayTimer -= deltaTime;
		if (displayTimer <= 0) {
			System.out.println("Time elapsed: "+totalTimeElapsed);
			displayTimer = 1;
		}
		
		if (getEngine().isKeyboardPressed(Window.KEY_ENTER)) {
			game.restart();
		}
	}
	

	
	
	private void createInitialObjects() {
		SceneNode root = getRoot();
		float WINDOW_WIDTH = Game.WINDOW_WIDTH;
		float WINDOW_HEIGHT = Game.WINDOW_HEIGHT;
		
		
		createWall(root, WINDOW_WIDTH/2, 32, WINDOW_WIDTH, 64);
		createWall(root, WINDOW_WIDTH/2, WINDOW_HEIGHT-32, WINDOW_WIDTH, 64);
		
		createWall(root, 32, WINDOW_HEIGHT/2, 64, WINDOW_HEIGHT-128);
		createWall(root, WINDOW_WIDTH-32, WINDOW_HEIGHT/2, 64, WINDOW_HEIGHT-128);
	
		//createWall(root, WINDOW_WIDTH/2, 64+32, 400, 64);
		//createWall(root, WINDOW_WIDTH/2, 64*2+32, 200, 64);
		
		//createWall(root, WINDOW_WIDTH/2, WINDOW_HEIGHT/2, 200, 200);
		
		createHole(root, WINDOW_WIDTH/2, WINDOW_HEIGHT/2, 70);
		
		createHole(root, 164, 164, 100);
		createHole(root, WINDOW_WIDTH-164, 164, 100);
		createHole(root, 164, WINDOW_HEIGHT-164, 100);
		createHole(root, WINDOW_WIDTH-164, WINDOW_HEIGHT-164, 100);

		
		SceneNode p1 = createPlayer(root, WINDOW_WIDTH/2 - 200, WINDOW_HEIGHT/2, false);
		SceneNode p2 = createPlayer(root, WINDOW_WIDTH/2 + 200, WINDOW_HEIGHT/2, true);
		SceneNode[] ps = {p1, p2};
		players = ps;
		
		createBackground(root, p1);
	}
	
	private SceneNode createBackground(SceneNode root, SceneNode target) {
		float r = Game.WINDOW_WIDTH/2 * 2 * 1.6f;
		float w = Game.WINDOW_WIDTH;
		float h = Game.WINDOW_HEIGHT;
		
		float x = Game.WINDOW_WIDTH/2;
		float y = Game.WINDOW_HEIGHT/2;
//		float x = 0;
//		float y = 0;
		
		VertexArray bvao = VertexArrayUtils.createCircle(r, 128);
		//VertexArray bvao = VertexArrayUtils.createRectangle(w, h);
		
		SceneNode b = new SceneNode();
		b.setPosXY(new Vec2(x, y));
		b.setZ(0.9f);
		b.addComponent(new RenderComponent(bvao, 0, 0));
		
		b.addComponent(new PhysicsComponent() );
		b.addComponent(new SeekObjectComponent(target, 6) );
		b.addComponent(new RotationComponent(0.1f) );
		
		root.addChild(b);
		
		return b;
	}
	
	private SceneNode createHole(SceneNode root, float x, float y, float radius) {
		float[] color = {0, 0, 0.3f};
		VertexArray vao = VertexArrayUtils.createCircle(radius, 32, color);
		
		SceneNode h = new SceneNode();
		h.setPosXY(new Vec2(x, y));
		h.setZ(0.1f);
		
		h.addComponent(new RenderComponent(vao, 0, 0));
		
		//add collision functionality to destroy players and enemies in the hole
		PhCircle phShape = new PhCircle(M.max(radius-16, 1) );
		CustomCollisionResolvement ccr = (selfCol, otherCol) -> otherCol.getOwner().destroy();
		
		CustomCollisionComponent ccc = new CustomCollisionComponent("hole", phShape);
		ccc.addResolvement("player", ccr);
		ccc.addResolvement("enemy", ccr);
		
		h.addComponent(new PhysicsComponent());
		h.addComponent(ccc);
		
		holes.add(h);
		root.addChild(h);
		return h;
	}
	
	//public Collideable collideEnemy
	private SceneNode createWall(SceneNode root, float x, float y, float width, float height) {
		VertexArray vao = VertexArrayUtils.createRectangle(width, height);
		
		SceneNode n = new SceneNode();
		
		n.addComponent(new RenderComponent(vao, width/2, height/2));
		
		n.addComponent(new NaturalCollisionComponent(new PhRectangle(width, height)));
		n.addComponent(new PhysicsComponent(0, 0.5f, 0.5f) );
		//nEnemy.addComponent(new SeekPositionComponent(x, y));
		
		n.setX(x);
		n.setY(y);
		root.addChild(n);
		
		return n;
	}
	
	private SceneNode createPlayer(SceneNode root, float x, float y, boolean bot) {
		SceneNode nCharacter = new SceneNode(); //not rendered
		nCharacter.setX(x);
		nCharacter.setY(y);
		
		nCharacter.addComponent(new BoomerangProjectileAbility("wave", 1f, 0.24f*2, 0.2f, 15, 0.55f, 500) );//0.6f
		nCharacter.addComponent(new LineProjectileAbility("dab", 0.2f, 0.15f, 0.08f, 30, 10) );
		nCharacter.addComponent(new DashAbility("dab", 2, 0.5f, 0.2f, 30));//2000));
		
		if (!bot) nCharacter.addComponent(new ControllableComponent() );
		else nCharacter.addComponent(new CharacterAiComponent(holes.toArray(new SceneNode[0])));
		
		nCharacter.addComponent(new CharacterComponent() );
		
		PhCircle phShape = new PhCircle(16);
		nCharacter.addComponent(new NaturalCollisionComponent(phShape));
		nCharacter.addComponent(new CustomCollisionComponent("player", phShape));
		
		nCharacter.addComponent(new PhysicsComponent(80, 0.5f, 0.8f).setDrawVectors(false));
		
		nCharacter.addComponent(new DrawVecComponent() );
		
		
		VertexArray vaoHead = VertexArrayUtils.createRectangle(32, 32);
		VertexArray vaoLupperArm = VertexArrayUtils.createRectangle(32, 16);
		VertexArray vaoRupperArm = VertexArrayUtils.createRectangle(32, 16);
		VertexArray vaoLlowerArm = VertexArrayUtils.createRectangle(32, 12);
		VertexArray vaoRlowerArm = VertexArrayUtils.createRectangle(32, 12);
		VertexArray vaoLhandStar = VertexArrayUtils.createRectangle(8, 8);
		VertexArray vaoRhandStar = VertexArrayUtils.createRectangle(8, 8);
		
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
		
		//ordering
		
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
		
		root.addChild(nCharacter);
		
		return nCharacter;
	}

}
