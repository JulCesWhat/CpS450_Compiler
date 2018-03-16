package cps450;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;



public class CodeGen extends FloydBaseVisitor<Type> {

	String fileName = null;
	ArrayList<TargetInstruction> instrucs = null;
	int ifNumGlob;
	int ifNumLoc;
	int loopNumGlob;
	Options options;
	boolean gFlag;
	String curMethName;

	public CodeGen(String newFileName, Options newOptions) {
		this.fileName = newFileName;
		this.instrucs = new ArrayList<>();
		this.ifNumGlob = -1;
		this.ifNumLoc = -1;
		this.loopNumGlob = -1;
		this.options = newOptions;
		this.gFlag = this.options.sourceLevDebug;
		this.curMethName = "";
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

		
		if (this.gFlag) {
			this.emitDir(".file", "\"" + this.fileName + "\"");
			this.emitDir(".stabs", "\"" + this.fileName + "\",100,0,0,.Ltext0");
			this.emitDir(".text", "");
			this.emitLab(".Ltext0:");
			this.emitDir(".stabs", "\"int:t(0,1)=r(0,1);-2147483648;2147483647;\",128,0,0,0");
		}

		this.emitLab(".data");
		
		for (FloydParser.Var_declContext varDecl : ctx.claVarDecs) {
			Type newType = visit(varDecl);
		}
		


		this.emitLab(".text");

		for (FloydParser.Method_declContext metDecl : ctx.claMetDecs) {
			Type newType = visit(metDecl);
		}
		
		this.createAssemblyFile();

		return null;
	}

	@Override
	public Type visitVar_decl(FloydParser.Var_declContext ctx) {

		String varName = ctx.IDENTIFIER().getText();
		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String colon = ctx.COLON().getText() + " ";

		
		this.emitComm(tok.getLine(), varName, colon, ctx.type().getText());
		this.emitDir(".comm", varName + ",4,4");
		if (this.gFlag) {
			this.emitDir(".stabs", "\"" + varName + ":G(0,1)\",32,0,0,0");
		}
		this.emitnewLin();

		return null;
	}

	@Override
	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {

		int lineMs = ctx.idMeS.getLine();
		String methName = ctx.idMeS.getText();
		this.emitComm();
		this.emitComm(lineMs, ctx.idMeS.getText(), "()");
		this.emitComm();

		if ((methName).equals("start")) {
			this.emitLab(".global	main");
			this.curMethName = "main";
			if (this.gFlag) {
				this.emitDir(".stabs", "\"main:F\",36,0,0,main");
			}
			this.emitLab("main:");
		} else {
			this.emitLab(".global	" + methName);
			this.curMethName = methName;
			if (this.gFlag) {
				this.emitDir(".stabs", "\"" + methName + ":F\",36,0,0," + methName);
			}
			this.emitLab(methName + ":");
		}

		for (FloydParser.Var_declContext metVarDecl : ctx.metVarDecs) {
			System.out.println("Need to print here in visitMethod_decl() -> metVarDecs");
		}

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
		
		int lineMe = ctx.idMeE.getLine();
		
		this.emitnewLin();
		this.emitComm();
		this.emitComm(lineMe, "end ", ctx.idMeE.getText() );
		this.emitComm();
		
		if(this.gFlag) {
			this.emitDir(".stabn", "68,0," + lineMe + ",.line" + lineMe + "-" + this.curMethName);
			this.emitLab(".line" + lineMe + ":");
		}
		
		
		this.emitInst("pushl", " $0", null);
		this.emitInst("call", "exit", null);

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

		this.emitnewLin();
		this.emitComm();
		this.emitComm(lineNum, varName, " := ", ctx.e2.getText());
		this.emitComm();
		
		if(this.gFlag) {
			this.emitDir(".stabn", "68,0," + lineNum + ",.line" + lineNum + "-" + this.curMethName);
			this.emitLab(".line" + lineNum + ":");
		}


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
		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String methName = ctx.IDENTIFIER().getText();
		int lineNum = tok.getLine();
		
		this.emitnewLin();
		this.emitComm();
		this.emitComm(lineNum, ctx.expression().getText(), ".", methName + "()");
		this.emitComm();
		
		if(this.gFlag) {
			this.emitDir(".stabn", "68,0," + lineNum + ",.line" + lineNum + "-" + this.curMethName);
			this.emitLab(".line" + lineNum + ":");
		}
		
		newType = visit(ctx.expression_list());

		this.emitInst("call", methName, null);
		this.emitInst("addl", "$4", "%esp");

		return newType;
	}
	
	
	
	////////////////////////////////////////////
	// Expression
	////////////////////////////////////////////
	@Override
	public Type visitExpression_list(FloydParser.Expression_listContext ctx) {
		
		for (FloydParser.ExpressionContext expr : ctx.expr) {
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
					this.emitInst("push", "%eax", null);
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
			this.emitInst("push", "%eax", null);
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
					this.emitInst("push", "%eax", null);
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
					this.emitInst("push", "%eax", null);
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
		this.emitInst("pushl", var, null);
		
		return Type.ERROR;
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
