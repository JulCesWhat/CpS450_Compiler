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
            System.out.println("<>START<>");

            CharStream input = CharStreams.fromFileName(fileNames[i]);
            FloydLexer lexer = new FloydLexer(input);
            
            // Read tokens from lexer
            Token t = lexer.nextToken();
            while (t.getType() != FloydLexer.EOF) {
                //System.out.println(t.getType() + "  -Type");

                String textVal = "";

                if (1 == FloydLexer.EOF) {
                    System.out.println("capivare");
                }

                //if (t.getType() != FloydLexer.INVALID_TYPE) {
                    if(t.getType() > 0 && t.getType() <= 21) {
                        textVal = "keyword:" + t.getText();
                    } else {
                        
                        switch (t.getType()) {
                            case 22:
                                textVal = "identifier:" + t.getText();
                                break;
                            case 24:
                                textVal = "string lit:" + t.getText();
                                break;
                            case 25:
                                textVal = "operator:'" + t.getText() + "'";
                                break;
                            case 26:
                                textVal = "'" + t.getText() + "'";
                                break;
                            case 28:
                                textVal = "cr";
                                break;
                            default:
                                textVal = "Unrecognized char   ---   " + t.getType();
                                break;
                        }
                    }
                //} else {
                //    System.out.println("CApivara");
                //}
            

                System.out.println(fileNames[i] + ":" + t.getLine() + "," + t.getCharPositionInLine() + ":" + textVal);
                t = lexer.nextToken();
            }
        }
    }

}

