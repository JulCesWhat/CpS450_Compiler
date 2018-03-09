package cps450;

//import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.TestCase;

public class SymbolTableTest extends TestCase {

/*	@Test
	public void test() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testSymbolTable() throws Exception {
		
		SymbolTable table = SymbolTable.getInstance();

		Symbol s = table.push("s", new VarDecl(Type.INT));
		assertTrue(s.getName().equals("s"));
		
		table.beginScope();
		assertTrue(table.getScope() == 1);
		
		Symbol x = table.push("x", new VarDecl(Type.INT));
		Symbol y = table.push("y", new VarDecl(Type.INT));
		
		Symbol foundX = table.lookup("x");
		assertNotNull(foundX);
		
		table.beginScope();
		assertTrue(table.getScope() == 2);
		
		Symbol z = table.push("z", new VarDecl(Type.STRING));
		
		table.endScope();
		
		assertEquals(1, table.getScope());
		
		Symbol foundY = table.lookup("y");
		assertNotNull(foundY);
		assertTrue(foundY.getName().equals("y"));
		
		table.endScope();
		
		assertEquals(0, table.getScope());
		
		Symbol foundS = table.lookup("s");
		assertNotNull(foundS);
		assertTrue(foundS.getName().equals("s"));
	}

}
