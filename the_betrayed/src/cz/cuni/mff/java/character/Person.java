package cz.cuni.mff.java.character;

import java.io.Serializable;

import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.equipment.Weapon;

/**
 * Abstract model for the character. Opponent and Hero are derived from this.
 * @author Andrej
 *
 */
public abstract class Person implements Serializable {
	
	private static final long serialVersionUID = 1;
	private String name;
	protected int attack;
	protected int defence;
	protected int reflexes;
	protected int strength;
	/**
	 * max Life of the character.
	 */
	protected int hp;
	/**
	 * Current value of life. Used in Fights.
	 * Resets before every fight to maximum.
	 */
	private int life;
	private Armour armour;
	private Weapon weapon;
	
	/**
	 * Assigns skills according to the skill group set by the user.
	 * @param sg
	 */
	protected void assignSkills(String sg) {
		int[] at = {3, 1, 2, 2, 20};
		int[] df = {1, 3, 2, 1, 25}; 
		int[] rf = {2, 2, 3, 1, 15}; 
		int[] skills;
		switch (sg) {
			case "attack":
				skills = at;
				break;
			case "defence": 
				skills = df;
				break;
			case "reflexes":
				skills = rf;
				break;
			default: 
				skills = rf;
		}
		setAttack(skills[0]);
		setDefence(skills[1]);
		setReflexes(skills[2]);
		setStrength(skills[3]);
		setHP(skills[4]);
		setLife(getHP());
	}
	
	protected void setName(String s) {
		name = s;
	}
	
	public String getName() {
		return name;
	}
	
	protected void setAttack(int i) {
		attack = i;
	}
	
	public int getAttack() {
		return attack;
	}
	
	protected void setDefence(int i) {
		defence = i;
	}
	
	public int getDefence() {
		return defence;
	}
	
	protected void setReflexes(int i) {
		reflexes = i;
	}

	public int getReflexes() {
		return reflexes;
	}
	
	protected void setStrength(int i) {
		strength = i;
	}
	
	public int getStrength() {
		return strength;
	}
	
	protected void setHP(int i) {
		hp = i;
	}

	public int getHP() {
		return hp;
	}
	
	protected void setLife(int i) {
		life = i;
	}
	
	public int getLife() {
		return life;
	}
	
	/**
	 * Causes life damage to the character. Then evaluates, if the character is still alive.
	 * @param damage - how much life to take
	 * @return if the character is dead, returns true - ends fight, otherwise returns false.
	 */
	public boolean damage(int damage) {
		life -= damage;
		return (life <= 0 ? true : false);
		
	}
	
	public void setArmour(Armour ar) {
		armour = ar;
	}
	
	public Armour getArmour() {
		return armour;
	}
	
	public void setWeapon(Weapon wp) {
		weapon = wp;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
}
