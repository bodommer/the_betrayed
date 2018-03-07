package cz.cuni.mff.betrayed.main;

import java.util.prefs.Preferences;

public interface Prefs {
	
	public default Preferences getPrefs() {
		return Preferences.userNodeForPackage(Prefs.class);
	}
}
