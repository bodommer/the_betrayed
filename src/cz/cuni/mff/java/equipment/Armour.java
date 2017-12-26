package cz.cuni.mff.java.equipment;

import java.io.File;
import java.util.Arrays;

import cz.cuni.mff.java.main.MyFileReader;

public class Armour {
	
	private String name;
	private int defence;
	private int weight;
	private String description;
	
	public Armour(String name, int defence, int weight, String description) {
		this.name = name;
		this.defence = defence;
		this.weight = weight;
		this.description = description;
	}
	
	public Armour(String name) {
		this.name = name;
		loadData();
	}
	
	private void loadData() {
		MyFileReader mfr = new MyFileReader("equipment", "ArmourList");
		int index = Arrays.asList(mfr.readAndSeparateLine()).indexOf(name);
		for(int i=0; i < index; i++) {
			mfr.readLine();
		}
		String[] attr = mfr.readAndSeparateLine();
		setDefence(Integer.parseInt(attr[1]));
		setWeight(Integer.parseInt(attr[2]));
		setDescription(attr[3]);
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
	
	public void setDescription(String i) {
		description = i;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getDefIndex(int strength) {
		return (defence - (0.4 - 0.01 * strength) * weight);
	}
	
	
	public static void main(String[] args) {
		File currentDirectory = new File(new File("cz/cuni/mff/java/equipment/ArmourList").getAbsolutePath());
		System.out.println(currentDirectory.toString());
	}
	
	@Override
	public String toString() {
		return name;
	}
}

