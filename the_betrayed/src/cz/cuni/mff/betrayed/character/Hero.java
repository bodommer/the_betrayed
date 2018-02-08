package cz.cuni.mff.java.character;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.equipment.Weapon;
import cz.cuni.mff.java.main.Controller;

/**
 * Hero is the main concept of the game. It stores all the progress of the game, game data etc.
 * @author Andrej
 *
 */
public class Hero extends Person {

	private static final long serialVersionUID = 2;
	private int score = 0;
	private int coins = 0;
	private int xp = 0;
	private int level = 1;
	private int kills = 0;
	private Set<String> weapons = new HashSet<String>();
	private Set<String> armours = new HashSet<String>();
	private int fights = 0;
	private Set<Integer> opponents = new HashSet<Integer>();

	/**
	 * The constructor.
	 * @param name - random name set by user
	 * @param skillGroup - skill group of the hero, set by user (one of three)
	 */
	public Hero(String name, String skillGroup) {
		setName(name);
		assignSkills(skillGroup);
		addArmour("paper");
		addWeapon("hands");
		
	}

	public int getLevel() {
		return level;
	}

	/**
	 * Called after a won fight in the Arena. Adds coins and XP to user as well as to kills number.
	 */
	public void addKill() {
		kills += 1;
		int x = 2 * 100 + getLife() - getHP();
		int c = level * (new Random().nextInt(100) + 50) + new Random().nextInt(10);
		xp += x;
		coins += c;
		addScore(x);
		addScore(c);
		System.out.printf(Controller.getController().getResourceBundle().getString("playerWon"), c, x);
		setLife(getHP());
		addFight();
	}

	public int getCoins() {
		return coins;
	}

	public int getXP() {
		return xp;
	}

	private void addScore(int value) {
		score += value;
	}

	public int getScore() {
		return score;
	}

	public int getKills() {
		return kills;
	}

	public void addWeapon(String s) {
		weapons.add(s);
		setWeapon(new Weapon(s));
	}

	public void addArmour(String s) {
		armours.add(s);
		setArmour(new Armour(s));
	}

	public Set<String> getWeaponsSet() {
		return weapons;
	}

	public Set<String> getArmourSet() {
		return armours;
	}

	public void spendCoins(int i) {
		coins -= i;
	}

	public void addFight() {
		fights += 1;
		if (fights % 6 == 0) {
			level += 1;
			opponents = new HashSet<Integer>();
		}
	}
	
	public int getFight() {
		return fights;
	}
	
	public boolean foughtBefore(int i) {
		return opponents.contains(i);
	}
	
	public void addOpponent(int i) {
		opponents.add(i);
	}
	/**
	 * Improves a certain skill of the hero.
	 * @param skill - which attribute is to be improved
	 * @return false if there is not enough xp to level up selected skill, true if there is enough
	 * 
	 */
	// returns 
	public boolean spendXP(String skill) {
		switch (skill) {
		case "attack":
			if (attack * 50 <= xp) {
				xp -= attack * 50;
				attack += 1;
			} else {
				return false;
			}
			break;
		case "defence":
			if (defence * 50 <= xp) {
				xp -= defence * 50;
				defence += 1;
			} else {
				return false;
			}
			break;
		case "reflexes":
			if (reflexes * 50 <= xp) {
				xp -= reflexes * 50;
				reflexes += 1;
			} else {
				return false;
			}
			break;
		case "strength":
			if (strength * 50 <= xp) {
				xp -= strength * 50;
				strength += 1;
			} else {
				return false;
			}
			break;
		case "hp":
			if (hp * 5 <= xp) {
				xp -= hp * 5;
				hp += 5;
			} else {
				return false;
			}
			break;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return fights + " " + coins;
	}

	// TODO implement xp-system
}
