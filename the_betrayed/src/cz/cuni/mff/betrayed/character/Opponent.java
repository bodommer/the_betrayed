package cz.cuni.mff.java.character;

import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.equipment.Weapon;
import cz.cuni.mff.java.main.MyFileReader;
/**
 * Opponent to the hero in the Arena.
 * @author Andrej
 *
 */
public class Opponent extends Person {
	
	private static final long serialVersionUID = 3;

	/**
	 * The constructor. Reads data from given file and line to create a new Opponent with unique set of skills.
	 * @param level - 1, 2 or 3
	 * @param line - line 11 in the file is the level boss, other lines are ordinary enemies
	 */
	public Opponent(int level, int line) {
		MyFileReader mfr;
		
		try {
			String str = "level" + level;
			mfr = new MyFileReader(str);
			for (int i = 0; i < line - 1; i++) {
				mfr.readLine();
			}
			String[] attrs = mfr.readAndSeparateLine();
			setName(attrs[0]);
			setAttack(Integer.parseInt(attrs[1]));
			setDefence(Integer.parseInt(attrs[2]));
			setReflexes(Integer.parseInt(attrs[3]));
			setStrength(Integer.parseInt(attrs[4]));
			setHP(Integer.parseInt(attrs[5]));
			setLife(getHP());
			setWeapon(new Weapon(attrs[6]));
			setArmour(new Armour(attrs[7]));
		} catch (Exception e) {
			setHP(-1);	
		} finally {
			mfr = null;
		}
	}
	
	//testing main
	/* public static void main(String[] args) {
		Opponent o = new Opponent(1, 1);
		System.out.println("FINITO");
	} */
}
