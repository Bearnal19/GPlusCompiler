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
public class TSimbolos {
ArrayList<TSDatos> ts;
    ArrayList<String> var;
    
    public TSimbolos(){
       ts =new ArrayList<TSDatos>();
       var = new ArrayList<String>();
       System.out.print("Tabla semantica creada");    
    }
    
    public boolean insertar(String idnombre){
        TSDatos simbolo = buscar(idnombre);
        
        if (simbolo == null) {
            TSDatos nuevo = new TSDatos();
            nuevo.lexema = idnombre;
            
            ts.add(nuevo);
            
            return true;
        }        
        return false;
        /*else {
           VPrincipal.ManejadorDeErrores.add(new errorsin("Identificador "+idnombre+" Duplicado"));
            System.out.print("Identificador "+idnombre+" Duplicado\n");
        }*/
    }
    
    public boolean actualizar(String lexema, String atributo, Object atributoTipo){
        TSDatos simbolo = buscar(lexema);
        if (simbolo != null) {
            ts.remove(simbolo);
            switch(atributo){
                case "tipo":
                    simbolo.tipo = atributoTipo.toString();
                    break;
                case "valor":
                    simbolo.valor = atributoTipo;
                    break;
                case "linea":
                    simbolo.linea = atributoTipo.toString();
                    break;
                case "columna":
                    simbolo.columna = atributoTipo.toString();
                    break;
                case "maxomin": 
                    simbolo.maxomin= atributoTipo.toString();
                    break;
                case "nombre": 
                    simbolo.nombre= atributoTipo.toString();
                    break;
                case "trabajo": 
                    simbolo.trabajo= atributoTipo.toString();
                    break;
                case "setfeatures": 
                    simbolo.features= getfeatures(atributoTipo.toString());
                    break;
                case "Affability": simbolo.features.Affability=(Integer.parseInt(atributoTipo.toString())); break;
                case "Reasoning": simbolo.features.setReasoning(Integer.parseInt(atributoTipo.toString()));break;
                case "Stability": simbolo.features.setStability(Integer.parseInt(atributoTipo.toString()));break;
                case "Dominance": simbolo.features.setDominance(Integer.parseInt(atributoTipo.toString()));break;
                case "Liveliness": simbolo.features.setLiveliness(Integer.parseInt(atributoTipo.toString()));break;
                case "CareToStandards": simbolo.features.setCareToStandards(Integer.parseInt(atributoTipo.toString()));break;
                case "Dare": simbolo.features.setDare(Integer.parseInt(atributoTipo.toString()));break;
                case "Sensitivity": simbolo.features.setSensitivity(Integer.parseInt(atributoTipo.toString()));break;
                case "Surveillance": simbolo.features.setSurveillance(Integer.parseInt(atributoTipo.toString()));break;
                case "Abstraction": simbolo.features.setAbstraction(Integer.parseInt(atributoTipo.toString()));break;
                case "Privacy": simbolo.features.setPrivacy(Integer.parseInt(atributoTipo.toString())); break;
                case "apprehension": simbolo.features.setApprehension(Integer.parseInt(atributoTipo.toString())); break;
                case "OpennessToChange": simbolo.features.setOpennessToChange(Integer.parseInt(atributoTipo.toString())); break;
                case "SelfSufficiency": simbolo.features.setSelfSufficiency(Integer.parseInt(atributoTipo.toString())); break;
                case "Perfectionism": simbolo.features.setPerfectionism(Integer.parseInt(atributoTipo.toString())); break;
                case "Tension": simbolo.features.setTension(Integer.parseInt(atributoTipo.toString())); break;
                case "Sociability": simbolo.features.setSociability(Integer.parseInt(atributoTipo.toString())); break;
                case "Anxiety": simbolo.features.setAnxiety(Integer.parseInt(atributoTipo.toString())); break; 
                case "Hardness": simbolo.features.setHardness(Integer.parseInt(atributoTipo.toString())); break; 
                case "Independence": simbolo.features.setIndependence(Integer.parseInt(atributoTipo.toString())); break; 
                case "SelfControl": simbolo.features.setSelfControl(Integer.parseInt(atributoTipo.toString())); break; 
            }
            ts.add(simbolo);
            
            return true;
        }
        else {
         //   error.add("No existe: "+lexema);
            return false;
        }
    }
    
    public TSDatos buscar(String lexema){
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return ts.get(i);
            }
        }
        return null;
    }
    
    public boolean buscarLex(String lexema){
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return true;
            }
        }
        return false;
    }
    public String RegresarTipo(String lexema){
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return ts.get(i).tipo;
            }
        }
        return "";
    }
    public Object RegresarValorString(String lexema){
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return ts.get(i).valor;
            }
        }
        return null;
    }
     private Features getfeatures(String lexema) {
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return ts.get(i).features;
            }
        }
        return null;
    }
    public int regresarSumaFeatures(String lexema){
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                return ts.get(i).features.Abstraction+ts.get(i).features.Affability+ts.get(i).features.Anxiety
                      +ts.get(i).features.CareToStandards+ts.get(i).features.Dare+ts.get(i).features.Dominance
                      +ts.get(i).features.Hardness+ts.get(i).features.Independence+ts.get(i).features.Liveliness
                      +ts.get(i).features.OpennessToChange+ts.get(i).features.Perfectionism+ts.get(i).features.Privacy
                      +ts.get(i).features.Reasoning+ts.get(i).features.SelfControl+ts.get(i).features.SelfSufficiency
                      +ts.get(i).features.Sensitivity+ts.get(i).features.Sociability+ts.get(i).features.Stability
                      +ts.get(i).features.Surveillance+ts.get(i).features.Tension+ts.get(i).features.apprehension;
            }
        }
        return -1;
    }
    public int RegresarValorFeatures(String lexema,String feature){
        int valor=0;
        for(int i=0;i<ts.size();i++){
            
            if(ts.get(i).lexema.equals(lexema)){
                switch(feature.toLowerCase()){
                  case "affability": valor=ts.get(i).features.getAffability(); break;
                case "reasoning": valor=ts.get(i).features.getReasoning();  break;
                case "stability": valor=ts.get(i).features.getStability();  break;
                case "dominance": valor=ts.get(i).features.getDominance();  break;
                case "liveliness":  valor=ts.get(i).features.getLiveliness();  break;
                case "caretostandards": valor=ts.get(i).features.getCareToStandards();  break;
                case "dare": valor=ts.get(i).features.getDare();  break;
                case "sensitivity": valor=ts.get(i).features.getSensitivity();  break;
                case "surveillance": valor=ts.get(i).features.getSurveillance();  break;
                case "abstraction": valor=ts.get(i).features.getAbstraction(); break;
                case "privacy": valor=ts.get(i).features.getPrivacy();  break;
                case "apprehension": valor=ts.get(i).features.getApprehension();  break;
                case "opennesstochange": valor=ts.get(i).features.getOpennessToChange();  break;
                case "selfsufficiency": valor=ts.get(i).features.getSelfSufficiency();  break;
                case "perfectionism": valor=ts.get(i).features.getPerfectionism();  break;
                case "tension": valor=ts.get(i).features.getTension();  break;
                case "sociability": valor=ts.get(i).features.getSociability();  break;
                case "anxiety": valor=ts.get(i).features.getAnxiety();  break; 
                case "hardness": valor=ts.get(i).features.getHardness();  break; 
                case "independence": valor=ts.get(i).features.getIndependence();  break; 
                case "selfcontrol": valor=ts.get(i).features.getSelfControl();  break; 
                
                }
                return valor;
            }
        }
        return -1;
    }
    public void pushVar(String tipo, String lexema, String fila, Object columna){
        var.add(tipo+","+lexema+","+fila+","+columna);
    }
}