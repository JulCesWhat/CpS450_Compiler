package cps450;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import org.antlr.v4.runtime.Token;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	SymbolTable sblTable;
	String fileName;
	ClassDecl classDec = null;
	//HashMap<String, Declaration> methods = new HashMap<>();
	
	public SemanticChecker(String newFileName) {
		this.sblTable = SymbolTable.getInstance();
		this.fileName = newFileName;
		
		//MethodDecl method1 = MethodDecl()
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
  			this.classDec = new ClassDecl(ctx.idClS.getText());
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
  		Type newType = Type.ERROR;
  		String varName = ctx.IDENTIFIER().getText();
  		int curScope = this.sblTable.getScope();
  		
  		if(curScope == 0) {
  			if(ctx.COLON() != null) {
  				if(ctx.ASGOP() == null) {
  	  				if(this.sblTable.lookupInScope(varName, curScope)) {
  	  					Token tok = (Token) ctx.IDENTIFIER().getPayload();
  	  					this.printError(tok, "Redefined identifier " + varName);
  	  					
  	  				} else {
  	  					newType = visit(ctx.type());
  	  					Symbol newSymbol = this.sblTable.push(varName, new VarDecl(newType));
  	  				}
  				} else {
  					Token tok = (Token) ctx.ASGOP().getPayload();
  	  				this.printError(tok, "feature unsupported");
  				}

  			} else {
  				if(ctx.ASGOP() != null) {
  					Token tok = (Token) ctx.ASGOP().getPayload();
  	  				this.printError(tok, "feature unsupported");
  				} else {
  					Token tok = (Token) ctx.IDENTIFIER().getPayload();
  	  				this.printError(tok, "no type specified");
  				}
  			}
  			
  		} else {
  			System.out.println(ctx.IDENTIFIER().getText());
  			
  		}
  		
		
		return newType;
	}
  	
  	
  	@Override
  	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {
  		this.sblTable.beginScope();
  		
  		if(!(ctx.idMeS.getText()).equals(ctx.idMeE.getText())) {
  			System.out.println("Method not declared correctly");
  		} else if(!(ctx.idMeS.getText()).equals("start")) {
  			this.printErrorNT(ctx.idMeS.getLine(), ctx.idMeE.getCharPositionInLine(), "feature unsupported");
  		}
  		
  		MethodDecl method = new MethodDecl();
  		
  		if(ctx.argument_decl_list() != null) {
  	  		for (FloydParser.Argument_declContext argsDecl : ctx.argument_decl_list().argsDec) {
  	  			String argName = argsDecl.IDENTIFIER().getText();
  	  			Symbol sym = this.sblTable.lookup(argName);
  	  			
  	  			if(sym == null || sym.getScopeS() < this.sblTable.getScope()) {
  	  				Type newType = visit(argsDecl);
  	  				VarDecl var = new VarDecl(newType);
  	  				method.parameters.put(argName , var);
  	  			} else {
  	  				Token tok = (Token) argsDecl.IDENTIFIER().getPayload();
  	  				String error = "Redefined identifier " + argName;
  	  				this.printErrorNT(tok.getLine(), tok.getCharPositionInLine(), error);
  	  			}  			
  	  		}
  		}
  		
  		for (FloydParser.Var_declContext metVarDecl : ctx.metVarDecs) {
			Type newType = visit(metVarDecl);
			if(newType != null) {
				VarDecl var = new VarDecl(newType);
				method.localVars.put(metVarDecl.IDENTIFIER().getText(), var);	
			}
		}
  		
  		if(ctx.statement_list() != null) {
  			
  	  		for (FloydParser.StatementContext metStmtDecl : ctx.statement_list().stmts) {
  				Type newType = visit(metStmtDecl);
  	  		}
  		}
  		
  		try {
  	  		this.sblTable.endScope();
  		} catch(Exception ex){
  			System.out.println(ex);
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
		//Token tok = (Token) ctx.getPayload();
		System.out.println("unsupported feature - ()");
		//this.printError(tok, "feature unsupported");
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
		} else {
			Type newType = visit(ctx.e2);
			Declaration newVar = newSymbol.getAttributes();
			String leftExp = newVar.type.name;
			String rightExp = newType.name;
			
			if(!(rightExp).equals("<error>") && !(leftExp).equals(rightExp)) {
				//Token tok = (Token) ctx.e2.getPayload();
				//this.printError(tok, "cannot assign " + rightExp + " to " + leftExp);
				System.out.println("cannot assign " + rightExp + " to " + leftExp + "  visitAssignment_stmt()");
			}
		}
		
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
		String methName = ctx.IDENTIFIER().getText();
		if(!this.classDec.methods.containsKey(methName)) {
			Token tok = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(tok, "Undefined function " + methName);
		}
		
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
		if(ctx.andExpr.size() > 1) {
			
			for (FloydParser.And_exprContext andExprDecl : ctx.andExpr) {
				newType = visit(andExprDecl);
				if(!(newType.name).equals("boolean")) {
					newType = Type.ERROR;
					System.out.println("Need to print Error somewhere in? visitOr_expr() ");
					break;
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
		if(ctx.relExpr.size() > 1) {
			
			for (FloydParser.Relational_exprContext relExprDecl : ctx.relExpr) {
				newType = visit(relExprDecl);
				if(!(newType.name).equals("boolean")) {
					newType = Type.ERROR;
					System.out.println("Need to print Error somewhere in? visitAnd_expr() ");
					break;
				}	
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

			if(!(type1.name).equals(type2.name)) {
				System.out.println("Need to print Token <error1> in visitRelational_expr()");
				newType = Type.ERROR;
				//The fallowing doesn't work, need to find out Why!!!!
				//Token tok = (Token) ctx.strExpr2.getPayload();
				//this.printError(tok, "feature unsupported");
			} else if((ctx.relational_op().getText()).equals("=")) {
				if((type1.name).equals("<error>")) {
					System.out.println("Need to print Token <error2> in visitRelational_expr()");
					newType = Type.ERROR;
				}
			} else {
				if(!(type1.name).equals("string") && !(type1.name).equals("int")) {
					System.out.println("Need to print Token <error> in visitRelational_expr()");
					newType = Type.ERROR;
				}
			}
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
				if(!(newType.name).equals("string")) {
					//Token tok = (Token) asExprDecl.getPayload();
					//this.printError(tok, "& requires strings");
					System.out.println("Need to print Token <error> in visitString_expr()");
					newType = Type.ERROR;
					break;
				}
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

			for (FloydParser.Mul_div_exprContext mdExprDecl : ctx.mdExpr) {
				newType = visit(mdExprDecl);
				if(!newType.name.equals("int")) {
					System.out.println("Need to print Token <error> in visitAdd_sub_expr()");
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
		
		if(ctx.unaExpr.size() > 1) {
			
			for (FloydParser.Unary_exprContext unaExprDecl : ctx.unaExpr) {
				newType = visit(unaExprDecl);
				if(!newType.name.equals("int")) {
					System.out.println("Need to print Token <error> in visitMul_div_expr()");
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
		
		if(ctx.unary_op() != null) {
			if(ctx.unary_op().NOT() != null) {
				if(!(newType.name).equals("boolean")) {
					System.out.println("Might need to throw an error here visitUnary_expr() 1");
					newType = Type.ERROR;
				}
			} else {
				if(!(newType.name).equals("int")) {
					System.out.println("Might need to throw an error here visitUnary_expr() 2");
					newType = Type.ERROR;
				}
			}
		}

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
		String methName = ctx.IDENTIFIER().getText();
		if(!this.classDec.methods.containsKey(methName)) {
			Token tok = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(tok, "Undefined function " + methName);
		}
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
