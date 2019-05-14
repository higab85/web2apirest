/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;


import java.sql.*;
import java.util.ArrayList;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import pojo.Competicion;
import pojo.Competiciones;
import pojo.Deporte;
import pojo.Usuario;
/**
 *
 * @author squid
 */
// https://www.tutorialspoint.com/sqlite/sqlite_java.htm
@Singleton
public class JDBC {
  
    DataSource dataSource = null;
    Connection c = null;
    
    public JDBC() throws NamingException{
        InitialContext initialcontext = new InitialContext();
        dataSource = (DataSource) initialcontext.lookup("jdbc/competicionesREST");
//        testTables();
    }
    
    private void connectDB() throws ClassNotFoundException{
        try{
            c = dataSource.getConnection();
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    private void disconnectDB(){
        try{
            c.close();
            System.out.println("Database disconnected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot disconnect the database!", e);
        }
    }
    private void sqlError(Exception e, String query){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        System.err.println("Couldn't finish SQL operation: " + query);

        disconnectDB();
    }
    
    private void execute(String statement){
        try{
            connectDB();
            Statement stmt = this.c.createStatement();
            stmt.executeUpdate(statement);
            stmt.close();   
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e, statement);
        }
        disconnectDB();
    }
    
    private void execute(ArrayList<String> statements){
        try{
            connectDB();
            this.c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            for (String statement : statements){
                System.out.println(statement);
                stmt.executeUpdate(statement);
            }
            this.c.commit();
            stmt.close();   
            this.c.setAutoCommit(true);
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e, statements.get(0));
        }
        disconnectDB();
    }

    private void testTables(){
        String query = "SELECT * FROM Usuarios;";
        try {
            connectDB();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            rs.close();
            System.out.println("Db.SQLiteJDBC.testTables()");
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            createTables();
        } catch ( Exception e ) {
            sqlError(e, query);
        }
        disconnectDB();
        System.out.println("SUCCESS");
    }
    
    private void createTables() {
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
            execute(statement);
            System.out.println("Table created successfully");
        }
        System.out.println("ALL tables created successfully");

      } catch ( Exception e ) {
        sqlError(e, "createTables");

      }
    }
 public Usuario getUser(String username){
        Statement stmt = null;
        Usuario user = null;
        String query = "SELECT * FROM Usuarios WHERE USERNAME='" + username + "';";
        System.out.println(query);
        try {
            connectDB();
            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            while ( rs.next() ) {
                Integer  id = rs.getInt("ID");
                String  password = rs.getString("PASSWORD");
                String token = getToken(id);
                user = new Usuario(id, username, password, token);
                
            }
            rs.close();
            stmt.close();
        }catch ( Exception e ) {
            sqlError(e, query);
        }
        disconnectDB();
        return user;
    }
    
    private String getToken(Integer userId) {
        Statement stmt = null;
        String query = "SELECT * FROM Tokens WHERE USUARIO_ID='" + userId + "';";
        try {
            connectDB();
            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            while ( rs.next() ) {
                return rs.getString("TOKEN");
            }
            rs.close();
            stmt.close();
            this.c.close();
        }catch ( Exception e ) {
            sqlError(e, query);
        }
        disconnectDB();
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
        execute( query1 );
        try {
            connectDB();
            System.out.println("Returning ID: " + query2);
            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query2 );
            rs.next();
            int userId = rs.getInt("ID");
            disconnectDB();
            return userId;
        }catch ( Exception e ) {
          sqlError(e, query1);
          disconnectDB();
          return -1;
        }
        
    }

    Usuario getUserFromToken(String token) {
        Statement stmt = null;
        String query = "SELECT * FROM Tokens WHERE TOKEN='" + token + "';";
        try {
            connectDB();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            rs.next();
            Integer userId = rs.getInt("USUARIO_ID");
            rs.close();
            ResultSet rs2 = stmt.executeQuery( "SELECT * FROM Usuarios WHERE ID='" + userId + "';" );
            rs2.next();
            String username = rs2.getString("USERNAME");
            String password = rs2.getString("PASSWORD");
            stmt.close();
            disconnectDB();
            return new Usuario(username, password, token);
        }catch ( Exception e ) {
            sqlError(e, query);
       }
        return null;   
    }

    String addToken(int userId, String token) {
        String query1 = "INSERT INTO Tokens (USUARIO_ID, TOKEN) VALUES('"
                    + userId + "','"
                    + token + "');";
        execute(query1);
        return token;
    }

    void clear(String table) {
        String query1 = "DELETE FROM " + table + ";";
        execute( query1 );
    }
    
    ArrayList<Competicion> getCompeticiones(){
        Statement stmt = null;
        ArrayList<Competicion> competiciones = new ArrayList();
        String query = "SELECT * FROM Competiciones;";
        try {
            connectDB();
            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
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
            disconnectDB();
        }catch ( Exception e ) {
            sqlError(e, query);
       }
        return competiciones;   
    }
    
    private ArrayList<Deporte> getDeportes(Competicion competicion) {
        Statement stmt = null;
        ArrayList<Deporte> deportes = new ArrayList();
        String query = "SELECT * FROM Deportes WHERE "
                    + "COMPETICION_ID=" + competicion.getId() + ";";
        try {
            connectDB();
//            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
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
            
        }catch ( Exception e ) {
            sqlError(e, query);
       }
        disconnectDB();
        return deportes;   
    }

    void clearCompeticiones() {
        clear("Competiciones");
    }

    void addCompeticion(Competicion competicion) {
        String query = "INSERT INTO Competiciones VALUES(" 
                + competicion.getId() + ", '" + competicion.getNombre() + "');";
        ArrayList<String> statements = new ArrayList();
        statements.add(query);
        
        for (Deporte deporte : competicion.getDeportes()){
            String deporteQuery = "INSERT INTO Deportes VALUES(" +
                    + deporte.getId() + ", '"
                    + deporte.getNombre()+ "', '"
                    + deporte.getTipo()+ "', '"
                    + deporte.getEquipos()+ "', '"
                    + deporte.getTamanoEquipo()+ "', "
                    + competicion.getId() + ");";
            statements.add(deporteQuery);
        }
        execute(statements);
    }

    void deleteCompeticion(int id) {
        String query = "DELETE FROM  Competiciones WHERE ID='" + id + "';";
        execute( query );
    }

    void addDeporte(Competicion competicion, Deporte deporte) {
        String query = "INSERT INTO Deportes VALUES(" +
                    + deporte.getId() + ", '"
                    + deporte.getNombre()+ "', '"
                    + deporte.getTipo()+ "', '"
                    + deporte.getEquipos()+ "', '"
                    + deporte.getTamanoEquipo()+ "', "
                    + competicion.getId() + ");";
        execute( query );
    }

    void deleteDeporte(Competicion competicion, int idDeporte) {
        String query = "DELETE FROM  Deportes WHERE "
                + "ID=" + idDeporte + " AND "
                + "COMPETICION_ID=" + competicion.getId() + ";";
        execute( query );

    }

    Competiciones getCompeticiones(Usuario user) {
        Statement stmt = null;
        Competiciones competiciones = new Competiciones(getCompeticiones());
        String query = "SELECT * FROM CompeticionesUsuarios WHERE "
                + "USUARIO_ID=" + user.getId() +  ";";
        System.out.println(query);
        ArrayList<Integer> competitionIds = new ArrayList();
        try {
            connectDB();
//            this.c.setAutoCommit(false);

            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            while ( rs.next() ) {
                Integer id = rs.getInt("COMPETICION_ID");
                competitionIds.add(id);
            }
            competiciones.getCompeticiones()
                    .removeIf( c -> !competitionIds.contains(c.getId()));

            stmt.close();
            disconnectDB();
        }catch ( Exception e ) {
            sqlError(e, query);
       }
        return competiciones;  
    }

    void linkCompeticionUsuario(Competicion competicion, Usuario user) {
        String query = "INSERT INTO CompeticionesUsuarios VALUES(" +
            + user.getId() + ", "
            + competicion.getId() + ");";
        execute( query );
    }

}
