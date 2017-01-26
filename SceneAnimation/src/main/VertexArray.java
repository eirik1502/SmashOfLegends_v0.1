package main;
import org.lwjgl.opengl.*;

import utils.BufferUtils;


public class VertexArray {

	public static int VERTEX_LOCATION = 0;
	public static int COLORS_LOCATION = 1;

	int vaoId;
	int verticesId;
	int colorsId;
	int indicesId;
	
	int indicesCount;
	
	public VertexArray(float[] vertices, float[] colors, byte[] indices) {
		indicesCount = indices.length;
		createVertexArray(vertices, colors, indices);
		
	}
	
	public int getIndicesCount() {
		return indicesCount;
	}
	
	public void bind() {
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
	}
	public void unbind() {
		GL30.glBindVertexArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	private int createVertexArray(float[] vertices, float[] colors, byte[] indices) {
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		verticesId = createVertexBuffer(VERTEX_LOCATION, 3, vertices);
		colorsId = createVertexBuffer(COLORS_LOCATION, 3, colors);
		indicesId = createIndicesBuffer(indices);
		
		GL30.glBindVertexArray(0);
		
		return vaoId;
	}
	private int createVertexBuffer(int attribIndex, int size, float[] data) {
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribIndex, size, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(attribIndex);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		return vboId;
	}
	
	private int createIndicesBuffer(byte[] indices) {
		int indicesId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		return indicesId;
	}
}
