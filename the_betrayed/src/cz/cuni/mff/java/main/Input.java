/**
 * 
 */
package cz.cuni.mff.java.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * This is just a wrapper class for static method get()
 * 
 * @author Andrej
 *
 */
public class Input {

	/**
	 * This is a static method for getting an option from the user, which must be
	 * one of the options in the Collection provided
	 * 
	 * @param col
	 *            - a collection of allowed options
	 * @param scanner
	 *            - a scanner from where input shall be gotten
	 * @return - returns the option from 'col' which was input by the user
	 */
	public static String get(Collection<String> col) {
		ResourceBundle rs = Controller.getController().getResourceBundle();
		HashMap<String, String> map = new HashMap<String, String>();
		Scanner scanner = Controller.getController().getScanner();

		for (String s : col) {
			map.put(Controller.getController().getResourceBundle().getString(s), s);
		}

		System.out.printf(rs.getString("inputOptions"), String.join(", ", map.keySet()));
		String input;
		while (true) {
			input = scanner.nextLine().trim();
			if (map.keySet().contains(input)) {
				return map.get(input);
			} else {
				System.out.println(rs.getString("inputInvalidCommand"));
			}
		}
	}
}
