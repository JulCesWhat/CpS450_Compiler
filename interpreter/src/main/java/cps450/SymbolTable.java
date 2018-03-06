package cps450;
import java.util.HashMap;
import java.util.ArrayList;


public final class SymbolTable {
	
	private static SymbolTable sbInstance = null;
	public HashMap<String, ArrayList<Symbol>> symbolHM = null;
	private int scope;
	
	
	protected SymbolTable() {
		this.symbolHM = new HashMap<>();
		this.scope = 0;
	}

	public static SymbolTable getInstance() {
		if (sbInstance == null) {
			sbInstance = new SymbolTable();
		}
		return sbInstance;
	}
	
	public int getScope() {
		return scope;
	}
	
	public Symbol push(String name,Declaration decl) {
		Symbol newSymbol = new Symbol(this.scope, name, decl);
		String stScope = Integer.toString(this.scope);
		
		symbolHM.putIfAbsent(stScope, new ArrayList<Symbol>());
		symbolHM.get(stScope).add(newSymbol);
		
		return newSymbol;
	}
	
	
	//Looks up for a symbol 
	public Symbol lookup(String name) {
		
		for(int i = this.scope; i > -1; i--) {
			if(symbolHM.containsKey(Integer.toString(i))) {
				ArrayList<Symbol> symbolList = symbolHM.get(Integer.toString(i));
				for(int j = symbolList.size() -1; -1 < j; j--) {
					Symbol extSymbol = symbolList.get(j);
					if(extSymbol.getName().equals(name)) {
						return extSymbol;
					}
				}	
			}
		}
		
		return null;
	}
	
	public void beginScope() {
		this.scope++;
	}
	
	public void endScope() throws Exception {
		
		if(this.getScope() == 0) {
			throw new Exception("Error! Scope is 0.");
		}
		
		String strScope = Integer.toString(this.scope);
		
		if(symbolHM.containsKey(strScope)) {
			symbolHM.remove(strScope);
		}
		this.scope--;
		
	}

}
