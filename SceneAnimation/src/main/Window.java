package main;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;


public class Window {

	
	public static final int ACTION_PRESS = GLFW_PRESS;
	public static final int ACTION_RELEASE = GLFW_RELEASE;
	
	public static final int KEY_A = GLFW_KEY_A;
	public static final int KEY_W = GLFW_KEY_W;
	public static final int KEY_D = GLFW_KEY_D;
	public static final int KEY_S = GLFW_KEY_S;
	public static final int KEY_Q = GLFW_KEY_Q;
	public static final int KEY_E = GLFW_KEY_E;
	
	public static final int KEY_ENTER = GLFW_KEY_ENTER;
	
	public static final int MOUSE_BUTTON_1 = GLFW_MOUSE_BUTTON_1;
	public static final int MOUSE_BUTTON_2 = GLFW_MOUSE_BUTTON_2;
	public static final int MOUSE_BUTTON_3 = GLFW_MOUSE_BUTTON_3;
	
	
	private static long window = -1;
	
	private static boolean GLinitialized = false;
	private static boolean GLFWinitialized = false;
	
	
	public static void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public static void pollIoEvents() {
		glfwPollEvents();
		
	}
	
	public static boolean windowShouldClosed() {
		return glfwWindowShouldClose(window);
	}
	
	public static void initGLFW() {
		if (!glfwInit()) {
			throw new IllegalStateException("Could not initialize GLFW!");
		}
		
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        
        GLFWinitialized = true;
	}
	
	public static void initGL() {
		if (window == -1) throw new IllegalStateException("cannot init OpenGL before a window is created");
		
		GL.createCapabilities(); //get opengl context
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
 		glEnable(GL_DEPTH_TEST);
		//glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		//print version
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		
		GLinitialized = true;
	}
	
	/**
	 * creates a GLFW window with an openGL context
	 * @param width
	 * @param height
	 * @param title
	 */
	static public void createWindow(float width, float height, String title) {
		int iwidth = (int)width;
		int iheight = (int)height;
		
		if (!GLFWinitialized) throw new IllegalStateException("Cannot create window because GLFW is not initialized");
		
        window =  glfwCreateWindow(iwidth, iheight, title, NULL, NULL);
		
		if (window == NULL) {
			throw new IllegalStateException("Could not create GLFW window!");
		}
		
		// Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (vidmode.width() - iwidth) / 2,
            (vidmode.height() - iheight) / 2
        );
        
		glfwMakeContextCurrent(window); //hmmm
        glfwSwapInterval(1);// Enable v-sync
        
        
        showGlfwWindow(window);		
        glfwSwapBuffers(window);
	}
	
	static public void closeWindow() {
		glfwDestroyWindow(window);
	}

	static private void showGlfwWindow(long window) {
		glfwShowWindow(window);
	}
	
	static public void setMouseButtonCallback(GLFWMouseButtonCallbackI call) {
		glfwSetMouseButtonCallback( window, call);
	}
	
	static public void setCursorPosCallback(GLFWCursorPosCallbackI call) {
		glfwSetCursorPosCallback( window, call);
	}
	
	static public void setKeyCallback(GLFWKeyCallbackI call) {
		glfwSetKeyCallback( window, call);
	}
	
}
