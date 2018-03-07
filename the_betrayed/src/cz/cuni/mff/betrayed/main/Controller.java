package cz.cuni.mff.betrayed.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.inputOptions.Options;
import cz.cuni.mff.betrayed.places.Arena;
import cz.cuni.mff.betrayed.places.Armoury;
import cz.cuni.mff.betrayed.places.Home;
import cz.cuni.mff.betrayed.places.TrainingGround;
import cz.cuni.mff.betrayed.places.WeaponsShop;

/**
 * This is the main class of the game which controls the main/game menus and
 * also is the last class used before exiting the game. It is a singleton.
 * 
 * @author Andrej
 *
 */
public class Controller{

	private final int FIGHTS_PER_LEVEL = 6;
	private final int LEVEL_COUNT = 3;
	private final String SAVE_FILE_NAME = "hero.ser";
	private final String SAVE_LOCATION = "." + File.separator + SAVE_FILE_NAME;
	private final String LANGUAGE = "language";
	private final String LANGUAGE_SLOVAK = "sk-SK";
	private final String LANGUAGE_CZECH = "cs-CZ";
	private final String LOCALISATION_ADDRESS = "localization.resource";
	
	private Hero hero;
	private static final Controller controller = new Controller();
	private Arena arena;
	private int exit = 2;
	private ResourceBundle rs;

	/**
	 * The Controller constructor. It can be gained only from public getController()
	 * method.
	 */
	private Controller() {
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
	 */
	private void gameMenu() {
		System.out.println(rs.getString("gameMenu"));
		switch (Input.showOptionsAndGetInput(Options.GAME_MENU)) {
		case "fight":
			boolean boss = false;
			if (isBossFight()) {
				boss = true;
			}
			if (arena.startFight(boss)) {
				exit = 2;
			} else {
				hero.addKill();
				if (isBossFight()) {
					System.out.println(rs.getString("nextFightBoss"));
				}
				if (hero.getFight() == LEVEL_COUNT * FIGHTS_PER_LEVEL) {
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

	private boolean isBossFight() {
		return hero.getFight() % FIGHTS_PER_LEVEL == FIGHTS_PER_LEVEL - 1;
	}
	
	/**
	 * New game procedure - setting hero's name, attributes etc.
	 */
	private void startProcedure() {
		System.out.println(rs.getString("introText"));
		System.out.println(rs.getString("controlsInfo"));
		System.out.println(rs.getString("getName"));
		String name = Input.getScanner().nextLine();
		System.out.println(rs.getString("getSkillset"));
		hero = new Hero(name, Input.showOptionsAndGetInput(Options.SKILLSET));
		System.out.println(rs.getString("heroCreated"));
		arena = new Arena(hero);
	}

	/**
	 * Controls the Main menu and its commands.
	 */
	private void mainMenu() {
		System.out.println(rs.getString("mainMenu"));
		switch (Input.showOptionsAndGetInput(Options.MAIN_MENU)) {
		case "newGame": // start a new game and enter game menu
			startProcedure();
			exit = 1; // set menu to game menu
			break;
		case "load":
			loadSavedGame();
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
		if (Input.showOptionsAndGetInput(Options.YES_NO).equals("no")) {
			System.out.println(rs.getString("gameNotSaved"));
			return;
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SAVE_LOCATION)));){		
			oos.writeObject(hero);
			System.out.println(rs.getString("gameSaved"));
		} catch (IOException e) {
			System.out.println(rs.getString("saveFailed"));
		}
	}

	private void loadSavedGame() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME));) {
			hero = (Hero) ois.readObject();
			arena = new Arena(hero);
			System.out.print(rs.getString("successfulLoad"));
			exit = 1;
		} catch (IOException e) {
			System.out.println(rs.getString("loadFailed"));
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
		System.out.println("en: English\nsk: Slovenina");
		String choice = Input.showOptionsAndGetInput(Options.LANGUAGES);
		if (!(choice.equals("exit"))) {
			Preferences prefs = Prefs.getPrefs(); // Interface -> Controller implements Prefs with default getPrefs() method
			if (!(prefs.get("language", null).equals(choice))) {
				prefs.put("language", choice);
				System.out.println(rs.getString("languageChanged"));
			}
		}
	}
 
	private void setLanguage() {
		Preferences prefs = Prefs.getPrefs(); // Interface -> Controller implements Prefs with default getPrefs() method
		if (prefs.get(LANGUAGE, null) == null) {
			Locale l = Locale.getDefault();
			if (l.toString().equals(LANGUAGE_SLOVAK) || l.toString().equals(LANGUAGE_CZECH)) {
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
		rs = ResourceBundle.getBundle(LOCALISATION_ADDRESS, loc);
	}
 
	public ResourceBundle getResourceBundle() {
		return rs;
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
