package cz.cuni.mff.betrayed.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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
public class Controller {

    private enum GameState {
        MAIN_MENU, GAME_MENU, EXIT
    }

    private final int FIGHTS_PER_LEVEL = 6;
    private final int LEVEL_COUNT = 3;
    private final String SAVE_FILE_NAME = "hero.ser";
    private final String SAVE_LOCATION = "." + File.separator + SAVE_FILE_NAME;
    private final String LANGUAGE = "language";
    private final String LANGUAGE_SLOVAK = "sk-SK";
    private final String LANGUAGE_CZECH = "cs-CZ";
    private final String LOCALISATION_ADDRESS = "localization.resource";
    private Logger logger = Logger.getLogger(Controller.class.getName());

    private Hero hero;
    private static final Controller controller = new Controller();
    private Arena arena;
    private GameState exit = GameState.MAIN_MENU;
    private ResourceBundle rs;

    /**
     * The Controller constructor. It can be gained only from public getController()
     * method.
     */
    private Controller() {
        setLanguage();
        try {
            logger.setUseParentHandlers(false);
            FileHandler fh = new FileHandler("error_log.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            logger.addHandler(fh);
        } catch (IOException io) {
        
        }
    }

    /**
     * This method starts the game - it is running the whole time, it is the core of
     * the program. Has three states - Main menu, Game menu and Exit (see GameState
     * enum).
     */
    private void startGame() {
        System.out.println(rs.getString("welcome"));
        while (true) {
            if (exit.equals(GameState.MAIN_MENU)) {
                mainMenu();
            }
            if (exit.equals(GameState.EXIT)) {
                System.out.println(rs.getString("goodbye"));
                System.exit(0);
            }
            while (exit.equals(GameState.GAME_MENU)) {
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
                    exit = GameState.MAIN_MENU;
                } else {
                    hero.addKill();
                    if (isBossFight()) {
                        System.out.println(rs.getString("nextFightBoss"));
                    }
                    if (hero.getFight() == LEVEL_COUNT * FIGHTS_PER_LEVEL) {
                        System.out.println(rs.getString("endGameLine"));
                        System.out.printf(rs.getString("scoreLine"), hero.getScore());
                        exit = GameState.EXIT;
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
                exit = GameState.MAIN_MENU;
                break;
            case "help":
                // TODO implement showing controls/available commands
                System.out.println("Sorry, there is not help yet!");
                break;
            case "exit":
                exit = GameState.EXIT;
            default:
                logger.log(Level.WARNING, "GameMenu input returned an invalid value! Check."); // if there is some
                                                                                               // invalid command
                                                                                               // (shouldn't happen!)
                break;

        }
    }

    /**
     * Checks if the next fight of the hero is a fight against a level boss, or not.
     * 
     * @return - boolean value - true, if the next fight is against a level boss,
     *         false, if it is against a regular opponent from the corresponding
     *         level.
     */
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
                exit = GameState.GAME_MENU; // set menu to game menu
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
                exit = GameState.EXIT;
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SAVE_LOCATION)));) {
            oos.writeObject(hero);
            System.out.println(rs.getString("gameSaved"));
        } catch (IOException e) {
            System.out.println(rs.getString("saveFailed"));
            logger.log(Level.WARNING, "Did not manage to save the Game.", e);
        }
    }

    /**
     * Loads the saved game (or at least tries). If there is no files/failed to
     * load, the player is notified and the game is redirected back to the main
     * menu, where the player can try to perform this action again (in case there
     * was some kind of error...).
     */
    private void loadSavedGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME));) {
            hero = (Hero) ois.readObject();
            arena = new Arena(hero);
            System.out.print(rs.getString("successfulLoad"));
            exit = GameState.GAME_MENU;
        } catch (IOException e) {
            System.out.println(rs.getString("loadFailed"));
            logger.log(Level.WARNING, "Did not manage to load the Game.", e);
        } catch (ClassNotFoundException e) {
            System.out.println(rs.getString("loadFailedClassNotFound"));
            logger.log(Level.WARNING, "Did not manage to load the Game.", e);
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

    /**
     * This method is used in case the user wants to change the current setting of
     * the language of the game.
     */
    private void languageSelection() {
        System.out.println(rs.getString("availableLanguages"));
        System.out.println("en: English\nsk: Slovenina");
        String choice = Input.showOptionsAndGetInput(Options.LANGUAGES);
        if (!(choice.equals("exit"))) {
            Preferences prefs = Prefs.getPrefs();
            if (!(prefs.get("language", null).equals(choice))) {
                prefs.put("language", choice);
                System.out.println(rs.getString("languageChanged"));
            }
        }
    }

    /**
     * This method is taking care of language setting. Default is English, if the
     * player's computer is in Czech/Slovak/player-set Slovak language - the game
     * switches to Slovak localisation.
     */
    private void setLanguage() {
        Preferences prefs = Prefs.getPrefs();
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

    /**
     * Loads corresponding language of the game - according to the settings -
     * setLanguage() method.
     * 
     * @param loc
     *            - Locale, which shall be used.
     */
    private void setResourceBundle(Locale loc) {
        rs = ResourceBundle.getBundle(LOCALISATION_ADDRESS, loc);
    }

    /**
     * @return - the resourceBundle which is set to the correct language
     *         localisation - used everywhere in the game to print
     *         language-customised lines and dialogs.
     */
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
