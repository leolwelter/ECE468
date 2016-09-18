grammar Micro;

program: (PROGRAM id BEGIN pgm_body END);

id: IDENTIFIER;

pgm_body: VARDEC | FUNCDEC ;

VARDEC : VARTYPE VARLIST;
VARTYPE : (INT) | (FLOAT) | (STRING);
VARLIST : IDENTIFIER ((',')IDENTIFIER)*(';') ;

FUNCDEC : ('FUNCTION') FUNCTYPE IDENTIFIER ARGLIST BEGIN FUNCBODY END 
FUNCTYPE : (INT) | (FLOAT) | (STRING) | (VOID);
ARGLIST : '('((VARDEC)((',')VARDEC)*)?')' ;
FUNCBODY : 

IFSTATEMENT : ('IF') COND FUNCBODY ELSIFSTATEMENT* ('ENDIF')

ELSIFSTATEMENT : ('ELSIF') COND FUNCBODY

ARITHEXPR : IDENTIFIER ARITHOP

ARITHOP : ('+') | ('-') | ('*') | ('/') | ('=') | ('!=') | ('<') | ('>') | ('<=') | ('>=') 



KEYWORD :	('PROGRAM') | ('BEGIN') | ('END') | ('FUNCTION') | ('READ') | ('WRITE')
	|	('IF') | ('ELSIF') | ('ENDIF') | ('DO') | ('WHILE') | ('CONTINUE') | ('BREAK')
	| ('RETURN') | ('INT') | ('VOID') | ('STRING') | ('FLOAT') | ('TRUE') | ('FALSE');

IDENTIFIER  :	('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')* ;

INTLITERAL :	'0'..'9'+ ;

FLOATLITERAL :   ('0'..'9')+ '.' ('0'..'9')* | '.' ('0'..'9')+ ;

COMMENT :   '--'  ~('\n')* '\n' -> skip;

STRINGLITERAL :  '"' ( ~('"') )* '"' ;

OPERATOR : (':=') | ('+') | ('-') | ('*') | ('/') | ('=') | ('!=')
| ('<') | ('>') | ('(') | (')') | (';') | (',') | ('<=') | ('>=') ;

WHITESPACE : [ \t\r\n]+ -> skip;
