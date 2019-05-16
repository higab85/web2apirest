/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CompeticionesClienteWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import pojo.Deporte;

/**
 *
 * @author squid
 */
@WebServlet(name = "DeporteServlet", urlPatterns = {"/deportes"})
public class DeporteServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DeporteServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeporteServlet at " + request.getContextPath() + "</h1>");
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
        
        ServiciosCompeticiones cs = new ServiciosCompeticiones();

        String idCompeticion = request.getParameter("competicion");
        String idDeporte = request.getParameter("deporte");
        
        Optional<String> token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token_competiciones"))
                .map(Cookie::getValue)
                .findAny();
        
        Deporte deporte = cs.getDeporte(Deporte.class, idCompeticion, idDeporte, token.get());
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DeporteServlet</title>");            
            out.println("</head>");
            out.println("<body>");
                out.println("<ul>");
                out.println("<li><b>ID:</b> " + deporte.getId() + "</li>");
                out.println("<li><b>Nombre:</b> " + deporte.getNombre() + "</li>");
                out.println("<li><b>Tipo:</b> " + deporte.getTipo() + "</li>");
                out.println("<li><b>Equipos:</b> " + deporte.getEquipos() + "</li>");
                out.println("<li><b>Tamaño equipo:</b> " + deporte.getTamanoEquipo() + "</li>");
            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");
        }
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
        
        ServiciosCompeticiones cs = new ServiciosCompeticiones();
       
        Optional<String> token = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("token_competiciones"))
                .map(Cookie::getValue)
                .findAny();
        String nombre = request.getParameter("nombre");
        String tipo = request.getParameter("tipo");
        String equipos = request.getParameter("equipos");
        String tamanoEquipos = request.getParameter("tamanoEquipo");
        String idCompeticion = request.getParameter("competicion");
        
        Deporte deporte = new Deporte(nombre, tipo, equipos, tamanoEquipos);
        cs.postDeporte(deporte, String.class, idCompeticion, token.get());
        response.sendRedirect("/CompeticionesRESTClienteWeb/home");
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
