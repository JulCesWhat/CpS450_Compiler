// Main.java

package cps450;
import java.io.IOException;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;


public class Main
{
    public static void main(String[] arguments) throws IOException {
        if(arguments.length == 0)
        {
            System.out.println("usage:");
            System.out.println("  parser -ds -dp <filename>");
            System.exit(1);
        }


        boolean debugScanner = false;
        boolean debugParser = false;
        ArrayList<Integer> filenames = new ArrayList<Integer>();
        for (String arg: arguments) {
        	if (arg.equals("-ds"))
        		debugScanner = true;
        	else if (arg.equals("-dp"))
        		debugParser = true;
        	else
        		filenames.add(arg);
        }

        
        if (filename == null) {
        	System.err.println("You must supply a filename to parse.");
        	System.exit(1);
        }


        CharStream input = CharStreams.fromFileName(filename);
        MyTinyLexer lexer = new MyTinyLexer(input, debugScanner);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TinyParser parser = new TinyParser(tokens);
        
        //parser.setErrorHandler(new TinyErrorHandler());
        // Suppress default error messages
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        // Register my own error handler
        parser.addErrorListener(new MyTinyErrorListener());
        
        ParseTree tree = parser.program();
        
        if (debugParser)
        	// Display graphical tree
        	Trees.inspect(tree, parser);
        
        System.out.println(parser.getNumberOfSyntaxErrors() + " syntax error(s)");
    }

//     //Does the reading for every files that was inputed
//     public static void scanFile(ArrayList<Integer> fileNames, Boolean debug) throws IOException {

//         for(int i = 0; i < fileNames.length; i++) {
//             System.out.println("<*>START<*>");

//             CharStream input = CharStreams.fromFileName(fileNames[i]);
//             FloydLexer lexer = new FloydLexer(input);
            
//             // Read tokens from lexer
//             Token t = lexer.nextToken();
//             while (t.getType() != FloydLexer.EOF) {
//                 String textVal = "";

//                 if (debug) {
//                     textVal = DSflag(t);
//                 } else {
//                     textVal = NoDSflag(t);
//                 }

//                 if(!textVal.equals("")) {
//                     System.out.println(fileNames[i] + ":" + t.getLine() + "," + t.getCharPositionInLine() + ":" + textVal);
//                 }

//                 t = lexer.nextToken();
//             }
//         }
//     }

//     //Printing for -ds flag
//     public static String DSflag(Token t) {
//         String textVal = "";

//         if(t.getType() > 0 && t.getType() <= 21) {
//             textVal = "keyword:" + t.getText();
//         } else if(t.getType() > 27 && t.getType() <= 35) {
//             textVal = "'" + t.getText() + "'";
//         } 
//         else {            
//             switch (t.getType()) {
//                 case 22:
//                     textVal = "identifier:" + t.getText();
//                     break;
//                 case 23:
//                     textVal = "integer lit:" + t.getText();
//                     break;
//                 case 24:
//                     textVal = "string lit:" + t.getText();
//                     break;
//                 case 25:
//                     textVal = "Illegal string:" + t.getText();
//                     break;
//                 case 26:
//                     textVal = "Unterminated string:" + t.getText();
//                     break;
//                 case 27:
//                     textVal = "operator:'" + t.getText() + "'";
//                     break;
//                 case 38:
//                     textVal = "cr";
//                     break;
//                 case 41:
//                     textVal = "Unrecognized char: " + t.getText();
//                     break;
//             }
//         }
//         return textVal;
//     }

//     //Printing for non -ds flag
//     public static String NoDSflag(Token t) {
//         String textVal = "";

//         switch (t.getType()) {
//             case 25:
//                 textVal = "Illegal string:" + t.getText();
//                 break;
//             case 26:
//                 textVal = "Unterminated string:" + t.getText();
//                 break;
//             case 41:
//                 textVal = "Unrecognized char: " + t.getText();
//                 break;
//         }
//         return textVal;
//     }
// }

