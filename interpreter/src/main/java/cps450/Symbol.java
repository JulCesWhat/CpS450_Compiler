package cps450;

public class Symbol {
	
	private int scope;
	private String name;
	private Declaration attributes;
	
	
	public Symbol(int newScope, String newName, Declaration newAttribute) {
		this.scope = newScope;
		this.name = newName;
		this.attributes = newAttribute;
	}
	
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
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
