package cps450;

import java.util.HashMap;

public class ObjReference {

	private static ObjReference orInstance = null;
	public HashMap<String, ClassDecl> classesMap = null;
	
	protected ObjReference() {
		classesMap = new HashMap<String, ClassDecl>();
	}
	
	public static ObjReference getInstance() {
		if (orInstance == null) {
			orInstance = new ObjReference();
		}
		return orInstance;
	}
}