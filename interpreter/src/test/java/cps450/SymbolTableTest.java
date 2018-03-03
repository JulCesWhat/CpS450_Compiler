package cps450;

import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.TestCase;

public class SymbolTableTest extends TestCase {

/*	@Test
	public void test() {
		fail("Not yet implemented");
	}*/

	public void testSymbolTable() {
		SymbolTable table = new SymbolTable();

		Symbol s = table.push("x", new VarDecl(Type.INT));
		assertTrue(s.getName().equals("x"));
		// ...
	}

}
