package cz.cuni.mff.betrayed.places;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.equipment.Weapon;
import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.Input;
import cz.cuni.mff.betrayed.main.MyFileReader;

/**
 * This class has no constructor; here, the player can buy weapons for coins.
 * 
 * @author Andrej
 *
 */
public final class WeaponsShop {

    /**
     * Shows offer, prompts to select wanted weapon and if there is enough funds,
     * the weapon is bought and set as the active weapon.
     * 
     * @param hero
     */
    public static void shop(Hero hero) {
        ResourceBundle rs = Controller.getController().getResourceBundle();
        System.out.println(rs.getString("weaponShopWelcome"));
        HashMap<String, Integer> attr = new HashMap<String, Integer>();

        // load list of weapons and show what weapons a user can buy
        MyFileReader mfr = new MyFileReader("WeaponList");
        String[] options = mfr.readAndSeparateLine();
        System.out.println(rs.getString("weaponShopOffer"));
        Set<String> weapon = hero.getArmourSet();
        mfr.readLine();
        String itemOption = rs.getString("weaponShopOption");
        for (int i = 1; i < options.length - 1; i++) {
            String[] data = mfr.readAndSeparateLine();
            if (!(weapon.contains(options[i]))) {
                System.out.printf(itemOption, rs.getString(data[0] + "C"), rs.getString(data[0] + "N"), data[9],
                        data[1], data[2], rs.getString(data[0] + "D"));
                attr.put(data[0] + "C", Integer.parseInt(data[9]));
            }
        }
        mfr.close();
        System.out.printf(rs.getString("weaponShopPrompt"), hero.getCoins());

        Set<String> inputOptions = attr.keySet();
        String[] o = new String[inputOptions.size() + 1];
        inputOptions.toArray(o);
        o[inputOptions.size()] = "exit";
        // prompts user to choose an option
        while (true) {
            String input = Input.showOptionsAndGetInput(o);
            if (!(input.equals("exit"))) {
                if (hero.getCoins() >= attr.get(input)) {
                    hero.addWeapon(input.substring(0, input.length() - 1));
                    hero.spendCoins(attr.get(input));
                    System.out.printf(rs.getString("weaponShopPurchase"), hero.getCoins());
                    System.out.println(rs.getString("weaponShopNextPrompt"));
                    hero.setWeapon(new Weapon(input.substring(0, input.length() - 1)));
                    inputOptions.remove(input);
                    inputOptions.toArray(o);
                    o[inputOptions.size()] = "exit";
                    o[inputOptions.size() + 1] = null;
                } else {
                    System.out.println(rs.getString("weaponShopNotEnoughMoney"));
                }
            } else {
                return;
            }
        }
    }
}
