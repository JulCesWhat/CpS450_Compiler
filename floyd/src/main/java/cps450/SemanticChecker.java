package cps450;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	SymbolTable sblTable;
	String fileName;
	
	String nameClass = null;
	String idClsName = null;					//Used for PointMethExpr
	String idVarName = null;
	String methParamCall = null;	
	Token methParamCallTok = null;
	String assignMethCall = null;
	ObjReference orInstance = null;
	
	Options optInstance = null;
	
	public SemanticChecker(String newFileName) {
		this.sblTable = SymbolTable.getInstance();
		this.fileName = newFileName;
		
		orInstance = ObjReference.getInstance();
		this.optInstance = Options.getInstance(null);
		
		this.StartDefinedClasses();
	}
	
	private void StartDefinedClasses() {
		
		//Adding the in class to our class holder
		MethodDecl methReadInt = new MethodDecl(Type.INT);
		ClassDecl classIn = new ClassDecl();
		classIn.methods.put("readint", methReadInt);
		orInstance.classesMap.put("in", classIn);
		
		this.sblTable.push("in", classIn);
		
		
		//Adding the out class to your class holder
		VarDecl paramWriteOut = new VarDecl(Type.INT, 8);
		MethodDecl methWriteInt = new MethodDecl(Type.VOID);
		methWriteInt.parameters.put("out", paramWriteOut);
		ClassDecl classOut = new ClassDecl();
		classOut.methods.put("writeint", methWriteInt);
		orInstance.classesMap.put("out", classOut);
		
		this.sblTable.push("out", classOut);
	}
	
	@Override
	public Type visitProgram(FloydParser.ProgramContext ctx) {
		
		if(ctx.classes.size() == 0 || ctx.classes.size() > 1) {
			System.out.println("failure to define exactly one class in a source file");
		} else {
			for(FloydParser.Class_declContext classDecl : ctx.classes) {
				Type something = visit(classDecl);
			}
		}
		
		return null;
	}
	
  	@Override
  	public Type visitClass_decl(FloydParser.Class_declContext ctx) {
  		
  		if(!ctx.idClS.getText().equals(ctx.idClE.getText())) {
  			System.out.println("failure to define exactly one class in a source file capi");
  		} else if(ctx.idClS.getText() == "int" || ctx.idClS.getText() == "string" || ctx.idClS.getText() == "boolean") {
  			System.out.println("failure to define class that is not a type");
  		} else {
  			
  			ClassDecl clsDec = new ClassDecl();
  			nameClass = ctx.idClS.getText();
  			
  			//Instantiate New Scope
  			this.sblTable.push(nameClass, clsDec);
  			this.sblTable.beginNewScope();
  			

  			//Class variables
  			for (FloydParser.Var_declContext varDecl : ctx.claVarDecs) {
  				Type newType = visit(varDecl);
  				clsDec.glovalVars.put(varDecl.IDENTIFIER().getText(), new VarDecl(newType));
  			}
  			this.orInstance.classesMap.put(nameClass, clsDec);
  			
  			//Class methods
  			for (FloydParser.Method_declContext metDecl : ctx.claMetDecs) {
  				Type newType = visit(metDecl);
  			}
  			
  			
  			try {
  	  	  		this.sblTable.endScope();
  	  		} catch(Exception ex){
  	  			System.out.println(ex);
  	  		}
  			
  			nameClass = null;
  		}
  		

  		return null;
  	}
  	
  	@Override
	public Type visitVar_decl(FloydParser.Var_declContext ctx) {
  		Type newType = Type.ERROR;
  		String varName = ctx.IDENTIFIER().getText();
		int curScope = this.sblTable.getScope();

		if (ctx.COLON() != null) {
			if (ctx.ASGOP() == null) {
				if (this.sblTable.lookupInScope(varName, curScope)) {
					Token tok = (Token) ctx.IDENTIFIER().getPayload();
					this.printError(tok, "Redefined identifier " + varName);

				} else {
					newType = visit(ctx.type());
					this.sblTable.push(varName, new VarDecl(newType));

				}
			} else {
				Token tok = (Token) ctx.ASGOP().getPayload();
				this.printError(tok, "feature unsupported");
			}

		} else {
			if (ctx.ASGOP() != null) {
				Token tok = (Token) ctx.ASGOP().getPayload();
				this.printError(tok, "feature unsupported");
			} else {
				Token tok = (Token) ctx.IDENTIFIER().getPayload();
				this.printError(tok, "no type specified");
			}
		}

		return newType;
	}
  	
  	
  	@Override
  	public Type visitMethod_decl(FloydParser.Method_declContext ctx) {
  		
  		int locVarPos = 0;
  		if(!(ctx.idMeS.getText()).equals(ctx.idMeE.getText())) {
  			System.out.println("Method not declared correctly");
  		}
  		
 
  		//If it goes through the method returns a value
  		Type methType = Type.VOID;
  		if(ctx.COLON() != null) {
  			methType = visit(ctx.type());
  			locVarPos = -4;
  		}
  		
  		
  		MethodDecl method = new MethodDecl(methType);
  		String methodName = ctx.idMeS.getText();
  		
  		this.sblTable.push(methodName, method);
  		this.sblTable.beginNewScope();
  		
  		int i = 8;  		
  		//Parameters
  		if(ctx.argument_decl_list() != null) {
  	  		for (FloydParser.Argument_declContext argsDecl : ctx.argument_decl_list().argsDec) {
  	  			String argName = argsDecl.IDENTIFIER().getText();
  	  			Symbol sym = this.sblTable.lookup(argName);
  	  			
  	  			if(sym == null || sym.getScopeS() < this.sblTable.getScope()) {
  	  				Type newType = visit(argsDecl);
  	  				VarDecl var = new VarDecl(newType, i);
  	  				method.parameters.put(argName, var);
  	  				this.sblTable.push(argName, var);
  	  			} else {
  	  				Token tok = (Token) argsDecl.IDENTIFIER().getPayload();
  	  				String error = "Redefined identifier " + argName;
  	  				this.printErrorNT(tok.getLine(), tok.getCharPositionInLine(), error);
  	  			}
  	  			i+=4;
  	  		}
  		}
  		
  		i = -4 + locVarPos;
  		//Local variables
  		for (FloydParser.Var_declContext metVarDecl : ctx.metVarDecs) {
			Type newType = visit(metVarDecl);
			if(newType != null) {
				String locVarName = metVarDecl.IDENTIFIER().getText();
				VarDecl var = new VarDecl(newType, i);
				method.localVars.put(locVarName, var);
			}
			i-=4;
		}
  		
  		ClassDecl clsDec = this.orInstance.classesMap.get(nameClass);
  		clsDec.methods.put(methodName, method);
  		this.orInstance.classesMap.put(nameClass, clsDec);
  		
  		if(ctx.statement_list() != null) {
  			for (FloydParser.StatementContext stmts : ctx.statement_list().stmts) {
  				Type newType = visit(stmts);
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
  		
  		this.sblTable.push(ctx.IDENTIFIER().getText(), new VarDecl(newType, 0));
  		return newType;
  	}
	
  	
  	//Type
	@Override
	public Type visitIntType(FloydParser.IntTypeContext ctx) {
		return Type.INT;
	}

	@Override
	public Type visitStrType(FloydParser.StrTypeContext ctx) {
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
		if (ctx.e1 != null) {
			Token tok = (Token) ctx.e1.getPayload();
			this.printError(tok, "feature unsupported");
			return null;
		}

		String varName = ctx.IDENTIFIER().getText();
		Symbol newSymbol = this.sblTable.lookup(varName);
		if (newSymbol == null) {
			Token newToken = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(newToken, "Undeclared identifier " + varName);
		} else {
			// Using this for method check in return
			this.assignMethCall = null;
			
			Type newType = visit(ctx.e2);

			if (newType != null && newType != Type.ERROR) {
				Declaration newVar = newSymbol.getAttributes();
				String leftExp = newVar.type.name;
				String rightExp = newType.name;

				if (newVar.type != Type.ERROR && newVar.type != newType) {
					
					//We know that e2 must be a method call
					if(this.assignMethCall != null) {
						Token tok = (Token) ctx.ASGOP().getPayload();
						if(newType != Type.VOID) {
							this.printError(tok, "incompatible types for assignment");
						} else {
							this.printError(tok, rightExp + " method");
						}
					} else {
						Token tok = (Token) ctx.ASGOP().getPayload();
						this.printError(tok, "cannot assign " + rightExp + " to " + leftExp);
					}
				}
				
				this.methParamCall = null;
			} // Else will be handle by the visit to ctx.e2
		}

		return null;
	}
	
	@Override
	public Type visitIf_stmt(FloydParser.If_stmtContext ctx) {
		Type typeOne = visit(ctx.expression());
		Type typeThree = null;
		
		if(typeOne != Type.BOOLEAN) {
			Token tok = ctx.ifS;
			this.printError(tok, "wrong type for IF");
			typeOne = Type.ERROR;
		}
		
		Type typeTwo = visit(ctx.e1);
		
		// There must be an else part
		if(ctx.ELSE() != null) {
			typeThree = visit(ctx.e2);
		}
		
		if(typeOne == Type.ERROR || typeTwo == Type.ERROR) {
			typeOne = Type.ERROR;
		}
		
		return typeOne;
	}
	
	@Override
	public Type visitLoop_stmt(FloydParser.Loop_stmtContext ctx) {
		Type typeOne = visit(ctx.expression());
		Type typeTwo = null;
		
		if(typeOne != Type.BOOLEAN) {
			Token tok = (Token) ctx.WHILE().getPayload();
			this.printError(tok, "wrong type for while condition");
			typeOne = Type.ERROR;
		}
		
		typeTwo = visit(ctx.statement_list());
		
		return typeOne;
	}
	
	@Override
	public Type visitCall_stmt(FloydParser.Call_stmtContext ctx) {
		Type newType = null;
		String methName = ctx.IDENTIFIER().getText();

		if (ctx.POINT() != null) {
			this.idClsName = null;
			this.idVarName = null;
			// Setting flag for className
			Type clsType = visit(ctx.expression());

			if (clsType != null) {
				//The found item was either a variable or methodS
				if(this.idClsName == null) {
					Token tok = (Token) ctx.POINT().getPayload();
					this.printError(tok, this.idVarName + " is not an object that contains " + methName);
				} else {
					// The found item was a class
					ClassDecl classDec = this.orInstance.classesMap.get(this.idClsName);
					MethodDecl methDec = classDec.methods.get(methName);
					if (methDec != null) {
						Type methType = null;

						// Information needed to check method paramters
						this.methParamCall = methName;
						this.methParamCallTok = (Token) ctx.IDENTIFIER().getPayload();

						// This will run if it has parameters
						if (ctx.expression_list() != null) {
							methType = visit(ctx.expression_list());
						} else if(methDec.parameters.size() > 0) {
							Token tok = (Token) ctx.IDENTIFIER().getPayload();
							this.printError(tok, "to few parameters");
							methType = Type.ERROR;
						}

						newType = methDec.type;

						// If parameter don't match... there will be an error
						if (methType == Type.ERROR) {
							newType = methType;
						}

						// Cleaning parameter checking information
						this.methParamCall = null;
						this.methParamCallTok = null;
					} else {
						// Method cannot be found
						Token tok = (Token) ctx.IDENTIFIER().getPayload();
						this.printError(tok, "no such " + methName + " method for " + this.idClsName);
					}
				}
			} // else (error was printed in idCTX)
			this.idClsName = null;
			this.idVarName = null;
		} else {
			// Must be a method defined in local class
			Symbol newSym = this.sblTable.lookup(methName);
			if (newSym != null) {
				MethodDecl methDecl = (MethodDecl) newSym.getAttributes();
				Type methType = null;
				
				
				//Information needed to check method paramters
				this.methParamCall = methName;
				this.methParamCallTok = (Token) ctx.IDENTIFIER().getPayload();
				
				this.assignMethCall = methName;
				
				newType = methDecl.type;
				
				//This will run if it has parameters
				if(ctx.expression_list() != null) {
					methType = visit(ctx.expression_list());
				}
				
				//If parameter don't match... there will be an error
				if(methType == Type.ERROR) {
					newType = methType;
				}
				
				//Cleaning parameter checking information
				this.methParamCall = null;
				this.methParamCallTok = null;
			} else {
				// Method cannot be found
				Token tok = (Token) ctx.IDENTIFIER().getPayload();
				this.printError(tok, "Undefined method " + methName);
			}
		}

		return newType;
	}
	
	
	
	//Expression
	@Override
	public Type visitExpression_list(FloydParser.Expression_listContext ctx) {
		String methName = this.methParamCall;
		Token tok = this.methParamCallTok;
		
		ClassDecl classDec = null;
		if(this.idClsName != null) {	// If this exprList belongs ot a point.method
			classDec = this.orInstance.classesMap.get(this.idClsName);
		} else {	//If this exprList belongs to a method with no point
			classDec = this.orInstance.classesMap.get(this.nameClass);
		}
		MethodDecl  methDec = classDec.methods.get(methName);


		Type newType = methDec.type;
		boolean errFlag = false;
		int paramPos = 0;
		
		for (FloydParser.ExpressionContext expr : ctx.expr) {
			Type curType = visit(expr);

			if (paramPos < methDec.parameters.size()) {
				VarDecl newVar = methDec.getParamByPos(paramPos);
				Type paramType = newVar.type;

				//Error that the paramters don't match
				if (curType != paramType) {
					newType = Type.ERROR;
					errFlag = true;
					this.printError(tok, "wrong parm type");
					break;
				}
			} else {
				//Error that to many paramters are being passed
				newType = Type.ERROR;
				errFlag = true;
				this.printError(tok, "too many parms");
				break;
			}
			paramPos++;
		}
		
		//Error that to few paramters are being passsed to function
		if (!errFlag && paramPos < methDec.parameters.size()) {
			newType = Type.ERROR;
			this.printError(tok, "too few parms");
		}
		
		return newType;
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
			int posOp = 0;
			for (FloydParser.And_exprContext andExprDecl : ctx.andExpr) {
				newType = visit(andExprDecl);
				if(newType != Type.BOOLEAN && newType != Type.ERROR) {
					int realPosOp = posOp == 0 ? 0 : posOp -1;
					Token tok = (Token) ctx.OR(realPosOp).getPayload();
					this.printError(tok, "the " + ctx.OR(realPosOp).getText() + " bin op can only be used with booleans");			
					newType = Type.ERROR;
					break;
				}
				posOp++;
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
			int posOp = 0;
			for (FloydParser.Relational_exprContext relExprDecl : ctx.relExpr) {
				newType = visit(relExprDecl);
				if(newType != Type.BOOLEAN && newType != Type.ERROR) {
					int realPosOp = posOp == 0 ? 0 : posOp -1;
					Token tok = (Token) ctx.AND(realPosOp).getPayload();
					this.printError(tok, "the " + ctx.AND(realPosOp).getText() + " bin op can only be used with booleans");
					newType = Type.ERROR;
					break;
				}
				posOp++;
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
			String opVal = ctx.relational_op().getText();
			Token tok = null;
			
			switch(opVal) {
			case ">":
				tok = (Token) ctx.relational_op().GT().getPayload();
				break;
			case ">=":
				tok = (Token) ctx.relational_op().GTEQ().getPayload();
				break;
			case "=":
				tok = (Token) ctx.relational_op().EQ().getPayload();
				break;
			}

			if(type1 != type2) {
				this.printError(tok, "the " + opVal + " can't work on " + type1.name + " " + type2.name);
				newType = Type.ERROR;
			} else if((ctx.relational_op().getText()).equals("=")) {
				if(type1 == Type.ERROR) {
					this.printError(tok, "the " + opVal + " can't work on " + type1.name + " " + type2.name);
					newType = Type.ERROR;
				}
			} else {
				if(type1 != Type.INT && type1 != Type.STRING) {
					this.printError(tok, "the " + opVal + " can't work on " + type1.name + " " + type2.name);
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
			int posOp = 0;
			for (FloydParser.Add_sub_exprContext asExprDecl : ctx.asExpr) {
				newType = visit(asExprDecl);
				if(newType != Type.STRING && newType != Type.ERROR) {
					int realPosOp = posOp == 0 ? 0 : posOp -1;
					Token tok = (Token) ctx.SIGNAND(realPosOp).getPayload();
					this.printError(tok, "the " + ctx.SIGNAND(realPosOp).getText() + " bin op can only be used with strings");
					newType = Type.ERROR;
					break;
				}
				posOp++;
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

			int posOp = 0;
			for (FloydParser.Mul_div_exprContext mdExprDecl : ctx.mdExpr) {
				newType = visit(mdExprDecl);
				if(newType != Type.INT && newType != Type.ERROR) {
					int realPosOp = posOp == 0 ? 0 : posOp -1;
					FloydParser.Add_sub_opContext addsubOp = ctx.add_sub_op(realPosOp);
					
					//Doing this to be able to get the token
					Token tok = null;
					if(addsubOp.MINUS() != null) {
						tok = (Token) addsubOp.MINUS().getPayload();
					} else {
						tok = (Token) addsubOp.PLUS().getPayload();
					}
					this.printError(tok, "the " + addsubOp.getText() + " bin op cannot be used with " + newType.name);
					newType = Type.ERROR;
					break;
				}
				posOp++;

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
			int posOp = 0;
			for (FloydParser.Unary_exprContext unaExprDecl : ctx.unaExpr) {
				newType = visit(unaExprDecl);
				if(newType != Type.INT && newType != Type.ERROR) {
					int realPosOp = posOp == 0 ? 0 : posOp -1;
					FloydParser.Mul_div_opContext muldivOp = ctx.mul_div_op(realPosOp);
					
					//Doing this to be able to get the token
					Token tok = null;
					if (muldivOp.MUL() != null) {
						tok = (Token) muldivOp.MUL().getPayload();
					} else {
						tok = (Token) muldivOp.DIV().getPayload();
					}
					this.printError(tok, "the " + muldivOp.getText() + " op can only be used with ints and reals");
					newType = Type.ERROR;
					break;
				}
				posOp++;
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
				if(newType != Type.BOOLEAN && newType != Type.ERROR) {
					Token tok = (Token) ctx.unary_op().NOT().getPayload();
					this.printError(tok, "the " + ctx.unary_op().NOT().getText() + " requires a boolean");
					newType = Type.ERROR;
				}
			} else {
				if(newType != Type.INT && newType != Type.ERROR) {
					Token tok = null;
					String opName = null;
					if (ctx.unary_op().MINUS() != null) {
						tok = (Token) ctx.unary_op().MINUS().getPayload();
						opName = ctx.unary_op().MINUS().getText();
					} else {
						tok = (Token) ctx.unary_op().PLUS().getPayload();
						opName = ctx.unary_op().PLUS().getText();
					}

					this.printError(tok, "the " + opName + " unary op cannot be used with a " + newType.name);
					newType = Type.ERROR;
				}
			}
		}

		return newType;
	}
	
	
	
	////////////////////////////////////////////
	// Method New Expression  ==>  (method_new_expr)
	////////////////////////////////////////////
	@Override
	public Type visitNewExpr(FloydParser.NewExprContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");

		return Type.ERROR;
	}
	
	@Override
	public Type visitPointMethExpr(FloydParser.PointMethExprContext ctx) {
		this.idClsName = null;
		Type newType = visit(ctx.primary_expr());
		if(newType == Type.ERROR) {
			//Throw an error
			System.out.println("class not defined error in visitPointMethExpr()");
		} else {
			String methName = ctx.IDENTIFIER().getText();
			ClassDecl classDec = this.orInstance.classesMap.get(this.idClsName);
			MethodDecl methDec = classDec.methods.get(methName);
			
			//Symbol newSymbol = this.sblTable.lookup(methName);
			if(methDec == null) {
				Token newToken = (Token) ctx.IDENTIFIER().getPayload();
				this.printError(newToken, "Undeclared identifier " + methName);
			} else {
				Type pointMethType = null;
				
				this.methParamCall = methName;
				this.methParamCallTok = (Token) ctx.IDENTIFIER().getPayload();
				
				this.assignMethCall = methName;
				
				newType = methDec.type;
				
				//This will run if it has parameters
				if(ctx.expression_list() != null) {
					pointMethType = visit(ctx.expression_list());
				}
				
				//If parameter don't match... there will be an error
				if(pointMethType == Type.ERROR) {
					newType = pointMethType;
				}
				
				this.methParamCall = null;
				this.methParamCallTok = null;
			}
		}

		this.idClsName = null;
		
		return newType;
	}
	
	@Override
	public Type visitMethExpr(FloydParser.MethExprContext ctx) {
		Type newType = Type.ERROR;
		
		String methName = ctx.IDENTIFIER().getText();
		
		if (this.sblTable.lookup(methName) == null) {
			Token tok = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(tok, "Undefined function " + methName);
		} else {
			ClassDecl classDec = this.orInstance.classesMap.get(nameClass);
			MethodDecl  methDec = classDec.methods.get(methName);
			newType = methDec.type;
			Type methType = null;
			
			//Information needed to check method paramters
			this.methParamCall = methName;
			this.methParamCallTok = (Token) ctx.IDENTIFIER().getPayload();
			
			this.assignMethCall = methName;
			
			//This will run if it has parameters
			if(ctx.expression_list() != null) {
				methType = visit(ctx.expression_list());
			}
			
			//If parameter don't match... there will be an error
			if(methType == Type.ERROR) {
				newType = methType;
			}
			
			//Cleaning parameter checking information
			this.methParamCall = null;
			this.methParamCallTok = null;
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
	public Type visitArrayExpr(FloydParser.ArrayExprContext ctx) {
		Token tok = (Token) ctx.getPayload();
		this.printError(tok, "feature unsupported");
		
		return Type.ERROR;
	}
	
	@Override
	public Type visitIdTerm(FloydParser.IdTermContext ctx) {
		Type newType = null;
		String name = ctx.IDENTIFIER().getText();
		Symbol newSymbol = this.sblTable.lookup(name);

		if (newSymbol == null) {
			Token tok = (Token) ctx.IDENTIFIER().getPayload();
			this.printError(tok, "Use of undeclared variable " + name);
			newType = Type.ERROR;
		} else {
			Declaration newDeclaration = newSymbol.getAttributes();

			// Will be used to know if the id is a variable, method, or class
			switch (newDeclaration.kind) {
			case "class":
				this.idClsName = name;
				break;
			case "method":
				break;
			case "variable":
				this.idVarName = name;
				break;
			}
			newType = newDeclaration.type;
		}

		return newType;
	}
	
	@Override
	public Type visitStrExpr(FloydParser.StrExprContext ctx) {
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
		this.optInstance.addSemanticErrors();
		System.err.println(this.fileName + ":" + line + "," + col + ":" + error);
	}
	
	public void printErrorNT(int line, int col, String error) {
		this.optInstance.addSemanticErrors();
		System.err.println(this.fileName + ":" + line + "," + col + ":" + error);
	}
	
}
