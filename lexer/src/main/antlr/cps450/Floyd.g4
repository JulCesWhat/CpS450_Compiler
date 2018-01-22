grammar Floyd;


start : ;


// Keywords

// KEYWORDS
//    : BOOLEAN | BEGIN | CLASS | EL | SE | END | FALSE | FROM | IF | INHERITS |
//      INT | IS | LOOP | ME | NEW | NOT | NULL | STRING | THEN | TRUE | WHILE
//    ;

BOOLEAN
   : 'boolean'
   ;


BEGIN
   : 'begin'
   ;


CLASS
   : 'class'
   ;


EL
   : 'el'
   ;


SE
   : 'se'
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


ILLIGAL_STRING_LITERAL
   : '"' (~["\\\t?\n] | '\\'~[VALID_ESCAPE_SEQUENCES])* '"'
   ;


UNREQ_STRING_LITERAL
   : '"' (~["\\\t?\n] | '\\'VALID_ESCAPE_SEQUENCES)*
   ;


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

OPERATORS
    : AND | SIGN | TIMES | DIV | GT | GTEQ | EQ
    ;

fragment AND
   : '&'
   ;


fragment SIGN
   : ('+' | '-')
   ;


fragment TIMES
   : '*'
   ;


fragment DIV
   : '/'
   ;


fragment GT
   : '>'
   ;


fragment GTEQ
   : '>='
   ;


fragment EQ
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

UNREQ_CHAR
   : (~[\r?\n])
   ;