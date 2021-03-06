package cps450;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.Token;



public class CodeGen extends FloydBaseVisitor<Type> {

	String fileName = null;
	ArrayList<TargetInstruction> instrucs = null;
	int ifNumGlob;
	int ifNumLoc;
	int loopNumGlob;
	
	Options options;
	String curMethName = null;
	String curClsName = null;
	
	ObjReference orInstance = null;
	
	public CodeGen(String newFileName, Options newOptions) {
		this.fileName = newFileName;
		this.instrucs = new ArrayList<>();
		this.ifNumGlob = -1;
		this.ifNumLoc = -1;
		this.loopNumGlob = -1;
		this.options = newOptions;
		
		orInstance = ObjReference.getInstance();
	}

	@Override
	public Type visitClass_decl(FloydParser.Class_declContext ctx) {

		this.emitComm();
		this.emitComm("# Jwhat331 ...");
		this.emitComm("# CpS450 - Phase4 ...");
		this.emitComm("# " + this.fileName + " ...");
		this.emitComm();
		this.emitnewLin();
		this.emitnewLin();

		this.emitLab(".data");
		
		for (FloydParser.Var_declContext varDecl : ctx.claVarDecs) {
			Type newType = visit(varDecl);
		}
		

		this.emitLab(".text");
		
		//Setting the class
		this.curClsName = ctx.idClS.getText();

		for (FloydParser.Method_declContext metDecl : ctx.claMetDecs) {
			Type newType = visit(metDecl);
		}
		
		this.createAssemblyFile();

		return null;
	}

	@Override
	public Type visitVar_decl(FloydParser.Var_declContext ctx) {

		String varName = ctx.IDENTIFIER().getText();
		int position = findVariablePosition(varName);
		if(position == 0) {
			Token tok = (Token) ctx.IDENTIFIER().getPayload();
			String colon = ctx.COLON().getText() + " ";
			
			this.emitComm(tok.getLine(), varName, colon, ctx.type().getText());
			this.emitDir(".comm", varName + ",4,4");
			this.emitnewLin();
		}
		
		return null;
	}

	@Override
	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {	

		this.emitnewLin();
		this.emitnewLin();
		this.emitnewLin();
		
		int lineMs = ctx.idMeS.getLine();
		String methName = ctx.idMeS.getText();
		this.emitComm();
		this.emitComm(lineMs, ctx.idMeS.getText(), "()");
		this.emitComm();

		if ((methName).equals("start")) {
			this.emitLab(".global	main");
			//this.curMethName = "main";
			this.emitLab("main:");
		} else {
			this.emitLab(".global	" + methName);
			//this.curMethName = methName;
			this.emitLab(methName + ":");
		}
		
		curMethName = methName;
		ClassDecl clsDec = this.orInstance.classesMap.get(curClsName);
		MethodDecl methDec = clsDec.methods.get(methName);
		
		//Parameters
		/*int parameters = 4;
		if(ctx.argument_decl_list() != null ) {
			for (FloydParser.Argument_declContext metVarDecl : ctx.argument_decl_list().argsDec) {
				String capi = metVarDecl.IDENTIFIER().getText();
				parameters+=4;
				
			}
		}*/
		
		
		//Preamble
		this.emitInst("pushl", "%ebp", null);
		this.emitInst("movl", "%esp", "%ebp");
		

		//Saving space for local variables
		if(methDec.localVars.size() > 0) {
			if(methDec.type != Type.VOID) {
				//If the method has a return value
				int size = methDec.localVars.size() + 1;
				this.emitInst("subl", "$" + size*4, "%esp");
				this.emitInst("movl", "$0", "-4(%ebp)");
			} else {
				int size = methDec.localVars.size();
				this.emitInst("subl", "$" + size*4, "%esp");
			}
		} else {
			if(methDec.type != Type.VOID) {
				this.emitInst("subl", "$4", "%esp");
				this.emitInst("movl", "$0", "-4(%ebp)");
			}
		}
		
		
		// Instantiating local variables
		for(FloydParser.Var_declContext varDecl: ctx.metVarDecs) {
			String locVarName = varDecl.IDENTIFIER().getText();
			VarDecl varDec = methDec.localVars.get(locVarName);
			int pos = varDec.position;
			if(varDec.type == Type.INT || varDec.type == Type.BOOLEAN) {
				this.emitInst("movl", "$0", pos + "(%ebp)");
			}
		}
		
		
		
		//Statement lists
		if (ctx.statement_list() != null) {
			for (FloydParser.StatementContext stmts : ctx.statement_list().stmts) {
				
				//With this we handle subIfs
				if(stmts.if_stmt() != null) {
					this.ifNumGlob++;
					this.ifNumLoc = -1;
				}
				Type newType = visit(stmts);
			}
		}
		
		//Postlude
		this.emitnewLin();
		if(methDec.type != Type.VOID) {
			this.emitInst("movl", "-4(%ebp)", "%eax");	
		}
		
		this.emitInst("movl", "%ebp", "%esp");
		this.emitInst("popl", "%ebp", null);
		this.emitInst("ret", "", null);
		
		
		if ((methName).equals("start")) {
			int lineMe = ctx.idMeE.getLine();
			
			this.emitnewLin();
			this.emitComm();
			this.emitComm(lineMe, "end ", ctx.idMeE.getText() );
			this.emitComm();
			
			this.emitInst("pushl", " $0", null);
			this.emitInst("call", "exit", null);
		}

		return null;
	}

	
	
	////////////////////////////////////////////
	// Statement  -->  (statement)
	////////////////////////////////////////////
	@Override
	public Type visitAssignment_stmt(FloydParser.Assignment_stmtContext ctx) {

		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String varName = ctx.IDENTIFIER().getText();
		int lineNum = tok.getLine();
		
		
		//Will look in current method for the variable
		varName = findVariableValue(varName);
		

		this.emitnewLin();
		this.emitComm();
		this.emitComm(lineNum, varName, " := ", ctx.e2.getText());
		this.emitComm();
		

		this.emitComm("# Evaluate RHS ...");
		Type newType = visit(ctx.e2);
		this.emitComm("# Now, do the assignment...");
		this.emitInst("popl", varName, null);

		return null;
	}

	//NEED TO FIX THE NESTED IFS
	@Override
	public Type visitIf_stmt(FloydParser.If_stmtContext ctx) {
		this.ifNumLoc++;

		String ifNum = this.ifNumGlob + "" + this.ifNumLoc;

		this.emitnewLin();
		this.emitComm();
		this.emitComm(ctx.ifS.getLine(), ctx.ifS.getText(), " " + ctx.expression().getText(), " then");
		this.emitComm();

		Type newType = visit(ctx.expression());

		this.emitInst("popl", "%eax", null);
		this.emitInst("cmpl", "$0", "%eax");
		this.emitInst("jne", "_doif" + ifNum, null);

		if (ctx.ELSE() != null) {
			this.emitInst("jmp", "_else" + ifNum, null);
		} else {
			this.emitInst("jmp", "_endif" + ifNum, null);
		}

		this.emitLab("_doif" + ifNum + ":");
		if (ctx.e1 != null) {
			for (FloydParser.StatementContext stmts : ctx.e1.stmts) {
				newType = visit(stmts);
			}
		}
		this.emitInst("jmp", "_endif" + ifNum, null);

		if (ctx.e2 != null) {
			Token tok = (Token) ctx.ELSE().getPayload();

			this.emitnewLin();
			this.emitComm();
			this.emitComm(tok.getLine(), ctx.ELSE().getText(), "", "");
			this.emitComm();

			this.emitLab("_else" + ifNum + ":");
			for (FloydParser.StatementContext stmts : ctx.e2.stmts) {
				newType = visit(stmts);
			}
		}

		this.emitLab("_endif" + ifNum + ":");

		return null;
	}
	
	@Override
	public Type visitLoop_stmt(FloydParser.Loop_stmtContext ctx) {
		this.loopNumGlob++;
		int locNum = this.loopNumGlob;

		this.emitnewLin();
		this.emitComm();
		this.emitComm(ctx.loS.getLine(), ctx.loS.getText(), " " + ctx.expression().getText(), "");
		this.emitComm();

		this.emitLab("_while" + locNum + ":");

		Type newType = visit(ctx.expression());

		this.emitInst("popl", "%eax", null);
		this.emitInst("cmpl", "$0", "%eax");
		this.emitInst("jne", "_startwhilebody" + locNum, null);
		this.emitInst("jmp", "_endwhile" + locNum, null);

		this.emitLab("_startwhilebody" + locNum + ":");
		if (ctx.expression() != null) {

			for (FloydParser.StatementContext stmts : ctx.statement_list().stmts) {
				newType = visit(stmts);
			}
		}

		this.emitInst("jmp", "_while" + locNum, null);
		this.emitLab("_endwhile" + locNum + ":");

		return null;
	}
	
	@Override
	public Type visitCall_stmt(FloydParser.Call_stmtContext ctx) {
		Type newType = null;
		String methName = ctx.IDENTIFIER().getText();
		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		int lineNum = tok.getLine();
		
		
		//Setting name is class or local method
		this.emitnewLin();
		this.emitComm();
		if(ctx.POINT() != null) {
			this.emitComm(lineNum, ctx.expression().getText(), ".", methName + "()");
		} else {
			this.emitComm(lineNum, "", "", methName + "()");
			ClassDecl clsDec = this.orInstance.classesMap.get(this.curClsName);
			MethodDecl methDec = clsDec.methods.get(methName);
			newType = methDec.type;
		}
		this.emitComm();
		
		
		//If method has parameters and the return type does not matter
		if(ctx.expression_list() != null) {
			Type noMatterType = visit(ctx.expression_list());
		}

		this.emitInst("call", methName, null);
		
		if(ctx.expression_list() != null) {
			int params = ctx.expression_list().expression().size();
			this.emitInst("addl", "$" + params * 4, "%esp");
		}

		return newType;
	}
	
	
	
	////////////////////////////////////////////
	// Expression
	////////////////////////////////////////////
	@Override
	public Type visitExpression_list(FloydParser.Expression_listContext ctx) {
		
		for(int i = ctx.expr.size() - 1; i > -1; i--) {
			FloydParser.ExpressionContext expr = ctx.expr.get(i);
			visit(expr);
		}
	
		return null;
	}
	
	@Override
	public Type visitExpression(FloydParser.ExpressionContext ctx) {

		Type newType = visit(ctx.or_expr());

		return newType;
	}
	
	@Override
	public Type visitOr_expr(FloydParser.Or_exprContext ctx) {
		Type newType = null;
		if (ctx.andExpr.size() > 1) {

			for (int i = 0; i < ctx.andExpr.size(); i++) {
				FloydParser.And_exprContext andExprDecl = ctx.andExpr.get(i);
				newType = visit(andExprDecl);

				if (i > 0) {

					this.emitInst("call", "or", null);
					this.emitInst("addl", "$8", "%esp");
					this.emitInst("pushl", "%eax", null);
				}

			}
		} else {
			newType = visit(ctx.andExpr.get(0));
		}

		return newType;
	}

	@Override
	public Type visitAnd_expr(FloydParser.And_exprContext ctx) {
		Type newType = null;
		if (ctx.relExpr.size() > 1) {

			for (FloydParser.Relational_exprContext relExprDecl : ctx.relExpr) {
				newType = visit(relExprDecl);
				System.out.println("Need to still implements visitAnd_expr()'s");
			}
		} else {
			newType = visit(ctx.relExpr.get(0));
		}

		return newType;
	}

	@Override
	public Type visitRelational_expr(FloydParser.Relational_exprContext ctx) {
		Type newType = Type.BOOLEAN;
		if (ctx.relational_op() != null) {
			Type type1 = visit(ctx.strExpr1);
			Type type2 = visit(ctx.strExpr2);

			switch (ctx.relational_op().getText()) {
			case "=":
				this.emitInst("call", "eq", null);
				break;
			case ">=":
				this.emitInst("call", "gtreq", null);
				break;
			case ">":
				this.emitInst("call", "gtr", null);
				break;
			}

			this.emitInst("addl", "$8", "%esp");
			this.emitInst("pushl", "%eax", null);
		} else {
			newType = visit(ctx.strExpr1);
		}

		return newType;
	}

	@Override
	public Type visitString_expr(FloydParser.String_exprContext ctx) {
		Type newType = null;
		if (ctx.asExpr.size() > 1) {
			for (FloydParser.Add_sub_exprContext asExprDecl : ctx.asExpr) {
				newType = visit(asExprDecl);
				System.out.println("Need to still implements visitString_expr()'s");
			}
		} else {
			newType = visit(ctx.asExpr.get(0));
		}

		return newType;
	}

	@Override
	public Type visitAdd_sub_expr(FloydParser.Add_sub_exprContext ctx) {
		Type newType = null;
		ArrayList<String> values = new ArrayList<String>();
		
		if (ctx.mdExpr.size() > 1) {

			for (int i = 0; i < ctx.mdExpr.size(); i++) {
				FloydParser.Mul_div_exprContext mdExprDecl = ctx.mdExpr.get(i);
				newType = visit(mdExprDecl);

				if (i > 0) {

					switch (ctx.add_sub_op().get(i - 1).getText()) {
					case "+":
						this.emitInst("call", "add", null);
						break;
					case "-":
						this.emitInst("call", "sub", null);
						break;
					}

					this.emitInst("addl", "$8", "%esp");
					this.emitInst("pushl", "%eax", null);
				}
			}

		} else {
			newType = visit(ctx.mdExpr.get(0));
		}

		return newType;
	}
	
	@Override
	public Type visitMul_div_expr(FloydParser.Mul_div_exprContext ctx) {
		Type newType = null;

		if (ctx.unaExpr.size() > 1) {

			for (int i = 0; i < ctx.unaExpr.size(); i++) {
				FloydParser.Unary_exprContext unaExprDecl = ctx.unaExpr.get(i);
				newType = visit(unaExprDecl);

				if (i > 0) {
					switch (ctx.mul_div_op().get(i - 1).getText()) {
					case "*":
						this.emitInst("call", "mul", null);
						break;
					case "/":
						this.emitInst("call", "div", null);
						break;
					}


					this.emitInst("addl", "$8", "%esp");
					this.emitInst("pushl", "%eax", null);
				}
			}

		} else {
			newType = visit(ctx.unaExpr.get(0));
		}

		return newType;
	}
	
	@Override
	public Type visitUnary_expr(FloydParser.Unary_exprContext ctx) {

		Type newType = visit(ctx.method_new_expr());

		if (ctx.unary_op() != null) {
			if (ctx.unary_op().NOT() != null) {
				this.emitInst("popl", "%eax", null);
				this.emitInst("xorl", "$1", "%eax");
				this.emitInst("pushl", "%eax", null);
				// xor eax, 1
			} else if (ctx.unary_op().MINUS() != null) {
				this.emitInst("call", "neg", null);
				this.emitInst("addl", "$4", "%esp");
				this.emitInst("pushl", "%eax", null);
			}
		}

		return newType;
	}
	
	@Override
	public Type visitPointMethExpr(FloydParser.PointMethExprContext ctx) {

		//Type newType = visit(ctx.primary_expr());

		String methName = ctx.IDENTIFIER().getText();
		
		this.emitInst("call", methName, null);
		this.emitInst("pushl", "%eax", null);


		return null;
	}

	
	////////////////////////////////////////////
	// Method New Expression  ==>  (method_new_expr)
	////////////////////////////////////////////
	@Override
	public Type visitMethExpr(FloydParser.MethExprContext ctx) {
		Type newType = null;
		//Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String methName = ctx.IDENTIFIER().getText();
		//int lineNum = tok.getLine();
		
		
		ClassDecl clasDec = this.orInstance.classesMap.get(this.curClsName);
		MethodDecl methDec = clasDec.methods.get(methName);
		int params = methDec.parameters.size();
				
		
	/*	this.emitnewLin();
		this.emitComm();
		this.emitComm(lineNum, "", "", methName + "()");
		this.emitComm();
		*/
		
		newType = visit(ctx.expression_list());

		this.emitInst("call", methName, null);
		
		//If method has parameters
		if(params > 0) {
			this.emitInst("addl", "$" + params * 4, "%esp");	
		}
		
		// IF method has return value
		if(methDec.type != Type.VOID) {
			this.emitInst("pushl", "%eax", null);
		}

		return newType;
	}

	@Override
	public Type visitPrimExpr(FloydParser.PrimExprContext ctx) {
		Type newType = visit(ctx.primary_expr());
		
		return newType;
	}
	
	
	
	////////////////////////////////////////////
	// Primay Expression  -->  (primary_expr)
	////////////////////////////////////////////
	@Override
	public Type visitIdTerm(FloydParser.IdTermContext ctx) {
		String var = ctx.getText();
		
		//Will look in current method for the variable
		var = findVariableValue(var);
		
		this.emitInst("pushl", var, null);
		
		return Type.OBJ;
	}

	@Override
	public Type visitIntExpr(FloydParser.IntExprContext ctx) {
		
		String num = ctx.getText();
		this.emitInst("pushl", "$" + num, null);
		
		return Type.INT;
	}

	@Override
	public Type visitTrueExpr(FloydParser.TrueExprContext ctx) {
		
		this.emitInst("pushl", "$1", null);
		
		return Type.BOOLEAN;
	}

	@Override
	public Type visitFalseExpr(FloydParser.FalseExprContext ctx) {
		
		this.emitInst("pushl", "$0", null);
		
		return Type.BOOLEAN;
	}

	@Override
	public Type visitParExpr(FloydParser.ParExprContext ctx) {
		Type newType = visit(ctx.expression());
		return newType;
	}

	
	
	
	////////////////////////////////////////////
	// Helpful Methods
	////////////////////////////////////////////
	public String findVariableValue(String varName) {
		
		ClassDecl clsDec = this.orInstance.classesMap.get(this.curClsName);
		MethodDecl methDec = clsDec.methods.get(curMethName);
		
		if (methDec.parameters.containsKey(varName) ) {
			VarDecl varDec = methDec.parameters.get(varName);
			varName = varDec.position + "(%ebp)";
		} else if (methDec.localVars.containsKey(varName)) {
			VarDecl varDec = methDec.localVars.get(varName);
			varName = varDec.position + "(%ebp)";
		} else if(methDec.type != Type.VOID && varName.equals(curMethName)) {
			varName = "-4(%ebp)";
		}
		//Else it the variable must be global
		
		return varName;
	}
	
	public int findVariablePosition(String varName) {
		int position = 0;
		ClassDecl clsDec = this.orInstance.classesMap.get(this.curClsName);
		
		if(this.curClsName == null) {
			return position;
		}
		
		MethodDecl methDec = clsDec.methods.get(curMethName);
		
		if (methDec.parameters.containsKey(varName) ) {
			VarDecl varDec = methDec.parameters.get(varName);
			position = varDec.position;
		} else if (methDec.localVars.containsKey(varName)) {
			VarDecl varDec = methDec.localVars.get(varName);
			position = varDec.position;
		}
		
		return position;
	}
	
	
	
	

	////////////////////////////////////////////
	// Defined Methods
	////////////////////////////////////////////
	public void emitLab(String newLab) {
		this.instrucs.add(new Label(newLab));
	}
	
	public void emitInst(String newIns, String newOp1, String newOp2) {
		if(newOp2 == null) {
			this.instrucs.add(new Instruction(newIns, newOp1));
		} else {
			this.instrucs.add(new Operands(newIns, newOp1, newOp2));
		}
	}
	
	public void emitComm(int line, String name, String op, String comp) {
		//# Line 2: x: int
		//  or
		//# Line 7: x := 5
		//  or
		//# Line 9: out.writeint(x)
		String comment = "# Line " + line + ": " + name + op + comp;
		this.instrucs.add(new Comment(comment));
	}
	
	public void emitComm(int line, String methName, String methComp) {
		String comment = "# Line " + line + ": " + methName + methComp;
		this.instrucs.add(new Comment(comment));
	}
	
	public  void emitComm(String newComm) {
		String comm = "        " + newComm;
		this.instrucs.add(new Comment(comm));
	}
	
	public void emitComm() {
		this.instrucs.add(new Comment("# -----------------------------------------"));
	}
	
	public void emitnewLin() {
		this.instrucs.add(new Comment(""));
	}
	
	public void emitDir(String newDir, String dirComp) {
		this.instrucs.add(new Directive(newDir, dirComp));
	}
	
	
	
	public void createAssemblyFile() {
		String[] fileNaPar = this.fileName.split("\\.");

		List<String> lines = new ArrayList<>();
		for (TargetInstruction targIns : this.instrucs) {
			lines.add(targIns.getText());
		}

		try {

			Path file = Paths.get(fileNaPar[0] + ".s");
			Files.write(file, lines, Charset.forName("UTF-8"));

			if(!this.options.createASM) {
				ProcessBuilder pr = new ProcessBuilder("gcc", "-m32", fileNaPar[0] + ".s", "stdlib.o", "-o" + fileNaPar[0]);
				Process process = pr.start();

				process.waitFor();
				
				if(process.exitValue() == 0) {
					System.out.println("Succes at creating the executable.");
				}
			}

		} catch (Exception e) {
			System.out.println("There was an error: " + e.getMessage());
		}
	}
}
