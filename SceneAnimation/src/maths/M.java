package maths;

import java.lang.Math;



public class M {
	
	public static final float PI = (float)Math.PI;
	
	
	public static float min(float x, float y) {
		return Math.min(x, y);
	}
	public static float max(float x, float y) {
		return Math.max(x, y);
	}
	public static float abs(float x) {
		return Math.abs(x);
	}
	public static float clamp(float x, float min, float max) {
		return min( max(x, min), max );
	}
	
	public static float random() {
		return (float)Math.random();
	}

	public static float cos(float angle) {
		return (float)Math.cos((double) angle);
	}
	public static float sin(float angle) {
		return (float)Math.sin((double) angle);
	}
	public static float tan(float angle) {
		return (float)Math.tan((double) angle);
	}
	
	public static float atan(float n) {
		return (float)Math.atan((double)n);
	}
	public static float atan2(float n1, float n2) {
		return (float) Math.atan2(n1, n2);
	}
	
	
	public static float pow2(float n) {
		return n*n;
	}
	public static float pow(float n, int p) {
		return (float)( Math.pow((float)(n), p) );
	}
	
	public static float sqrt(float n) {
		return (float)Math.sqrt((double)n);
	}
	
}
