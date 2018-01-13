package cz.cuni.mff.java.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
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
		System.out.println(rs.getString("intro"));
		while (true) {
			if (exit == 2) {
				mainMenu();
			}
			if (exit == 0) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
			while (exit == 1) {
				parse(getInput());
			}
		}
	}

	/**
	 * This method is used to get some answers and commands from the user.
	 * 
	 * @return user input as an answer to game's prompt
	 */
	private String getInput() {
		return scanner.nextLine();
	}

	/**
	 * The method controls the Game menu - gets the command from user and decides
	 * what to do accordingly.
	 * 
	 * @param input
	 *            - command, that user writes to choose an option from the game menu
	 */
	private void parse(String input) {
		System.out.println(
				"You are in game menu. Your key options are: fight, visit home, visit armoury, visit weapons shop, level up, save, menu, help and exit.\n What is gonna be your next step?");
		
		switch (Input.get(new HashSet<String>(Arrays.asList("fight", "visit armoury", "visit weapons shop", "level up", "visit home", "save", "menu", "help", "exit")), scanner)) {
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
					System.out.println("Watch out, your next opponent is level BOSS! You shall prepare for the fight!");
				}
				if (hero.getFight() == 18) {
					System.out.println(
							"So this is it. You are the master fighter! All local guards are afraid of you and let you walk away from the city with your beloved girl. WELL DONE!");
					System.out.printf("Your score was %d. A solid game!", hero.getScore());
					exit = 0;
				}
			}
			break;
		case "visit armoury":
			Armoury.shop(hero, scanner);
			break;
		case "visit weapons shop":
			WeaponsShop.shop(hero, scanner);
			break;
		case "level up":
			TrainingGround.levelUp(hero, scanner);
			break;
		case "visit home":
			new Home(hero, scanner);
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
		System.out.println(
				"Before you set off on an amazing journey, your hero needs to be created.\nEnter your warrior's name: ");
		String name = scanner.nextLine();
		System.out.println("Now...what is his strongest abiility? Attack, defence, or reflexes? ");
		Set<String> options = new HashSet<String>(Arrays.asList("attack", "defence", "reflexes"));
		hero = new Hero(name, Input.get(options, scanner));
		System.out.println(
				"Great! Now we have a new hero. His destiny is only in your hands! What is going to be his first step on the way to saving his dear lady and also his very own life?");
		arena = new Arena(hero, scanner);
	}

	/**
	 * Controls the Main menu and its commands.
	 */
	private void mainMenu() {
		System.out.println("Main menu options: new game, load, help, change language, exit.");
		switch (Input.get(new HashSet<String>(Arrays.asList("new game", "load", "help", "change language", "exit")),
				scanner)) {
		case "new game": // start a new game and enter game menu
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
		case "change language":
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
		System.out.println("Saving current game will erase other saved game (if any). Do you want to continue? yes/no");
		if (Input.get(new HashSet<String>(Arrays.asList("yes", "no")), scanner).equals("no")) {
			System.out.println("Your game was not saved.");
			return;
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./hero.ser")));
			oos.writeObject(hero);
			oos.close();
			System.out.println("Your hero is now safely saved!");
		} catch (IOException e) {
			System.out.println("The hero does not want to be saved. Try again to persuade him!");
		}
	}

	private void loadSavedGame() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("hero.ser"));
			hero = (Hero) ois.readObject();
			ois.close();
			arena = new Arena(hero, scanner);
			System.out.print("Your hero has woken up and is ready to fight again!");
		} catch (IOException e) {
			System.out
					.println("The powers that be decided that you are not to get your game loaded. Please, try again!");
		} catch (ClassNotFoundException e) {
			System.out.println(
					"Your hero is hiding and we were unable to locate him. Please, try again for a higher chance to find him!");
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
		String choice = Input.get(new HashSet<String>(Arrays.asList("en", "sk", "exit")), scanner);
		if (!(choice.equals("exit"))) {
			Preferences prefs = Prefs.getPrefs();
			if (!(prefs.get("language", null).equals(choice))) {
				prefs.put("language", choice);
				setLanguage();
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
