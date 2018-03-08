package cps450;

import java.util.ArrayList;

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
	public ArrayList<VarDecl> localVars;
	public ArrayList<VarDecl> parameters;
	
	public MethodDecl(Type newType) {
		super.type = newType;
		this.localVars = new ArrayList<>();
		this.parameters = new ArrayList<>();
	}
}

class ClassDecl extends Declaration {
	// more will go here eventually ...
	public ArrayList<MethodDecl> methods;
	public ArrayList<VarDecl> glovalVars;
	
	public ClassDecl(Type newType) {
		super.type = newType;
		this.methods = new ArrayList<>();
		this.glovalVars = new ArrayList<>();
	}
}