package cps450;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

public class LexerTest {
	FloydLexer lex;

	@Test
	public void testSuccessfulScan() throws IOException {
		CharStream input = CharStreams.fromStream(
				getClass().getResourceAsStream("phase1test.floyd"));
		lex = new FloydLexer(input);

		//assertNextToken(FloydLexer.UNREQ_CHAR, "%");
		assertNextToken(FloydLexer.STRING_LITERAL);
		assertNextToken(FloydLexer.ENDOFLINE_1);
		assertNextToken(FloydLexer.CLASS, "class");
		assertNextToken(FloydLexer.IDENTIFIER, "Main");
		assertNextToken(FloydLexer.IS, "is");
		//assertNextToken(FloydLexer.EOF);

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
