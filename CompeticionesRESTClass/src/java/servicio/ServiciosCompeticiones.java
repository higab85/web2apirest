/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

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
import pojo.Competicion;
import pojo.Deporte;


/**
 * REST Web Service
 *
 * @author squid
 */
@Singleton // WHY ????
@Path("competiciones")
public class ServiciosCompeticiones {

    ArrayList<Competicion> competiciones = new ArrayList<Competicion>();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiciosCompeticiones
     */
    public ServiciosCompeticiones() {
    }

    
//  ---- COMPETICIONES ----  
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getCompeticiones() {
        StringBuilder res = new StringBuilder();
        res.append("<competiciones>");
        for(Competicion competicion : this.competiciones){
            res.append(competicion);
        }
        res.append("\n</competiciones>");
        return res.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public String putCompeticiones(ArrayList<Competicion> competiciones) {
        this.competiciones = competiciones;
        return "Competiciones modificadas";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public String postCompeticion(Competicion competicion){
        try{
            this.competiciones.add(competicion);
            return competicion.getDeportes().get(0).toString();
        }
        catch (Exception ex) {
            return ex.toString();
        }
        
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public String getCompeticion(@PathParam("id") int id) {
        try {
            Competicion competicion = competiciones.get(id);
            return competicion.toString();
        }catch (IndexOutOfBoundsException ex){
            return "No existe la competicion " + id;
        }   
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putCompeticion(@PathParam("id") int id, Competicion competicion) {
        try {
            competiciones.set(id, competicion);
            return "Competición actualizada";
        } catch (IndexOutOfBoundsException ex){
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
            competiciones.remove(id);
            return "Competición borrada";
        } catch (IndexOutOfBoundsException ex){
            return "No existe la competicion " + id;
        }  
    }
    
//    ---- DEPORTES ----
    

    @POST
    @Path("{idCompeticion}/deportes")
    @Consumes(MediaType.APPLICATION_XML)
    public String postDeporte(@PathParam("idCompeticion") int idCompeticion, Deporte deporte){
        try{
            Competicion competicion = this.competiciones.get(idCompeticion);
            competicion.anadirDeporte(deporte);
            return "Competición añadida";
        }
        catch (Exception ex) {
            return ex.toString();
        }
        
    }
    
    /**
     * Retrieves representation of an instance of servicio.ServiciosCompeticiones
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{idCompeticion}/deportes/{idDeporte}")
    @Produces(MediaType.APPLICATION_XML)
    public String getDeporte(@PathParam("idCompeticion") int idCompeticion, @PathParam("idDeporte") int idDeporte) {
        try {
            Competicion competicion = competiciones.get(idCompeticion);
            Deporte deporte = competicion.getDeportes().get(idDeporte);
            return deporte.toString();
        }catch (IndexOutOfBoundsException ex){
            return "No existe el deporte " + idDeporte + " de la competicion " + idCompeticion;
        }   
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
            Competicion competicion = competiciones.get(idCompeticion);
            competicion.getDeportes().set(idDeporte, deporte);
            return "Deporte actualizada";
        } catch (IndexOutOfBoundsException ex){
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
            competiciones.get(idCompeticion).getDeportes().remove(idDeporte);
            return "Deporte borrado";
        } catch (IndexOutOfBoundsException ex){
            return "No existe el deporte " + idDeporte + " de la competicion " + idCompeticion;
        }  
    }    
    

}
