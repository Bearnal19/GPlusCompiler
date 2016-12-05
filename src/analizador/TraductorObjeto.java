/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

/**
 *
 * @author Cesar
 */
public class TraductorObjeto {
String archivoci,codigogenerado,data;
int ntemporales;
TSimbolos tablaSimbolos;
    public TraductorObjeto(String intermedio,TSimbolos tabla) {
        archivoci = intermedio;
        
        codigogenerado = "include \\masm32\\include\\masm32rt.inc\n" 
                + "includelib \\masm32\\lib\\kernel32.lib\n" 
                + "includelib \\masm32\\lib\\masm32.lib\n";
        data = ".data\n";//Seccion DATA
        
        tablaSimbolos = tabla;
        ntemporales = Generador.contadorTemp;
        generarDatos();
                /*
                + ".data?\n\n" //Seccion DATA?
                + ".code\nstart: \n\n"
                + "exit\n"//Seccion CODE
                + "end start\n";*/
    }

    public String getCodigogenerado() {
        return codigogenerado;
    }

    public void setCodigogenerado(String codigogenerado) {
        this.codigogenerado = codigogenerado;
    }

    private void generarDatos() {
        for (int i = 0; i <= ntemporales; i++) {
            data += "    t" + i + " db 0\n";
        }
        //data += "    cen db 0\n    dece db 0\n    uni db 0\n";
        String lex, lex2[];
        for (int i = 0; i < tablaSimbolos.ts.size(); i++) {
            if (tablaSimbolos.ts.get(i).valor != null) {
                lex = tablaSimbolos.ts.get(i).lexema;
                lex2 = lex.split("\\.");
                if (lex2.length > 1) {
                    lex = lex2[0] + "_" + lex2[1];
                }
                data += "    " + lex + " db 0\n";//
            } else {
                lex = tablaSimbolos.ts.get(i).lexema;
                data += "    " + lex + " db 0\n";//
            }
        }
        codigogenerado += data + ".code\nstart: \n\n"
                + "exit\n"//Seccion CODE
                + "end start\n";
    }
    
    
    
}
