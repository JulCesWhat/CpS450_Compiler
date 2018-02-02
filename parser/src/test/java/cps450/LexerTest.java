package cps450;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class LexerTest {
	FloydLexer lex;

	//Basic tests for Floyd language
	@Test
	public void testSuccessfulScan() throws IOException {
		CharStream input = CharStreams.fromStream(
				getClass().getResourceAsStream("phase1test.floyd"));
		lex = new FloydLexer(input);

		//assertNextToken(FloydLexer.UNREQ_CHAR, "%");
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.END);
		assertNextToken(FloydLexer.UNREQ_STRING_LITERAL);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.ILLIGAL_STRING_LITERAL);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.CLASS);
		assertNextToken(FloydLexer.IDENTIFIER, "Main");
		assertNextToken(FloydLexer.IS);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.IDENTIFIER, "x");
		assertNextToken(FloydLexer.COLON);
		assertNextToken(FloydLexer.INT);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.ENDOFLINE_1);

		assertNextToken(FloydLexer.IDENTIFIER, "start");
		assertNextToken(FloydLexer.LPAREN);
		assertNextToken(FloydLexer.RPAREN);
		assertNextToken(FloydLexer.IS);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.IDENTIFIER, "i");
		assertNextToken(FloydLexer.COLON);
		assertNextToken(FloydLexer.INT);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.BEGIN);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.IDENTIFIER, "print");
		assertNextToken(FloydLexer.LPAREN);
		//assertNextToken(FloydLexer.STRING_LITERAL, "\"Hey,\"Sue!\"\"");  <-- Took me some time to realize that it is not the same. :)
		assertNextToken(FloydLexer.STRING_LITERAL);
		assertNextToken(FloydLexer.OPERATORS, "&");
		assertNextToken(FloydLexer.IDENTIFIER, "name");
		assertNextToken(FloydLexer.RPAREN);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.END);
		assertNextToken(FloydLexer.ENDOFLINE_1);

		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.UNREQ_CHAR, "%");
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.INTEGER_LITERAL);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.INTEGER_LITERAL);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.OPERATORS, "&");
		assertNextToken(FloydLexer.OPERATORS, "+");
		assertNextToken(FloydLexer.STRING_LITERAL);
		assertNextToken(FloydLexer.EOF);

	}

	private void assertNextToken(int type, String value) throws IOException {
		Token tok = lex.nextToken();
		System.err.println(tok.getLine() + ":" + tok.getCharPositionInLine() + ":" + tok.getText());
		assertTrue(tok.getType() == type);
		assertTrue(tok.getText().equals(value));

	}

	private void assertNextToken(int type) throws IOException {
		Token tok = lex.nextToken();
		System.err.println(tok.getLine() + ":" + tok.getCharPositionInLine() + ":" + tok.getText());
		assertTrue(tok.getType() == type);
	}
}
