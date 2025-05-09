package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pcm.dao.RequestDAO;
import com.pcm.model.Request;
import com.pcm.model.User;

@WebServlet("/request")
public class RequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        Request maintenanceRequest = new Request();
        maintenanceRequest.setId(user.getId());
        maintenanceRequest.setFirstName(request.getParameter("firstName"));
        maintenanceRequest.setLastName(request.getParameter("lastName"));
        maintenanceRequest.setEmail(request.getParameter("email"));
        maintenanceRequest.setTelephone(request.getParameter("telephone"));
        maintenanceRequest.setUnit(request.getParameter("unit"));
        maintenanceRequest.setRequestType(request.getParameter("requestType"));
        maintenanceRequest.setStatus("PENDING");
        
        RequestDAO requestDAO = new RequestDAO();
        requestDAO.addRequest(maintenanceRequest);
        
        response.sendRedirect("user/dashboard.jsp");
    }
} 