package cz.cuni.mff.java.equipment;

import java.util.Arrays;
import java.util.Random;

import cz.cuni.mff.java.main.MyFileReader;

public class Weapon {

	private String name;
	private int attack;
	private int weight;
	private int slashMin;
	private int slashMax;
	private int stabMin;
	private int stabMax;
	private int throwMin;
	private int throwMax;
	private String description;

	public Weapon(String name) {
		this.name = name;
		loadData();
	}

	private void loadData() {
		MyFileReader mfr = new MyFileReader("equipment", "WeaponList");
		int index = Arrays.asList(mfr.readAndSeparateLine()).indexOf(name);
		for (int i = 0; i < index; i++) {
			mfr.readLine();
		}
		String[] attr = mfr.readAndSeparateLine();
		setAttack(Integer.parseInt(attr[1]));
		setWeight(Integer.parseInt(attr[2]));
		setDescription(attr[3]);
		setWeaponRanges(Integer.parseInt(attr[4]), Integer.parseInt(attr[5]), Integer.parseInt(attr[6]),
				Integer.parseInt(attr[7]), Integer.parseInt(attr[8]), Integer.parseInt(attr[9]));
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

	private void setDescription(String s) {
		description = s;
	}

	public String getDescription() {
		return description;
	}

	public double getPowerIndex(int strength) {
		return (attack - (0.4 - 0.01 * strength) * weight);
	}

	private void setWeaponRanges(int slmin, int slmax, int stmin, int stmax, int twmin, int twmax) {
		slashMin = slmin;
		slashMax = slmax;
		stabMin = stmin;
		stabMax = stmax;
		throwMin = twmin;
		throwMax = twmax;
	}

	public int getSlashHit() {
		return new Random().nextInt(slashMax - slashMin + 1) + slashMin;
	}

	public int getStabHit() {
		return new Random().nextInt(stabMax - stabMin + 1) + stabMin;
	}

	public int getThrowHit() {
		return new Random().nextInt(throwMax - throwMin + 1) + throwMin;
	}
	
	public int getSlashMax() {
		return slashMax; 	
	}

	@Override
	public String toString() {
		return name;
	}

	public static void main(String[] args) {
		Weapon w = new Weapon("cc");
		System.out.println(w);
	}
}
