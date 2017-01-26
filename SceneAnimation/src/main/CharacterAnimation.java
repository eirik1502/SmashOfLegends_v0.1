package main;

import java.util.Arrays;

public class CharacterAnimation {

	private int stateDataCount = 5*3;
	
	private CharacterAnimationState[] states;
	
	
	public CharacterAnimation(CharacterAnimationState[] states) {
		setData(states);
	}
	
	public CharacterAnimation(float[] stateData) {
		setData(stateData);
	}
	
	public CharacterAnimationState getState(int index) {
		return states[index];
	}
	
	private void setData(CharacterAnimationState[] states) {
		this.states = states;
	}
	
	private void setData(float[] stateData) {
		if (stateData.length % stateDataCount != 0) throw new IllegalStateException("CharacterAnimation not given stateData of valid length");
		int stateCount = stateData.length / stateDataCount;
		
		CharacterAnimationState[] characterStates = new CharacterAnimationState[stateCount];
		for (int i = 0; i < stateCount; i++) {
			int dataStartIndex = i*stateDataCount;
			characterStates[i] = new CharacterAnimationState(Arrays.copyOfRange(stateData, dataStartIndex, dataStartIndex+stateDataCount));
		}
		setData(characterStates);
	}
	
	public int getStateCount() {
		return states.length;
	}
	
	
	
	public String toString() {
		String result = "[CharacterAnimation;";
		for (int i = 0; i < states.length; i++) {
			result += "\nstate "+i+"= " + states[i];
		}
		result+="\n]";
		return result;
	}
}
