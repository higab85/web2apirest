/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompeticionesClienteWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pojo.Deporte;
import pojo.Competicion;
import pojo.Competiciones;


/**
 *
 * @author squid
 */
@WebServlet(name = "MainServlet", urlPatterns = {"/home"})
public class MainServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
//        String nombreCompeticion = request.getParameter("nombre");
//        String nombreDeporte = request.getParameter("nombreDeporte");
//        String tipoDeporte = request.getParameter("tipoDeporte");
//        String equiposDeporte = request.getParameter("equiposDeporte");
//        String tamEquipoDeporte = request.getParameter("tamEquipoDeporte");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RESTclient</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RESTclient at " + request.getContextPath() + "</h1>");
            
            
            ServiciosCompeticiones cs = new ServiciosCompeticiones();
            
            // https://www.baeldung.com/java-servlet-cookies-session
            Optional<String> token = Arrays.stream(request.getCookies())
                            .filter(c -> c.getName().equals("token_competiciones"))
                            .map(Cookie::getValue)
                            .findAny();
            Competiciones cCompeticiones = cs.getCompeticiones(Competiciones.class, token.get());
            ArrayList<Competicion> competiciones = cCompeticiones.getCompeticiones();

            out.println("<h3> Competiciones:</h3>");
            out.println("<div>");
            if (competiciones.isEmpty())
                out.println("<h4>No hay competiciones</h4>");
            else
                for(Competicion competicion : competiciones){
                    out.println("<div style='display: inline-block'>");
                        out.println("<h4>" + competicion.getId() + " - " + competicion.getNombre() + "</h4>");
                        out.println("<h5>Deportes </h5>");
                        out.println("<ul>");
                        for (Deporte deporte : competicion.getDeportes()){
                            out.println("<li>");
                                out.println("<ul>");
                                    out.println("<li><b>ID:</b> " + deporte.getId() + "</li>");
                                    out.println("<li><b>Nombre:</b> " + deporte.getNombre() + "</li>");
                                    out.println("<li><b>Tipo:</b> " + deporte.getTipo() + "</li>");
                                    out.println("<li><b>Equipos:</b> " + deporte.getEquipos() + "</li>");
                                    out.println("<li><b>Tamaño equipo:</b> " + deporte.getTamanoEquipo() + "</li>");
                                out.println("</ul>");
                            out.println("</li>");
                        }
                        out.println("</ul>");
                    out.println("</div>");
                }
            out.println("</div>");

            
//            http://localhost:8080/CompeticionesRESTClass/CompeticionesService?tester
//            out.println(""
//                    + "<script type=\"text/javascript\">\n" +
//                        "function OnSubmitForm(){\n" +
//                        "  var id = document.anadirDeporte.competicion;" +
//                        "  document.anadirDeporte.action ='competicion/' + id;\n" +
//                        "  return true;\n" +
//                        "}\n" +
//                        "function obtenerDeporte(){\n" + 
//                        "  var idCompeticion = document.anadirDeporte.competicion;" +
//                        "  var idDeporte = document.anadirDeporte.deporte;" +
//                        "  document.anadirDeporte.action ='competicion/' + idCompeticion + '/deporte/' + idDeporte;\n" +
//                        "  return true;\n" +
//                        "}\n" +
//                        "</script>");
            out.println("<h3>Compartir competición </h3>");
            out.println("<form action='/CompeticionesRESTClienteWeb/compartir' method = \"POST\">");
                out.println("<div>ID Competicion: <input type = \"text\" name = 'competicion' /></div>");
                out.println("<div>Nombre usuario: <input type = \"text\" name = 'username' /></div>");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Añadir deporte</h3>");
            out.println("<form action='/CompeticionesRESTClienteWeb/deportes' method = \"POST\">");
                out.println("<div>ID Competicion: <input type = \"text\" name = 'competicion' /></div>");
                out.println("<div>Nombre: <input type = \"text\" name = 'nombre' /></div>");
                out.println("<div>Tipo: <input type = \"text\" name = \"tipo\" /></div>");
                out.println("<div>Equipos: <input type = \"text\" name = \"equipos\" /></div>");
                out.println("<div>Tamaño equipo: <input type = \"text\" name = \"tamanoEquipo\" /></div>");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Obtener deporte</h3>");
            out.println("<form action = '/CompeticionesRESTClienteWeb/deportes' method = \"GET\">");
                out.println("ID competicion: <input type = \"text\" name = \"competicion\" />");
                out.println("ID deporte: <input type = \"text\" name = 'deporte' />");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Borrar deporte</h3>");
            out.println("<form action = '/CompeticionesRESTClienteWeb/delete' method = \"POST\">");
                out.println("ID competicion: <input type = \"text\" name = \"competicion\" />");
                out.println("ID deporte: <input type = \"text\" name = 'deporte' />");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Añadir competicion</h3>");
            out.println("<form action='/CompeticionesRESTClienteWeb/competiciones' method = \"POST\">");
                out.println("<div>Nombre: <input type = \"text\" name = 'nombre' /></div>");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Borrar competicion</h3>");
            out.println("<form action = '/CompeticionesRESTClienteWeb/delete' method = \"POST\">");
                out.println("ID competicion: <input type = \"text\" name = \"competicion\" />");
                out.println("<input type = \"submit\" value = \"Mandar\" />");
            out.println("</form>");
            out.println("<h3>Validar archivo</h3>");
            out.println(" <form action=\"validarArchivo\" enctype='multipart/form-data' method='POST'>\n" +
                "  <input type=\"file\" name=\"archivo\" accept='.xml'>\n" +
                "  <input type=\"submit\" value='Mandar'>\n" +
                "</form> ");
            out.println("</body>");
            out.println("</html>");
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
