package cz.cuni.mff.betrayed.places;

import java.util.ResourceBundle;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.inputOptions.Options;
import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.Input;

/**
 * There are only static methods in this class; here a user can spend hero's
 * experience points (XP) to improve his attributes.
 * 
 * @author Andrej
 *
 */
public class TrainingGround {

	private final static int SKILL_COST_MODIFIER = 50;
	private final static int HP_COST_MODIFIER = 5;

	/**
	 * This method is called when entering the Training Ground.
	 * 
	 * @param hero
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
		System.out.printf(rs.getString("trainingAttack"), hero.getAttack(), hero.getAttack() * SKILL_COST_MODIFIER);
		System.out.printf(rs.getString("trainingDefence"), hero.getDefence(), hero.getDefence() * SKILL_COST_MODIFIER);
		System.out.printf(rs.getString("trainingReflexes"), hero.getReflexes(),
				hero.getReflexes() * SKILL_COST_MODIFIER);
		System.out.printf(rs.getString("trainingStrength"), hero.getStrength(),
				hero.getStrength() * SKILL_COST_MODIFIER);
		System.out.printf(rs.getString("trainingHP"), hero.getHP(), hero.getHP() * HP_COST_MODIFIER);
		System.out.println(rs.getString("trainingPrompt"));
	}

	/**
	 * Gets command from the user and analyses it.
	 * 
	 * @param hero
	 * @return true for 'skill was levelled up' and therefore the loop of the
	 *         training ground dialog continues (possibly for other skill
	 *         improvements), false is returned when the user selected 'exit' and
	 *         wants to leave this place
	 */
	private static boolean getAnswer(Hero hero, ResourceBundle rs) {
		String input = Input.showOptionsAndGetInput(Options.TRAINING);
		if (input.equals("exit")) {
			return false;
		} else {
			if (hero.spendXP(input)) {
				System.out.println(rs.getString("trainingUpgrade"));
			} else {
				System.out.println(rs.getString("trainingNotEnoughXP"));
			}
			return true;
		}
	}
}
