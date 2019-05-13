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
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
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

    Competiciones competiciones = new Competiciones();
    Database db = new Database();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiciosCompeticiones
     */
    public ServiciosCompeticiones() {
    }
    
    private int findCompeticionIndex(int id) throws IndexOutOfBoundsException{
        for (int i = 0; i<competiciones.getCompeticiones().size() ; i++ )
            if(competiciones.getCompeticiones().get(i).getId() == id)
                return i;
        throw new IndexOutOfBoundsException();
    }
    
//  ---- COMPETICIONES ----  
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Competiciones getCompeticiones() {
        return competiciones;
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public String putCompeticiones(ArrayList<Competicion> competiciones) {
        this.competiciones.setCompeticiones(competiciones);
        return "Competiciones modificadas";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Competicion postCompeticion(Competicion competicion){
        this.competiciones.getCompeticiones().add(competicion);
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
        for (Competicion competicion : competiciones.getCompeticiones())
            if(competicion.getId() == id)
                return competicion;
        throw new IndexOutOfBoundsException("No existe la competicion " + id);   
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putCompeticion(@PathParam("id") int id, Competicion competicion) {
        try{
            competiciones.getCompeticiones().set(findCompeticionIndex(id), competicion);
            return "Competición actualizada";
        } catch (IndexOutOfBoundsException e){
            return "No existe la competicion " + id;
        }
    }

    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @DELETE
    @Path("{id}")
    public String deleteCompeticion(@PathParam("id") int id) {
        try {
            competiciones.getCompeticiones().remove(findCompeticionIndex(id));
            return "Competición borrada";
        } catch (IndexOutOfBoundsException e){
            return "No existe la competicion " + id;
        }
    }
    
//    ---- DEPORTES ----
    

    @POST
    @Path("{idCompeticion}/deportes")
    @Consumes(MediaType.APPLICATION_XML)
    public Deporte postDeporte(@PathParam("idCompeticion") int idCompeticion, Deporte deporte){
        return competiciones.getCompeticiones().get(findCompeticionIndex(idCompeticion)).anadirDeporte(deporte);
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{idCompeticion}/deportes/{idDeporte}")
    @Produces(MediaType.APPLICATION_XML)
    public Deporte getDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte) {
        Competicion competicion = competiciones.getCompeticiones().get(findCompeticionIndex(idCompeticion));
        int indexDeporte = competicion.findDeporteIndex(idDeporte);
        Deporte deporte = competicion.getDeportes().get(indexDeporte);
        return deporte;
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @PUT
    @Path("{idCompeticion}/deportes/{idDeporte}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte, Deporte deporte) {
        try {
            Competicion competicion = competiciones.getCompeticiones().get(findCompeticionIndex(idCompeticion));
            int indexDeporte = competicion.findDeporteIndex(idDeporte);
            competicion.getDeportes().set(indexDeporte, deporte);
            return "Deporte actualizado";
        }catch (IndexOutOfBoundsException ex){
            return "No existe el deporte " + idDeporte + " de la competicion " + idCompeticion;
        }
    }
        /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @DELETE
    @Path("{idCompeticion}/deportes/{idDeporte}")
    public String deleteDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte) {
        try {
            Competicion competicion = competiciones.getCompeticiones().get(findCompeticionIndex(idCompeticion));
            int indexDeporte = competicion.findDeporteIndex(idDeporte);
            competicion.getDeportes().remove(indexDeporte);
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
            return "Could not login";
        return token;
    }
    
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_XML)
    public String signup(Usuario credenciales){
        System.out.println("servicio.ServiciosCompeticiones.signup()");
        String token = db.signup(credenciales);
        return token;
    }
}
