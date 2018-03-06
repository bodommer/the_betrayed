package cz.cuni.mff.betrayed.places;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import cz.cuni.mff.betrayed.character.Hero;
import cz.cuni.mff.betrayed.character.Opponent;
import cz.cuni.mff.betrayed.character.Person;
import cz.cuni.mff.betrayed.equipment.Weapon;
import cz.cuni.mff.betrayed.inputOptions.Options;
import cz.cuni.mff.betrayed.main.Controller;
import cz.cuni.mff.betrayed.main.Input;

/**
 * This is the most important place. Here, the fights take place and it is the
 * whole idea of the game.
 * 
 * @author Andrej
 *
 */
public class Arena {

	private Random random = new Random();
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
	 * @return returns true if hero dies, false if the opponent died.
	 */
	public boolean startFight(boolean boss) {
		Fight fight = new Fight(boss);

		if (fight.error) {
			return false;
		}

		boolean end = false;
		int start = random.nextInt(2); // 1 - hero starts, 0 - opponent starts
		System.out.println(rs.getString("fightIntro"));
		printFightersInfo(fight);

		if (start == 1) { // version where hero starts
			System.out.println(rs.getString("playerStartsBattle"));
			while (!(end)) {
				end = fight.heroTurn(); // get hero's turn
				if (end) { // if opponent is true
					return false; // return false - hero is NOT dead
				} else {
					System.out.printf(rs.getString("opponentHP"), fight.opponent.getLife());
					try {
						end = opponentTurn(fight); // get opponent's turn
					} catch (InterruptedException ie) {
						return false;
					}
					if (isAlive(hero)) { // check if hero is still alive
						System.out.printf(rs.getString("playerHP"), hero.getLife());
					}
				}
			}
			System.out.printf(rs.getString("opponentWin"), hero.getScore());
			return true;
		} else { // version where opponent starts first
			System.out.println(rs.getString("opponentStartsBattle"));
			while (!(end)) {
				try {
					end = opponentTurn(fight); // get opponent's turn
				} catch (InterruptedException ie) {
					return false;
				}
				if (end) {
					return true;
				} else {
					System.out.printf(rs.getString("playerHP"), hero.getLife());
					end = fight.heroTurn();
					if (isAlive(fight.opponent)) {
						System.out.printf(rs.getString("opponentHP"), fight.opponent.getLife());
					}
				}
			}
			return false;
		}
	}

	private void printFightersInfo(Fight fight) {

		System.out.printf(rs.getString("heroFightInfo"), hero.getName(), hero.getAttack(), hero.getDefence(),
				hero.getHP(), hero.getWeapon().toString(), hero.getArmour().toString(), fight.heroStats.toString());

		System.out.printf(rs.getString("opponentFightInfo"), fight.opponent.getName(), fight.opponent.getAttack(),
				fight.opponent.getDefence(), fight.opponent.getHP(), fight.opponent.getWeapon().toString(),
				fight.opponent.getArmour().toString(), fight.opponentStats.toString());
	}

	private boolean opponentTurn(Fight fight) throws InterruptedException {
		System.out.println(rs.getString("opponentTurn"));
		try {
			TimeUnit.SECONDS.sleep(1);
			return fight.opponentTurn();
		} catch (InterruptedException ie) {
			System.out.println(rs.getString("playerWin"));
			throw ie;
		}
	}

	private boolean isAlive(Person p) {
		return p.getLife() > 0;
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
		protected Fight(boolean isBoss) {

			opponent = getOpponent(hero.getLevel(), isBoss); // create a new opponent
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
		private Opponent getOpponent(int level, boolean isBoss) {
			int line;
			if (isBoss) {
				line = 11;
			} else {
				while (true) { // check if the user has not fought yet against this opponent
					line = random.nextInt(10) + 1;
					if (!(hero.foughtBefore(line))) {
						break;
					}
				}
			}
			hero.addOpponent(line);
			return new Opponent(level, line);
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
				int ran = random.nextInt(100);
				if (ran < 34) {
					if (opponent.getWeapon().getCode().equals("hands")) {
						answer = "punch";
					} else {
						answer = "stab";
					}
				} else {
					if (opponent.getWeapon().getCode().equals("hands")) {
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
			String[] options;
			if (hero.getWeapon().getCode().equals("hands")) {
				options = Options.HANDS_FIGHT.getOptions();
			} else {
				options = Options.WEAPON_FIGHT.getOptions();
			}
			if (options.length == 2) {
				System.out.println(rs.getString("bareHandsOptions"));
			} else {
				System.out.println(rs.getString("weaponOptions"));
			}
			return performAttack("hero", Input.showOptionsAndGetInput(options));
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
				damage = slashAttack(who, attacker, attackerStats);
				/*
				 * ran = random.nextDouble(); if (ran >= attackerStats.getSlashChance()) { if
				 * (who.equals("hero")) { if (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("playerSlapFail")); } else {
				 * System.out.println(rs.getString("playerSlashFail")); } } else { if
				 * (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("opponentSlapFail")); } else {
				 * System.out.println(rs.getString("opponentSlashFail")); } } } else { if
				 * (who.equals("hero")) { if (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("playerSlapHit")); } else {
				 * System.out.println(rs.getString("playerSlashHit")); } } else { if
				 * (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("opponentSlapHit")); } else {
				 * System.out.println(rs.getString("opponentSlashHit")); } } damage =
				 * attacker.getWeapon().getSlashHit(); }
				 */
				break;

			case "stab": // attack == stab
				damage = stabAttack(who, attacker, attackerStats);
				/*
				 * ran = random.nextDouble(); if (ran >= attackerStats.getStabChance()) { if
				 * (who.equals("hero")) { if (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("playerPunchFail")); } else {
				 * System.out.println(rs.getString("playerStabFail")); } } else { if
				 * (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.printf(rs.getString("opponentPunchFail"), attacker.getName()); }
				 * else { System.out.printf(rs.getString("opponentStabFail"),
				 * attacker.getName()); } } } else { if (who.equals("hero")) { if
				 * (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("playerPunchHit")); } else {
				 * System.out.println(rs.getString("playerStabHit")); } } else { if
				 * (attacker.getWeapon().getCode().equals("hands")) {
				 * System.out.println(rs.getString("opponentPunchHit")); } else {
				 * System.out.println(rs.getString("opponentStabHit")); } } damage =
				 * attacker.getWeapon().getStabHit(); }
				 */
				break;

			case "throwWeapon": // attack == throw weapon (get rid of it)
				damage = throwAttack(who, attacker, attackerStats);

				/*
				 * ran = random.nextDouble(); if (ran >= attackerStats.getThrowWeaponChance()) {
				 * if (who.equals("hero")) { System.out.println(
				 * rs.getString("playerThrowFail")); } else { System.out.println(
				 * rs.getString("opponentThrowFail")); } } else { if (who.equals("hero")) {
				 * System.out.println( rs.getString("playerThrowHit")); } else {
				 * System.out.println( rs.getString("opponentThrowHit")); } damage =
				 * attacker.getWeapon().getThrowHit(); } if
				 * (!(attacker.getWeapon().getCode().equals("boomerang"))) { // boomerang always
				 * returns after // throwing attacker.setWeapon(new Weapon("hands")); // bare
				 * hands after you throw away your weapon }
				 */
				break;

			default:
				break;
			}
			return defender.damage(damage);
		}

		private int slashAttack(String who, Person attacker, FightStats attackerStats) {
			int damage = 0;
			double ran = random.nextDouble();

			if (ran >= attackerStats.getSlashChance()) {
				if (who.equals("hero")) {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("playerSlapFail"));
					} else {
						System.out.println(rs.getString("playerSlashFail"));
					}
				} else {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("opponentSlapFail"));
					} else {
						System.out.println(rs.getString("opponentSlashFail"));
					}
				}
			} else {
				if (who.equals("hero")) {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("playerSlapHit"));
					} else {
						System.out.println(rs.getString("playerSlashHit"));
					}
				} else {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("opponentSlapHit"));
					} else {
						System.out.println(rs.getString("opponentSlashHit"));
					}
				}
				damage = attacker.getWeapon().getSlashHit();
			}
			return damage;
		}

		private int stabAttack(String who, Person attacker, FightStats attackerStats) {

			int damage = 0;
			double ran = random.nextDouble();

			if (ran >= attackerStats.getStabChance()) {
				if (who.equals("hero")) {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("playerPunchFail"));
					} else {
						System.out.println(rs.getString("playerStabFail"));
					}
				} else {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.printf(rs.getString("opponentPunchFail"), attacker.getName());
					} else {
						System.out.printf(rs.getString("opponentStabFail"), attacker.getName());
					}
				}
			} else {
				if (who.equals("hero")) {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("playerPunchHit"));
					} else {
						System.out.println(rs.getString("playerStabHit"));
					}
				} else {
					if (attacker.getWeapon().getCode().equals("hands")) {
						System.out.println(rs.getString("opponentPunchHit"));
					} else {
						System.out.println(rs.getString("opponentStabHit"));
					}
				}
				damage = attacker.getWeapon().getStabHit();
			}
			return damage;
		}

		private int throwAttack(String who, Person attacker, FightStats attackerStats) {

			int damage = 0;
			double ran = random.nextDouble();

			if (ran >= attackerStats.getThrowWeaponChance()) {
				if (who.equals("hero")) {
					System.out.println(rs.getString("playerThrowFail"));
				} else {
					System.out.println(rs.getString("opponentThrowFail"));
				}
			} else {
				if (who.equals("hero")) {
					System.out.println(rs.getString("playerThrowHit"));
				} else {
					System.out.println(rs.getString("opponentThrowHit"));
				}
				damage = attacker.getWeapon().getThrowHit();
			}
			if (!(attacker.getWeapon().getCode().equals("boomerang"))) { // boomerang always returns after
																			// throwing
				attacker.setWeapon(new Weapon("hands")); // bare hands after you throw away your weapon
			}
			return damage;
		}

		/**
		 * Contains chances on successful attack/defence
		 * 
		 * @author Andrej
		 *
		 */
		private class FightStats {

			private final double ATTACK_SLASHATT_MODIFIER = 0.4;
			private final double REFLEXES_SLASHATT_MODIFIER = 0.1;
			private final double WEAPON_SLASHATT_MODFIER = 0.5;
			private final double ATTACK_STABATT_MODIFIER = 0.45;
			private final double WEAPON_STABATT_MODIFIER = 0.55;
			private final double ATTACK_THROWATT_MODIFIER = 0.3;
			private final double STRENGTH_THROWATT_MODIFIER = 0.2;
			private final double WEAPON_THROWATT_MODIFIER = 0.5;
			private final double DEFENCE_SLAHSDEF_MODIFIER = 0.6;
			private final double REFLEXES_SLASHDEF_MODIFIER = 0.2;
			private final double DEFENCE_STABDEF_MODIFIER = 0.4;
			private final double REFLEXES_STABDEF_MODIFIER = 0.5;
			private final double REFLEXES_THROWDEF_MODIFIER = 0.5;
			private final double DEFENCE_THROWDEF_MODIFIER = 0.3;
			private final double SLASH_CHANCE_MODIFIER = 1.35;
			private final double STAB_CHANCE_MODIFIER = 0.97;
			private final double THROW_CHANCE_MODIFIER = 0.38;
			private final double SLASH_HIGH_VALUE = 0.99;
			private final double SLASH_LOW_VALUE = 0.4;
			private final double STAB_HIGH_VALUE = 0.69;
			private final double STAB_LOW_VALUE = 0.2;
			private final double THROW_HIGH_VALUE = 0.29;
			private final double THROW_LOW_VALUE = 0.08;
			private final int PERCENTAGE = 100;
			
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
				slashAtt = p.getAttack() * ATTACK_SLASHATT_MODIFIER + p.getReflexes() * REFLEXES_SLASHATT_MODIFIER
						+ WEAPON_SLASHATT_MODFIER * (p.getWeapon().getPowerIndex(p.getStrength()));
				
				stabAtt = ATTACK_STABATT_MODIFIER * p.getAttack() + WEAPON_STABATT_MODIFIER * (p.getWeapon().getPowerIndex(p.getStrength()));
				
				throwWeaponAtt = ATTACK_THROWATT_MODIFIER * p.getAttack() + STRENGTH_THROWATT_MODIFIER * p.getStrength()
						+ WEAPON_THROWATT_MODIFIER * (p.getWeapon().getPowerIndex(p.getStrength()));

				slashDef = p.getDefence() * DEFENCE_SLAHSDEF_MODIFIER + p.getReflexes() * REFLEXES_SLASHDEF_MODIFIER + p.getArmour().getDefIndex(p.getStrength());
				
				stabDef = p.getDefence() * DEFENCE_STABDEF_MODIFIER + p.getReflexes() * REFLEXES_STABDEF_MODIFIER + p.getArmour().getDefIndex(p.getStrength());
				
				throwWeaponDef = p.getReflexes() * REFLEXES_THROWDEF_MODIFIER + p.getDefence() * DEFENCE_THROWDEF_MODIFIER + p.getArmour().getDefIndex(p.getStrength());
			}

			/**
			 * create success percentages to attackers
			 * 
			 * @param otherGuy
			 */
			protected void percentages(FightStats otherGuy) {
				setSlashChance(SLASH_CHANCE_MODIFIER * getSlashAtt() / otherGuy.getSlashDef());
				setStabChance(STAB_CHANCE_MODIFIER * getStabAtt() / otherGuy.getStabDef());
				setThrowWeaponChance(THROW_CHANCE_MODIFIER * getThrowWeaponAtt() / otherGuy.getThrowWeaponDef());
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
				if (d > SLASH_HIGH_VALUE) {
					slashChance = SLASH_HIGH_VALUE;
				} else if (d < SLASH_LOW_VALUE) {
					slashChance = SLASH_LOW_VALUE;
				} else {
					slashChance = ((double) Math.round(d * PERCENTAGE)) / PERCENTAGE;
				}
			}

			public double getSlashChance() {
				return slashChance;
			}

			private void setStabChance(double d) {
				if (d > STAB_HIGH_VALUE) {
					stabChance = STAB_HIGH_VALUE;
				} else if (d < STAB_LOW_VALUE) {
					stabChance = STAB_LOW_VALUE;
				} else {
					stabChance = ((double) Math.round(d * PERCENTAGE)) / PERCENTAGE;
				}
			}

			public double getStabChance() {
				return stabChance;
			}

			private void setThrowWeaponChance(double d) {
				if (d > THROW_HIGH_VALUE) {
					throwWeaponChance = THROW_HIGH_VALUE;
				} else if (d < THROW_LOW_VALUE) {
					throwWeaponChance = THROW_LOW_VALUE;
				} else {
					throwWeaponChance = ((double) Math.round(d * PERCENTAGE)) / PERCENTAGE;
				}
			}

			public double getThrowWeaponChance() {
				return throwWeaponChance;
			}

			@Override
			public String toString() {
				StringBuilder sb = new StringBuilder();
				sb.append(rs.getString("stabChance"));
				sb.append(Math.round(stabChance * PERCENTAGE));
				sb.append(rs.getString("slashChance"));
				sb.append(Math.round(slashChance * PERCENTAGE));
				sb.append(rs.getString("throwChance"));
				sb.append(Math.round(throwWeaponChance * PERCENTAGE));
				sb.append("%");
				return rs.getString("stabChance") + Math.round(stabChance * PERCENTAGE) + 
						rs.getString("slashChance") + Math.round(slashChance * PERCENTAGE) + 
						rs.getString("throwChance") + Math.round(throwWeaponChance * PERCENTAGE) + "%";
			}
		}
	}

	// testing main
	/*
	 * public static void main(String[] args) { Arena a = new Arena(new Hero("John",
	 * "att"), new Scanner(System.in)); a.startFight(false); }
	 */
}
