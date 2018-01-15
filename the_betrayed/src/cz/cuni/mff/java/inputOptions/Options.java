package cz.cuni.mff.java.inputOptions;

/**
 * This enum contains various menu options for various section of the game - in
 * one place.
 * 
 * @author Andrej
 *
 */
public enum Options {
	MAIN_MENU("newGame", "load", "help", "changeLanguage", "exit"), 
	GAME_MENU("fight", "visitArmoury", "visitWeaponsShop", "levelUp", "visitHome", "save", "menu", "help", "exit"), 
	SKILLSET("attack", "defence", "reflexes"), 
	YES_NO("yes", "no"), 
	LANGUAGES("en", "sk", "exit"), 
	HOME("seeWeapons", "seeArmour",	"changeWeapon", "changeArmour",	"exit"), 
	TRAINING("attack", "defence", "reflexes", "strength", "hp", "exit"),
	HANDS_FIGHT("slap", "punch"),
	WEAPON_FIGHT("slash", "stab", "throwWeapon");

	private final String[] list;

	private Options(String... options) {
		this.list = options;
	}

	public String[] getOptions() {
		return list;
	}
}
