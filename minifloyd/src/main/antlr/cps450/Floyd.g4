grammar Floyd;



program
   : ENDOFLINE_1* classes+=class_decl ( ENDOFLINE_1+ classes+=class_decl )* ENDOFLINE_1*
   ;


//class

class_decl
   : CLASS idClS=IDENTIFIER ( INHERITS FROM idClIn=IDENTIFIER )? IS ENDOFLINE_1+
     claVarDecs+=var_decl*
     claMetDecs+=method_decl*
     END idClE=IDENTIFIER
   ;


//var declaration

var_decl
   : IDENTIFIER ( COLON type )? ( ASGOP expression )? ENDOFLINE_1+
   ;


//method declaration

method_decl
   : idMeS=IDENTIFIER LPAREN (argument_decl_list)? RPAREN ( COLON type )? IS ENDOFLINE_1+
     metVarDecs+=var_decl*
     BEGIN ENDOFLINE_1+
     statement_list
     END idMeE=IDENTIFIER ENDOFLINE_1+
   ;


argument_decl_list
   : ( argsDec+=argument_decl SEMICOL )* argsDec+=argument_decl
   ;


argument_decl
   : IDENTIFIER COLON type
   ;


type
   : INT								# IntType
   | STRING								# StrType
   | BOOLEAN							# BoolType
   | IDENTIFIER							# IdType
   | type LBRACK expression RBRACK		# ExpType
   ;


statement_list
   : ( stmts+=statement ENDOFLINE_1+ )*
   ;


statement
   : assignment_stmt
   | if_stmt
   | loop_stmt
   | call_stmt
   ;


assignment_stmt
   : IDENTIFIER ( LBRACK e1=expression RBRACK )* ASGOP e2=expression
   ;


if_stmt
   : ifS=IF expression THEN (ENDOFLINE_1)+
     e1=statement_list
     ( ELSE ENDOFLINE_1+ e2=statement_list )?
     END ifE=IF
   ;


loop_stmt
   : loS=LOOP WHILE expression ENDOFLINE_1+
     statement_list
     END loE=LOOP
   ;


call_stmt
   : ( expression POINT )? IDENTIFIER LPAREN ( expression_list )? RPAREN
   ;


expression_list
   : ( expr+=expression COMMA )* expr+=expression
   ;


expression
   : or_expr
   ;

or_expr
   : andExpr+=and_expr ( OR andExpr+=and_expr )*
   ;

and_expr
   : relExpr+=relational_expr ( AND relExpr+=relational_expr )*
   ;

relational_expr
   : strExpr1=string_expr ( relational_op strExpr2=string_expr )?
   ;

string_expr
   : asExpr+=add_sub_expr ( SIGNAND asExpr+=add_sub_expr )*
   ;

add_sub_expr
   : mdExpr+=mul_div_expr ( add_sub_op mdExpr+=mul_div_expr )*
   ;

mul_div_expr
   : unaExpr+=unary_expr ( mul_div_op unaExpr+=unary_expr )*
   ;

unary_expr
   : unary_op method_new_expr
   | method_new_expr
   ;
   
method_new_expr
   : NEW type															#NewExpr
   | primary_expr POINT IDENTIFIER LPAREN expression_list? RPAREN		#PointMethExpr
   | IDENTIFIER LPAREN expression_list? RPAREN							#MethExpr
   | primary_expr														#PrimExpr
   ;

primary_expr
   : IDENTIFIER LBRACK expression RBRACK ( LBRACK expression RBRACK )		# ArrayExpr
   | IDENTIFIER				# IdTerm
   | STRING_LITERAL			# StrExpr
   | INTEGER_LITERAL		# IntExpr
   | TRUE					# TrueExpr
   | FALSE					# FalseExpr
   | NULL					# NullExpr
   | ME						# MeExpr
   | LPAREN expression RPAREN		# ParExpr
   ;


relational_op
    : EQ
    | GT
    | GTEQ
    ;

// MIGHT BE USEFUL SOMETIME IN THE FUTURE.
// method_new_op
//     : POINT
//     | NEW
//     ;

unary_op
    : MINUS
    | PLUS
    | NOT
    ;

add_sub_op
    : MINUS
    | PLUS
    ;

mul_div_op
    : MUL
    | DIV
    ;












AND
  : 'and'
  ;

BOOLEAN
   : 'boolean'
   ;


BEGIN
   : 'begin'
   ;


CLASS
   : 'class'
   ;


ELSE
   : 'else'
   ;


END
   : 'end'
   ;


FALSE
   : 'false'
   ;


FROM
   : 'from'
   ;


IF
   : 'if'
   ;


INHERITS
   : 'inherits'
   ;


INT
   : 'int'
   ;


IS
   : 'is'
   ;

LOOP
   : 'loop'
   ;


ME
   : 'me'
   ;


NEW
   : 'new'
   ;


NOT
   : 'not'
   ;


NULL
   : 'null'
   ;


OR 
   : 'or'
   ;


STRING
   : 'string'
   ;


THEN
   : 'then'
   ;


TRUE
   : 'true'
   ;


WHILE
   : 'while'
   ;


// Identifier

IDENTIFIER
    : VALID_ID_START VALID_ID_CHAR*
    ;


fragment VALID_ID_START
   : ('a' .. 'z') | ('A' .. 'Z') | '_'
   ;


fragment VALID_ID_CHAR
   : VALID_ID_START | VALID_ID_INT
   ;


// Integer literal

INTEGER_LITERAL
   : '-' ? VALID_ID_INT +
   ;


fragment VALID_ID_INT
   : ('0' .. '9')
   ;


// String literal

STRING_LITERAL
   : '"' (~["\\\t?\n] | '\\'VALID_ESCAPE_SEQUENCES)* '"'
   ;


// ILLIGAL_STRING_LITERAL
//    : '"' (~["\\\t?\n] | '\\'~[VALID_ESCAPE_SEQUENCES])* '"'
//    ;


// UNREQ_STRING_LITERAL
//    : '"' (~["\\\t?\n] | '\\'VALID_ESCAPE_SEQUENCES)*
//    ;


fragment VALID_ESCAPE_SEQUENCES
   : TAB | NEWLINE | FORMFEED | CARRIAGERETURN | DOUBLEQUOTE | BACKSLASH | OCTAL
   ;


fragment TAB
   : ( '011' | 't' )
   ;


fragment NEWLINE
   : ( '012' | 'n' )
   ;


fragment FORMFEED
   : ( '014' | 'f' )
   ;


fragment CARRIAGERETURN
   : ( '015' | 'r' )
   ;


fragment DOUBLEQUOTE
   : ( '042' | '"' )
   ;


fragment BACKSLASH
   : ( '134' | '\\' )
   ;


fragment OCTAL
   : ([0-7][0-7][0-7])
   ;


// Operators

// OPERATORS
//     : AND | SIGN | TIMES | DIV | GT | GTEQ | EQ
//     ;

SIGNAND
   : '&'
   ;


PLUS
   : '+'
   ;

MINUS
   : '-'
   ;


MUL
   : '*'
   ;


DIV
   : '/'
   ;


GT
   : '>'
   ;


GTEQ
   : '>='
   ;


EQ
   : '='
   ;


// Miscellaneous

// Miscellaneous
//    : ASGOP | LPAREN | RPAREN | LBRACK | RBRACK | COMMA | SEMICOL | COLON | POINT
//    ;

ASGOP
   : ':='
   ;


LPAREN
   : '('
   ;


RPAREN
   : ')'
   ;


LBRACK
   : '['
   ;


RBRACK
   : ']'
   ;


COMMA
   : ','
   ;


SEMICOL
   : ';'
   ;


COLON
   : ':'
   ;


POINT
   : '.'
   ;


// Comment

COMMENT
    : '~' ~[\r\n]* -> skip
    ;


ENDOFLINE_1
    : '\r'? '\n'
    ;


ENDOFLINE_2
    : '_' '\r'? '\n' -> skip
    ;


WS
   : [ \t] + -> skip
   ;

   
ILLIGAL_STRING_LITERAL
    : '"' ~[\r\n]*? '"'
    ;
    
UNREQ_STRING_LITERAL
    : '"' ~[\r\n"]*
    ;


UNREQ_CHAR
   : (~[\r?\n])
   ;