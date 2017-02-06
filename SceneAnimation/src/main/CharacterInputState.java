package main;


/**
 * key state: right, left, up, down, ab1, ab2, ab3, ab4
 * @author Eirik
 *
 */
public class CharacterInputState {

	
	private static final int inputCount = 8;
	
	private float mouseX;
	private float mouseY;
	
	private static final int moveLeft=0, moveRight=1, moveUp=2, moveDown=3, ability1=4, ability2=5, ability3=6, ability4=7;
	private boolean[] keys = new boolean[inputCount];

	
	public CharacterInputState(float mouseX, float mouseY, boolean... keys) {
		if (keys.length != inputCount) throw new IllegalArgumentException("Wrong number of inputs");
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.keys = keys;
	}
	public CharacterInputState() {
		this(0, 0,   false, false, false, false, false, false, false, false);
	}
	
	public float getMouseX() {
		return mouseX;
	}
	public float getMouseY() {
		return mouseY;
	}
	public boolean getMoveLeft() {
		return keys[moveLeft];
	}
	public boolean getMoveRight() {
		return keys[moveRight];
	}
	public boolean getMoveUp() {
		return keys[moveUp];
	}
	public boolean getMoveDown() {
		return keys[moveDown];
	}
	public boolean getAbility1() {
		return keys[ability1];
	}
	public boolean getAbility2() {
		return keys[ability2];
	}
	public boolean getAbility3() {
		return keys[ability3];
	}
	public boolean getAbility4() {
		return keys[ability4];
	}
	
	/**
	 * move number i, 0-indexd
	 * @param i
	 * @return
	 */
	public boolean getMove(int i) {
		if (i < 0 || i > 3) throw new IllegalStateException("Illegal move number");
		int abilityIndexStart = 0;
		return keys[abilityIndexStart+i];
	}
	
	/**
	 * abilitynumber i, 0-indexed
	 * @param i
	 * @return
	 */
	public boolean getAbility(int i) {
		if (i < 0 || i > 3) throw new IllegalStateException("Illegal ability number");
		int abilityIndexStart = 4;
		return keys[abilityIndexStart+i];
	}
}
