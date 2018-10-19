package cz.cuni.mff.java.places;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cz.cuni.mff.java.character.Hero;

public class TrainingGround {
	public static void levelUp(Hero hero, Scanner scanner) {
		boolean stay = true;

		System.out.println(
				"Welcome to the training ground! Here you can use your experience points to upgrade your fighting stats.");
		while (stay) {
			printOptions(hero);
			stay = getAnswer(scanner, hero);
		}
	}

	private static void printOptions(Hero hero) {
		System.out.printf("You have %d XP to use.\n", hero.getXP());
		System.out.printf("Your attack level is %d and the next level will cost you %d XP.\n", hero.getAttack(),
				hero.getAttack() * 50);
		System.out.printf("Your defence level is %d and the next level will cost you %d XP.\n", hero.getDefence(),
				hero.getDefence() * 50);
		System.out.printf("Your reflexes level is %d and the next level will cost you %d XP.\n", hero.getReflexes(),
				hero.getReflexes() * 50);
		System.out.printf("Your strength level is %d and the next level will cost you %d XP.\n", hero.getStrength(),
				hero.getStrength() * 50);
		System.out.printf("Your HP level is %d and the next level will cost you %d XP.\n", hero.getHP(),
				hero.getHP() * 5);
		System.out.println("What do you want to do now? Enter skill you want to improve, or exit");
	}

	private static boolean getAnswer(Scanner scanner, Hero hero) {
		String input;
		List<String> options = Arrays.asList("attack", "defence", "reflexes", "strength", "hp", "exit");

		while (true) {
			input = scanner.nextLine();
			if (options.contains(input)) {
				break;
			} else {
				System.out.println("Invalid command");
			}
		}

		switch (input) {
		case "exit":
			return false;
		default:
			if (hero.spendXP(input)) {
				System.out.println("Your skills were successfully upgraded!");
			} else {
				System.out.println("You have not got enough XP for this!");
			}
			System.out.println("Skill successfully upgraded!");
		}
		return true;
	}

}
