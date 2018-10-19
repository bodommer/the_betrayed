package cz.cuni.mff.java.main;

import java.util.Scanner;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.places.Arena;
import cz.cuni.mff.java.places.Armoury;
import cz.cuni.mff.java.places.TrainingGround;
import cz.cuni.mff.java.places.WeaponsShop;

public class Controller {

	private Hero hero;
	private static Controller controller = new Controller();
	private Scanner scanner;
	private Arena arena;
	private int exit = 2;

	private Controller() {
		scanner = new Scanner(System.in);
		startGame();
	}

	private void startGame() {
		System.out.println("Welcome to the game! What do you wish to start with?");
		while (true) {
			if (exit == 2) {
				mainMenu();
			}
			if (exit == 0) {
				System.exit(0);
			}
			while (exit == 1) {
				System.out.println(
						"You are in game menu. Your key options are: fight, visit home, visit armoury, visit weapons shop, level up, menu, help and exit.\n What is gonna be your next step?");
				parse(getInput());
			}
		}
	}

	private String getInput() {
		return scanner.nextLine();
	}

	private void parse(String input) {

		switch (input) {
		case "fight":
			if (arena.startFight(false)) {
				exit = 2;
			} else {
				hero.addKill();
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
		case "save":
			break;
		case "menu":
			exit = 2;
			break;
		case "help":
			// show options
			break;
		case "exit":
			exit = 0;
		default:
			break;
		}
		// TODO implement flow of the game
	}

	private void startProcedure() {
		System.out.print("Enter your warrior's name: ");
		String name = scanner.nextLine();
		System.out.print("Now...what is his strongest abiility? Attack, defence, or reflexes? ");
		String skillGroup = scanner.nextLine();
		hero = new Hero(name, skillGroup);
		System.out.print(
				"Great! Now we have a new hero. His destiny is only in your hands! What is going to be his first step on the way to saving his dear lady and also his very own life?");
		arena = new Arena(hero, scanner);
	}

	private void mainMenu() {
		System.out.println("Main menu options: new game, load, help, language, exit.");
		String input = getInput();
		while (exit == 2) {
			switch (input) {
			case "new game":
				startProcedure();
				exit = 1;
			case "load":
				// load from serialized file
				exit = 1;
				break;
			case "help":
				// show options
				break;
			case "language":
				// show language options, expect language options
				break;
			case "exit":
				exit = 0;
			default:
				break;
			}
		}
	}

	public static Controller getController() {
		return controller;
	}

	public static void main(String[] args) {
		Controller c = Controller.getController();
	}
}
