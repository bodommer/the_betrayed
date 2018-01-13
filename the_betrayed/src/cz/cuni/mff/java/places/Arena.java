package cz.cuni.mff.java.places;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.java.character.Hero;
import cz.cuni.mff.java.character.Opponent;
import cz.cuni.mff.java.character.Person;
import cz.cuni.mff.java.equipment.Weapon;
import cz.cuni.mff.java.main.Controller;
import cz.cuni.mff.java.main.Input;

/**
 * This is the most important place. Here, the fights take place and it is the
 * whole idea of the game.
 * 
 * @author Andrej
 *
 */
public class Arena {

	private Hero hero;
	private ResourceBundle rs;

	/**
	 * The constructor.
	 * 
	 * @param hero
	 */
	public Arena(Hero hero) {
		this.hero = hero;
		rs = Controller.getController().getResourceBundle();
	}

	/**
	 * This method starts a new fight. Creates a new Fight inner class instance,
	 * where the whole fight takes place. This method controls the flow of the
	 * fight.
	 * 
	 * @param boss
	 * @return
	 */
	// returns true if hero died, false if opponent died
	public boolean startFight(boolean boss) {
		Fight fight = new Fight(false);

		if (fight.error) {
			return false;
		}

		boolean end = false;
		int start = new Random().nextInt(2); // 1 - hero starts, 0 - opponent starts
		System.out.println(rs.getString("fightIntro"));
		System.out.printf(rs.getString("heroFightInfo"), hero.getName(), hero.getAttack(), hero.getDefence(),
				hero.getHP(), hero.getWeapon(), hero.getArmour(), fight.heroStats.toString());
		System.out.printf(rs.getString("opponentFightInfo"), fight.opponent.getName(), fight.opponent.getAttack(),
				fight.opponent.getDefence(), fight.opponent.getHP(), fight.opponent.getWeapon(),
				fight.opponent.getArmour(), fight.opponentStats.toString());

		if (start == 1) { // version where hero starts
			System.out.println(rs.getString("playerStartsBattle"));
			while (!(end)) {
				end = fight.heroTurn(); // get hero's turn
				if (end) { // if opponent is true
					return false; // return false - here is NOT dead
				} else {
					System.out.printf(rs.getString("opponentHP"), fight.opponent.getLife());
					System.out.println(rs.getString("opponentTurn"));
					try {
						TimeUnit.SECONDS.sleep(1);
						end = fight.opponentTurn(); // get opponent's turn
					} catch (InterruptedException ie) { // if an error occurs
						System.out.println(rs.getString("playerWin"));
						return false;
					}
					if (hero.getLife() > 0) { // check if hero is still alive
						System.out.printf(rs.getString("playerHP"), hero.getLife());
					}
				}
			}
			System.out.printf(rs.getString("opponentWin"), hero.getScore());
			return true;
		} else { // version where opponent starts first
			System.out.println(rs.getString("opponentStartsBattle"));
			while (!(end)) {
				System.out.println(rs.getString("playerTurn"));
				try {
					TimeUnit.SECONDS.sleep(1);
					end = fight.opponentTurn();
				} catch (InterruptedException ie) {
					System.out.println(rs.getString("playerWin"));
					return false;
				}
				if (end) {
					return true;
				} else {
					System.out.printf(rs.getString("playerHP"), hero.getLife());
					end = fight.heroTurn();
					if (fight.opponent.getLife() > 0) {
						System.out.printf(rs.getString("opponentHP"), fight.opponent.getLife());
					}
				}
			}
			return false;
		}
	}

	/**
	 * Inner class - contains the attributes and methods used in the Fight.
	 * 
	 * @author Andrej
	 *
	 */
	private class Fight {

		private Opponent opponent;
		private FightStats heroStats;
		private FightStats opponentStats;
		public boolean error = false;

		/**
		 * The constructor.
		 * 
		 * @param boss
		 *            - whether it is the fight against the boss, or not
		 */
		protected Fight(boolean boss) {
			int line;

			if (boss) {
				line = 11;
			} else {
				while (true) { // check if the user has not fought yet against this opponent
					line = new Random().nextInt(10) + 1;
					if (!(hero.foughtBefore(line))) {
						break;
					}
				}
			}
			opponent = getOpponent(hero.getLevel(), line); // create a new opponent
			hero.addOpponent(line);
			if (opponent.getHP() > 0) {
				createChances(); // create percentual chances for attack success for all attack types.
			} else { // if an error occurs when creating the opponent (HP = -1 then)
				System.out.println(rs.getString("arenaError"));
				error = true;
			}
		}

		/**
		 * Creates hero's opponent for the fight
		 * 
		 * @param level
		 *            - which file
		 * @param line
		 *            - which line in the file
		 * @return
		 */
		private Opponent getOpponent(int level, int line) {
			Opponent o = new Opponent(level, line);
			return o;
		}

		/**
		 * creates FightStats for opponent and hero
		 */
		private void createChances() {
			heroStats = new FightStats(hero);
			opponentStats = new FightStats(opponent);
			heroStats.percentages(opponentStats);
			opponentStats.percentages(heroStats);
		}

		/**
		 * generates opponent's turn
		 * 
		 * @return - if hero is dead, return true
		 */
		protected boolean opponentTurn() {
			String answer;

			if (opponent.getHP() < hero.getWeapon().getSlashMax()) {
				answer = "throw weapon";
			} else {
				int ran = new Random().nextInt(100);
				if (ran < 34) {
					if (opponent.getWeapon().getName().equals("Bare hands")) {
						answer = "punch";
					} else {
						answer = "stab";
					}
				} else {
					if (opponent.getWeapon().getName().equals("Bare hands")) {
						answer = "slap";
					} else {
						answer = "slash";
					}
				}
			}
			return performAttack("opponent", answer);
		}

		/**
		 * player is prompted to attack
		 * 
		 * @return - returns true, if the opponent is dead after hero's latest attacking
		 *         move.
		 */
		protected boolean heroTurn() {
			List<String> list;
			if (hero.getWeapon().getName().equals("Bare hands")) {
				list = Arrays.asList("punch", "slap");
			} else {
				list = Arrays.asList("slash", "stab", "throw weapon");
			}
			if (list.size() == 2) {
				System.out.println(rs.getString("bareHandsOptions"));
			} else {
				System.out.println(rs.getString("weaponOptions"));
			}
			return performAttack("hero", Input.get(new HashSet<String>(list)));
		}

		/**
		 * Perform the selected type of attack.
		 * 
		 * @param who
		 *            - who is the attacker? hero or opponent?
		 * @param attackType
		 *            - what kind of attack?
		 * @return
		 */
		private boolean performAttack(String who, String attackType) {

			// if the weapon is "Bare hands"
			if (attackType.equals("slap")) {
				attackType = "slash";
			} else if (attackType.equals("punch")) {
				attackType = "stab";
			}

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
			case "slash": // attack == slash
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getSlashChance()) {
					if (who.equals("hero")) {
						System.out.println(rs.getString("playerSlashFail"));
					} else {
						System.out.println(rs.getString("opponentSlashFail"));
					}
				} else {
					if (who.equals("hero")) {
						System.out.println(rs.getString("playerSlashHit"));
					} else {
						System.out.println(rs.getString("opponentSlashHit"));
					}
					damage = attacker.getWeapon().getSlashHit();
				}
				break;

			case "stab": // attack == stab
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getStabChance()) {
					if (who.equals("hero")) {
						System.out.println(rs.getString("playerStabFail"));
					} else {
						System.out.printf(rs.getString("opponentStabFail"), attacker.getName());
					}
				} else {
					if (who.equals("hero")) {
						System.out.println(rs.getString("playerStabHit"));
					} else {
						System.out.println(rs.getString("opponentStabHit"));
					}
					damage = attacker.getWeapon().getStabHit();
				}
				break;

			case "throw weapon": // attack == throw weapon (get rid of it)
				ran = new Random().nextDouble();
				if (ran >= attackerStats.getThrowWeaponChance()) {
					if (who.equals("hero")) {
						System.out.println(
								rs.getString("playerThrowFail"));
					} else {
						System.out.println(
								rs.getString("opponentThrowFail"));
					}
				} else {
					if (who.equals("hero")) {
						System.out.println(
								rs.getString("playerThrowHit"));
					} else {
						System.out.println(
								rs.getString("opponentThrowHit"));
					}
					damage = attacker.getWeapon().getThrowHit();
					if (!(attacker.getWeapon().getName().equals("A boomerang"))) { // boomerang always returns after
																					// throwing
						attacker.setWeapon(new Weapon("Bare hands")); // bare hands after you throw away your weapon
					}
				}
				break;

			default:
				break;
			}
			return defender.damage(damage);
		}

		/**
		 * Contains chances on successful attack/defence
		 * 
		 * @author Andrej
		 *
		 */
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

			/**
			 * create attack/defence indexes
			 * 
			 * @param p
			 */
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

			/**
			 * create success percentages to attackers
			 * 
			 * @param otherGuy
			 */
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
				sb.append(rs.getString("stabChance"));
				sb.append((int) stabChance * 100);
				sb.append(rs.getString("slashChance"));
				sb.append((int) slashChance * 100);
				sb.append(rs.getString("throwChance"));
				sb.append((int) throwWeaponChance * 100);
				sb.append("%");
				return sb.toString();
			}
		}
	}

	// testing main
	/*
	 * public static void main(String[] args) { Arena a = new Arena(new Hero("John",
	 * "att"), new Scanner(System.in)); a.startFight(false); }
	 */
}
