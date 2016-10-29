package analizador;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bárbara Mariela
 */
public class entradaTS {
    int linea;
    int columna;
    int numToken;
    String valor;
    sym simbolo;
entradaTS (int linea, int columna, int numToken, String valor){
    this.linea = linea+1;
    this.columna = columna+1;
    this.numToken = numToken;
    this.valor = valor;
    simbolo = null;
}
entradaTS (int linea, int columna, int numToken, String valor, sym s){
    this.linea = linea+1;
    this.columna = columna+1;
    this.numToken = numToken;
    this.valor = valor;
    simbolo = s;
}

public String toString() {
        return "Token #"+numToken+"Simbolo: "+simbolo+" C.Lexico: "+valor+" ["+linea
        + "," +columna + "]";
    }
}
