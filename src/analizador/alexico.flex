/* --------------------Seccion de codigo de usuario---------------------- */
package analizador;

import java.util.ArrayList;
import java_cup.runtime.*;

%% //inicio de opciones

/* ----------Seccion de opciones y declaraciones de JFlex----------- */

//Cambiamos el nombre de la clase del analizador
%class AnalizadorLexico
//Cambiamos el nombre la funcion para el siguiente token por nextToken
%function next_token
//Clase publica
%public
//Cuando se alcanza el fin del archivo de entrada
%eof{
    //System.out.println("Fin del archivo de codigo");
%eof}

%ignorecase

//Activar el contador de lineas, variable yyline
%line
//Activar el contador de columna, variable yycolumn
%column
/*Activamos la compatibilidad con Java CUP para analizadores sintáctics (parser)*/
%type java_cup.runtime.Symbol
%cup

%init{
TablaSimbolos= new ArrayList<entradaTS>();
ManejadorDeErrores= new ArrayList<Error1>();
contador=0;
%init}


/*Declaraciones:
El código entre %{ y %} será copiado íntegramente en el analizador generado*/
%{
public String lexema;
int contador;
ArrayList<entradaTS> TablaSimbolos;
ArrayList<Error1> ManejadorDeErrores;

    /* Generamos un java_cup.Symbol para guardar el tipo de token encontrado*/
    private Symbol symbol(int type){
        return new Symbol(type,yyline,yycolumn);
    }
    
    /* Generamos un Symbol para el tipo de token encontrado junto con su valor*/
    private Symbol symbol(int type,Object value){
        return new Symbol(type,yyline,yycolumn,value);
    }
%}  

//Fin de opciones


/*Macro declaraciones:
Declaramos expresiones regulares que después usaremos en las reglas léxicas */
//Expresiones regulares
//Declaraciones
LET=[A-Z|a-z]
DIG=[0-9]
ALPHANUMERIC={LET}|{DIG}
NUM=({DIG})+
ID={LET}({ALPHANUMERIC})*
IGNORE=[ \t\r\n]
BLANK=[ ]
COMENTARIOS= "/*"~"*/"
COMENT="//"({ALPHANUMERIC}|{BLANK})+|"--"
COMENTS="//"([¨*+"[""]"{}'´·!-&¡Ññ\¿?"<"">"¡#$%&/()=°¬"|"\^]|[0-9]|Ä|Ë|Ï|Ö|Ü|[A-Z|a-z]|á|é|í|ó|ú|ý|Á|É|Í|Ó|Ú|Ý|{BLANK})+
COMENTSS="/"([¨*+"[""]"{}'´·!-&¡Ññ\¿?"<"">"¡#$%&/()=°¬"|"\^]|[0-9]|Ä|Ë|Ï|Ö|Ü|[A-Z|a-z]|á|é|í|ó|ú|ý|Á|É|Í|Ó|Ú|Ý|{BLANK})+
INVALIDO=[([¨*+"[""]"{}'´·!-&¡Ññ\¿?"<"">"¡#$%&/()=°¬"|"\^]|[0-9]|Ä|Ë|Ï|Ö|Ü|[A-Z]|Á|É|Í|Ó|Ú|Ý|)]+
STRING=("\"" ({LET}|{NUM}|{BLANK})* "\"")
NOACEPTADOS=[·!-&¡0-9Ññ/\“\”áéíóúúÁÉÍÓÚÝÄËÏÖÜ]
NO={NUM}{ID}
//fin declaraciones



/* Seccion de reglas lexicas */
%% 
//Regla     {Acciones}

<YYINITIAL>{
"INT"   {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.INT_WORD,yytext());}

"BOOLEAN" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.BOOLEAN_WORD,yytext());}

"STRING" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.STRING_WORD,yytext());}

"ABSTRACTION" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ABSTRACTION,yytext());}

"AFFABILITY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.AFFABILITY,yytext());}

"APPREHENSION" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.APPREHESION,yytext());}

"ANXIETY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ANXIETY,yytext());}

"CARETOSTANDARDS" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.CARETOSTANDARDS,yytext());}

"DARE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.DARE,yytext());}

"DOMINANCE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.DOMINANCE,yytext());}

"HARDNESS" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.HARDNESS,yytext());}

"INDEPENDENCE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.INDEPENDENCE,yytext());}

"LIVELINESS" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.LIVELINESS,yytext());}

"OPENNESSTOCHANGE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.OPENNESSTOCHANGE,yytext());}

"PERFECTIONISM" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PERFECTIONISM,yytext());}

"PRIVACY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PRIVACY,yytext());}

"REASONING" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.REASONING,yytext());}

"SELFCONTROL" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SELFCONTROL,yytext());}

"SELFSUFFICIENCY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SELFSUFFICIENCY,yytext());}

"SENSITIVITY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SENSITIVITY,yytext());}

"SOCIABILITY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SOCIABILITY,yytext());}

"STABILITY" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.STABILITY,yytext());}

"STRESS" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.STRESS,yytext());}

"SURVEILLANCE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SURVEILLANCE,yytext());}

"ARRAYSTRING" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ARRAYSTRING,yytext());}

"FEATURES" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.FEATURES,yytext());}

"GROUP" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GROUP,yytext());}

"NODE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.NODE,yytext());}

"READFEATVAL" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.READFEATVAL,yytext());}

"READSTRING" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.READSTRING,yytext());}

"GENERATEGROUP" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GENERATEGROUP,yytext());}

"PRINTGRAPH" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PRINTGRAPH,yytext());}

"PRINTLN" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PRINTLN,yytext());}

"IF" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.IF,yytext());}

"ELSE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ELSE,yytext());}

"LOOP" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.LOOP,yytext());}

"FALSE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.FALSE,yytext());}

"TRUE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.TRUE,yytext());}


"DECLARE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.DECLARE,yytext());}

"GETFEATURES" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GETFEATURES,yytext());}

"GETSIMILITUDE" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GETSIMILITUDE,yytext());}

"MAIN" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.MAIN,yytext());}

"MAX" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.MAX,yytext());}

"MIN" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.MIN,yytext());}

"NEW" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.NEW,yytext());}

"NOT" {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.NOT,yytext());}



{STRING} {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.STRING,yytext());}

{IGNORE} {/*IGNORE*/}

{ID}   {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext()+" ID")); 
return symbol(sym.ID,yytext());}

{NUM}    {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.NUM,new Integer(yytext()));}

("==")  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.EQUAL_OP,yytext());}

("(") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PARENTH1,yytext());}

(")") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.PARENTH2,yytext());}

("{") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.CURLY_BR1,yytext());}

("}") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.CURLY_BR2,yytext());}

\[ {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.BRACKET1,yytext());}

\] {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.BRACKET2,yytext());}

(">=")  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GREATER_EQUAL_OP,yytext());}

("<=")  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.LESS_EQUAL_OP,yytext());}

("!=")  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.NOT_EQUAL_OP,yytext());}

("&&")  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.AND_OP,yytext());}

("%%")  {
System.out.println("OR_OP "+yytext());
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.OR_OP,yytext());}

">"  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.GREATER_OP,yytext());}

"<"  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.LESS_OP,yytext());}

"="  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ASSIG_OP,yytext());}

"+"  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.ADD_OP,yytext());}

"-"  {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SUBS_OP,yytext());}

(";") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.SEMICOLON_SIGN,yytext());}

(".") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.POINT_SIGN,yytext());}

(",") {
TablaSimbolos.add(new entradaTS(yyline, yycolumn, contador++, yytext())); 
return symbol(sym.COMMA_SIGN,yytext());}

{COMENTARIOS} {
    System.out.println("Comentarios");
/*IGNORE*/
}

{COMENTS} {
    System.out.println("Coments");/*IGNORE*/}

{COMENTSS} {System.out.println("Comentss");/*IGNORE*/}

{COMENT} {System.out.println("Coment");/*IGNORE*/}  


(({LET}* {NOACEPTADOS}+ {LET}* {NOACEPTADOS}*)+)  {
ManejadorDeErrores.add(new Error1("Lexico",yyline,yycolumn,yytext()));
return symbol(sym.ERRORLEX,yytext());}

{NO} {
ManejadorDeErrores.add(new Error1("Lexico",yyline,yycolumn,yytext())); 
return symbol(sym.ERRORLEX,yytext());}

[^] {
ManejadorDeErrores.add(new Error1("Lexico",yyline,yycolumn,yytext()));
return symbol(sym.ERRORLEX,yytext());}
}