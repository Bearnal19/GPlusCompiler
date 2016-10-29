/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

/**
 *
 * @author MARIELA
 */
public class Error1 {
    
    String tipo,valor;
    int linea, columna;
    
    public Error1(String tipo,int yyline,int yycolumn,String yytext){
        this.tipo=tipo;
        linea=yyline+1;
        columna=yycolumn+1;
        valor=yytext;
    }

    Error1(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String toString(){
        switch(tipo){
            case "sintactico":
                return " Syntax error: malformed program structure"+valor;
            case "Lexico":
                return "Lexical error: "+valor+" is an invalid String.  Line: "+linea+", column: "+columna+".";
            case "id_noexiste":
                return "Error en linea: "+linea+". Columna: "+columna+". El id "+"\""+valor+"\"";
            case "irrecuperable":
                return "";
            case "No se encontró ;":
                return "Syntax error: malformed program structure"+valor;
            default: 
                return valor;
        }
    }
}
