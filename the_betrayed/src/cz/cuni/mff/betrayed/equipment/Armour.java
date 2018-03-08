package cz.cuni.mff.betrayed.equipment;

import java.io.Serializable;
import java.util.Arrays;

import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.MyFileReader;

/**
 * This class contains a collection of data belonging to a unique Armour -
 * represents attributes of a piece of armour.
 * 
 * @author Andrej
 *
 */
public class Armour implements Serializable {

    private final double DEFENCE_BASE_MODIFIER = 0.4;
    private final double DEFENCE_STRENGTH_MODIFIER = 0.01;

    private static final long serialVersionUID = 4;
    private String code;
    private int defence;
    private int weight;

    /**
     * The constructor - it loads its data just by setting the name of it.
     * 
     * @param code
     *            - code of the armour (see resources/files/ArmourList for armour
     *            format/armour codes)
     */
    public Armour(String code) {
        this.code = code;
        loadData();
    }

    /**
     * Used by the constructor. Loads its own data from a file and assigns it to
     * itself.
     */
    private void loadData() {
        @SuppressWarnings("resource")
        MyFileReader mfr = new MyFileReader("ArmourList");
        int index = Arrays.asList(mfr.readAndSeparateLine()).indexOf(code);
        for (int i = 0; i < index; i++) {
            mfr.readLine();
        }
        String[] attr = mfr.readAndSeparateLine();
        setDefence(Integer.parseInt(attr[1]));
        setWeight(Integer.parseInt(attr[2]));
    }

    public void setDefence(int i) {
        defence = i;
    }

    public int getDefence() {
        return defence;
    }

    public void setWeight(int i) {
        weight = i;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Defence index of the armour applied for the hero.
     * 
     * @param strength
     *            - strength of the hero
     * @return a defence index
     */
    public double getDefIndex(int strength) {
        return (defence - (DEFENCE_BASE_MODIFIER - DEFENCE_STRENGTH_MODIFIER * strength) * weight);
    }

    @Override
    public String toString() {
        return Controller.getController().getResourceBundle().getString(code + "N");
    }
}
