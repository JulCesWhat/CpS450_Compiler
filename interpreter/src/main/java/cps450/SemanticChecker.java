package cps450;

import java.util.HashMap;

import cps450.FloydParser.Assignment_stmtContext;
import cps450.FloydParser.IntExprContext;
import cps450.FloydParser.Var_declContext;



public class SemanticChecker extends FloydBaseVisitor<Type> {
	
	HashMap<String, Double> variables = new HashMap<>();
	SymbolTable sblTable = SymbolTable.getInstance();
	
	@Override
	public Type visitVar_decl(Var_declContext ctx) {
		
		String id = ctx.IDENTIFIER().getText();
		
		if (!ctx.COLON().getText().equals(null)) {
			System.out.println("No colon");
		}

		Symbol x = sblTable.push(id, new VarDecl(Type.INT));
		System.out.println(id);
		
		return null;
	}
	
	@Override
	public Type visitAssignment_stmt(Assignment_stmtContext ctx) {
		
		String id = ctx.IDENTIFIER().getText();
		
		Symbol sblFound = sblTable.lookup(id);
		if(sblFound == null) {
			System.out.println("There was an error!");
		}
		
		Type value = visit(ctx.expression(0));		
		
		System.out.println(value + " capi?");
		
		
		return null;
	}
	
  	@Override
	public Type visitIntExpr(IntExprContext ctx) {
  		
		String id = ctx.INTEGER_LITERAL().getText();
		System.out.println(id + "  work??");
		return Type.INT;
	}
}
