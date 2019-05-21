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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author squid
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="usuario")
@XmlType(propOrder = {"username", "password"})
public class Usuario implements Serializable {

    @XmlTransient
    private int id;
    @XmlElement
    private String username;
    @XmlElement
    private String password;
//    @XmlElement
//    private String token;
    
    
    public Usuario(){}
    
     public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
     
//    public Usuario(String username, String password, String token) {
//        this.username = username;
//        this.password = password;
////        this.token = token;
//    }

    public Usuario(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
//        this.token = token;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

//    /**
//     * @return the token
//     */
//    public String getToken() {
//        return token;
//    }
//
//    /**
//     * @param token the token to set
//     */
//    public void setToken(String token) {
//        this.token = token;
//    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    
}
