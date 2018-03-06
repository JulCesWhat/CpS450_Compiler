package cps450;

import java.util.ArrayList;

class Declaration {
	Type type; // all symbols have a Type
	
	
}

class VarDecl extends Declaration {
	// more will go here eventually ...
	public VarDecl(Type newType) {
		super.type = newType;
	}
}

class MethodDecl extends Declaration {
	// more will go here eventually ...
	ArrayList<VarDecl> localVar = new ArrayList<>();
	ArrayList<VarDecl> parameters = new ArrayList<>();
}

class ClassDecl extends Declaration {
	// more will go here eventually ...
	ArrayList<MethodDecl> methods = new ArrayList<>();
	ArrayList<VarDecl> glovalVar = new ArrayList<>();
}