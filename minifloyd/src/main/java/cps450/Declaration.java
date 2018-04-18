package cps450;

import java.util.HashMap;
import java.util.ArrayList;;

class Declaration {
	public Type type;
	public String kind;
}

class VarDecl extends Declaration {
	int position = 0;
	public VarDecl(Type newType, int newPosition) {
		super.type = newType;
		super.kind = "variable";
		this.position = newPosition;
	}
	
	public VarDecl(Type newType) {
		super.type = newType;
		super.kind = "variable";
	}
}

class MethodDecl extends Declaration {
	// more will go here eventually ...
	public HashMap<String, VarDecl> localVars;
	public HashMap<String, VarDecl> parameters;
	
	public MethodDecl(Type newType) {
		super.type = newType;
		super.kind = "method";
		this.localVars = new HashMap<>();
		this.parameters = new HashMap<>();
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