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
public class Features {
     int Affability,Reasoning,Stability, Dominance, Liveliness,CareToStandards,Dare,Sensitivity,Surveillance, Abstraction, Privacy, apprehension, OpennessToChange, SelfSufficiency, Perfectionism,Tension,Sociability,Anxiety, Hardness, Independence, SelfControl;
    TSDatos d;
    public Features(){
    
    Affability=Reasoning=Stability=Dominance=Liveliness=CareToStandards=Dare=Sensitivity=Surveillance=Abstraction=
            Privacy=apprehension=OpennessToChange=SelfSufficiency=Perfectionism=Tension=Sociability=Anxiety=Hardness=Independence=SelfControl=0;
    
    }

    Features(TSDatos aThis) {
    d=aThis; 
    Affability=Reasoning=Stability=Dominance=Liveliness=CareToStandards=Dare=Sensitivity=Surveillance=Abstraction=
            Privacy=apprehension=OpennessToChange=SelfSufficiency=Perfectionism=Tension=Sociability=Anxiety=Hardness=Independence=SelfControl=0;
    
    }
    
    public int getAffability() {
        return Affability;
    }

    public void setAffability(int Affability) {
        this.Affability = Affability;
    }

    public int getReasoning() {
        return Reasoning;
    }

    public void setReasoning(int Reasoning) {
        this.Reasoning = Reasoning;
    }

    public int getStability() {
        return Stability;
    }

    public void setStability(int Stability) {
        this.Stability = Stability;
    }

    public int getDominance() {
        return Dominance;
    }

    public void setDominance(int Dominance) {
        this.Dominance = Dominance;
    }

    public int getLiveliness() {
        return Liveliness;
    }

    public void setLiveliness(int Liveliness) {
        this.Liveliness = Liveliness;
    }

    public int getCareToStandards() {
        return CareToStandards;
    }

    public void setCareToStandards(int CareToStandards) {
        this.CareToStandards = CareToStandards;
    }

    public int getDare() {
        return Dare;
    }

    public void setDare(int Dare) {
        this.Dare = Dare;
    }

    public int getSensitivity() {
        return Sensitivity;
    }

    public void setSensitivity(int Sensitivity) {
        this.Sensitivity = Sensitivity;
    }

    public int getSurveillance() {
        return Surveillance;
    }

    public void setSurveillance(int Surveillance) {
        this.Surveillance = Surveillance;
    }

    public int getAbstraction() {
        return Abstraction;
    }

    public void setAbstraction(int Abstraction) {
        this.Abstraction = Abstraction;
    }

    public int getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(int Privacy) {
        this.Privacy = Privacy;
    }

    public int getApprehension() {
        return apprehension;
    }

    public void setApprehension(int apprehension) {
        this.apprehension = apprehension;
    }

    public int getOpennessToChange() {
        return OpennessToChange;
    }

    public void setOpennessToChange(int OpennessToChange) {
        this.OpennessToChange = OpennessToChange;
    }

    public int getSelfSufficiency() {
        return SelfSufficiency;
    }

    public void setSelfSufficiency(int SelfSufficiency) {
        this.SelfSufficiency = SelfSufficiency;
    }

    public int getPerfectionism() {
        return Perfectionism;
    }

    public void setPerfectionism(int Perfectionism) {
        this.Perfectionism = Perfectionism;
    }

    public int getTension() {
        return Tension;
    }

    public void setTension(int Tension) {
        this.Tension = Tension;
    }

    public int getSociability() {
        return Sociability;
    }

    public void setSociability(int Sociability) {
        this.Sociability = Sociability;
    }

    public int getAnxiety() {
        return Anxiety;
    }

    public void setAnxiety(int Anxiety) {
        this.Anxiety = Anxiety;
    }

    public int getHardness() {
        return Hardness;
    }

    public void setHardness(int Hardness) {
        this.Hardness = Hardness;
    }

    public int getIndependence() {
        return Independence;
    }

    public void setIndependence(int Independence) {
        this.Independence = Independence;
    }

    public int getSelfControl() {
        return SelfControl;
    }

    public void setSelfControl(int SelfControl) {
        this.SelfControl = SelfControl;
    }


}
