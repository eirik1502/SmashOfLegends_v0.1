package main;

import components.CharacterAbilityComponent;

public interface AbilityOwner {

	public void onAbilityEnd(CharacterAbilityComponent ability);
}
