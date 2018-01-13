package cz.cuni.mff.java.places;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.main.MyFileReader;

/**
 * This is where a user can buy new Armour - also, this method contains only
 * static methods.
 * 
 * @author Andrej
 *
 */
public class Armoury {

	/**
	 * Read ArmourList to see what options there are for purchase.
	 * 
	 * @param hero
	 * @param scanner
	 */
	public static void shop(Hero hero, Scanner scanner) {
		System.out.println("Welcome to the shop! ");
		HashMap<String, Integer> attr = new HashMap<String, Integer>();

		// show options
		MyFileReader mfr = new MyFileReader("equipment", "ArmourList");
		String[] options = mfr.readAndSeparateLine();
		System.out.println("Here is our inventory of armour, which you do not own yet:");
		Set<String> armour = hero.getArmourSet();
		for (int i = 1; i < options.length - 1; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (!(armour.contains(options[i]))) {
				System.out.printf("Option %s: %s, %s coins. Defence: %s, Weight: %s, Description: %s\n", data[4],
						data[0], data[5], data[1], data[2], data[3]);
				attr.put(data[4], Integer.parseInt(data[5]));
			}
		}
		mfr = null;
		System.out.printf("Do you want to buy something, or exit? You currently have %d coins.\n", hero.getCoins());
		String input;

		// wait for command
		while (true) {
			input = scanner.nextLine();
			if (attr.containsKey(input)) {
				if (hero.getCoins() >= attr.get(input)) {
					hero.addArmour(input);
					hero.spendCoins(attr.get(input));
					System.out.printf(
							"You have successfully bought a new shiny piece of armour! You now have %d coins.",
							hero.getCoins());
					hero.setArmour(new Armour(input));
					return;
				} else {
					System.out.println("You have not got enough coins for this armour!");
				}
			} else if (input.equals("exit")) {
				return;
			} else {
				System.out.println("Invalid command!");
			}
		}
	}

	// testing main
	/*
	 * public static void main(String[] args) { System.out.println(((double)
	 * Math.round(0.12554534544444 * 100))/100); Armoury.shop(new Hero("GLorious",
	 * "att"), new Scanner(System.in)); }
	 */
}
