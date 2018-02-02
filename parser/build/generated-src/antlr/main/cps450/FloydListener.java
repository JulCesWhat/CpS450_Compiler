// Generated from cps450\Floyd.g4 by ANTLR 4.7.1
package cps450;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FloydParser}.
 */
public interface FloydListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FloydParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(FloydParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(FloydParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(FloydParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(FloydParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#class_N}.
	 * @param ctx the parse tree
	 */
	void enterClass_N(FloydParser.Class_NContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#class_N}.
	 * @param ctx the parse tree
	 */
	void exitClass_N(FloydParser.Class_NContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void enterVar_decl(FloydParser.Var_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void exitVar_decl(FloydParser.Var_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#method_decl}.
	 * @param ctx the parse tree
	 */
	void enterMethod_decl(FloydParser.Method_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#method_decl}.
	 * @param ctx the parse tree
	 */
	void exitMethod_decl(FloydParser.Method_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#argument_decl_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_decl_list(FloydParser.Argument_decl_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#argument_decl_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_decl_list(FloydParser.Argument_decl_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#argument_decl}.
	 * @param ctx the parse tree
	 */
	void enterArgument_decl(FloydParser.Argument_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#argument_decl}.
	 * @param ctx the parse tree
	 */
	void exitArgument_decl(FloydParser.Argument_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(FloydParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(FloydParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(FloydParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(FloydParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(FloydParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(FloydParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#assignment_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_stmt(FloydParser.Assignment_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#assignment_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_stmt(FloydParser.Assignment_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(FloydParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(FloydParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#loop_stmt}.
	 * @param ctx the parse tree
	 */
	void enterLoop_stmt(FloydParser.Loop_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#loop_stmt}.
	 * @param ctx the parse tree
	 */
	void exitLoop_stmt(FloydParser.Loop_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#call_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCall_stmt(FloydParser.Call_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#call_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCall_stmt(FloydParser.Call_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(FloydParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(FloydParser.Expression_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(FloydParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(FloydParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#binary_op}.
	 * @param ctx the parse tree
	 */
	void enterBinary_op(FloydParser.Binary_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#binary_op}.
	 * @param ctx the parse tree
	 */
	void exitBinary_op(FloydParser.Binary_opContext ctx);
	/**
	 * Enter a parse tree produced by {@link FloydParser#unary_op}.
	 * @param ctx the parse tree
	 */
	void enterUnary_op(FloydParser.Unary_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link FloydParser#unary_op}.
	 * @param ctx the parse tree
	 */
	void exitUnary_op(FloydParser.Unary_opContext ctx);
}