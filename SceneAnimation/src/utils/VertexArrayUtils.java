package utils;

import main.VertexArray;


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
}
