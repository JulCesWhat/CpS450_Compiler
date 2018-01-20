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

        if(arguments[0] == "-ds" && arguments.length > 1) {
            scanFile(arguments, true, 1);
        } else {
            scanFile(arguments, false, 0);
        }
    }

    //Does the reading for every files that was inputed
    public static void scanFile(String[] fileNames, Boolean debug, int position) throws IOException {
        for(int i = position; i < fileNames.length; i++) {
            System.out.println();

            CharStream input = CharStreams.fromFileName(fileNames[i]);
            FloydLexer lexer = new FloydLexer(input);
            
            // Read tokens from lexer
            Token t = lexer.nextToken();
            while (t.getType() != FloydLexer.EOF) {
            System.out.println(fileNames[i] + ":" + t.getLine() + ":" + t.getText());
            t = lexer.nextToken();
            }
        }
    }

}

