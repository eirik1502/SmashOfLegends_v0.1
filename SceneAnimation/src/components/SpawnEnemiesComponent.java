package components;

import java.util.ArrayList;

import main.Component;
import main.Game;
import main.SceneNode;
import main.VertexArray;
import main.Window;
import maths.M;
import physics.CustomCollisionComponent;
import physics.NaturalCollisionComponent;
import physics.PhCircle;
import physics.PhRectangle;
import physics.PhShape;
import physics.PhysicsComponent;
import utils.VertexArrayUtils;

public class SpawnEnemiesComponent extends Component {
	

	private GameControlComponent gameControlComp;
	
	private float timeElapsed = 0;
	
	private SceneNode[] players;
	private ArrayList<SceneNode> enemies = new ArrayList<>();
	
	private float enemyTimer = 0;
	private float bossTimer = 30;
	
	private float enemyTimeElapsed = 0;
	
	private boolean spawnEnemies = true;
	private boolean bossActive = false;
	private boolean preBossActive = false;
	
	
	float[] startX = {100, Game.WINDOW_WIDTH/2, Game.WINDOW_WIDTH-100, Game.WINDOW_WIDTH/2};
	float[] startY = {Game.WINDOW_HEIGHT/2, 100, Game.WINDOW_HEIGHT/2, Game.WINDOW_HEIGHT-100};
	
	
	@Override
	protected void start() {
		gameControlComp = (GameControlComponent) getOwner().getComponent(GameControlComponent.class);
		players = gameControlComp.getPlayers();
	}
	
	@Override
	public void update(float deltaTime) {
		timeElapsed += deltaTime;
		
		bossTimer -= deltaTime;
		if (bossTimer <= 0) {
			if (!preBossActive) {
				preBossActive = true;
				switchEnemiesSeekComponent();
				spawnEnemies = false;
				bossTimer = 5;
			}
			else if (!bossActive) {
				//activate bosses
				
				spawnBoss(timeElapsed);
				bossActive = true;
				bossTimer = 10 * (1+ timeElapsed/120f);
			}
			else {
				preBossActive = false;
				bossActive = false;
				switchEnemiesSeekComponent();
				spawnEnemies = true;
				bossTimer = 40;
			}
		}
		
		if (spawnEnemies) {
			enemyTimeElapsed += deltaTime;
			
			enemyTimer -= deltaTime;
			if (enemyTimer <= 0) {
				enemyTimer = M.random()*2;
				
				spawnEnemies(enemyTimeElapsed);
			}
		}
	}
	
	private void spawnEnemies(float timeElapsed) {
		float WINDOW_WIDTH = Game.WINDOW_WIDTH;
		float WINDOW_HEIGHT = Game.WINDOW_HEIGHT;
		
		float accel0 = 3;
		float accel60 = 6 - accel0;
		float accelVar = 1;
		int spawnCount0 = 1;
		int spawnCount60 = 3+1 - spawnCount0;
		float spawnCountVar = 1;
		
		float ratio60 = timeElapsed/60f;
		float accel = accel0 + ratio60 * accel60 + (1f-M.random()*2f)*accelVar;
		int spawnCount = spawnCount0 + M.floori(ratio60 * spawnCount60) + M.roundi( (1f-M.random()*2f)*spawnCountVar );
		
		float mass = 10 + M.random()*90;
		float radius = 8 + 16 * mass/90;
		
		for (int i = 0; i < spawnCount; i++) {
			int posi = M.floori( M.random()*startX.length );
			createEnemy(getRoot(), startX[posi], startY[posi], radius,  mass, accel, players);
		}
	}
	
	private void spawnBoss(float timeElapsed) {
		float accel0 = 10;
		float accel120 = 10 - accel0;
		float mass0 = 50;
		float mass120 = 100f + M.random()*100f - mass0;
		
		float moonRadius = 12;
		
		float ratio120 = timeElapsed/120f;

		float accel = accel0 + ratio120 * accel120;
		float mass = mass0 + ratio120 * mass120;
		float radius = 8 + 16 * mass/90;
		
		for (int i = 0; i < startX.length; i++) {
			SceneNode e = createEnemy(getRoot(), startX[i], startY[i], radius, mass, accel, players);
			SceneNode m = new SceneNode();
			float[] color = {1, 1, 0};
			VertexArray vao = VertexArrayUtils.createCircle(moonRadius, 12);//, color);
			m.addComponent(new RenderComponent(vao));
			m.addComponent(new OrbitComponent(radius + moonRadius + 4, 6f) );
			e.addChild(m);
		}
	}
	
	private SceneNode createEnemy(SceneNode root, float x, float y, float radius, float mass, float seekAccel, SceneNode...follow) {
		float width = radius * 2;
		float height = radius * 2;
		
		VertexArray vaoEnemy;
		PhShape phShape;
		RenderComponent renderComp;
		if (M.random() < 0.5f) {
			if (M.random() < 0.5f)
				vaoEnemy = VertexArrayUtils.createCircle2c(width/2, 16);
			else
				vaoEnemy = VertexArrayUtils.createCircle(width/2, 16);
			phShape = new PhCircle(width/2);
			renderComp = new RenderComponent(vaoEnemy, 0, 0);
		}
		else {
			vaoEnemy = VertexArrayUtils.createRectangle(width, height);
			phShape = new PhRectangle(width, height);
			renderComp = new RenderComponent(vaoEnemy, width/2, height/2);
		}
		
		SceneNode nEnemy = new SceneNode();
		nEnemy.setX(x);
		nEnemy.setY(y);
		
		nEnemy.addComponent(renderComp);
		nEnemy.addComponent(new SeekObjectComponent(seekAccel, follow) );
		
		nEnemy.addComponent(new NaturalCollisionComponent(phShape ));
		nEnemy.addComponent(new CustomCollisionComponent("enemy", phShape));
		
		nEnemy.addComponent(new PhysicsComponent(mass, 0.5f, 0.1f).setDrawVectors(false) );
		
		nEnemy.addComponent(new DrawVecComponent() );
		
		SceneNodeListenedComponent listenedComp = new SceneNodeListenedComponent();
		nEnemy.addComponent(listenedComp);
		
		listenedComp.assignEndListener(()->enemies.remove(nEnemy) );
	
		root.addChild(nEnemy);
		enemies.add(nEnemy);
		
		return nEnemy;
	}
	
//	private void updateSwitchEnemiesSeekComponent(float deltaTime) {
//		float timeBetweenSwitch = 0.3f;
//		
//		timeSinceEnemySeekSwitch += deltaTime;
//		if (getEngine().isKeyboardPressed(Window.KEY_E) && timeSinceEnemySeekSwitch > timeBetweenSwitch) {
//			switchEnemiesSeekComponent();
//			timeSinceEnemySeekSwitch = 0;
//		}
//	}
	
	private void setEnemiesSeekComponent(boolean value) {
		for (SceneNode e : enemies) {
			SeekObjectComponent spc = (SeekObjectComponent) e.getComponent(SeekObjectComponent.class);
			spc.setActive(value);
		}
	}
	private void switchEnemiesSeekComponent() {
		for (SceneNode e : enemies) {
			SeekObjectComponent spc = (SeekObjectComponent) e.getComponent(SeekObjectComponent.class);
			spc.setActive(!spc.isActive());
		}
	}
}
