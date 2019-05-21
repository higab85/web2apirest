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
    
    public static final int STRING = 0;
    public static final int INT = 1;

    
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
    
    private ArrayList<ArrayList> executeQuery(String query, ArrayList<Integer> types, ArrayList<String> names){
        Statement stmt = null;
        System.out.println(query);
        ArrayList<ArrayList> rows = new ArrayList();
        try {
            connectDB();
            stmt = this.c.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            
            while(rs.next()){
                ArrayList<Object> results = new ArrayList();
                for(int i = 0; i<types.size() ; i++){
                    int type = types.get(i);
                    switch (type){
                        case 0:
                            String string = rs.getString(names.get(i));
                            results.add(string);
                            break;
                        case 1:
                            Integer num = rs.getInt(names.get(i));
                            results.add(num);
                            break;
                    }
                }
                rows.add(results);
            }
            rs.close();
            stmt.close();
        }catch ( Exception e ) {
            sqlError(e, query);
        }
        disconnectDB();
        return rows;
    }
    
    
    public Usuario getUser(String username){
        String query = "SELECT * FROM Usuarios WHERE USERNAME='" + username + "';";

        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(INT);
        names.add("ID");
        types.add(STRING);
        names.add("PASSWORD");
        
        ArrayList<Object> values = executeQuery(query, types, names).get(0);
        
        Integer id = (Integer) values.get(0);
        String password = (String) values.get(1);
//        String token = getToken(id);
        
        return new Usuario(id, username, password);
    }
    
    public String getToken(Integer userId) {
        String query = "SELECT * FROM Tokens WHERE USUARIO_ID='" + userId + "';";
        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(STRING);
        names.add("TOKEN");
        
        try{
            ArrayList<Object> values = executeQuery(query, types, names).get(0);
            return (String) values.get(0);
        }catch(java.lang.IndexOutOfBoundsException e){
            return null;
        }
    }
    
    Integer createUser(Usuario submittedUser) {
        String query1 = "INSERT INTO Usuarios (USERNAME, PASSWORD) VALUES('"
                    + submittedUser.getUsername() + "','"
                    + submittedUser.getPassword() + "');";

        String query2 = "SELECT ID FROM Usuarios WHERE USERNAME='" + 
                        submittedUser.getUsername() + "';";
        execute( query1 );
        
        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(INT);
        names.add("ID");
        
        ArrayList<Object> values = executeQuery(query2, types, names).get(0);
        
        return (Integer) values.get(0);
    }

    Usuario getUserFromToken(String token) {
        String query1 = "SELECT * FROM Tokens WHERE TOKEN='" + token + "';";

        ArrayList<Integer> types1 = new ArrayList();
        ArrayList<String> names1 = new ArrayList();

        types1.add(INT);
        names1.add("USUARIO_ID");
        
        ArrayList<Object> values1 = executeQuery(query1, types1, names1).get(0);
        int usuarioId = (Integer) values1.get(0);
        
        String query2 = "SELECT * FROM Usuarios WHERE ID='" + usuarioId + "';";
        
        ArrayList<Integer> types2 = new ArrayList();
        ArrayList<String> names2 = new ArrayList();
        
        types2.add(STRING);
        names2.add("USERNAME");
        types2.add(STRING);
        names2.add("PASSWORD");
        
        ArrayList<Object> values2 = executeQuery(query2, types2, names2).get(0);
        
        return new Usuario( 
                (String) values2.get(0), 
                (String) values2.get(1));
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
        ArrayList<Competicion> competiciones = new ArrayList();
        String query = "SELECT * FROM Competiciones;";
        
        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(INT);
        names.add("ID");
        types.add(STRING);
        names.add("NOMBRE");
        
        ArrayList<ArrayList> competicionesVals = executeQuery(query, types, names);
        
        competicionesVals.forEach((values) -> {
            competiciones.add(new Competicion(
                    (Integer) values.get(0),
                    (String) values.get(1)
            ));
        });
        
        competiciones.forEach((competicion) -> {
            competicion.setDeportes(getDeportes(competicion));
        });
        
        return competiciones;   
    }
    
    private ArrayList<Deporte> getDeportes(Competicion competicion) {
        ArrayList<Deporte> deportes = new ArrayList();
        String query = "SELECT * FROM Deportes WHERE "
                    + "COMPETICION_ID=" + competicion.getId() + ";";
        
        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(INT);
        names.add("ID");
        types.add(STRING);
        names.add("NOMBRE");
        types.add(STRING);
        names.add("TIPO");
        types.add(STRING);
        names.add("EQUIPOS");
        types.add(STRING);
        names.add("TAMANOEQUIPO");
        
        ArrayList<ArrayList> deportesVals = executeQuery(query, types, names);
        
        deportesVals.forEach((values) -> {
            deportes.add(new Deporte(
                    (Integer) values.get(0),
                    (String) values.get(1),
                    (String) values.get(2),
                    (String) values.get(3),
                    (String) values.get(4)));});
        
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
        
        ArrayList<Integer> types = new ArrayList();
        ArrayList<String> names = new ArrayList();

        types.add(INT);
        names.add("COMPETICION_ID");
        
        ArrayList<ArrayList> values = executeQuery(query, types, names);
        System.out.println(values.size() + " competitions");
        ArrayList<Integer> competitionIds = new ArrayList();
        for(ArrayList competicionId : values)
            competitionIds.add( (Integer) competicionId.get(0));
       
        competiciones.getCompeticiones()
            .removeIf( c -> !competitionIds.contains(c.getId()));
        
        return competiciones;  
    }

    void linkCompeticionUsuario(Competicion competicion, Usuario user) {
        String query = "INSERT INTO CompeticionesUsuarios VALUES(" +
            + user.getId() + ", "
            + competicion.getId() + ");";
        execute( query );
    }

    void logoutUser(Usuario user) {
        String query = "DELETE FROM Tokens WHERE USUARIO_ID='" + user.getId() + "';";
        execute(query);
    }

}
