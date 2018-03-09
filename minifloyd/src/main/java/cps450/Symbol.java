package cps450;

public class Symbol {
	
	private int scopeS;
	private String name;
	private Declaration attributes;
	
	
	public Symbol(int newScopeS, String newName, Declaration newAttribute) {
		this.scopeS = newScopeS;
		this.name = newName;
		this.attributes = newAttribute;
	}
	
	public int getScopeS() {
		return scopeS;
	}
	public void setScopeS(int scopeS) {
		this.scopeS = scopeS;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Declaration getAttributes() {
		return attributes;
	}
	public void setAttributes(Declaration attributes) {
		this.attributes = attributes;
	}
}
