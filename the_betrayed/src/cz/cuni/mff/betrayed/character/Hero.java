package cz.cuni.mff.betrayed.character;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cz.cuni.mff.betrayed.equipment.Armour;
import cz.cuni.mff.betrayed.equipment.Weapon;
import cz.cuni.mff.betrayed.main.Controller;

/**
 * Hero is the main concept of the game. It stores all the progress of the game,
 * game data etc.
 * 
 * @author Andrej
 *
 */
public class Hero extends Person {

    private final int BONUS_MODIFIER = 100;
    private final int RANDOM_INT = 10;
    private final int XP_MODIFIER = 2;
    private final int COIN_BONUS = 50;
    private final int SKILL_COST_MODIFIER = 50;
    private final int HP_COST_MODIFIER = 5;
    private final int HP_INCREASE_AMOUNT = 5;
    private final int FIGHTS_PER_LEVEL = 6;

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
    private Random rand = new Random();

    /**
     * The constructor.
     * 
     * @param name
     *            - random name set by user
     * @param skillGroup
     *            - skill group of the hero, set by user (one of three)
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
     * Called after a won fight in the Arena. Adds coins and XP to user as well as
     * to kills number.
     */
    public void addKill() {
        kills++;
        int x = XP_MODIFIER * BONUS_MODIFIER + getLife() - getHP();
        int c = level * (rand.nextInt(BONUS_MODIFIER) + COIN_BONUS) + rand.nextInt(RANDOM_INT);
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

    /**
     * Adds a weapon to the set of all weapons that hero owns + hero wields the
     * weapon.
     * 
     * @param code
     *            - the code of the weapon assigned in the WeaponList (see game
     *            resources)
     */
    public void addWeapon(String code) {
        weapons.add(code);
        setWeapon(new Weapon(code));
    }

    /**
     * Adds an armour to the set of all armours that hero owns + hero uses the
     * armour.
     * 
     * @param code
     *            - the code of the armour assigned in the ArmourList (see game
     *            resources)
     */
    public void addArmour(String code) {
        armours.add(code);
        setArmour(new Armour(code));
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

    /**
     * Increases the amount of won fights (after a successful battle) and checks, if
     * hero is not eligible for the next level.
     */
    public void addFight() {
        fights += 1;
        if (fights % FIGHTS_PER_LEVEL == 0) {
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
     * 
     * @param skill
     *            - which attribute is to be improved
     * @return false if there is not enough xp to level up selected skill, true if
     *         there is enough
     * 
     */
    // returns
    public boolean spendXP(String skill) {
        switch (skill) {
            case "attack":
                if (attack * SKILL_COST_MODIFIER <= xp) {
                    xp -= attack * SKILL_COST_MODIFIER;
                    attack++;
                } else {
                    return false;
                }
                break;
            case "defence":
                if (defence * SKILL_COST_MODIFIER <= xp) {
                    xp -= defence * SKILL_COST_MODIFIER;
                    defence++;
                } else {
                    return false;
                }
                break;
            case "reflexes":
                if (reflexes * SKILL_COST_MODIFIER <= xp) {
                    xp -= reflexes * SKILL_COST_MODIFIER;
                    reflexes++;
                } else {
                    return false;
                }
                break;
            case "strength":
                if (strength * SKILL_COST_MODIFIER <= xp) {
                    xp -= strength * SKILL_COST_MODIFIER;
                    strength++;
                } else {
                    return false;
                }
                break;
            case "hp":
                if (hp * HP_COST_MODIFIER <= xp) {
                    xp -= hp * HP_COST_MODIFIER;
                    hp += HP_INCREASE_AMOUNT;
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
