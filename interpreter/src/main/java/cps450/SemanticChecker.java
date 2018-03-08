package cps450;

import java.util.HashMap;

import org.antlr.v4.runtime.Token;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	SymbolTable sblTable;
	
	public SemanticChecker() {
		this.sblTable = SymbolTable.getInstance();
	}
	
	@Override
	public Type visitProgram(FloydParser.ProgramContext ctx) {
		
		if(ctx.classes.size() == 0 || ctx.classes.size() > 1) {
			System.out.println("failure to define exactly one class in a source file");
		} else {
			Type something = visit(ctx.classes.get(0));
		}
		
		return null;
	}
	
  	@Override
  	public Type visitClass_decl(FloydParser.Class_declContext ctx) {
  		
  		//System.out.println(ctx.idS + "  " + ctx.idE);
  		Token idS = ctx.idS;
  		Token idE = ctx.idE;
  		Token idI = ctx.idI;
  		
  		if(idI != null) {
  			System.out.println("Inheritance unsupported feature");
  		} else if(!idS.getText().equals(idE.getText())) {
  			System.out.println("failure to define exactly one class in a source file capi");
  		} else if(idS.getText() == "int" || idS.getText() == "string" || idS.getText() == "boolean") {
  			System.out.println("failure to define class that is not a type");
  		} else {
  			for (FloydParser.Var_declContext varDecl : ctx.claVarDecs) {
  				Type newType = visit(varDecl);
  			}
  			
  			for (FloydParser.Method_declContext metDecl : ctx.claMetDecs) {
  				Type newType = visit(metDecl);
  			}
  		}
  		

  		return null;
  	}
  	
  	@Override
	public Type visitVar_decl(FloydParser.Var_declContext ctx) {
  		
		if(ctx.COLON() != null) {
			Type newType = visit(ctx.type());
			Symbol newSymbol = sblTable.push(ctx.IDENTIFIER().getText(), new VarDecl(newType));
		}
		
		if(ctx.ASGOP() != null) {
			System.out.println("Immediate initialization unsupported feature");
		}
		
		return null;
	}
  	
  	
  	@Override
  	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {
  		this.sblTable.beginScope();
  		
  		if(ctx.argument_decl_list() != null) {
  	  		for (FloydParser.Argument_declContext argsDecl : ctx.argument_decl_list().argsDec) {
  				Type newType = visit(argsDecl);
  	  		}
  		}
  		
  		for (FloydParser.Var_declContext metVarDecl : ctx.metVarDecs) {
			Type newType = visit(metVarDecl);
		}
  		
  		if(ctx.statement_list() != null) {
  	  		for (FloydParser.StatementContext metStmtDecl : ctx.statement_list().stmts) {
  				Type newType = visit(metStmtDecl);
  	  		}
  		}
  		
  		
  		return null;
  	}
  	
  	@Override
  	public Type visitArgument_decl(FloydParser.Argument_declContext ctx) {
  		
  		Type newType = visit(ctx.type());
  		
  		this.sblTable.push(ctx.IDENTIFIER().getText(), new VarDecl(newType));
  		return null;
  	}
	
  	
  	//Type
	@Override
	public Type visitIntType(FloydParser.IntTypeContext ctx) {
		return Type.INT;
	}

	@Override
	public Type visitStrType(FloydParser.StrTypeContext ctx) {
		System.out.println("String ussupported feature.");
		return Type.STRING;
	}
	
	@Override
	public Type visitBoolType(FloydParser.BoolTypeContext ctx) {
		return Type.BOOLEAN;
	}
	
	@Override
	public Type visitIdType(FloydParser.IdTypeContext ctx) {
		System.out.println("String feature unsupported.");
		return Type.ERROR;
	}
	
	
	//Statement
	@Override
	public Type visitAssignment_stmt(FloydParser.Assignment_stmtContext ctx) {
		if(ctx.e1 != null) {
			System.out.println("Not supported behaviour!");
		}
		
		Type newType = visit(ctx.e2);
		
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
		
		return null;
	}
	
	
	
	//Expression
	@Override
	public Type visitExpression_list(FloydParser.Expression_listContext ctx) {
		
		return null;
	}
	
	
	@Override
	public Type visitExpression(FloydParser.ExpressionContext ctx) {
		
		Type newType = visit(ctx.or_expr());
		
		return null;
	}
	
	@Override
	public Type visitOr_expr(FloydParser.Or_exprContext ctx) {
		Type newType = null;
		if(ctx.OR() != null) {
			for (FloydParser.And_exprContext andExprDecl : ctx.andExpr) {
				newType = visit(andExprDecl);
			}
		} else {
			newType = visit(ctx.andExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitAnd_expr(FloydParser.And_exprContext ctx) {
		Type newType = null;
		if(ctx.AND() != null) {
			for (FloydParser.Relational_exprContext relExprDecl : ctx.relExpr) {
				newType = visit(relExprDecl);
			}
		} else {
			newType = visit(ctx.relExpr.get(0));
		}

		return newType;
	}
	
	@Override
	public Type visitRelational_expr(FloydParser.Relational_exprContext ctx) {
		Type newType = null;
		if(ctx.relational_op() != null) {
			for (FloydParser.String_exprContext strExprDecl : ctx.strExpr) {
				newType = visit(strExprDecl);
			}
		} else {
			newType = visit(ctx.strExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitString_expr(FloydParser.String_exprContext ctx) {
		Type newType = null;
		if(ctx.SIGNAND() != null) {
			for (FloydParser.Add_sub_exprContext asExprDecl : ctx.asExpr) {
				newType = visit(asExprDecl);
			}
		} else {
			newType = visit(ctx.asExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitAdd_sub_expr(FloydParser.Add_sub_exprContext ctx) {
		Type newType = null;
		if(ctx.add_sub_op() != null) {
			for (FloydParser.Mul_div_exprContext mdExprDecl : ctx.mdExpr) {
				newType = visit(mdExprDecl);
			}
		} else {
			newType = visit(ctx.mdExpr.get(0));
		}

		return newType;
	}
	
	@Override
	public Type visitMul_div_expr(FloydParser.Mul_div_exprContext ctx) {
		Type newType = null;
		
		if(ctx.mul_div_op() != null) {
			for (FloydParser.Unary_exprContext unaExprDecl : ctx.unaExpr) {
				newType = visit(unaExprDecl);
			}
		} else {
			newType = visit(ctx.unaExpr.get(0));
		}
		
		return newType;
	}
	
	@Override
	public Type visitUnary_expr(FloydParser.Unary_exprContext ctx) {
		
		Type newType = visit(ctx.method_new_expr());

		return newType;
	}
	
	@Override
	public Type visitNewExpr(FloydParser.NewExprContext ctx) {
		
		System.out.println("feature unsupported");
		//Type newType = visit(ctx.type());
		return Type.ERROR;
	}
	
	@Override
	public Type visitPointMethExpr(FloydParser.PointMethExprContext ctx) {
	
		return null;
	}
	
	@Override
	public Type visitMethExpr(FloydParser.MethExprContext ctx) {
		
		return null;
	}

	@Override
	public Type visitPrimExpr(FloydParser.PrimExprContext ctx) {
		Type newType = visit(ctx.primary_expr());

		return null;
	}
	
	
	
	//Primay Expression
	@Override
	public Type visitArrayExpr(FloydParser.ArrayExprContext ctx) {
		System.out.println("feature unsupported");
		return Type.ERROR;
	}
	
	@Override
	public Type visitIdTerm(FloydParser.IdTermContext ctx) {
		String name = ctx.getText();
		Symbol newSymbol = this.sblTable.lookup(name);
		System.out.println(newSymbol.getName() + " " + newSymbol.getScope());
		Declaration newDeclaration = newSymbol.getAttributes();
		return newDeclaration.type;
	}
	
	@Override
	public Type visitStrExpr(FloydParser.StrExprContext ctx) {
		System.out.println("feature unsupported");
		return Type.STRING;
	}
	
	@Override
	public Type visitIntExpr(FloydParser.IntExprContext ctx) {
		
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
	public Type visitNullExpr(FloydParser.NullExprContext ctx) {
		System.out.println("feature unsupported");
		return Type.ERROR;
	}
	
	@Override
	public Type visitMeExpr(FloydParser.MeExprContext ctx) {
		System.out.println("feature unsupported");
		return Type.ERROR;
	}
	
	@Override
	public Type visitParExpr(FloydParser.ParExprContext ctx) {
		Type newType = visit(ctx.expression());
		return newType;
	}
	
}
