package cps450;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;

public class MyFloydLexer extends FloydLexer {
	
	boolean dumpTokens;

	public MyFloydLexer(CharStream input, boolean dumpTokens) {
		super(input);
		this.dumpTokens = dumpTokens;
	}

	@Override
	public Token nextToken() {
		Token tok = super.nextToken();
		if (dumpTokens) {
			System.out.println(tok.getLine() + ": " + tok.getText());
		}
		return tok;
	}

}
