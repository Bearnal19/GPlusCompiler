package analizador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MARIELA
 */
import java.util.*;
public class TablaSimbolos
{
 
    public static class ElementoTs  // elemento de la tabla de simbolos,
    {
        String lexema;               //aqui debemos poner todos los atributos que queremos usar en el compilador
        int longitudBytes;
        public ElementoTs(String l) // constructor que recibe lexema
        {
            this.lexema=l;
        }
        public void setLexema(String l)   // a partir de aqui ponemos todos los "getters" y los "setters"
        {
            this.lexema=l;
        }
        public String getLexema()
        {
            return this.lexema;
        }
 
    }
 
    public static final int p=23;  // el tamaño del arreglo de hash (un número primo)
 
    public static int h(String x)  // la funcion de hash
    {   int suma=0;
        for (int i=0; i<x.length(); i++)
            suma+=x.charAt(i);
        return suma%p;
    }
    public static void main(String args[])
    {
 
 
    LinkedList<ElementoTs>[] listaLigada = (LinkedList<ElementoTs>[]) new LinkedList[p];  // el arreglo de hash de tamaño p, es un arreglo de listas ligadas
 
    for(int i=0; i<listaLigada.length; i++)
        listaLigada[i] = new LinkedList<ElementoTs>();   /* inicializamos el arreglo de listas ligadas*/
 
// creamos 2 datos de prueba
    ElementoTs a=new ElementoTs("ident01");
    ElementoTs b=new ElementoTs("ident02");
 
// Ingresamos datos (lexemas) usando el metodo de hash
    listaLigada[h(a.getLexema())].add(a);
    System.out.println("Agrego en lista "+h(a.getLexema()) );
    listaLigada[h(b.getLexema())].add(b);
    System.out.println("Agrego en lista "+h(b.getLexema()) );
 
// Ahora una busqueda
    String lexema01="ident02";  // lexema a buscar
    int buscar=h(lexema01);     // calculamos funcion de hash (lo pongo aqui, aparte, para  ser  claro)
    for (ElementoTs elemento : listaLigada[buscar] )   // solo buscamos en la lista ligada de todos los lexemas que tengan la misma funcion de hash
        if (elemento.getLexema().equals(lexema01)) System.out.println("Lo encontro: " + elemento.getLexema()+" estaba en la lista "+buscar);
 
 
    }
}
