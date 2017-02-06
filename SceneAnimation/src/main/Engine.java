package main;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import maths.M;
import maths.Mat4;
import physics.Collideable;
import physics.CustomCollisionComponent;
import physics.NaturalCollisionComponent;
import physics.PhysicsEngine;
import utils.VertexArrayUtils;

public class Engine {

	private float windowWidth = -1;
	private float windowHeight = -1;
	private String windowTitle = "";
	
	
	private BasicShader shader;
	private Mat4 projectionTransform;
	private SceneNode root;
	
	private PhysicsEngine physicsEngine;
	
	private boolean initialized = false;
	private boolean running = true;
	
	private long lastTime;
	
	private boolean updateLock = false;
	
    private float mouseX;
    private float mouseY;

    private boolean[] keyPressed = new boolean[562];
    private boolean[] mouseButtonPressed = new boolean[16];
	
	
    private int objectsUpdated = 0;
    private int componentsUpdated = 0;
    private float updatePrintTimer = 1;
    
    
    private float fpsTimer = 0;
    
	
	
	public void setWindowSize(float width, float height) {
		this.windowWidth = width;
		this.windowHeight = height;
	}
	public void setWindowTitle(String title) {
		windowTitle = title;
	}
	public void setRoot(SceneNode root) {
		this.root = root;
		root.start(null, this);
	}
	
	public void enableUserInput() {
		if (!initialized) throw new IllegalStateException("Have to init engine before enabling user input");
		
		Window.setMouseButtonCallback( (window, button, action, mods) -> {
			if (action == Window.ACTION_RELEASE) {
				mouseButtonPressed[button] = false;
			}
			else if (action == Window.ACTION_PRESS) {
				mouseButtonPressed[button] = true;
			}
		});
		
		Window.setCursorPosCallback( (window, xpos, ypos) -> {
			mouseX = (float)xpos;
			mouseY = (float)ypos;
		});
		
        Window.setKeyCallback( (window, key, scancode, action, mods) -> {
        	
            if (action == Window.ACTION_RELEASE){
            	keyPressed[key] = false;
            }
            else if (action == Window.ACTION_PRESS){
            	keyPressed[key] = true;
            }
        });
	}
	
	public void init() {
		checkInitState();
		
		Window.initGLFW();
		Window.createWindow(windowWidth, windowHeight, windowTitle);
		Window.initGL();
		
		shader = new BasicShader();
		
		projectionTransform = Mat4.orthographic(0.0f, windowWidth, windowHeight, 0.0f, 1.0f, -1.0f);
		
		physicsEngine = new PhysicsEngine();
		
		initialized = true;
	}
	private void checkInitState() {
		//check engine init state
		String softErrorMsg = "";
		String fatalErrorMsg = "";
		if (windowWidth == -1 || windowHeight == -1) fatalErrorMsg += "Window size not set, ";
		if (root == null) fatalErrorMsg += "root not set, ";
		
		if (windowTitle == "") softErrorMsg += "window title not set, ";
		
		//report state
		if (softErrorMsg.isEmpty() && fatalErrorMsg.isEmpty()) {
			System.out.println("Engine state at init: valid!");
		}
		else {
			System.err.println("Init engine resulted in error:");
			System.err.println("Soft error: \t" + softErrorMsg);
			System.err.println("Fatal Error: \t" + fatalErrorMsg);
		}
	}
	
	/**
	 * start game loop
	 */
	public byte start() {
		if (!initialized) throw new IllegalStateException("Engine is not initialized!");
		
		float updateRate = 1f/60f;
		float timeToUpdate = 0;
		getDeltaTime(); //set reference for deltaTime
		
		while( running ) {
			
			float deltaTime = getDeltaTime();
			timeToUpdate -= deltaTime;
			if (timeToUpdate <= 0) {
				timeToUpdate += updateRate;
				
				System.out.println("delta time: " + deltaTime + " fps: "+updateRate);
				update(updateRate);
				afterUpdate();
				render();
				
				if (Window.windowShouldClosed()) {
					running = false;
				}
			}

		}
		Window.closeWindow();
		return 1;
	}
	
	public void stop() {
		running = false;
	}
	
	private void update(float deltaTime) {
		Window.pollIoEvents();
		
		setUpdateLock(true);
		root.update(deltaTime);
		
		physicsEngine.resolveAll();
		setUpdateLock(false);
		
		updatePrintTimer -= deltaTime;
		if (updatePrintTimer <= 0) {
			updatePrintTimer = 1;
			
			System.out.println("Objects updated: " + objectsUpdated+" Components updated: "+componentsUpdated);
		}
		objectsUpdated = componentsUpdated = 0;
	}
	private void afterUpdate() {
		root.afterUpdate();
	}
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		root.render(Mat4.identity());
		
		Window.swapBuffers();
	}

	
	public void incrementUpdatedObjects() {
		this.objectsUpdated++;
	}
	public void incrementUpdatedComponents() {
		this.componentsUpdated++;
	}
	public boolean isUpdateLock() {
		return updateLock;
	}
	private void setUpdateLock(boolean value) {
		this.updateLock = value;
	}

	private float getDeltaTime() {
		long newTime = System.nanoTime();
		int deltaTime = (int)(newTime - lastTime);
		float deltaTimeF = (float) deltaTime;
		
		lastTime = newTime;
		
		return deltaTimeF/1000000000;
	}
	
	public void renderVertexArray(VertexArray vao, Mat4 transform) {
		shader.bind();
		vao.bind();
		
		Mat4 finalTransform = projectionTransform.multiply(transform);
		shader.setTransform(finalTransform);
		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.indicesCount, GL11.GL_UNSIGNED_BYTE, 0);

		vao.unbind();
		shader.unbind();
	}
	

	public void addNaturalCollideable(NaturalCollisionComponent c) {
		physicsEngine.addCollideable(c);
	}
	public void removeNaturalCollideable(NaturalCollisionComponent c) {
		physicsEngine.removeCollideable(c);
	}
	public void addCustomCollideable(CustomCollisionComponent c) {
		physicsEngine.addCustomCollideable(c);
	}
	public void removeCustomCollideable(CustomCollisionComponent c) {
		physicsEngine.removeCustomCollideable(c);
	}
	
    public float getMouseX() {
        return mouseX;
    }
    public float getMouseY() {
        return mouseY;
    }
    public boolean isMousePressed( int mouseButton ) {
        return mouseButtonPressed[mouseButton];
    }
    public boolean isKeyboardPressed( int keyCode) {
        return keyPressed[keyCode];
    }
	public SceneNode getRoot() {
		return root;
	}
}
