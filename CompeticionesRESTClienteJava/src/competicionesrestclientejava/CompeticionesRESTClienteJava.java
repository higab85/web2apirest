/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package competicionesrestclientejava;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import javax.ws.rs.Path;
import pojo.Competicion;
import pojo.Competiciones;
import pojo.Deporte;
import pojo.Usuario;



/**
 *
 * @author squid
 */
public class CompeticionesRESTClienteJava {

    static String idCompeticion;
    static Scanner scanner;
    static String breaker;
    static ServiciosCompeticiones cs;
    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        breaker = "        --- ";

        // TODO code application logic here 
        scanner = new Scanner(System.in);
        cs = new ServiciosCompeticiones();
        
        authenticate();
        seleccionarCompeticion();
        crearMenu();
    }

    public static void crearMenu() {
        System.out.println(breaker);
        System.out.println("1.  Añadir deporte");
        System.out.println("2.  Listar deportes");
        System.out.println("3.  Obtener deporte");
        System.out.println("4.  Validar archivo");
        System.out.println("5.  Borrar competicion");
        System.out.println("6.  Borrar deporte");
        System.out.println("7.  Modificar competicion");
        System.out.println("8.  Compartir competicion");
        System.out.println("9.  Cambiar competicion");
        System.out.println("10.  Cerrar sesión");
        System.out.println("11.  Salir");

        System.out.println(breaker);

        
        int opcion = scanner.nextInt();
        comprobar(opcion);

    }

    public static void comprobar(int opcion) {
        try{
            switch (opcion) {
                case 1:
                    crearDeporte();
                    break;
                case 2:
                    listarDeportes();
                    break;
                case 3:
                    obtenerDeporte();
                    break;
                case 4:
                    validarArchivo();
                    break;
                case 5:
                    borrarCompeticion();
                    break;
                case 6:
                    borrarDeporte();
                    break;
                case 7:
                    modificarCompeticion();
                    break;
                case 8:
                    compartirCompeticion();
                    break;
                case 9:
                    seleccionarCompeticion();
                    break;
                case 10:
                    logout();
                    break;
                default:
                    System.exit(0);
            }
        }catch(javax.ws.rs.InternalServerErrorException  e){
            System.err.println("Error");
        }
        crearMenu();
    }
    
    public static Competicion nuevaCompeticion(){
        System.out.println("Nombre competicion nueva: ");
        String nombre = scanner.next();
        Competicion competi = new Competicion(nombre);
        return cs.postCompeticion(competi, Competicion.class);
    }
    public static void iniciarSesion(){
        System.out.println("Username: ");
        String username = scanner.next();
        System.out.println("Contraseña: ");
        String password = scanner.next();
        Usuario usuario = new Usuario(username, password);
        String token = cs.login(usuario);
        if (token == null){
            System.out.println("No se pudo iniciar sesión, inténtelo de nuevo");
            iniciarSesion();
        }
        else
            cs.setToken(token);
    }
    public static void registrarse(){
        System.out.println("Username: ");
        String username = scanner.next();
        System.out.println("Contraseña: ");
        String password = scanner.next();
        Usuario usuario = new Usuario(username, password);
        String token = cs.signup(usuario);
        cs.setToken(token);
    }
    public static void authenticate(){
        System.out.println("Que quiere hacer:");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");
        Integer tarea = scanner.nextInt();
        if (tarea == 1 )
            iniciarSesion();
        else if(tarea == 2)
            registrarse();
        else {
            System.out.println("Opción no válida. Opciones válidas: 1, 2");
            authenticate();
        }
    }

    public static void seleccionarCompeticion(){
        try{
            System.out.println("Seleccionar competicion a editar:");
            Competiciones competicionesC = cs.getCompeticiones(Competiciones.class);
            ArrayList<Competicion> competiciones = competicionesC.getCompeticiones();
            if ( competiciones.isEmpty())
                idCompeticion = Integer.toString(nuevaCompeticion().getId());
            else{
                for (Competicion competicion : competiciones)
                    System.out.println(competicion.getId() + " - " + competicion.getNombre());
                idCompeticion =  Integer.toString(cs.getCompeticion(Competicion.class, scanner.next()).getId());
            }
        }
        catch(javax.ws.rs.NotAuthorizedException e){
            authenticate();
            seleccionarCompeticion();
        }catch(javax.ws.rs.InternalServerErrorException  e){
            System.err.println("Por favor introduzca una competicion existente");
            seleccionarCompeticion();
        }
    }
    
       public static void imprimirBonito(Deporte deporte){
        if ( deporte == null)
            System.out.println("Deporte no existe");
        else{
            System.out.println("Nombre: " + deporte.getNombre());
            System.out.println("Tipo: " + deporte.getTipo());
            System.out.println("Equipos: " + deporte.getEquipos());
            System.out.println("Tamaño equipo: " + deporte.getTamanoEquipo());
        }
   }
    
    public static void obtenerDeporte(){
        System.out.println("Seleccionar índice deporte:");
        listarDeportes();
        String idDeporte = scanner.next();
        Deporte deporte = cs.getDeporte(Deporte.class, idCompeticion, idDeporte);
        imprimirBonito(deporte);
    }
    
    public static void crearDeporte() {
        System.out.println("Nombre: ");
        String nombre = scanner.next();
        System.out.println("Tipo: ");
        String tipo = scanner.next();
        System.out.println("Equipos por partido/juego (2+ significa mas de dos): ");
        String equipos = scanner.next();
        System.out.println("Tamaño equipo: ");
        String tamEquipo = scanner.next();
        Deporte d = new Deporte();
        d.setNombre(nombre);
        d.setTipo(tipo);
        d.setEquipos(equipos);
        d.setTamanoEquipo(tamEquipo);
        cs.postDeporte(d, Deporte.class, idCompeticion);
    }
        
    private static void listarDeportes() {
        List<Deporte> deportes = cs.getCompeticion( Competicion.class, idCompeticion).getDeportes();
        if (deportes.size() > 0){
            int i = 1;
            for (Deporte d : deportes) {
                System.out.println(d.getId() + " - " + d.getNombre());
            }
        }else
            System.out.println("Lista vacía!");
    }
    
    private static String getFile(){
        System.out.println("Nombre del archivo: ");
        String nombreArchivo = scanner.next();
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(nombreArchivo), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            System.out.println("No existe el archivo " + nombreArchivo);
        }
        return contentBuilder.toString();
    }
    
    private static void validarArchivo() {
        String content = getFile();
        if(content != null){
            String response = cs.validacion(content);
            System.out.println(response);
        }
    }

    private static void borrarCompeticion() {
        cs.deleteCompeticion(idCompeticion);
        System.exit(0);
    }

    private static void borrarDeporte() {
        listarDeportes();
        System.out.println("ID de deporte a borrar:");
        String idDeporte = scanner.next();
        cs.deleteDeporte(idCompeticion, idDeporte);
    }

    private static void modificarCompeticion(){
        System.out.println("Nuevo nombre de competicion: ");
        String nombre = scanner.next();
        Competicion competicion = cs.getCompeticion(Competicion.class, idCompeticion);
        competicion.setNombre(nombre);
        cs.putCompeticion(competicion, idCompeticion);
    }

    private static void compartirCompeticion() {
        System.out.println("Nombre de usuario con el que quieres compartir esta competicion: ");
        String nombre = scanner.next();
        cs.shareCompeticion(idCompeticion, nombre);
        System.out.println("Competicion compartida");

    }

    private static void logout() {
        cs.logout();
        authenticate();
    }

}
