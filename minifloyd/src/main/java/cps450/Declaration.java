package cps450;

import java.util.HashMap;
import java.util.ArrayList;;

class Declaration {
	public Type type;
	public String kind;
}

class VarDecl extends Declaration {
	public VarDecl(Type newType) {
		super.type = newType;
		super.kind = "variable";
	}
}

class MethodDecl extends Declaration {
	// more will go here eventually ...
	public HashMap<String, VarDecl> localVars;
	public ArrayList<VarDecl> parameters;
	
	public MethodDecl(Type newType) {
		super.type = newType;
		super.kind = "method";
		this.localVars = new HashMap<>();
		this.parameters = new ArrayList<>();
	}
}

class ClassDecl extends Declaration {
	// more will go here eventually ...
	public HashMap<String, MethodDecl> methods;
	public HashMap<String, VarDecl> glovalVars;
	
	public ClassDecl() {
		super.kind = "class";
		super.type = Type.VOID;
		this.methods = new HashMap<>();
		this.glovalVars = new HashMap<>();
	}
}