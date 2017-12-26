package cz.cuni.mff.java.places;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.character.Opponent;
import cz.cuni.mff.java.character.Person;

public class Arena {

	private Hero hero;
	private Scanner scanner;

	public Arena(Hero hero, Scanner scanner) {
		this.hero = hero;
		this.scanner = scanner;
	}

	// returns true if hero died, false if opponent died
	public boolean startFight(boolean boss) {
		Fight fight = new Fight(false);

		boolean end = false;
		int start = new Random().nextInt(2);
		System.out.println("This is going to be a tough fight.");
		System.out.printf("Some info about you: %s, attack: %d, defence: %d, hp: %d, weapon: %s, armour: %s, %s\n",
				hero.getName(), hero.getAttack(), hero.getDefence(), hero.getHP(), hero.getWeapon(), hero.getArmour(),
				fight.heroStats.toString());
		System.out.printf(
				"Some info about your opponent: %s, attack: %d, defence: %d, hp: %d, weapon: %s, armour: %s, %s\n",
				fight.opponent.getName(), fight.opponent.getAttack(), fight.opponent.getDefence(),
				fight.opponent.getHP(), fight.opponent.getWeapon(), fight.opponent.getArmour(),
				fight.opponentStats.toString());
		if (start == 1) {
			System.out.println("You are the first one to start the battle. What are you gonna do?");
			while (!(end)) {
				end = fight.heroTurn();
				if (end) {
					
					return false;
				} else {
					System.out.printf("Your opponent's life remaining: %d HP\n", fight.opponent.getLife());
					System.out.println("Your opponent's turn.");
					try {
						TimeUnit.SECONDS.sleep(1);
						end = fight.opponentTurn();
					} catch (InterruptedException ie) {
						System.out.println("Your opponent got a heart attack. You win!");
						return false;
					}
					if (hero.getLife() > 0) {
						System.out.printf("Your remaining life: %d HP.\n", hero.getLife());
					}
				}
			}
			System.out.printf("You were unable to beat your opponent and you died. What a tragedy! End of game, your score was: %d. GOOD GAME!", hero.getScore());
			return true;
		} else {
			System.out.println("Your opponent starts the battle");
			while (!(end)) {
				System.out.println("Your opponents turn.");
				try {
					TimeUnit.SECONDS.sleep(1);
					end = fight.opponentTurn();
				} catch (InterruptedException ie) {
					System.out.println("Your opponent got a heart attack. You win!");
					return false;
				}
				if (end) {
					return true;
				} else {
					System.out.printf("Your remaining life: %d HP\n", hero.getLife());
					end = fight.heroTurn();
					if (fight.opponent.getLife() > 0) {
						System.out.printf("Your opponent's remaining life: %d HP \n", fight.opponent.getLife());
					}
				}
			}
			return false;
		}
	}

	private class Fight {

		private Opponent opponent;
		private FightStats heroStats;
		private FightStats opponentStats;

		protected Fight(boolean boss) {
			int line;

			if (boss) {
				line = 11;
			} else {
				line = new Random().nextInt(10) + 1;
			}
			opponent = getOpponent(hero.getLevel(), line);
			if (opponent.getHP() > 0) {
				createChances();
			} else {
				// TODO something went wrong, just return from arena and ask pal to try again
			}
		}

		private Opponent getOpponent(int level, int line) {
			Opponent o = new Opponent(level, line);
			return o;
		}

		private void createChances() {
			heroStats = new FightStats(hero);
			opponentStats = new FightStats(opponent);
			heroStats.percentages(opponentStats);
			opponentStats.percentages(heroStats);
		}

		protected boolean opponentTurn() {
			String answer;

			if (opponent.getHP() < hero.getWeapon().getSlashMax()) {
				answer = "throw weapon";
			} else {
				int ran = new Random().nextInt(100);
				if (ran < 34) {
					answer = "stab";
				} else {
					answer = "slash";
				}
			}
			return performAttack("opponent", answer);
		}

		protected boolean heroTurn() {
			List<String> list = Arrays.asList("slash", "stab", "throw weapon");
			String answer;

			while (true) {
				System.out.println(
						"It is your turn. How do you want to attack your opponent? Slash, stab, or throw your weapon?");
				answer = scanner.nextLine();
				if (list.indexOf(answer) >= 0) {
					break;
				}
			}
			return performAttack("hero", answer);
		}

		private boolean performAttack(String who, String attackType) {

			double ran;
			int damage = 0;
			Person attacker;
			Person defender;
			FightStats attackerStats;

			if (who.equals("hero")) {
				attacker = hero;
				attackerStats = heroStats;
				defender = opponent;
			} else {
				attacker = opponent;
				attackerStats = opponentStats;
				defender = hero;
			}

			switch (attackType) {
			case "slash":
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getSlashChance()) {
					if (who.equals("hero")) {
						System.out.println("Your slash attack was lame and your enemy managed to defend well!");
					} else {
						System.out.println("Your opponent tried to slash you, but it wasn't very effective...");
					}
				} else {
					if (who.equals("hero")) {
						System.out.println("WOW! What a slash! Your enemy was badly wounded!!");
					} else {
						System.out.println("Your opponent slashed you really bad!");
					}
					damage = attacker.getWeapon().getSlashHit();
				}
				break;

			case "stab":
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getStabChance()) {
					if (who.equals("hero")) {
						System.out.println("You tried to stab your opponent, but he somehow managed to avoid it!");
					} else {
						System.out.printf("%s tried to stab you, but you were fast enough to fend off his attack!\n", attacker.getName());
					}
				} else {
					if (who.equals("hero")) {
					System.out.println("AAARGH! Your stab attack was effective and your opponent is badly wounded!!");
					} else {
						System.out.println("Your opponent stabbed you and it left serious wounds on your body. Ouch!");
					}
					damage = attacker.getWeapon().getStabHit();
				}
				break;

			case "throw weapon":
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getThrowWeaponChance()) {
					if (who.equals("hero")) {
					System.out.println("You tried to throw your weapon, but it was a girly throw. Now you are left with your bare hands only.");
					} else {
						System.out.println("A desperate situation leads to desperate actions. Your tired opponent tried to throw a weapon at you, but how can this peasant hurt a warrior as great as you??");
					}
				} else {
					if (who.equals("hero")) {
						System.out.println("WHAT A THROW! Despite the fact that you are now left with your bare hands only, your thrw caused serious damage to your opponent.");
					} else {
						System.out.println("Despite being almot dead, this warrior tried to throw his weapon at you and he did not miss. Did you think it was going to be an easy win?");
					}
					damage = attacker.getWeapon().getThrowHit();
				}
				break;

			default:
				break;
			}
			return defender.damage(damage);
		}

		private class FightStats {

			private double slashAtt;
			private double stabAtt;
			private double throwWeaponAtt;

			private double slashDef;
			private double stabDef;
			private double throwWeaponDef;

			private double slashChance;
			private double stabChance;
			private double throwWeaponChance;

			protected FightStats(Person p) {
				slashAtt = p.getAttack() * 0.4 + p.getReflexes() * 0.1
						+ 0.5 * (p.getWeapon().getPowerIndex(p.getStrength()));
				stabAtt = 0.45 * p.getAttack() + 0.55 * (p.getWeapon().getPowerIndex(p.getStrength()));
				throwWeaponAtt = 0.3 * p.getAttack() + 0.2 * p.getStrength()
						+ 0.5 * (p.getWeapon().getPowerIndex(p.getStrength()));

				slashDef = p.getDefence() * 0.6 + p.getReflexes() * 0.2 + p.getArmour().getDefIndex(p.getStrength());
				stabDef = p.getDefence() * 0.4 + p.getReflexes() * 0.5 + p.getArmour().getDefIndex(p.getStrength());
				throwWeaponDef = p.getReflexes() * 0.5 + p.getDefence() * 0.3
						+ p.getArmour().getDefIndex(p.getStrength());
			}

			protected void percentages(FightStats otherGuy) {
				setSlashChance(1.35 * getSlashAtt() / otherGuy.getSlashDef());
				setStabChance(0.97 * getStabAtt() / otherGuy.getStabDef());
				setThrowWeaponChance(0.38 * getThrowWeaponAtt() / otherGuy.getThrowWeaponDef());
			}

			public double getSlashAtt() {
				return slashAtt;
			}

			public double getStabAtt() {
				return stabAtt;
			}

			public double getThrowWeaponAtt() {
				return throwWeaponAtt;
			}

			public double getSlashDef() {
				return slashDef;
			}

			public double getStabDef() {
				return stabDef;
			}

			public double getThrowWeaponDef() {
				return throwWeaponDef;
			}

			private void setSlashChance(double d) {
				if (d > 0.99) {
					slashChance = 0.99;
				} else if (d < 0.4) {
					slashChance = 0.4;
				} else {
					slashChance = ((double) Math.round(d * 100)) / 100;
				}
			}

			public double getSlashChance() {
				return slashChance;
			}

			private void setStabChance(double d) {
				if (d > 0.69) {
					stabChance = 0.69;
				} else if (d < 0.2) {
					stabChance = 0.2;
				} else {
					stabChance = ((double) Math.round(d * 100)) / 100;
				}
			}

			public double getStabChance() {
				return stabChance;
			}

			private void setThrowWeaponChance(double d) {
				if (d > 0.29) {
					throwWeaponChance = 0.29;
				} else if (d < 0.08) {
					throwWeaponChance = 0.08;
				} else {
					throwWeaponChance = ((double) Math.round(d * 100)) / 100;
				}
			}

			public double getThrowWeaponChance() {
				return throwWeaponChance;
			}

			@Override
			public String toString() {
				StringBuilder sb = new StringBuilder();
				sb.append("stab chance: ");
				sb.append((int) stabChance * 100);
				sb.append("%, slash chance: ");
				sb.append((int) slashChance * 100);
				sb.append("%, throw weapon chance: ");
				sb.append((int) throwWeaponChance * 100);
				sb.append("%");
				return sb.toString();
			}
		}
	}

	public static void main(String[] args) {
		Arena a = new Arena(new Hero("John", "att"), new Scanner(System.in));
		a.startFight(false);
	}
}
