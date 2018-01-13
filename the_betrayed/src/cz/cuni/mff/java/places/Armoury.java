package cz.cuni.mff.java.places;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.main.Controller;
import cz.cuni.mff.java.main.Input;
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
	 */
	public static void shop(Hero hero) {
		ResourceBundle rs = Controller.getController().getResourceBundle();
		System.out.println(rs.getString("armouryWelcome"));
		HashMap<String, Integer> attr = new HashMap<String, Integer>();

		// show options
		MyFileReader mfr = new MyFileReader("equipment", "ArmourList");
		String[] options = mfr.readAndSeparateLine();
		System.out.println(rs.getString("armouryOffer"));
		Set<String> armour = hero.getArmourSet();
		String itemOption = rs.getString("armouryOption");
		for (int i = 1; i < options.length - 1; i++) {
			String[] data = mfr.readAndSeparateLine();
			if (!(armour.contains(options[i]))) {
				System.out.printf(itemOption, data[4],
						data[0], data[5], data[1], data[2], data[3]);
				attr.put(data[4], Integer.parseInt(data[5]));
			}
		}
		mfr.close();
		System.out.printf(rs.getString("armouryPrompt"), hero.getCoins());
		Set<String> inputOptions = attr.keySet();
		String[] o = new String[inputOptions.size() + 1];
		inputOptions.toArray(o);
		o[inputOptions.size()] = "exit";

		while (true) {
			String input = Input.get(o);
			if (!(input.equals("exit"))) {
				if (hero.getCoins() >= attr.get(input)) {
					hero.addArmour(input);
					hero.spendCoins(attr.get(input));
					System.out.printf(
							rs.getString("armouryPurchase"),
							hero.getCoins());
					hero.setArmour(new Armour(input));
					attr.remove(input);
					inputOptions.remove(input);
				} else {
					System.out.println(rs.getString("armouryNotEnoughMoney"));
				}
			} else {
				return;
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
