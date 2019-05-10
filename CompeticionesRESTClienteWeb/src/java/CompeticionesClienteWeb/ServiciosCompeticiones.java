/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompeticionesClienteWeb;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:ServiciosCompeticiones
 * [competiciones]<br>
 * USAGE:
 * <pre>
 *        ServiciosCompeticiones client = new ServiciosCompeticiones();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author squid
 */
public class ServiciosCompeticiones {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/CompeticionesRESTClass/webresources";

    public ServiciosCompeticiones() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("competiciones");
    }

    public String deleteCompeticion(String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete(String.class);
    }

    public String putCompeticion(Object requestEntity, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), String.class);
    }

    public String putCompeticiones(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), String.class);
    }

    public String putDeporte(Object requestEntity, String idCompeticion, String idDeporte) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/deportes/{1}", new Object[]{idCompeticion, idDeporte})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), String.class);
    }

    public <T> T getDeporte(Class<T> responseType, String idCompeticion, String idDeporte) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/deportes/{1}", new Object[]{idCompeticion, idDeporte}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public String deleteDeporte(String idCompeticion, String idDeporte) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/deportes/{1}", new Object[]{idCompeticion, idDeporte})).request().delete(String.class);
    }

    public <T> T postCompeticion(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public <T> T getCompeticiones(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T getCompeticion(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T postDeporte(Object requestEntity, Class<T> responseType, String idCompeticion) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}/deportes", new Object[]{idCompeticion})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    public void close() {
        client.close();
    }
    
}
