// Main.java

package cps450;
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;


public class Main
{
    public static void main(String[] arguments) throws IOException {
        if(arguments.length < 1)
        {
            System.out.println("usage:");
            System.out.println("  lexer <filename>");
            System.out.println("  lexer -ds <filename>");
            System.exit(1);
        }

        if(arguments[0].equals("-ds") && arguments.length > 1) {
            scanFile(arguments, true, 1);
        } else {
            scanFile(arguments, false, 0);
        }
    }

    //Does the reading for every files that was inputed
    public static void scanFile(String[] fileNames, Boolean debug, int position) throws IOException {

        for(int i = position; i < fileNames.length; i++) {
            System.out.println("<*>START<*>");

            CharStream input = CharStreams.fromFileName(fileNames[i]);
            FloydLexer lexer = new FloydLexer(input);
            
            // Read tokens from lexer
            Token t = lexer.nextToken();
            while (t.getType() != FloydLexer.EOF) {
                String textVal = "";

                if (debug) {
                    textVal = DSflag(t);
                } else {
                    textVal = NoDSflag(t);
                }

                if(!textVal.equals("")) {
                    System.out.println(fileNames[i] + ":" + t.getLine() + "," + t.getCharPositionInLine() + ":" + textVal);
                }

                t = lexer.nextToken();
            }
        }
    }

    //Printing for -ds flag
    public static String DSflag(Token t) {
        String textVal = "";

        if(t.getType() > 0 && t.getType() <= 21) {
            textVal = "keyword:" + t.getText();
        } else if(t.getType() > 27 && t.getType() <= 35) {
            textVal = "'" + t.getText() + "'";
        } 
        else {            
            switch (t.getType()) {
                case 22:
                    textVal = "identifier:" + t.getText();
                    break;
                case 23:
                    textVal = "integer lit:" + t.getText();
                    break;
                case 24:
                    textVal = "string lit:" + t.getText();
                    break;
                case 25:
                    textVal = "Illegal string:" + t.getText();
                    break;
                case 26:
                    textVal = "Unterminated string:" + t.getText();
                    break;
                case 27:
                    textVal = "operator:'" + t.getText() + "'";
                    break;
                case 38:
                    textVal = "cr";
                    break;
                case 41:
                    textVal = "Unrecognized char: " + t.getText();
                    break;
            }
        }
        return textVal;
    }

    //Printing for non -ds flag
    public static String NoDSflag(Token t) {
        String textVal = "";

        switch (t.getType()) {
            case 25:
                textVal = "Illegal string:" + t.getText();
                break;
            case 26:
                textVal = "Unterminated string:" + t.getText();
                break;
            case 41:
                textVal = "Unrecognized char: " + t.getText();
                break;
        }
        return textVal;
    }
}

