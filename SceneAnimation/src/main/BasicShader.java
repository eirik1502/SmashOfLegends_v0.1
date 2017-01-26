package main;

import maths.Mat4;

public class BasicShader extends Shader{

	private static String UNIFORM_TRANSFORM = "transform";
	
	private final int transformLocation;
	
	private static String vertexPath = "shaders/shader.vert";
	private static String fragPath = "shaders/shader.frag";
	
	
	public BasicShader() {
		super(vertexPath, fragPath);
		
		transformLocation = super.getUniformLocation(UNIFORM_TRANSFORM);
	}
	
	
	public void setTransform(Mat4 transform) {
		super.setUniformMat4f(transformLocation, transform);
	}

}
