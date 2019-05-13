/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import pojo.Usuario;

/**
 *
 * @author squid
 */
public class Database {
    
    SQLiteJDBC jdbc = null;
    
    public Database(){
        try{
            jdbc = new SQLiteJDBC();
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
            return user.getToken();
        }
        return "Wrong password";
    }

    public String signup(Usuario submittedUser) {
        System.out.println("Db.Database.signup()");
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);
        submittedUser.setToken(token);
        int userId = jdbc.createUser(submittedUser);
        if( userId != -1 ){
            return jdbc.addToken(userId, token);
        }
        else
            return "User already exists";
    }

    public Usuario getToken(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getUsernameFromToken(String token) {
        return jdbc.getUserFromToken(token).getUsername();
    }
}
