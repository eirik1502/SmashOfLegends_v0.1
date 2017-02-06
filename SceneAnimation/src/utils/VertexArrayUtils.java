package utils;

import java.util.ArrayList;
import java.util.Arrays;

import main.VertexArray;
import maths.M;


public class VertexArrayUtils {

	private VertexArrayUtils() {
		
	}
	
	
	public static VertexArray createRectangle(float width, float height) {
		
		float[] vertices = new float[] {
			0.0f, 0.0f, 0.0f,
			0.0f, height, 0.0f,
			width, height, 0.0f,
			width, 0.0f, 0.0f
		};
			
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		float sr = 1;//(float)Math.random(); //to variate the spread around 1
		float[] colors = new float[] { //texture coordinates
			sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random(),
			sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random(),
			sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random(),
			sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random()
		};
		
		return new VertexArray( vertices, colors, indices );
	}
	
	public static VertexArray createCircle(float radius, int sides) {
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Byte> indices = new ArrayList<>();
		
		float anglesPerSide = 2*M.PI / sides;
		
		vertices.add(0.0f);//center point
		vertices.add(0.0f);
		vertices.add(0.0f);
		for (int i = 0; i < sides; i++) {
			float angle = anglesPerSide * i;
			
			vertices.add(radius * M.cos(angle));
			vertices.add(radius * M.sin(angle));
			vertices.add(0.0f);
			
			indices.add((byte)(0		));
			indices.add((byte)(i + 1	));
			if (i == sides-1) {
				indices.add((byte)(1	));
			}
			else {
				indices.add((byte)(i + 2));
			}
		}
				
		
		float sr = 1;//(float)Math.random(); //to variate the spread around 1
		
		float[] colors = new float[(sides+1) * 3];
		colors[0] = sr*(float)Math.random();
		colors[1] = sr*(float)Math.random();
		colors[2] = sr*(float)Math.random();
		
		//float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
		
		for (int i = 1; i < sides+1; i++) {
			float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
			
			colors[i*3 + 0] = boundaryColor[0];
			colors[i*3 + 1] = boundaryColor[1];
			colors[i*3 + 2] = boundaryColor[2];
		}
		
		float[] verts = new float[vertices.size()];
		byte[] inds = new byte[indices.size()];
		for (int i = 0; i < verts.length; i++) {
			verts[i] = vertices.get(i);
		}
		for (int i = 0; i < inds.length; i++) {
			inds[i] = indices.get(i);
		}
		
		return new VertexArray( verts, colors, inds );
	}
	
	public static VertexArray createCircle(float radius, int sides, float[] color) {
		if (color.length != 3) throw new IllegalStateException("illegal color data given");
		
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Byte> indices = new ArrayList<>();
		
		float anglesPerSide = 2*M.PI / sides;
		
		vertices.add(0.0f);//center point
		vertices.add(0.0f);
		vertices.add(0.0f);
		for (int i = 0; i < sides; i++) {
			float angle = anglesPerSide * i;
			
			vertices.add(radius * M.cos(angle));
			vertices.add(radius * M.sin(angle));
			vertices.add(0.0f);
			
			indices.add((byte)(0		));
			indices.add((byte)(i + 1	));
			if (i == sides-1) {
				indices.add((byte)(1	));
			}
			else {
				indices.add((byte)(i + 2));
			}
		}
				
		
		float sr = 1;//(float)Math.random(); //to variate the spread around 1
		
		float[] colors = new float[(sides+1) * 3];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = color[i%3];
		}
		
//		colors[0] = sr*(float)Math.random();
//		colors[1] = sr*(float)Math.random();
//		colors[2] = sr*(float)Math.random();
//		
//		//float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
//		
//		for (int i = 1; i < sides+1; i++) {
//			float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
//			
//			colors[i*3 + 0] = boundaryColor[0];
//			colors[i*3 + 1] = boundaryColor[1];
//			colors[i*3 + 2] = boundaryColor[2];
//		}
		
		float[] verts = new float[vertices.size()];
		byte[] inds = new byte[indices.size()];
		for (int i = 0; i < verts.length; i++) {
			verts[i] = vertices.get(i);
		}
		for (int i = 0; i < inds.length; i++) {
			inds[i] = indices.get(i);
		}
		
		return new VertexArray( verts, colors, inds );
	}
	
	public static VertexArray createCircle2c(float radius, int sides) {
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Byte> indices = new ArrayList<>();
		
		float anglesPerSide = 2*M.PI / sides;
		
		vertices.add(0.0f);//center point
		vertices.add(0.0f);
		vertices.add(0.0f);
		for (int i = 0; i < sides; i++) {
			float angle = anglesPerSide * i;
			
			vertices.add(radius * M.cos(angle));
			vertices.add(radius * M.sin(angle));
			vertices.add(0.0f);
			
			indices.add((byte)(0		));
			indices.add((byte)(i + 1	));
			if (i == sides-1) {
				indices.add((byte)(1	));
			}
			else {
				indices.add((byte)(i + 2));
			}
		}
				
		
		float sr = 1;//(float)Math.random(); //to variate the spread around 1
		
		float[] colors = new float[(sides+1) * 3];
		colors[0] = sr*(float)Math.random();
		colors[1] = sr*(float)Math.random();
		colors[2] = sr*(float)Math.random();
		
		float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
		
		for (int i = 1; i < sides+1; i++) {
			//float[] boundaryColor = {sr*(float)Math.random(), sr*(float)Math.random(), sr*(float)Math.random() };
			
			colors[i*3 + 0] = boundaryColor[0];
			colors[i*3 + 1] = boundaryColor[1];
			colors[i*3 + 2] = boundaryColor[2];
		}
		
		float[] verts = new float[vertices.size()];
		byte[] inds = new byte[indices.size()];
		for (int i = 0; i < verts.length; i++) {
			verts[i] = vertices.get(i);
		}
		for (int i = 0; i < inds.length; i++) {
			inds[i] = indices.get(i);
		}
		
		return new VertexArray( verts, colors, inds );
	}
}
