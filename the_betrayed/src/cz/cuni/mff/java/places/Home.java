package cz.cuni.mff.java.places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.equipment.Weapon;
import cz.cuni.mff.java.main.Input;
import cz.cuni.mff.java.main.MyFileReader;

/**
 * This is where hero can see what weapons/armours he owns and choose which one
 * he wants to use.
 * 
 * @author Andrej
 *
 */
public class Home {
	Set<String> heroWeapons;
	Set<String> heroArmours;
	HashMap<String, String> weapons;
	HashMap<String, String> armours;
	Scanner scanner;
	Hero hero;

	/**
	 * The constructor.
	 * 
	 * @param hero
	 * @param scanner
	 */
	public Home(Hero hero, Scanner scanner) {
		this.scanner = scanner;
		this.hero = hero;
		weapons = new HashMap<String, String>();
		armours = new HashMap<String, String>();
		heroWeapons = hero.getWeaponsSet();
		heroArmours = hero.getArmourSet();
		visitHome();
	}

	/**
	 * Waits for the input - where user writes what he wants to do.
	 */
	private void visitHome() {
		System.out.println("Welcome home!");
		while (true) {
			System.out
					.println("What do you want to do? See weapons, see armour, change weapon, change armour, or exit?");
			String input = scanner.nextLine();
			switch (input) {
			case "see weapons":
				seeWeapons();
				break;
			case "see armour":
				seeArmour();
				break;
			case "change weapon":
				changeWeapon();
				break;
			case "change armour":
				changeArmour();
				break;
			case "exit":
				return;
			default:
				System.out.println("Invalid command!");
			}
		}
	}

	/**
	 * Shows list of owned weapons with statistics.
	 */
	private void seeWeapons() {
		MyFileReader mfr = new MyFileReader("equipment", "WeaponList");
		int len = mfr.readLine().length();
		for (int i = 0; i < len; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (heroWeapons.contains(data[10])) {
				System.out.printf("Option %s, %s, Attack: %s, Weight: %s, Description: %s\n", data[10], data[0],
						data[1], data[2], data[3]);
				weapons.put(data[10], data[0]);
			}
		}
	}

	/**
	 * Shows list of owned armours with statistics.
	 */
	private void seeArmour() {
		MyFileReader mfr = new MyFileReader("equipment", "ArmourList");
		int len = mfr.readLine().length();
		for (int i = 0; i < len; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (heroArmours.contains(data[4])) {
				System.out.printf("Option %s: %s, Defence: %s, Weight: %s, Description: %s\n", data[4], data[0],
						data[1], data[2], data[3]);
				armours.put(data[4], data[0]);
			}
		}
	}

	/**
	 * Offers a list of weapons that a user can choose from.
	 */
	private void changeWeapon() {
		System.out.printf("Your weapons: %s",
				new ArrayList<String>(heroWeapons).stream().collect(Collectors.joining(", ")));
		System.out.println("Which weapon do you want to use?");
		heroWeapons.add("exit");
		String answer = Input.get(heroWeapons, scanner);
		if (!(answer.equals("exit"))) {
			hero.setWeapon(new Weapon(weapons.get(answer)));
			System.out.printf("You are now using %s.\n", weapons.get(answer));
		}
		heroWeapons.remove("exit");
	}

	/**
	 * Offers a list of armours that a user can choose from.
	 */
	private void changeArmour() {
		System.out.printf("Your armours: %s",
				new ArrayList<String>(heroArmours).stream().collect(Collectors.joining(", ")));
		System.out.println("Which armour do you want to wear?");
		heroArmours.add("exit");
		String answer = Input.get(heroArmours, scanner);
		if (!(answer.equals("exit"))) {
			hero.setArmour(new Armour(armours.get(answer)));
			System.out.printf("You are now wearing %s.\n", armours.get(answer));
		} 
		heroArmours.remove("exit");
	}
}
