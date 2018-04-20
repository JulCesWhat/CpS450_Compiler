package cps450;
import java.util.HashMap;
import java.util.ArrayList;


public final class SymbolTable {
	
	private static SymbolTable sbInstance = null;
	public ArrayList<HashMap<String, Symbol>> symbolAL = null;
	private int scope;
	
	
	protected SymbolTable() {
		this.symbolAL = new ArrayList<>();
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
	
	public Symbol push(String name, Declaration decl) {
		Symbol newSymbol = new Symbol(this.scope, name, decl);
		
		if(symbolAL.size() -1 >= this.scope) {
			symbolAL.get(this.scope).put(name, newSymbol);
		} else {
			HashMap<String, Symbol> localHM = new HashMap<>();
			localHM.put(name, newSymbol);
			symbolAL.add(localHM);
		}
		
		return newSymbol;
	}
	
	
	//Looks up for a symbol 
	public Symbol lookup(String name) {
		
		for(int i = symbolAL.size() -1; i > -1; i--) {
			if(symbolAL.get(i).containsKey(name)) {
				return symbolAL.get(i).get(name);
			}
		}
		
		return null;
	}
	
	//Looks up for a symbol 
		public Symbol lookup(String name, Symbol parSbl) {
			
			for(int i = symbolAL.size() -1; i > -1; i--) {
				if(symbolAL.get(i).containsKey(name)) {
					Symbol fndSbl = symbolAL.get(i).get(name);
					Declaration fndDec = fndSbl.getAttributes();
					
					Declaration parDec = parSbl.getAttributes();
					if(parDec.kind.equals(fndDec.kind)) {
						return fndSbl;
					}
					//return symbolAL.get(i).get(name);
				}
			}
			
			return null;
		}
	
	public boolean lookupInScope(String newName, int newScope) {
		boolean found = false;

		if(this.symbolAL.size() > newScope && this.symbolAL.get(newScope).containsKey(newName)) {
			found = true;
		}

		return found;
	}
	
	public void beginNewScope() {
		this.scope++;
	}
	
	public void endScope() throws Exception {
		
		if(this.getScope() == 0) {
			throw new Exception("Error! Scope is 0.");
		}
		
		if(symbolAL.size() -1 >= this.scope) {
			symbolAL.remove(this.scope);
		}
		
		this.scope--;
		
	}

}
