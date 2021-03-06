package cz.cuni.mff.betrayed.places;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.equipment.Armour;
import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.Input;
import cz.cuni.mff.betrayed.main.MyFileReader;

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
        MyFileReader mfr = new MyFileReader("ArmourList");
        String[] options = mfr.readAndSeparateLine();
        System.out.println(rs.getString("armouryOffer"));
        Set<String> armour = hero.getArmourSet();
        String itemOption = rs.getString("armouryOption");
        for (String armourString : options) {
            String[] data = mfr.readAndSeparateLine();
            if (!(armour.contains(armourString))) {
                System.out.printf(itemOption, rs.getString(data[0] + "C"), rs.getString(data[0] + "N"), data[3],
                        data[1], data[2], rs.getString(data[0] + "D"));
                attr.put(data[0] + "C", Integer.parseInt(data[3]));
            }
        }
        mfr.close();
        System.out.printf(rs.getString("armouryPrompt"), hero.getCoins());
        Set<String> inputOptions = attr.keySet();
        String[] o = new String[inputOptions.size() + 1];
        inputOptions.toArray(o);
        o[inputOptions.size()] = "exit";

        while (true) {
            String input = Input.showOptionsAndGetInput(o);
            if (!(input.equals("exit"))) {
                if (hero.getCoins() >= attr.get(input)) {
                    hero.addArmour(input.substring(0, input.length() - 1));
                    hero.spendCoins(attr.get(input));
                    System.out.printf(rs.getString("armouryPurchase"), hero.getCoins());
                    hero.setArmour(new Armour(input.substring(0, input.length() - 1)));
                    inputOptions.remove(input);
                    inputOptions.toArray(o);
                    o[inputOptions.size()] = "exit";
                    o[inputOptions.size() + 1] = null;
                } else {
                    System.out.println(rs.getString("armouryNotEnoughMoney"));
                }
            } else {
                return;
            }
        }
    }
}
