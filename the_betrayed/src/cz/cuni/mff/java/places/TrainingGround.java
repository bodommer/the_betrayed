package cz.cuni.mff.java.places;

import java.util.Arrays;
import java.util.ResourceBundle;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.main.Controller;
import cz.cuni.mff.java.main.Input;

/**
 * There are only static methods in this class; here a user can spend hero's
 * experience points (XP) to improve his attributes.
 * 
 * @author Andrej
 *
 */
public class TrainingGround {

	/**
	 * This method is called when entering the Training Ground.
	 * 
	 * @param hero
	 * @param scanner
	 */
	public static void levelUp(Hero hero) {
		ResourceBundle rs = Controller.getController().getResourceBundle();
		boolean stay = true;
		System.out.println(rs.getString("trainingWelcome"));
		// if stay == false - leave training ground
		while (stay) {
			printOptions(hero, rs);
			stay = getAnswer(hero, rs);
		}
	}

	/**
	 * Shows options and price for each upgrade.
	 * 
	 * @param hero
	 */
	private static void printOptions(Hero hero, ResourceBundle rs) {
		System.out.printf(rs.getString("trainingXP"), hero.getXP());
		System.out.printf(rs.getString("trainingAttack"), hero.getAttack(),
				hero.getAttack() * 50);
		System.out.printf(rs.getString("trainingDefence"), hero.getDefence(),
				hero.getDefence() * 50);
		System.out.printf(rs.getString("trainingReflexes"), hero.getReflexes(),
				hero.getReflexes() * 50);
		System.out.printf(rs.getString("trainingStrength"), hero.getStrength(),
				hero.getStrength() * 50);
		System.out.printf(rs.getString("trainingHP"), hero.getHP(),
				hero.getHP() * 5);
		System.out.println(rs.getString("trainingPrompt"));
	}

	/**
	 * Gets command from the user and analyses it.
	 * 
	 * @param scanner
	 * @param hero
	 * @return
	 */
	private static boolean getAnswer(Hero hero, ResourceBundle rs) {
		String input = Input.get(Arrays.asList("attack", "defence", "reflexes", "strength", "hp", "exit"));
		switch (input) {
		case "exit":
			return false;
		default:
			if (hero.spendXP(input)) {
				System.out.println(rs.getString("trainingUpgrade"));
			} else {
				System.out.println(rs.getString("trainingNotEnoughXP"));
			}
		}
		return true;
	}
}
