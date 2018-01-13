package cz.cuni.mff.java.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.prefs.Preferences;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.places.Arena;
import cz.cuni.mff.java.places.Armoury;
import cz.cuni.mff.java.places.Home;
import cz.cuni.mff.java.places.TrainingGround;
import cz.cuni.mff.java.places.WeaponsShop;

/**
 * This is the main class of the game which controls the main/game menus and
 * also is the last class used before exiting the game. It is a singleton.
 * 
 * @author Andrej
 *
 */
public class Controller {

	private Hero hero;
	private static Controller controller = new Controller();
	private Scanner scanner;
	private Arena arena;
	private int exit = 2;
	ResourceBundle rs; 	
	
	/**
	 * The Controller constructor. It can be gained only from public getController()
	 * method.
	 */
	private Controller() {
		scanner = new Scanner(System.in);
		setLanguage();
	}

	/**
	 * This method starts the game - this method runs the whole time, it is the core
	 * of the program. It has three states - Main menu, Game menu and Exit
	 */
	private void startGame() {
		System.out.println(rs.getString("welcome"));
		while (true) {
			if (exit == 2) {
				mainMenu();
			}
			if (exit == 0) {
				System.out.println(rs.getString("goodbye"));
				System.exit(0);
			}
			while (exit == 1) {
				gameMenu();
			}
		}
	}

	/**
	 * The method controls the Game menu - gets the command from user and decides
	 * what to do accordingly.
	 * 
	 * @param input
	 *            - command, that user writes to choose an option from the game menu
	 */
	private void gameMenu() {
		System.out.println(rs.getString("gameMenu"));
		switch (Input.get(Arrays.asList("fight", "visitArmoury", "visitWeaponsShop", "levelUp", "visitHome", "save", "menu", "help", "exit"))) {
		case "fight":
			boolean boss = false;
			if (hero.getFight() % 6 == 5) {
				boss = true;
			}
			if (arena.startFight(boss)) {
				exit = 2;
			} else {
				hero.addKill();
				if (hero.getFight() % 6 == 5) {
					System.out.println(rs.getString("nextFightBoss"));
				}
				if (hero.getFight() == 18) {
					System.out.println(rs.getString("endGameLine"));
					System.out.printf(rs.getString("scoreLine"), hero.getScore());
					exit = 0;
				}
			}
			break;
		case "visitArmoury":
			Armoury.shop(hero);
			break;
		case "visitWeaponsShop":
			WeaponsShop.shop(hero);
			break;
		case "levelUp":
			TrainingGround.levelUp(hero);
			break;
		case "visitHome":
			new Home(hero);
			break;
		case "save":
			saveGame();
			break;
		case "menu":
			exit = 2;
			break;
		case "help":
			// TODO implement showing controls/available commands
			break;
		case "exit":
			exit = 0;
		default:
			break; // if there is some invalid command
		}
	}

	/**
	 * New game procedure - setting hero's name, attributes etc.
	 */
	private void startProcedure() {
		System.out.println(rs.getString("getName"));
		String name = scanner.nextLine();
		System.out.println(rs.getString("getSkillset"));
		List<String> options = Arrays.asList("attack", "defence", "reflexes");
		hero = new Hero(name, Input.get(options));
		System.out.println(rs.getString("heroCreated"));
		arena = new Arena(hero);
	}

	/**
	 * Controls the Main menu and its commands.
	 */
	private void mainMenu() {
		System.out.println(rs.getString("mainMenu"));
		switch (Input.get(Arrays.asList("newGame", "load", "help", "changeLanguage", "exit"))) {
		case "newGame": // start a new game and enter game menu
			startProcedure();
			exit = 1; // set menu to game menu
			break;
		case "load":
			loadSavedGame();
			exit = 1;
			break;
		case "help":
			// show options
			break;
		case "changeLanguage":
			languageSelection();
			break;
		case "exit":
			exit = 0;
			break;
		default:
			break;
		}
	}

	private void saveGame() {
		System.out.println(rs.getString("saveQuestion"));
		if (Input.get(Arrays.asList("yes", "no")).equals("no")) {
			System.out.println(rs.getString("gameNotSaved"));
			return;
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./hero.ser")));
			oos.writeObject(hero);
			oos.close();
			System.out.println(rs.getString("gameSaved"));
		} catch (IOException e) {
			System.out.println(rs.getString("saveFailed"));
		}
	}

	private void loadSavedGame() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("hero.ser"));
			hero = (Hero) ois.readObject();
			ois.close();
			arena = new Arena(hero);
			System.out.print(rs.getString("successfulLoad"));
		} catch (IOException e) {
			System.out
					.println(rs.getString("loadFailed"));
		} catch (ClassNotFoundException e) {
			System.out.println(rs.getString("loadFailedClassNotFound"));
		}
	}

	/**
	 * The only way to obtain a Controller instance
	 * 
	 * @return
	 */
	public static Controller getController() {
		return controller;
	}
	
	private void languageSelection() {
		System.out.println(rs.getString("availableLanguages"));
		System.out.println("en: English\nsk: Slovenčina");
		String choice = Input.get(Arrays.asList("en", "sk", "exit"));
		if (!(choice.equals("exit"))) {
			Preferences prefs = Prefs.getPrefs();
			if (!(prefs.get("language", null).equals(choice))) {
				prefs.put("language", choice);
				System.out.println(rs.getString("languageChanged"));
			}
		}
	}
	
	private void setLanguage() {
		Preferences prefs = Prefs.getPrefs();
		if (prefs.get("language", null) == null) {
			Locale l = Locale.getDefault();
			if (l.toString().equals("sk-SK") || l.toString().equals("cs-CZ")) {
				prefs.put("language", "sk");
			} else {
				prefs.put("language", "en");
			}
		} 
		String lang = prefs.get("language", null);
		Locale loc;
		if (lang.equals("sk")) {
			loc = new Locale("sk");
		} else {
			loc = Locale.ENGLISH;
		}
		setResourceBundle(loc);
	}
	
	private void setResourceBundle(Locale loc) {
		rs = ResourceBundle.getBundle("cz.cuni.mff.java.resources.localization.resource", loc);
	}
	
	public ResourceBundle getResourceBundle() {
		return rs;
	}

	public Scanner getScanner() {
		return scanner;
	}
	/**
	 * Game launcher.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Controller.getController().startGame();
	}
}
