package cz.cuni.mff.java.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyFileReader {
	BufferedReader in;
	
	public MyFileReader(String folder, String file) {
		try {
			StringBuilder sb = new StringBuilder().append(new File(new File(".").getAbsolutePath()).getCanonicalPath());
			sb.append(File.separator);
			sb.append("src");
			sb.append(File.separator);
			sb.append("cz");
			sb.append(File.separator);
			sb.append("cuni");
			sb.append(File.separator);
			sb.append("mff");
			sb.append(File.separator);
			sb.append("java");
			sb.append(File.separator);
			sb.append(folder);
			sb.append(File.separator);
			sb.append(file);
			
			String url = sb.toString();
			in = new BufferedReader(new FileReader(url));
		} catch (IOException io) {
			System.out.println("ERROR");
		}
	}
	
	public String readLine() {
		try {
			return in.readLine();
		} catch (IOException io) {
			return "ERROR";
		}
	}
	
	public String[] readAndSeparateLine() {
		try {
			return in.readLine().split(";");
		} catch (IOException io) {
			return "ERROR".split("");
		}
	}
	
}
