package cz.cuni.mff.betrayed.equipment;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.MyFileReader;

/**
 * This class is a container for the data belonging to a single kind of weapon,
 * defined in the WeaponList (see resources/files/WeaponList for mroe
 * information).
 * 
 * @author Andrej
 *
 */
public class Weapon implements Serializable {

    private final double ATTACK_BASE_MODIFIER = 0.4;
    private final double ATTACK_STRENGTH_MODIFIER = 0.01;

    private static final long serialVersionUID = 5;
    private Random rand = new Random();
    private String code;
    private int attack;
    private int weight;
    private int slashMin;
    private int slashMax;
    private int stabMin;
    private int stabMax;
    private int throwMin;
    private int throwMax;

    /**
     * The constructor. It self-assesses own attributes only from its own name
     * (reads WeaponList file and analyses the data).
     * 
     * @param code
     *            - code of the weapon (see resources/files/WeaponList for weapon
     *            format/weapon codes)
     */
    public Weapon(String code) {
        this.code = code;
        loadData();
    }

    /**
     * Reads the WeaponList and self-assesses attributes.
     * 
     * See resources/files/WeaponList for weapon indexes and more information.
     */
    private void loadData() {
        @SuppressWarnings("resource")
        MyFileReader mfr = new MyFileReader("WeaponList");
        int index = Arrays.asList(mfr.readAndSeparateLine()).indexOf(code);
        for (int i = 0; i < index; i++) {
            mfr.readLine();
        }
        String[] attr = mfr.readAndSeparateLine();
        setAttack(Integer.parseInt(attr[1]));
        setWeight(Integer.parseInt(attr[2]));
        setWeaponRanges(Integer.parseInt(attr[3]), Integer.parseInt(attr[4]), Integer.parseInt(attr[5]),
                Integer.parseInt(attr[6]), Integer.parseInt(attr[7]), Integer.parseInt(attr[8]));
        mfr = null;
    }

    private void setAttack(int i) {
        attack = i;
    }

    public int getAttack() {
        return attack;
    }

    private void setWeight(int i) {
        weight = i;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Returns the power index of the weapon according to hero's stats.
     * 
     * @param strength
     *            - the strength of the hero
     * @return the power index of the weapon, based on weapon's attributes and
     *         hero's strength
     */
    public double getPowerIndex(int strength) {
        return (attack - (ATTACK_BASE_MODIFIER - ATTACK_STRENGTH_MODIFIER * strength) * weight);
    }

    /**
     * Sets minimum and maximum damage that a weapon can cause for various attack
     * types. All the values are stored in the WeaponList, unique for every weapon.
     * 
     * @param slmin
     * @param slmax
     * @param stmin
     * @param stmax
     * @param twmin
     * @param twmax
     */
    private void setWeaponRanges(int slmin, int slmax, int stmin, int stmax, int twmin, int twmax) {
        slashMin = slmin;
        slashMax = slmax;
        stabMin = stmin;
        stabMax = stmax;
        throwMin = twmin;
        throwMax = twmax;
    }

    /**
     * 
     * @return - returns a pseudo-random slash hit damage.
     */
    public int getSlashHit() {
        return rand.nextInt(slashMax - slashMin + 1) + slashMin;
    }

    /**
     * 
     * @return - returns a pseudo-random stab hit damage.
     */
    public int getStabHit() {
        return rand.nextInt(stabMax - stabMin + 1) + stabMin;
    }

    /**
     * 
     * @return - returns a pseudo-random throw weapon hit damage.
     */
    public int getThrowHit() {
        return rand.nextInt(throwMax - throwMin + 1) + throwMin;
    }

    public int getSlashMax() {
        return slashMax;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return Controller.getController().getResourceBundle().getString(code + "N");
    }

    // testing main method
    /*
     * public static void main(String[] args) { Weapon w = new Weapon("cc");
     * System.out.println(w); }
     */
}
