package cps450;
import java.util.*;


public class SymbolTable {
	
	private Stack<Symbol> stStack;
	private int scope;
	
	
	public SymbolTable() {
		this.stStack = new Stack<>();
		this.scope = 0;
	}
	
	public Stack<Symbol> getStStack() {
		return stStack;
	}

/*	public void setStStack(Stack<Symbol> stStack) {
		this.stStack = stStack;
	}*/

	public int getScope() {
		return scope;
	}

/*	public void setScope(int scope) {
		this.scope = scope;
	}*/
	
	public Symbol push(String name,Declaration decl) {
		Symbol newSymbol = new Symbol(this.scope, name, decl);
		stStack.push(newSymbol);
		return newSymbol;
	}
	
	public Symbol lookup(String name) {
		Stack<Symbol> localStack = new Stack<>();
		Symbol newSymbol = null;
		
		for(int i = this.stStack.size(); i > -1; i--) {
			newSymbol = this.stStack.pop();
			localStack.push(newSymbol);
			if(newSymbol.getName().equals(name)) {
				i = -1;
			}
			newSymbol = null;
		}
		
		for(int i = localStack.size(); i > -1; i--) {
			newSymbol = localStack.pop();
			this.stStack.push(newSymbol);
		}
		
		return newSymbol;
	}
	
	public void beginScope() {
		this.scope++;
	}
	
	public void endScope() throws Exception {
		
		if(this.getScope() == 0) {
			throw new Exception("Error! Scope is 0.");
		}
		
		for(int i = this.stStack.size(); i > -1; i--) {
			Symbol newSymbol = this.stStack.pop();

			if(newSymbol.getScope() != this.scope) {
				i = -1;
				this.stStack.push(newSymbol);
			}
		}
	}

}
