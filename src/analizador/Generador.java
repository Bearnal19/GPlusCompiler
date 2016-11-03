/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

import java.io.PrintStream;

/**
 *
 * @author Neto
 */
public class Generador {

    public static final int ADD_OP = 1;
    public static final int SUBS_OPP = 2;
    public static final int ASSIG_OP = 3;
    public static final int IF = 4;
    public static final int ASIG = 5;
    public static final int GOTO = 6;
    public static final int LABEL = 7;
    public static final int PRINTLN = 8;
    public static final int GREATER_OP=9;
    public static final int LESS_OP=10;
    public static final int LESS_EQUAL_OP=11;
    public static final int GREATER_EQUAL_OP=12;
    public static final int EQUAL_OP=13;
    public static final int NOT_EQUAL_OP=14;
    public static final int NOT=15;
    public static final int AND=16;
    public static final int OR=17;
    public static final int READSTRING=18;
    public static final int READFEATVAL=19;
   public static final int GETFEATURES=20;
    public static final int PRINTGRAPH=21;
    public static final int GNGROUP=22;
    
    
    public static int contadorTemp = 0;
    public static int contadorEtiq = 0;
    protected static PrintStream out = System.out;

    public static String gc(int operacion, String arg1, String arg2, String resultado) {
        switch (operacion) {
            case ADD_OP:
                return "   	   " + resultado + " = " + arg1 + " + " + arg2 + "\n";
            case SUBS_OPP:
                return "   	   " + resultado + " = " + arg1 + " - " + arg2 + "\n";
            case ASSIG_OP:
                return "   	   " + resultado + " = " + arg1 + "\n";
            case IF:
                return "   	   si " + arg1 + " falso salta " + resultado + "\n";
            case GOTO:
                return "salta " + resultado + "\n";
            case ASIG:
                out.println("       " + resultado + " = " + arg1 + ";");
                return "   	   " + resultado + " = " + arg1 + "\n";
            case LABEL:
                return resultado + ":\n";
            case PRINTLN:
                return "   	   PRINTLN " + resultado + "\n";
            case PRINTGRAPH:
                return "   	   PRINTGRAPH " + resultado + "\n";
            case GNGROUP:
                return "   	   GENERATEGROUP " + resultado + "\n";
            case GETFEATURES:
                return "   	   GETFEATURES " + resultado + "\n";
            case GREATER_OP:
                return "   	   " + resultado + " = " + arg1 + " > " + arg2 + "\n";
            case LESS_OP:
                return "   	   " + resultado + " = " + arg1 + " < " + arg2 + "\n";
            case LESS_EQUAL_OP:
                return "   	   " + resultado + " = " + arg1 + " <= " + arg2 + "\n";
            case GREATER_EQUAL_OP:
                return "   	   " + resultado + " = " + arg1 + " >= " + arg2 + "\n";
            case EQUAL_OP:
                return "   	   " + resultado + " = " + arg1 + " == " + arg2 + "\n";
            case NOT_EQUAL_OP:
                return "   	   " + resultado + " = " + arg1 + " != " + arg2 + "\n";
            case NOT:
                return "   	   " + resultado + " = " + "NOT" + arg1 + arg2 + "\n";
            case AND:
                return "   	   " + resultado + " = " + arg1 + " && " + arg2 + "\n";
            case OR:
                return "   	   " + resultado + " = " + arg1 + " %% " + arg2 + "\n";
            case READSTRING:
                return "   	   " + resultado + " = InputString \n";
            case READFEATVAL:
                return "   	   " + resultado + " = InputInt \n";
                    
            default:
                return "Error en la generación de código\n";
        }
    }

    public static String nuevaTemp() {
        return "t" + contadorTemp++;
    }

    public static String nuevaEtiq() {
        return "L" + contadorEtiq++;
    }

}
