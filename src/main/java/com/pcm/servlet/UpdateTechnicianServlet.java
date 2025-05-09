package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pcm.dao.UserDAO;
import com.pcm.model.User;

@WebServlet("/admin/updateTechnician")
public class UpdateTechnicianServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String status = request.getParameter("status");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setStatus(status);
            userDAO.updateUser(user);
        }

        response.sendRedirect("technicians.jsp?success=Technician updated successfully");
    }
}
