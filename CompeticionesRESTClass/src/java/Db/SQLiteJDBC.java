/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;


import java.sql.*;
import java.util.ArrayList;
import pojo.Competicion;
import pojo.Deporte;
import pojo.Usuario;
/**
 *
 * @author squid
 */
// https://www.tutorialspoint.com/sqlite/sqlite_java.htm
public class SQLiteJDBC {
  
    String connectionAddress =  "jdbc:sqlite:competiciones.db";
    Connection c = null;

    public SQLiteJDBC(){
        testTables();
    }
    private void sqlError(Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.err.println("Couldn't finish SQL operation");
        try{
            this.c.close();
        }catch (SQLException eSQL){
            System.exit(0);
        }
    }
    
    private void executeUpdate(String query) throws SQLException{
        System.out.println(query);
        Statement stmt = c.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    private void testTables(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Usuarios;" );
            System.out.println("Db.SQLiteJDBC.testTables()");
        } catch (org.sqlite.SQLiteException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            createTables();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            createTables();
        } catch ( Exception e ) {
            sqlError(e);
        }
        System.out.println("SUCCESS");
    }
    
    private void createTables() {
      Statement stmt = null;
      try {
        String sql1 = "CREATE TABLE Usuarios " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " USERNAME       TEXT    NOT NULL, " + 
                        " PASSWORD       TEXT     NOT NULL);"; 
        String sql2 = "CREATE TABLE Tokens " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " TOKEN          TEXT, " +
                        " EXPIRY         DATE, " + 
                        " USUARIO_ID INTEGER NOT NULL REFERENCES Usuarios(ID));";
               
        String sql3 = "CREATE TABLE Competiciones " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NOMBRE         TEXT    NOT NULL);"; 
        String sql4 = "CREATE TABLE Deportes " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NOMBRE         TEXT    NOT NULL, " +
                        " TIPO           TEXT    NOT NULL, " + 
                        " EQUIPOS        TEXT    NOT NULL, " + 
                        " TAMANOEQUIPO   TEXT    NOT NULL, " +
                        " COMPETICION_ID INTEGER NOT NULL REFERENCES Competiciones(ID));"; 
        String sql5 = "CREATE TABLE CompeticionesUsuarios " +
                        "(USUARIO_ID INTEGER NOT NULL REFERENCES Usuarios(ID), " +
                        " COMPETICION_ID INTEGER NOT NULL REFERENCES Competiciones(ID));"; 
        
        String[] statements = new String[] {sql1, sql2, sql3, sql4, sql5};
        for (String statement : statements)
        {
            executeUpdate(statement);
            System.out.println("Table created successfully");
        }
        this.c.close();
        System.out.println("ALL tables created successfully");

      } catch ( Exception e ) {
        sqlError(e);

      }
    }
 public Usuario getUser(String username){
        Statement stmt = null;
        Usuario user = null;
        String query = "SELECT * FROM Usuarios WHERE USERNAME='" + username + "';";
        System.out.println(query);
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            while ( rs.next() ) {
                Integer  id = rs.getInt("ID");
                String  password = rs.getString("PASSWORD");
                String token = getToken(id);
                user = new Usuario(username, password, token);
                
            }
            rs.close();
            stmt.close();
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
        return user;
    }
    
    private String getToken(Integer userId) {
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Tokens WHERE USUARIO_ID='" + userId + "';" );
            while ( rs.next() ) {
                return rs.getString("TOKEN");
            }
            rs.close();
            stmt.close();
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
        return null;
    }
    
    Integer createUser(Usuario submittedUser) {
        Statement stmt = null;
        String query1 = "INSERT INTO Usuarios (USERNAME, PASSWORD) VALUES('"
                    + submittedUser.getUsername() + "','"
                    + submittedUser.getPassword() + "');";
        System.out.println("new user: " + query1);
        String query2 = "SELECT ID FROM Usuarios WHERE USERNAME='" + 
                        submittedUser.getUsername() + "';";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            stmt = this.c.createStatement();
            stmt.executeUpdate( query1 );
            stmt.close();
            this.c.close();
            this.c = DriverManager.getConnection(connectionAddress);
            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query2 );
            int userId = rs.getInt("ID");
            this.c.close();
            return userId;
        }catch ( Exception e ) {
          sqlError(e);
          return -1;
        }
    }

    Usuario getUserFromToken(String token) {
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Tokens WHERE TOKEN='" + token + "';" );
            rs.next();
            Integer userId = rs.getInt("USUARIO_ID");
            rs.close();
            ResultSet rs2 = stmt.executeQuery( "SELECT * FROM Usuarios WHERE ID='" + userId + "';" );
            rs2.next();
            String username = rs2.getString("USERNAME");
            String password = rs2.getString("PASSWORD");
            stmt.close();
            this.c.close();
            return new Usuario(username, password, token);
        }catch ( Exception e ) {
            sqlError(e);
       }
        return null;   
    }

    String addToken(int userId, String token) {
        String query1 = "INSERT INTO Tokens (USUARIO_ID, TOKEN) VALUES('"
                    + userId + "','"
                    + token + "');";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
         
            executeUpdate(query1);
            
            this.c.close();
            return token;
        }catch ( Exception e ) {
            sqlError(e);
            return "Could not add token";
        }
    }

    void clear(String table) {
        String query1 = "DELETE FROM " + table + ";";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            executeUpdate( query1 );
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
    }
    
    ArrayList<Competicion> getCompeticiones(){
        Statement stmt = null;
        ArrayList<Competicion> competiciones = new ArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
//            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Competiciones;" );
            while ( rs.next() ) {
                Integer  id = rs.getInt("ID");
                String  nombre = rs.getString("NOMBRE");
                competiciones.add(new Competicion(id,nombre));
                
            }
            stmt.close();
            for(Competicion competicion: competiciones){
                competicion.setDeportes(getDeportes(competicion));
                System.out.println(competicion.getId());
            }
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
       }
        return competiciones;   
    }
    
    private ArrayList<Deporte> getDeportes(Competicion competicion) {
        Statement stmt = null;
        ArrayList<Deporte> deportes = new ArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
//            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Deportes WHERE "
                    + "COMPETICION_ID=" + competicion.getId() + ";" );
            while ( rs.next() ) {
                Integer id = rs.getInt("ID");
                String  nombre = rs.getString("NOMBRE");
                String  tipo = rs.getString("TIPO");
                String  equipos = rs.getString("EQUIPOS");
                String  tamanoEquipo = rs.getString("TAMANOEQUIPO");
                
                Deporte deporte = new Deporte(id,nombre, tipo, equipos, tamanoEquipo);
                deportes.add(deporte);
            }
            stmt.close();
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
       }
        return deportes;   
    }

    void clearCompeticiones() {
        clear("Competiciones");
    }

    void addCompeticion(Competicion competicion) {
        String query = "INSERT INTO Competiciones VALUES('" 
                + competicion.getId() + "', '" + competicion.getNombre() + "');";
        ArrayList<String> statements = new ArrayList();
        statements.add(query);
        for (Deporte deporte : competicion.getDeportes()){
            String deporteQuery = "INSERT INTO Deportes VALUES('" +
                    + deporte.getId() + "', '"
                    + deporte.getNombre()+ "', '"
                    + deporte.getTipo()+ "', '"
                    + deporte.getEquipos()+ "', '"
                    + deporte.getTamanoEquipo()+ "', '"
                    + competicion.getId() + "');";
            statements.add(deporteQuery);
        }
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            for (String statement : statements)
                executeUpdate( statement );
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
    }

    void deleteCompeticion(int id) {
        String query = "DELETE FROM  Competiciones WHERE ID='" + id + "';";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            executeUpdate( query );
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
    }

    void addDeporte(Competicion competicion, Deporte deporte) {
        String query = "INSERT INTO Deportes VALUES(" +
                    + deporte.getId() + ", '"
                    + deporte.getNombre()+ "', '"
                    + deporte.getTipo()+ "', '"
                    + deporte.getEquipos()+ "', '"
                    + deporte.getTamanoEquipo()+ "', "
                    + competicion.getId() + ");";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            executeUpdate( query );
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
    }

    void deleteDeporte(Competicion competicion, int idDeporte) {
        String query = "DELETE FROM  Deportes WHERE "
                + "ID=" + idDeporte + " AND "
                + "COMPETICION_ID=" + competicion.getId() + ";";
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection(connectionAddress);
            executeUpdate( query );
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e);
        }
    }

}
