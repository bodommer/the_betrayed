package cz.cuni.mff.betrayed.main;

import java.util.prefs.Preferences;

/**
 * A class for storing the preferences of the user - currently only the language
 * preference of the user.
 * 
 * @author Andrej
 *
 */
public class Prefs {

    public static Preferences getPrefs() {
        return Preferences.userNodeForPackage(Prefs.class);
    }
}
