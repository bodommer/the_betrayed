package cz.cuni.mff.java.places;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.main.MyFileReader;

/**
 * This class has no constructor; here, the player can buy weapons for coins. 
 * @author Andrej
 *
 */
public final class WeaponsShop {

	/**
	 * Shows offer, prompts to select wanted weapon and if there is enough funds, the weapon is bought and set as the active weapon.
	 * @param hero
	 * @param scanner
	 */
	public static void shop(Hero hero, Scanner scanner) {
		System.out.println("Welcome to the shop! ");
		HashMap<String, Integer> attr = new HashMap<String, Integer>();

		//load list of weapons and show what weapons a user can buy
		MyFileReader mfr = new MyFileReader("equipment", "WeaponList");
		String[] options = mfr.readAndSeparateLine();
		System.out.println("Here is our inventory of weapons, which you do not own yet:");
		Set<String> weapon = hero.getArmourSet();
		mfr.readLine();
		for (int i = 1; i < options.length-1; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (!(weapon.contains(options[i]))) {
				System.out.printf("Option %s: %s, %s coins. Attack: %s, Weight: %s, Description: %s\n", data[10],
						data[0], data[11], data[1], data[2], data[3]);
				attr.put(data[10], Integer.parseInt(data[11]));
			}
		}
			mfr = null;
		System.out.printf("Do you want to buy something, or exit? You currently have %d coins.\n", hero.getCoins());
		String input;
		
		//prompts user to choose an option
		while (true) {
			input = scanner.nextLine();
			if (attr.containsKey(input)) {
				if (hero.getCoins() >= attr.get(input)) {
					hero.addWeapon(input);
					hero.spendCoins(attr.get(input));
					System.out.printf(
							"You have successfully bought a new weapon! You now have %d coins. \n",
							hero.getCoins());
					attr.remove(input);
				} else {
					System.out.println("You have not got enough coins for this weapon!\n");
				}
			} else if (input.equals("exit")) {
				return;
			} else {
				System.out.println("Invalid command!");
			}
			System.out.println("Do you want to buy another weapon?");
		}
	}

	//testing main
	/*
	public static void main(String[] args) {
		WeaponsShop.shop(new Hero("GLorious", "att"), new Scanner(System.in));
	} */
}
