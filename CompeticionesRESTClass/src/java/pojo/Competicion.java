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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Random;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="competicion")
@XmlType(propOrder = {"id", "nombre", "deportes"})
public class Competicion implements Serializable{
    
    @XmlElement(name="deporte")
    @XmlElementWrapper(name="deportes")
    private ArrayList<Deporte> deportes;
    
    @XmlElement
    private String nombre;
    
    @XmlElement
    private int id;
    
    
    public int findDeporteIndex(int id){
        for (int i = 0; i<deportes.size() ; i++ )
            if(deportes.get(i).getId() == id)
                return i;
        return -1;
    }
    
    
    public Competicion() {
        deportes=new ArrayList<>();
        this.id =  new Random().nextInt(10000);
    }
    
    public Competicion(String nombre) {
        this.nombre = nombre;
        this.id =  new Random().nextInt(10000);
    }

    
    public Competicion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public Competicion(String nombre, ArrayList<Deporte> deportes) {
        this.nombre = nombre;
        this.setDeportes(deportes);
        this.id =  new Random().nextInt(10000);

    }

    public Deporte anadirDeporte(Deporte deporte){
        getDeportes().add(deporte);
        return deporte;
    }
    
    public void limpiarCompeticion(){
        this.setDeportes(new ArrayList<>());
    }
    
//    public String toString(){
//        StringBuilder deportes = new StringBuilder();
//        for(Deporte deporte : this.deportes){
//            deportes.append(deporte.toString());
//        }
//        return "\n<competicion>"
//                + "\n<id>" + this.id + "</id>"
//                + "\n<nombre>" + this.nombre + "</nombre>"
//                + "\n<deportes>" + deportes + "\n</deportes>"
//                +"\n</competicion>";
//    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the deportes
     */
    public ArrayList<Deporte> getDeportes() {
        return deportes;
    }

    /**
     * @param deportes the deportes to set
     */
    public void setDeportes(ArrayList<Deporte> deportes) {
        this.deportes = deportes;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }
    
}


