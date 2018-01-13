package cz.cuni.mff.java.places;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.main.Controller;
import cz.cuni.mff.java.main.Input;
import cz.cuni.mff.java.main.MyFileReader;

/**
 * This class has no constructor; here, the player can buy weapons for coins.
 * 
 * @author Andrej
 *
 */
public final class WeaponsShop {

	/**
	 * Shows offer, prompts to select wanted weapon and if there is enough funds,
	 * the weapon is bought and set as the active weapon.
	 * 
	 * @param hero
	 */
	public static void shop(Hero hero) {
		ResourceBundle rs = Controller.getController().getResourceBundle();
		System.out.println(rs.getString("weaponShopWelcome"));
		HashMap<String, Integer> attr = new HashMap<String, Integer>();

		// load list of weapons and show what weapons a user can buy
		MyFileReader mfr = new MyFileReader("equipment", "WeaponList");
		String[] options = mfr.readAndSeparateLine();
		System.out.println(rs.getString("weaponShopOffer"));
		Set<String> weapon = hero.getArmourSet();
		mfr.readLine();
		String itemOption = rs.getString("weaponShopOption");
		for (int i = 1; i < options.length - 1; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (!(weapon.contains(options[i]))) {
				System.out.printf(itemOption, data[10], data[0], data[11], data[1], data[2], data[3]);
				attr.put(rs.getString(data[10]), Integer.parseInt(data[11]));
			}
		}
		mfr.close();
		System.out.printf(rs.getString("weaponShopPrompt"), hero.getCoins());

		Set<String> inputOptions = attr.keySet();
		String[] o = new String[inputOptions.size() + 1];
		inputOptions.toArray(o);
		o[inputOptions.size()] = "exit";
		// prompts user to choose an option
		while (true) {
			String input = Input.get(o);
			if (!(input.equals("exit"))) {
				if (hero.getCoins() >= attr.get(input)) {
					hero.addWeapon(input);
					hero.spendCoins(attr.get(input));
					System.out.printf(rs.getString("weaponShopPurchase"), hero.getCoins());
					System.out.println(rs.getString("weaponShopNextPrompt"));
					attr.remove(input);
					inputOptions.remove(input);
				} else {
					System.out.println(rs.getString("weaponShopNotEnoughMoney"));
				}
			} else {
				return;
			}
		}
	}

	// testing main
	/*
	 * public static void main(String[] args) { WeaponsShop.shop(new
	 * Hero("GLorious", "att"), new Scanner(System.in)); }
	 */
}
