/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="deporte")
public class Deporte implements Serializable{


    @XmlElement(name = "nombre")
    private String nombre;
    @XmlElement(name = "tipo")
    private String tipo;
    @XmlElement(name = "equipos")
    private String equipos;
    @XmlElement(name = "tamanoEquipo")
    private String tamanoEquipo;
    
    public Deporte(){    
    }
    
    public Deporte(String nombre, String tipo, String equipos, String tamanoEquipo){
        this.nombre = nombre;
        this.tipo = tipo;
        this.equipos = equipos;
        this.tamanoEquipo = tamanoEquipo;
    }
    
    
    public void imprimirBonito(){
        System.out.println("Nombre: " + this.getNombre());
        System.out.println("Tipo: " + this.getTipo());
        System.out.println("Equipos: " + this.getEquipos());
        System.out.println("Tamaño equipo: " + this.getTamanoEquipo());
    }
   
    public String toString(){
        StringBuilder deporte = new StringBuilder();
        deporte.append("\n<deporte>");
        deporte.append("\n<nombre>" + nombre + "</nombre>");
        deporte.append("\n<tipo>" + tipo + "</tipo>");  
        deporte.append("\n<equipos>" + equipos + "</equipos>");
        deporte.append("\n<tamanoEquipo>" + tamanoEquipo + "</tamanoEquipo>");
        deporte.append("\n</deporte>");
        return deporte.toString();
    }
    
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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
        /**
     * @return the equipos
     */
    public String getEquipos() {
        return equipos;
    }

    /**
     * @param equipos the equipos to set
     */
    public void setEquipos(String equipos) {
        this.equipos = equipos;
    }

    /**
     * @return the tamanoEquipo
     */
    public String getTamanoEquipo() {
        return tamanoEquipo;
    }

    /**
     * @param tamanoEquipo the tamanoEquipo to set
     */
    public void setTamanoEquipo(String tamanoEquipo) {
        this.tamanoEquipo = tamanoEquipo;
    }
}