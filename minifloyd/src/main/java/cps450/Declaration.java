package cps450;

import java.util.HashMap;

class Declaration {
	public Type type; // all symbols have a Type
	
}

class VarDecl extends Declaration {
	// more will go here eventually ...
	public VarDecl(Type newType) {
		super.type = newType;
	}
}

class MethodDecl extends Declaration {
	// more will go here eventually ...
	public HashMap<String, VarDecl> localVars;
	public HashMap<String, VarDecl> parameters;
	
	public MethodDecl() {
		this.localVars = new HashMap<>();
		this.parameters = new HashMap<>();
	}
}

class ClassDecl extends Declaration {
	// more will go here eventually ...
	public HashMap<String, MethodDecl> methods;
	public HashMap<String, VarDecl> glovalVars;
	public String name;
	
	public ClassDecl(String newName) {
		this.name = newName;
		this.methods = new HashMap<>();
		this.glovalVars = new HashMap<>();
	}
}