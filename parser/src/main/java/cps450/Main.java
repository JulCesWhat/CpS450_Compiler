// Main.java

package cps450;
import java.io.IOException;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.ArrayList;


public class Main
{
    public static void main(String[] arguments) throws IOException {
        if(arguments.length == 0)
        {
            System.out.println("usage:");
            System.out.println("  parser -ds -dp <filename>");
            System.exit(1);
        }

        Options options = new Options(arguments);
        
        if (options.getFilenames().size() == 0) {
        	System.err.println("You must supply a filename to parse.");
        	System.exit(1);
        }

        doLogic(options);
    }

    public static void doLogic(Options options) throws IOException {
        
        CharStream input = CharStreams.fromFileName(options.getFilenames().get(0));
        MyFloydLexer lexer = new MyFloydLexer(input, options.getScanner());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FloydParser parser = new FloydParser(tokens);
        
        //parser.setErrorHandler(new TinyErrorHandler());
        // Suppress default error messages
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        // Register my own error handler
        parser.addErrorListener(new MyFloydErrorListener());
        
        ParseTree tree = parser.program();
        
        if (options.getParser())
        	// Display graphical tree
        	Trees.inspect(tree, parser);
        
        System.out.println(parser.getNumberOfSyntaxErrors() + " syntax error(s)");
    }
}