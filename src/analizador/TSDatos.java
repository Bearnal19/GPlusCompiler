/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

import java.util.ArrayList;

/**
 *
 * @author Cesar
 */
public class TSDatos {

        
String lexema;
    String tipo;
    Object valor;
    String linea;
    String columna;
    String nombre;
    String trabajo;
    ArrayList<String> parametros;
    Features features=new Features();
    String maxomin;
    

    public void TSDatos(){
        this.lexema = "";
        this.tipo = "";
        this.valor = null;
        this.linea = "";
        this.columna = "";
        this.maxomin="";
        this.parametros = new ArrayList<String> ();
        //this.features= new Features(this);
    }
    public void TSDatos(String lex, String tip, String val){
        this.lexema = "";
        this.tipo = "";
        this.valor = null;
        this.linea = "";
        this.columna = "";
        this.maxomin="";
        //this.features= new Features();
        this.parametros = new ArrayList<String>();
        
    }
    
  
    
    public String[] getInformation() {
    return new String[] {"Lexema","Tipo","Valor","Linea","Columna"};
    }
}