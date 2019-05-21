/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import Db.Database;
import autentificacion.UsuarioNecesario;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.SecurityContext;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import pojo.Competicion;
import pojo.Competiciones;
import pojo.Usuario;
import pojo.Deporte;


/**
 * REST Web Service
 *
 * @author squid
 */
@Singleton // WHY ????
@Path("competiciones")
public class ServiciosCompeticiones {

    Database db = new Database();
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    /**
     * Creates a new instance of ServiciosCompeticiones
     */
    public ServiciosCompeticiones() {
    }
    
//    private int findCompeticionIndex(int id) throws IndexOutOfBoundsException{
//        for (int i = 0; i<competiciones.getCompeticiones().size() ; i++ )
//            if(competiciones.getCompeticiones().get(i).getId() == id)
//                return i;
//        throw new IndexOutOfBoundsException();
//    }
    
    private Usuario getUser(){
        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();
        return db.getUserFromUsername(username);
    }
//  ---- COMPETICIONES ----  
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @UsuarioNecesario
    @Produces(MediaType.APPLICATION_XML)
    public Competiciones getCompeticiones() {        
        Usuario user = getUser();
        System.out.println("getCompeticiones. user " + user.getUsername());
        Competiciones competicionesUsuario = db.getUserCompetitions(user);
        return competicionesUsuario;
    }
    
//    @PUT
//    @UsuarioNecesario
//    @Consumes(MediaType.APPLICATION_XML)
//    public String putCompeticiones(Competiciones competiciones) {
//        Usuario user = getUser();
//        this.competiciones = competiciones;
//        db.setCompeticiones(competiciones, user);
//        return "Competiciones modificadas";
//    }
    
    @POST
    @UsuarioNecesario
    @Consumes(MediaType.APPLICATION_XML)
    public Competicion postCompeticion(Competicion competicion){
        Usuario user = getUser();
        db.addCompeticion(competicion, user);
        return competicion;
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Competicion getCompeticion(@PathParam("id") int id) throws IndexOutOfBoundsException {
        Competicion competicionCorrecta = null;
        for (Competicion competicion : getCompeticiones().getCompeticiones())
            if(competicion.getId() == id){
                competicionCorrecta = competicion; 
                break;
            }
        Usuario user = getUser();
        System.out.println("getCompeticion. user " + user.getUsername());
        if (db.userIsOwner(user, competicionCorrecta))
            return competicionCorrecta;

        return null;   
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @param id
     * @param competicionNew
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putCompeticion(@PathParam("id") int id, Competicion competicionNew) {
        db.setCompeticion(id, competicionNew);
        return "Competición actualizada";
    }

    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @DELETE
    @Path("{id}")
    public String deleteCompeticion(@PathParam("id") int id) {
        db.deleteCompeticion(id);
        return "Competición borrada";
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @POST
    @Path("{id}/share/{username}")
    public String shareCompeticion(@PathParam("id") int id, @PathParam("username") String username) {
        Competicion competicion = getCompeticion(id);
        db.shareCompeticion(competicion, username);
        return "Competición compartida";
    }
    
//    ---- DEPORTES ----
    
    @UsuarioNecesario
    @POST
    @Path("{idCompeticion}/deportes")
    @Consumes(MediaType.APPLICATION_XML)
    public Deporte postDeporte(@PathParam("idCompeticion") int idCompeticion, Deporte deporte){
        Competicion competicion = getCompeticion(idCompeticion);
        db.addDeporte(competicion, deporte);
        return deporte;
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @GET
    @Path("{idCompeticion}/deportes/{idDeporte}")
    @Produces(MediaType.APPLICATION_XML)
    public Deporte getDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte) {
        Competicion competicion = getCompeticion(idCompeticion);
        int indexDeporte = competicion.findDeporteIndex(idDeporte);
        Deporte deporte = competicion.getDeportes().get(indexDeporte);
        return deporte;
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */        
    @UsuarioNecesario
    @PUT
    @Path("{idCompeticion}/deportes/{idDeporte}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte, Deporte deporte) {
        try {
            Competicion competicion = getCompeticion(idCompeticion);
            int indexDeporte = competicion.findDeporteIndex(idDeporte);
            competicion.getDeportes().set(indexDeporte, deporte);
            db.setDeporte(competicion, idDeporte, deporte);
            return "Deporte actualizado";
        }catch (IndexOutOfBoundsException ex){
            return "No existe el deporte " + idDeporte + " de la competicion " + idCompeticion;
        }
    }
        /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @UsuarioNecesario
    @DELETE
    @Path("{idCompeticion}/deportes/{idDeporte}")
    public String deleteDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte) {
        try {
            Competicion competicion = getCompeticion(idCompeticion);
            int indexDeporte = competicion.findDeporteIndex(idDeporte);
            competicion.getDeportes().remove(indexDeporte);
            db.deleteDeporte(competicion, idDeporte);
            return "Deporte actualizado";
        }catch (IndexOutOfBoundsException ex){
            return "No existe el deporte " + idDeporte + " de la competicion " + idCompeticion;
        }
    }    
    
//    ---- validacion ----

    @POST
    @Path("validacion")
    @Consumes(MediaType.APPLICATION_XML)
    public String validacion(String contenidoXml){
        String respuesta = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xsdFile = new File(classLoader.getResource("servicio/Competiciones.xsd").getFile());

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            Source ficheroXml = new StreamSource(new StringReader(contenidoXml));
            validator.validate(ficheroXml);
            respuesta = ("El fichero es válido");
        } catch (org.xml.sax.SAXException | IOException ex) {
            respuesta = (" NO es válido");
            System.out.println(ex);
        }
        return respuesta;
    }
    
    //  ---- AUTH ----  

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_XML)
    public String login(Usuario credenciales){
        String token = db.login(credenciales);
        if (token == null)
            throw new NotAuthorizedException("invalid credentials");
        return token;
    }
    @UsuarioNecesario
    @POST
    @Path("logout")
    public String logout(){
        Usuario user = getUser();
        db.logout(user);
        return "logged out";
    }
    
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_XML)
    public String signup(Usuario credenciales){
        
        if(db.checkUserExists(credenciales))
            return "Usuario ya existe";
        else {
            String token = db.signup(credenciales);
            return token;
        }
    }
}
