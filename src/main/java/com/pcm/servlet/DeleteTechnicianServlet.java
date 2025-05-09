package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pcm.dao.UserDAO;

@WebServlet("/deleteTechnician")
public class DeleteTechnicianServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/technicians.jsp?error=Invalid%20technician%20ID");
            return;
        }

        try {
            int technicianId = Integer.parseInt(idParam);
            UserDAO userDAO = new UserDAO();
            boolean isDeleted = userDAO.deleteUser(technicianId);

            if (isDeleted) {
                response.sendRedirect(request.getContextPath() + "/admin/technicians.jsp?success=Technician%20deleted%20successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/technicians.jsp?error=Failed%20to%20delete%20technician");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/technicians.jsp?error=Invalid%20technician%20ID%20format");
        }
    }
}