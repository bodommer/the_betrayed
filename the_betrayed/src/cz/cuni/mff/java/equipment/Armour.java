package cz.cuni.mff.java.equipment;

import java.io.Serializable;
import java.util.Arrays;

import cz.cuni.mff.java.main.Controller;
import cz.cuni.mff.java.main.MyFileReader;

/**
 * This class contains a collection of data belonging to a unique Armour.
 * 
 * @author Andrej
 *
 */
public class Armour implements Serializable {

	private static final long serialVersionUID = 4;
	private String code;
	private int defence;
	private int weight;

	/**
	 * The constructor.
	 * 
	 * @param name
	 *            - a name of the weapon
	 * @param defence
	 *            - a defending capability of the armour
	 * @param weight
	 *            - weight of it
	 * @param description
	 *            - a short description of the armour
	 */
	public Armour(String code, int defence, int weight, String description) {
		this.code = code;
		this.defence = defence;
		this.weight = weight;
	}

	/**
	 * Commonly used constructor - it loads its data just by setting the name of it.
	 * 
	 * @param name
	 *            - name of the armour
	 */
	public Armour(String code) {
		this.code = code;
		loadData();
	}

	/**
	 * Used by the constructor with name parameter only. Loads its own data from a
	 * file and sets it to itself.
	 */
	private void loadData() {
		MyFileReader mfr = new MyFileReader("ArmourList");
		int index = Arrays.asList(mfr.readAndSeparateLine()).indexOf(code);
		for (int i = 0; i < index; i++) {
			mfr.readLine();
		}
		String[] attr = mfr.readAndSeparateLine();
		setDefence(Integer.parseInt(attr[1]));
		setWeight(Integer.parseInt(attr[2]));
		mfr = null;
	}

	public void setDefence(int i) {
		defence = i;
	}

	public int getDefence() {
		return defence;
	}

	public void setWeight(int i) {
		weight = i;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * Defence index of the armour applied for the hero.
	 * @param strength - strength of the hero
	 * @return a defence index
	 */
	public double getDefIndex(int strength) {
		return (defence - (0.4 - 0.01 * strength) * weight);
	}

	//testing main
	/*
	public static void main(String[] args) {
		File currentDirectory = new File(new File("cz/cuni/mff/java/equipment/ArmourList").getAbsolutePath());
		System.out.println(currentDirectory.toString());
	}*/

	@Override
	public String toString() {
		return Controller.getController().getResourceBundle().getString(code+"N");
	}
}
