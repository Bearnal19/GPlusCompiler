/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

import java.util.ArrayList;

/**
 *
 * @author MARIELA
 */
public class ManejadorErrores {
    private ArrayList<Error1> mErrores;
    
    public ManejadorErrores(){
        mErrores = new ArrayList<Error1>();
    }
    
    public void clear(){
        mErrores.clear();
    }
    
    public String mostrarErrores(){
        String errores="";
        for(int i =0;i<mErrores.size();i++)
        {
            errores+=(mErrores.get(i).toString()+"\n");
        }
        return errores;
    }
    
    public String mostrarErrores2(){
        String errores="";
        for(int i =mErrores.size()-1;i>=0;i--)
        {
            errores+=(mErrores.get(i).toString()+"\n");
        }
        return errores;
    }
    
    public void anadirError(Error1 e){
        mErrores.add(e);
    }
    
    public boolean empty(){
        return mErrores.isEmpty();
    }
    
    
}
