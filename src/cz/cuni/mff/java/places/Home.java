package cz.cuni.mff.java.places;

import java.util.Scanner;
import java.util.Set;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.equipment.Armour;
import cz.cuni.mff.java.equipment.Weapon;

public class Home {
	Set<Weapon> weapons;
	Set<Armour> armours;
	Scanner scanner;
	Hero hero;
	
	public Home(Hero hero, Scanner scanner) {
		this.scanner = scanner;
		this.hero = hero;
		
		
		
		
		
		
		visitHome();
	}
	
	
	
	
	
	
	
	
	
	private void visitHome() {
		System.out.println("Welcome home! What do you want to do? See weapons, see armour, change weapon, change armour, or exit?");
		while(true) {
			String input = scanner.nextLine();
			switch (input) {
			case "see weapons":
				seeWeapons();
				break;
			case "see armour":
				seeArmour();
				break;
			case "change weapon":
				changeWeapon();
				break;
			case "change armour":
				changeArmour();
				break;
			case "exit":
				return;
			default:
				System.out.println("Invalid command!");
			}
		}
	}
	
	private void seeWeapons() {
		
	}
	
	private void seeArmour() {
		
	}
	
	private void changeWeapon() {
		
	}
	
	private void changeArmour() {
		
	}
	
	
}
	
