package cps450;

import java.util.HashMap;
import org.antlr.v4.runtime.Token;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	SymbolTable sblTable;
	String fileName;
	
	public SemanticChecker(String newFileName) {
		this.sblTable = SymbolTable.getInstance();
		this.fileName = newFileName;
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
  		
  		if(ctx.idClIn != null) {
  			this.printErrorNT(ctx.idClIn.getLine(), ctx.idClIn.getCharPositionInLine(), "feature unsupported");
  		} else if(!ctx.idClS.getText().equals(ctx.idClE.getText())) {
  			System.out.println("failure to define exactly one class in a source file capi");
  		} else if(ctx.idClS.getText() == "int" || ctx.idClS.getText() == "string" || ctx.idClS.getText() == "boolean") {
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
  		
  		String varName = ctx.IDENTIFIER().getText();
		if(ctx.COLON() != null) {
			int curScope = this.sblTable.getScope();		
			
			if(this.sblTable.lookupInScope(varName, curScope)) {
				Token tok = (Token) ctx.IDENTIFIER().getPayload();
				this.printError(tok, "Redefined identifier " + varName);
			} else {
				Type newType = visit(ctx.type());
				Symbol newSymbol = this.sblTable.push(varName, new VarDecl(newType));
			}
		}
		
		if(ctx.ASGOP() != null && this.sblTable.getScope() == 0) {
			Token tok = (Token) ctx.ASGOP().getPayload();
			this.printError(tok, "feature unsupported");
		}
		
		return null;
	}
  	
  	
  	@Override
  	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {
  		this.sblTable.beginScope();
  		
  		if(!(ctx.idMeS.getText()).equals(ctx.idMeE.getText())) {
  			System.out.println("Method not declared correctly");
  		} else if(!(ctx.idMeS.getText()).equals("start")) {
  			this.printErrorNT(ctx.idMeS.getLine(), ctx.idMeE.getCharPositionInLine(), "feature unsupported");
  		}
  		
  		
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
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		return Type.STRING;
	}
	
	@Override
	public Type visitBoolType(FloydParser.BoolTypeContext ctx) {
		return Type.BOOLEAN;
	}
	
	@Override
	public Type visitIdType(FloydParser.IdTypeContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		return Type.ERROR;
	}
	
	
	//Statement
	@Override
	public Type visitAssignment_stmt(FloydParser.Assignment_stmtContext ctx) {
		if(ctx.e1 != null) {
			Token tok = (Token) ctx.e1.getPayload();
			this.printError(tok, "feature unsupported");
		}
		
		String varName = ctx.IDENTIFIER().getText();
		Symbol newSymbol = this.sblTable.lookup(varName);
		if(newSymbol == null) {
			Token newToken = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(newToken, "Undeclared identifier " + varName);
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
		
		return newType;
	}
	
	@Override
	public Type visitOr_expr(FloydParser.Or_exprContext ctx) {
		Type newType = null;
		if(ctx.OR().size() > 0) {
			
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
		if(ctx.AND().size() > 0) {
			
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
		if(ctx.SIGNAND().size() > 0) {
			
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
		if(ctx.add_sub_op().size() > 0) {

			for (FloydParser.Mul_div_exprContext mdExprDecl : ctx.mdExpr) {
				newType = visit(mdExprDecl);
				if(!newType.name.equals("int")) {
					newType = Type.ERROR;
					break;
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
		
		if(ctx.mul_div_op().size() > 0) {
			
			for (FloydParser.Unary_exprContext unaExprDecl : ctx.unaExpr) {
				newType = visit(unaExprDecl);
				if(!newType.name.equals("int")) {
					newType = Type.ERROR;
					break;
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

		return newType;
	}
	
	@Override
	public Type visitNewExpr(FloydParser.NewExprContext ctx) {
		
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");

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

		return newType;
	}
	
	
	
	//Primay Expression
	@Override
	public Type visitArrayExpr(FloydParser.ArrayExprContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		
		return Type.ERROR;
	}
	
	@Override
	public Type visitIdTerm(FloydParser.IdTermContext ctx) {
		Type newType = null;
		String name = ctx.getText();
		System.out.println("I am here " + name);
		Symbol newSymbol = this.sblTable.lookup(name);
		if (newSymbol == null) {
			System.out.println("Use of undeclared variable " + name);
			newType = Type.ERROR;
		} else {
			Declaration newDeclaration = newSymbol.getAttributes();
			newType = newDeclaration.type;
		}

		return newType;
	}
	
	@Override
	public Type visitStrExpr(FloydParser.StrExprContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		
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
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		
		return Type.ERROR;
	}
	
	@Override
	public Type visitMeExpr(FloydParser.MeExprContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		
		return Type.ERROR;
	}
	
	@Override
	public Type visitParExpr(FloydParser.ParExprContext ctx) {
		Type newType = visit(ctx.expression());
		return newType;
	}
	
	
	
	//Errors
	public void printError(Token newToken, String error) {
		int line =  newToken.getLine();
		int col = newToken.getCharPositionInLine();
	
		System.err.println(this.fileName + ":" + line + "," + col + ":" + error);
	}
	
	public void printErrorNT(int line, int col, String error) {
	
		System.err.println(this.fileName + ":" + line + "," + col + ":" + error);
	}
}
