package cps450;



class TargetInstruction {
	String text;
	
	public String getText() {
		return null;
	}
}

// Ex => 	.text
class Label extends TargetInstruction {
	
	public Label(String newLab) {
		super.text = newLab;
	}
	
	public String getText() {
		return super.text;
	}
}

// Ex => 	pushl	$3
class Instruction extends TargetInstruction {
	
	public Instruction(String newInst, String newOp1) {
		super.text = "        " + newInst +  "    " + newOp1;
	}
	
	public String getText() {
		return super.text;
	}
}

// Ex => 	addl	%eax, %ebx
class Operands extends TargetInstruction {
	
	public Operands(String newInst, String newOp1, String newOp2) {
		super.text = "        " + newInst + "    " + newOp1 + ", " + newOp2;
	}
	
	public String getText() {
		return super.text;
	}
}

//Ex => 	# Evaluate RHS ...
class Comment extends TargetInstruction {
	
	public Comment(String newComm) {
		super.text = newComm;
	}
	
	public String getText(){
		return super.text;
	}
}


//This is the global var declaration
//Ex => 	.comm	y,4,4
class Directive extends TargetInstruction {
	
	public Directive(String newDir, String dirComp) {
		super.text = "	" + newDir + "	" + dirComp;
	}
	
	public String getText(){
		return super.text;
	}
}