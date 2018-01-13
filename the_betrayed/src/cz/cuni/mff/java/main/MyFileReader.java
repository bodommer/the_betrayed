package cz.cuni.mff.java.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is responsible for reading files with Buffered Reader.
 * 
 * @author Andrej
 *
 */
public class MyFileReader {
	BufferedReader in;

	/**
	 * Constructor - opens the file and is ready for reading it.
	 * 
	 * @param folder
	 *            - which folder/package is the file in?
	 * @param file
	 *            - what is the file name?
	 */
	public MyFileReader(String folder, String file) {
		try {
			StringBuilder sb = new StringBuilder().append(new File(new File(".").getAbsolutePath()).getCanonicalPath());
			// StringBuilder sb = new StringBuilder().append(".");
			/*
			 * sb.append(File.separator); sb.append("src"); sb.append(File.separator);
			 * sb.append("cz"); sb.append(File.separator); sb.append("cuni");
			 * sb.append(File.separator); sb.append("mff"); sb.append(File.separator);
			 * sb.append("java"); sb.append(File.separator); sb.append(folder);
			 */
			sb.append(File.separator);
			sb.append(file);

			String url = sb.toString();
			// System.out.println(url);
			in = new BufferedReader(new FileReader(url));
		} catch (IOException io) {
			System.out.println("ERROR");
		}
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
			return "ERROR".split(" ");
		}
	}

	public void close() {
		try {
			in.close();
		} catch (IOException io) {
		}
	}
}
