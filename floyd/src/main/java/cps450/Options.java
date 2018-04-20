package cps450;

import java.util.ArrayList;

//Options class that holds all the flags and file names needed 
//for the compiler to work properly.

public class Options {
	
	private static Options optsIntance = null;
	
	boolean debugScanner = false;
	boolean debugParser = false;
	boolean sourceLevDebug = false;
	boolean createASM = false;
	int semanticErrors = 0;
	ArrayList<String> filenames = null;

	public Options(String[] arguments) {
		this.filenames = new ArrayList<String>();
		
		for (String arg : arguments) {
			if (arg.equals("-ds"))
				this.debugScanner = true;
			else if (arg.equals("-dp"))
				this.debugParser = true;
			else if (arg.equals("-S")) {
				this.createASM = true;
			} else if (arg.equals("-g")) {
				this.sourceLevDebug = true;
			} else
				this.filenames.add(arg);
		}
	}
	
	public static Options getInstance(String[] arguments) {
		if(optsIntance == null) {
			optsIntance = new Options(arguments);
		}
		return optsIntance;
	}
	
	public void addSemanticErrors(){
		this.semanticErrors++;
	}

	public int getSemanticErrors() {
		return this.semanticErrors;
	}
	
	public boolean getScanner() {
		return this.debugScanner;
	}

	public boolean getParser() {
		return this.debugParser;
	}

	public boolean getSourceLevDebug() {
		return this.sourceLevDebug;
	}

	public boolean getCreateASM() {
		return this.createASM;
	}

	public ArrayList<String> getFilenames() {
		return this.filenames;
	}

	public int getFilenameSize() {
		return this.filenames.size();
	}
}