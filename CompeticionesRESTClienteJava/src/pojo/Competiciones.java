/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="competiciones")
public class Competiciones implements Serializable {
    
    @XmlElement(name="competicion")
    private ArrayList<Competicion> competiciones;

    public Competiciones() {
        competiciones = new ArrayList<>();
    }
    
    /**
     * @return the competiciones
     */
    public ArrayList<Competicion> getCompeticiones() {
        return competiciones;
    }

    /**
     * @param competiciones the competiciones to set
     */
    public void setCompeticiones(ArrayList<Competicion> competiciones) {
        this.competiciones = competiciones;
    }    
}


