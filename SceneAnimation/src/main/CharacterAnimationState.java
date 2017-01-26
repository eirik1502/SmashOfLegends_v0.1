package main;

public class CharacterAnimationState {

	
	private int nodeCount = 5;
	private int nodeDataCount = 3;
	private int dataCount = nodeDataCount*nodeCount;
	
	private int headi = 0, lUpperArmi = 1, rUpperArmi = 2, lLowerArmi = 3, rLowerArmi = 4;
	private NodeAnimationState[] characterPartsState = new NodeAnimationState[5];
	
	
	public CharacterAnimationState(
			NodeAnimationState headState, 
			NodeAnimationState lUpperArmState,
			NodeAnimationState rUpperArmState,
			NodeAnimationState lLowerArmState,
			NodeAnimationState rLowerArmState) {
		setData(headState, lUpperArmState, rUpperArmState, lLowerArmState, rLowerArmState);
	}
	public CharacterAnimationState(float[] stateData) {
		setData(stateData);
	}
	public CharacterAnimationState() {
		float[] nullData = {0,0,0, 0,0,0, 0,0,0, 0,0,0, 0,0,0};
		setData(nullData);
	}
	
	private void setData(
			NodeAnimationState headState, 
			NodeAnimationState lUpperArmState,
			NodeAnimationState rUpperArmState,
			NodeAnimationState lLowerArmState,
			NodeAnimationState rLowerArmState) {
		characterPartsState[0] = headState;
		characterPartsState[1] = lUpperArmState;
		characterPartsState[2] = rUpperArmState;
		characterPartsState[3] = lLowerArmState;
		characterPartsState[4] = rLowerArmState;
	}
	
	private void setData(NodeAnimationState[] nodeStates) {
		if (nodeStates.length != nodeCount) throw new IllegalStateException("CharacterAnimationState not given nodeStates of right length");
		setData(nodeStates[0], nodeStates[1], nodeStates[2], nodeStates[3], nodeStates[4]);
	}
	
	private void setData(float[] stateData) {
		if (stateData.length != dataCount) throw new IllegalStateException("CharacterAnimationState not given stateData of right length\n"+
				"length given: "+stateData.length);
		NodeAnimationState[] nodeStates = new NodeAnimationState[5];
		for (int i = 0; i < nodeCount; i++) {
			int dataStartIndex = i*nodeDataCount;
			nodeStates[i] = new NodeAnimationState(stateData[dataStartIndex], stateData[dataStartIndex +1], stateData[dataStartIndex +2]);
		}
		setData(nodeStates);
	}
	
	public NodeAnimationState[] getNodeStates() {
		return characterPartsState;
	}
	
	
	public String toString() {
		String result = "[CharacterAnimationState;"+
		" HeadState="+characterPartsState[headi]+
		" LupperArmState="+characterPartsState[lUpperArmi]+
		" RupperArmState="+characterPartsState[rUpperArmi]+
		" LlowerArmState="+characterPartsState[lLowerArmi]+
		" RlowerArmState="+characterPartsState[rLowerArmi]+
		"]";
		return result;
	}
}
