/**
 * 
 */
package cz.cuni.mff.java.main;

import java.util.Collection;
import java.util.Scanner;

/**
 * This is just a wrapper class for static method get()
 * @author Andrej
 *
 */
public class Input {
	
	/**
	 * This is a static method for getting an option from the user, which must be one of the options in the Collection provided
	 * 
	 * @param col - a collection of allowed options
	 * @param scanner - a scanner from where input shall be gotten
	 * @return - returns the option from 'col' which was input by the user
	 */
	public static String get(Collection<String> col, Scanner scanner) {
		System.out.printf("Your options are: %s.\n", String.join(", ", col));
		String input;
		while(true) {
			input = scanner.nextLine();
			if (col.contains(input)) {
				return input;
			} else { 
				System.out.println("Invalid command!");
			}
		}
	}
}
