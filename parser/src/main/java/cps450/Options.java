package cps450;

import java.util.ArrayList;


//Options class that holds all the flags and file names needed 
//for the compiler to work properly.

public class Options {
    boolean debugScanner = false;
    boolean debugParser = false;
    ArrayList<String> filenames = new ArrayList<String>();

    public Options(String[] arguments) {
        for (String arg: arguments) {
        	if (arg.equals("-ds"))
        		debugScanner = true;
        	else if (arg.equals("-dp"))
        		debugParser = true;
        	else
        		filenames.add(arg);
        }
    }

    public boolean getScanner() {
        return debugScanner;
    }

    public boolean getParser() {
        return debugParser;
    }

    public ArrayList<String> getFilenames() {
        return filenames;
    }

	// public int getFilenameLength() {
	// 	return filenames.length
	// }
}