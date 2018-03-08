package cps450;

import java.util.HashMap;

import org.antlr.v4.runtime.Token;

import cps450.FloydParser.Argument_declContext;
import cps450.FloydParser.Argument_decl_listContext;
import cps450.FloydParser.BoolTypeContext;
import cps450.FloydParser.Class_declContext;
import cps450.FloydParser.IdTypeContext;
import cps450.FloydParser.IntTypeContext;
import cps450.FloydParser.Method_declContext;
import cps450.FloydParser.ProgramContext;
import cps450.FloydParser.StatementContext;
import cps450.FloydParser.Statement_listContext;
import cps450.FloydParser.StrTypeContext;
import cps450.FloydParser.Var_declContext;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	SymbolTable sblTable;
	
	public SemanticChecker() {
		this.sblTable = SymbolTable.getInstance();
	}
	
	@Override
	public Type visitProgram(ProgramContext ctx) {
		
		if(ctx.classes.size() == 0 || ctx.classes.size() > 1) {
			System.out.println("failure to define exactly one class in a source file");
		} else {
			Type something = visit(ctx.classes.get(0));
		}
		
		return null;
	}
	
  	@Override
  	public Type visitClass_decl(Class_declContext ctx) {
  		
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
  			for (Var_declContext varDecl : ctx.claVarDecs) {
  				Type newType = visit(varDecl);
  			}
  			
  			for (Method_declContext metDecl : ctx.claMetDecs) {
  				Type newType = visit(metDecl);
  			}
  		}
  		

  		return null;
  	}
  	
  	@Override
	public Type visitVar_decl(Var_declContext ctx) {
  		
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
  	public Type visitMethod_decl(Method_declContext ctx) {
  		this.sblTable.beginScope();
  		
  		if(ctx.argument_decl_list() != null) {
  			Type arguments = visit(ctx.argument_decl_list());
  		}
  		
  		for (Var_declContext metVarDecl : ctx.metVarDecs) {
				Type newType = visit(metVarDecl);
		}
  		
  		if(ctx.metStmtDecs != null) {
  			
  		}
  		
  		for (StatementContext metStmtDecl : ctx.statement_list().stmts) {
			Type newType = visit(metStmtDecl);
  		}
  		
  		
  		return null;
  	}
  	
  	@Override
  	public Type visitArgument_decl_list(Argument_decl_listContext ctx) {
  		
  		for (Argument_declContext argDecl : ctx.argsDec) {
				Type newType = visit(argDecl);
		}
  		
  		return null;
  	}
  	
  	@Override
  	public Type visitArgument_decl(Argument_declContext ctx) {
  		
  		Type newType = visit(ctx.type());
  		
  		this.sblTable.push(ctx.IDENTIFIER().getText(), new VarDecl(newType));
  		return null;
  	}
	
	@Override
	public Type visitIntType(IntTypeContext ctx) {
		return Type.INT;
	}

	@Override
	public Type visitStrType(StrTypeContext ctx) {
		System.out.println("String ussupported feature.");
		return Type.STRING;
	}
	
	@Override
	public Type visitBoolType(BoolTypeContext ctx) {
		return Type.BOOLEAN;
	}
	
	@Override
	public Type visitIdType(IdTypeContext ctx) {
		System.out.println("String feature unsupported.");
		return Type.ERROR;
	}
}
