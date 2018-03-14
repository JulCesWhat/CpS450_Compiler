package cps450;

import java.util.ArrayList;
import org.antlr.v4.runtime.Token;

public class CodeGen extends FloydBaseVisitor<Type> {

	String fileName = null;
	ArrayList<TargetInstruction> instrucs = null;

	public CodeGen(String newFileName) {
		this.fileName = newFileName;
		this.instrucs = new ArrayList<>();
	}

	@Override
	public Type visitClass_decl(FloydParser.Class_declContext ctx) {

		for (FloydParser.Var_declContext varDecl : ctx.claVarDecs) {
			Type newType = visit(varDecl);
		}

		for (FloydParser.Method_declContext metDecl : ctx.claMetDecs) {
			Type newType = visit(metDecl);
		}

		return null;
	}

	@Override
	public Type visitVar_decl(FloydParser.Var_declContext ctx) {

		String varName = ctx.IDENTIFIER().getText();
		Token tok = (Token) ctx.IDENTIFIER().getPayload();

		System.out.println("# Line " + tok.getLine() + ": " + varName + ": " + ctx.type().getText());
		System.out.println("	.comm	" + varName + ",4,4 \n");

		return null;
	}

	@Override
	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {

		System.out.println("	.text");

		String lineMs = Integer.toString(ctx.idMeS.getLine());
		System.out.println("# -----------------------------------------");
		System.out.println("# Line " + lineMs + ": " + ctx.idMeS.getText() + "()");
		System.out.println("# -----------------------------------------\n");

		System.out.println(".global	main");
		System.out.println("main:");

		for (FloydParser.Var_declContext metVarDecl : ctx.metVarDecs) {
			System.out.println("Need to print here in visitMethod_decl() -> metVarDecs");
		}

		if (ctx.statement_list() != null) {
			for (FloydParser.StatementContext stmts : ctx.statement_list().stmts) {
				Type newType = visit(stmts);
			}
		}
		
		String lineMe = Integer.toString(ctx.idMeS.getLine());
		System.out.println("# -----------------------------------------");
		System.out.println("# Line " + lineMs + ": end " + ctx.idMeE.getText());
		System.out.println("# -----------------------------------------\n");
		System.out.println("        pushl $0");
		System.out.println("        call  exit");

		return null;
	}

	
	
	////////////////////////////////////////////
	// Statement  -->  (statement)
	////////////////////////////////////////////
	@Override
	public Type visitAssignment_stmt(FloydParser.Assignment_stmtContext ctx) {

		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String varName = ctx.IDENTIFIER().getText();

		System.out.println("# -----------------------------------------");
		System.out.println("# Line " + tok.getLine() + ": " + varName + " := " + ctx.e2.getText());
		System.out.println("# -----------------------------------------");

		System.out.println("	# Evaluate RHS ...");
		Type newType = visit(ctx.e2);
		System.out.println("	# Now, do the assignment...");
		System.out.println("        popl    " + varName + "\n\n");

		return null;
	}

	@Override
	public Type visitIf_stmt(FloydParser.If_stmtContext ctx) {
		
		return null;
	}
	
	@Override
	public Type visitLoop_stmt(FloydParser.Loop_stmtContext ctx) {
		
		return null;
	}
	
	@Override
	public Type visitCall_stmt(FloydParser.Call_stmtContext ctx) {
		Type newType = null;
		Token tok = (Token) ctx.IDENTIFIER().getPayload();
		String methName = ctx.IDENTIFIER().getText();
		
		System.out.println("# -----------------------------------------");
		System.out.println("# Line " + tok.getLine() + ": " + ctx.expression().getText() + "." + methName + "(" + ctx.expression_list().getText() + ")");
		System.out.println("# -----------------------------------------");
		
		newType = visit(ctx.expression_list());

		System.out.println("        call	" + methName);
		System.out.println("        addl	$4, %esp\n\n");

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
		if(ctx.andExpr.size() > 1) {
			
			for (FloydParser.And_exprContext andExprDecl : ctx.andExpr) {
				newType = visit(andExprDecl);
				System.out.println("Need to still implements visitOr_expr()'s");
			}
		} else {
			newType = visit(ctx.andExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitAnd_expr(FloydParser.And_exprContext ctx) {
		Type newType = null;
		if(ctx.relExpr.size() > 1) {
			
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
		if(ctx.relational_op() != null) {
			Type type1 = visit(ctx.strExpr1);
			Type type2 = visit(ctx.strExpr2);

			System.out.println("Need to still implements visitRelational_expr()'s");
		} else {
			newType = visit(ctx.strExpr1);
		}
		
		return newType;
	}
	
	@Override
	public Type visitString_expr(FloydParser.String_exprContext ctx) {
		Type newType = null;
		if(ctx.asExpr.size() > 1) {
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
		if(ctx.mdExpr.size() > 1) {
			
			for(int i = 0; i < ctx.mdExpr.size(); i++) {
				FloydParser.Mul_div_exprContext mdExprDecl = ctx.mdExpr.get(i);
				newType = visit(mdExprDecl);
				
				if(i > 0) {
					System.out.println("        call	add");
					System.out.println("        addl	$8, %esp");
					System.out.println("        push	%eax");
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
		
		if(ctx.unaExpr.size() > 1) {
			
			for (FloydParser.Unary_exprContext unaExprDecl : ctx.unaExpr) {
				newType = visit(unaExprDecl);
				System.out.println("Need to still implements visitMul_div_expr()'s");
			}
		} else {
			newType = visit(ctx.unaExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitUnary_expr(FloydParser.Unary_exprContext ctx) {
		
		Type newType = visit(ctx.method_new_expr());
		
		if(ctx.unary_op() != null) {
			if(ctx.unary_op().NOT() != null) {

			} else if(ctx.unary_op().MINUS() != null) {
				
			}
		}

		return newType;
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
		System.out.println("        pushl    " + var);
		
		return Type.ERROR;
	}

	@Override
	public Type visitIntExpr(FloydParser.IntExprContext ctx) {
		
		String num = ctx.getText();
		System.out.println("        pushl   $" + num);
		
		return Type.INT;
	}

	@Override
	public Type visitTrueExpr(FloydParser.TrueExprContext ctx) {

		return Type.BOOLEAN;
	}

	@Override
	public Type visitFalseExpr(FloydParser.FalseExprContext ctx) {

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
	public void emit(String toWrite) {

	}

	public void createAssemblyFile() {

	}
}
