package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pcm.dao.RequestDAO;
import com.pcm.model.Request;

@WebServlet("/assignTechnician")
public class AssignTechnicianServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        int technicianId = Integer.parseInt(request.getParameter("technicianId"));

        RequestDAO requestDAO = new RequestDAO();
        Request req = requestDAO.getRequestById(requestId);
        if (req != null) {
            req.setTechnicianId(technicianId);
            req.setStatus("TECHNICIAN_ASSIGNED");
            requestDAO.updateRequest(req);
            response.sendRedirect("admin/requests.jsp?success=Technician assigned successfully");
        } else {
            response.sendRedirect("admin/requests.jsp?error=Failed to assign technician");
        }
    }}

