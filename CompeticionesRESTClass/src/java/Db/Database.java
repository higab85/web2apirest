/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import pojo.Competicion;
import pojo.Competiciones;
import pojo.Deporte;
import pojo.Usuario;

/**
 *
 * @author squid
 */
public class Database {
    
    JDBC jdbc = null;
    
    public Database(){
        try{
            jdbc = new JDBC();
        }catch (Exception e){
            System.err.println(e);
        }
    }
    
    public String login(Usuario submittedUser){
        String username = submittedUser.getUsername();
        String password = submittedUser.getPassword();
        Usuario user = jdbc.getUser(username);
        if(user == null)
            return "User doesn't exist";
        else if (user.getPassword().equals(password)){
            return getToken(user);
        }
        return "Wrong password";
    }
    
    public String makeTokenForUser(Usuario user){
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        jdbc.addToken(user.getId(), token);
        return token;
    }

    public String signup(Usuario submittedUser) {
        System.out.println("Db.Database.signup()");
        int userId = jdbc.createUser(submittedUser);
        if( userId != -1 )
            return makeTokenForUser(submittedUser);
        else
            return "User already exists";
    }

    public String getToken(Usuario user) {
        String token = jdbc.getToken(user.getId());
        if (token == null)
            token = makeTokenForUser(user);
        System.out.println("Get token: " + token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        System.out.println("token: " + token);
        Usuario user = jdbc.getUserFromToken(token);
        System.out.println("user: " + user);
        if (user != null)
            return user.getUsername();
        return "Invalid token";
    }

    public Boolean isTokenValid(String token) {
        Usuario user = jdbc.getUserFromToken(token);
        System.out.println("user: " + user);
        if (user == null)
            return false;
        return true;
    }
    
    public void setCompeticiones(Competiciones competiciones, Usuario user) {
        jdbc.clearCompeticiones();
        for(Competicion competicion : competiciones.getCompeticiones()){
            addCompeticion(competicion, user);
        }
    }
    
    public Competiciones getCompeticiones() {
        Competiciones competiciones = new Competiciones();
        competiciones.setCompeticiones(jdbc.getCompeticiones());
        return competiciones;
    }

    public void addCompeticion(Competicion competicion, Usuario user) {
        jdbc.addCompeticion(competicion);
        jdbc.linkCompeticionUsuario(competicion, user);
    }

    public void setCompeticion(Integer competicionId, Competicion competicionNew) {
        jdbc.deleteCompeticion(competicionId);
        competicionNew.setId(competicionId);
        jdbc.addCompeticion(competicionNew);
    }

    public void deleteCompeticion(int id) {
        jdbc.deleteCompeticion(id);
    }

    public void addDeporte(Competicion competicion, Deporte deporte) {
        jdbc.addDeporte(competicion, deporte);
    }

    public void setDeporte(Competicion competicion, int idDeporte, Deporte deporte) {
        jdbc.deleteDeporte(competicion, idDeporte);
        jdbc.addDeporte(competicion, deporte);
    }

    public void deleteDeporte(Competicion competicion, int idDeporte) {
        jdbc.deleteDeporte(competicion, idDeporte);
    }

    public Usuario getUserFromUsername(String username) {
        return jdbc.getUser(username);
    }

    public Competiciones getUserCompetitions(Usuario user) {
        return jdbc.getCompeticiones(user);
    }

    public boolean userIsOwner(Usuario user, Competicion competicionCorrecta) {
        Competiciones competiciones = getUserCompetitions(user);
        for(Competicion competicion : competiciones.getCompeticiones())
            if (competicion.getId() == competicionCorrecta.getId())
                return true;
        return false;
    }

    public void shareCompeticion(Competicion competicion, String username) {
        Usuario user = jdbc.getUser(username);
        if (!userIsOwner(user, competicion))
            jdbc.linkCompeticionUsuario(competicion, user);
    }

    public void logout(Usuario user) {
        jdbc.logoutUser(user);
    }

    public boolean checkUserExists(Usuario credenciales) {
        Usuario user = jdbc.getUser(credenciales.getUsername());
        return user != null;
    }
}
