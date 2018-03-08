package cz.cuni.mff.betrayed.main;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * This is just a wrapper class for static method showOptionsAndGetInput(). It
 * helps the program to be used in different languages and 'translates' the
 * localised commands into the neutral language of 'command codes'. It also
 * prevents the user to go through with wrong commands. This class also owns the
 * scanner (as it is only used in this class and also in a few cases in the
 * Controller. The scanner can be obtained using the getScanner() method only.
 * 
 * @author Andrej
 *
 */
public class Input {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * This is a static method for getting an option from the user, which must be
     * one of the options in the Collection provided
     * 
     * @param keys
     *            - a String array of the possible options for the user. Use one of
     *            the game's constants (class Options in inputOptions).
     * @return - returns the index of the answer in the method parameter keys, which
     *         was input by the user
     */
    public static String showOptionsAndGetInput(String[] keys) {
        ResourceBundle rs = Controller.getController().getResourceBundle();
        List<String> values = new ArrayList<String>();

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

    public static Scanner getScanner() {
        return scanner;
    }
}
