/**
 * 
 */
package cz.cuni.mff.java.main;

import java.util.ArrayList;
import java.util.List;
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
	public static String get(String[] keys) {
		ResourceBundle rs = Controller.getController().getResourceBundle();
		List<String> values = new ArrayList<String>();
		Scanner scanner = Controller.getController().getScanner();

		for (String s : keys) {
			if (s != null) {
				values.add(rs.getString(s));
			}
		}

		System.out.printf(rs.getString("inputOptions"), String.join(", ", values));
		String input;
		while (true) {
			input = scanner.nextLine().trim();
			if (values.contains(input)) {
				return keys[values.indexOf(input)];
			} else {
				System.out.println(rs.getString("inputInvalidCommand"));
			}
		}
	}
}
