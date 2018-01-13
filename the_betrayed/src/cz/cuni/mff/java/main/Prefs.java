package cz.cuni.mff.java.main;

import java.util.prefs.Preferences;

public class Prefs {
	
	public static Preferences getPrefs() {
		return Preferences.userNodeForPackage(Prefs.class);
	}
	
}
