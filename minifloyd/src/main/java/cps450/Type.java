package cps450;

public class Type {

	public static final Type 
		ERROR = new Type("<error>"),
		INT = new Type("int"),
		BOOLEAN = new Type("boolean"),
		STRING = new Type("string"),
		VOID = new Type("void"),
		OBJ = new Type("object");

	protected String name;

	
	protected Type(String name) {
		this.name = name;
	}
}
