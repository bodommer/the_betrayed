package cz.cuni.mff.betrayed.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for reading files with Buffered Reader.
 * 
 * @author Andrej
 *
 */
public class MyFileReader implements AutoCloseable {

    private BufferedReader in;
    private Logger logger = Logger.getLogger(MyFileReader.class.getName());

    /**
     * Constructor - opens the file and is ready for reading it. All the files must
     * be located in the resources folder of the program.
     * 
     * @param file
     *            - what is the file name?
     */
    public MyFileReader(String file) {
        in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/files/" + file)));
    }

    /**
     * Reads a line of the opened file.
     * 
     * @return - a single line of file
     */
    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException io) {
            logger.log(Level.SEVERE, "Unable to read a line from a file.", io);
            return "ERROR";
        }
    }

    /**
     * Reads a line of the file and separates it into a String array.
     * 
     * @return - String array split according to a ";" delimiter.
     */
    public String[] readAndSeparateLine() {
        try {
            return in.readLine().split(";");
        } catch (IOException io) {
            logger.log(Level.SEVERE, "Unable to read and divide a line from a file.", io);
            return "ERROR".split(" ");
        }
    }

    /**
     * A method of the AutoCloseable interface.
     */
    public void close() {
        try {
            in.close();
        } catch (IOException io) {
            logger.log(Level.SEVERE, "An error occured while closing MyFileReader.", io);
        }
    }
}
