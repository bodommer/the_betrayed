package cz.cuni.mff.betrayed.places;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.equipment.Armour;
import cz.cuni.mff.betrayed.equipment.Weapon;
import cz.cuni.mff.betrayed.inputOptions.Options;
import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.Input;
import cz.cuni.mff.betrayed.main.MyFileReader;

/**
 * This is where hero can see what weapons/armours he owns and choose which one
 * he wants to use.
 * 
 * @author Andrej
 *
 */
public class Home {
    private Set<String> heroWeapons;
    private Set<String> heroArmours;
    private Hero hero;
    private ResourceBundle rs;

    /**
     * The constructor.
     * 
     * @param hero
     */
    public Home(Hero hero) {
        this.hero = hero;
        heroWeapons = hero.getWeaponsSet();
        heroArmours = hero.getArmourSet();
        rs = Controller.getController().getResourceBundle();
        visitHome();
    }

    /**
     * Waits for the input - where user writes what he wants to do.
     */
    private void visitHome() {
        System.out.println(rs.getString("homeWelcome"));
        while (true) {
            System.out.println(rs.getString("homePrompt1"));
            String answer = Input.showOptionsAndGetInput(Options.HOME);
            switch (answer) {
                case "seeWeapons":
                    seeWeapons();
                    break;
                case "seeArmour":
                    seeArmour();
                    break;
                case "changeWeapon":
                    changeWeapon();
                    break;
                case "changeArmour":
                    changeArmour();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }

    /**
     * Shows list of owned weapons with statistics.
     */
    @SuppressWarnings("resource")
    private void seeWeapons() {
        MyFileReader mfr = new MyFileReader("WeaponList");
        int len = mfr.readAndSeparateLine().length;
        String item = rs.getString("homeWeaponList");
        for (int i = 0; i < len; i++) {
            String[] data = mfr.readAndSeparateLine();
            if (heroWeapons.contains(data[0])) {
                System.out.printf(item, rs.getString(data[0] + "C"), rs.getString(data[0] + "N"), data[1], data[2],
                        rs.getString(data[0] + "D"));
            }
        }
    }

    /**
     * Shows list of owned armours with statistics.
     */
    @SuppressWarnings("resource")
    private void seeArmour() {
        MyFileReader mfr = new MyFileReader("ArmourList");
        int len = mfr.readAndSeparateLine().length;
        String item = rs.getString("homeArmourList");
        for (int i = 0; i < len; i++) {
            String[] data = mfr.readAndSeparateLine();
            if (heroArmours.contains(data[0])) {
                System.out.printf(item, rs.getString(data[0] + "C"), rs.getString(data[0] + "N"), data[1], data[2],
                        rs.getString(data[0] + "D"));
            }
        }
    }

    /**
     * Offers a list of weapons that a user can choose from.
     */
    private void changeWeapon() {
        System.out.printf(rs.getString("homeWeapons"), new ArrayList<String>(heroWeapons).stream()
                .map(s -> rs.getString(s + "N")).collect(Collectors.joining(", ")));
        seeWeapons();
        System.out.println(rs.getString("homeWeaponChoice"));
        String[] o = new String[heroWeapons.size() + 1];
        new ArrayList<String>(heroWeapons).stream().map(s -> s + "C").collect(Collectors.toList()).toArray(o);
        o[heroWeapons.size()] = "exit";
        String answer = Input.showOptionsAndGetInput(o);
        if (!(answer.equals("exit"))) {
            hero.setWeapon(new Weapon(answer.substring(0, answer.length() - 1)));
            System.out.printf(rs.getString("homeWeaponChosen"), hero.getWeapon().toString());
        }
        heroWeapons.remove("exit");
    }

    /**
     * Offers a list of armours that a user can choose from.
     */
    private void changeArmour() {
        System.out.printf(rs.getString("homeArmours"), new ArrayList<String>(heroArmours).stream()
                .map(s -> rs.getString(s + "N")).collect(Collectors.joining(", ")));
        seeArmour();
        System.out.println(rs.getString("homeArmourChoice"));
        String[] o = new String[heroArmours.size() + 1];
        new ArrayList<String>(heroArmours).stream().map(s -> s + "C").collect(Collectors.toList()).toArray(o);
        o[heroArmours.size()] = "exit";
        String answer = Input.showOptionsAndGetInput(o);
        if (!(answer.equals("exit"))) {
            hero.setArmour(new Armour(answer.substring(0, answer.length() - 1)));
            System.out.printf(rs.getString("homeArmourChosen"), hero.getArmour().toString());
        }
        heroArmours.remove("exit");
    }
}
