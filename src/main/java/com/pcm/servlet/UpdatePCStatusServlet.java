package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pcm.dao.PCDAO;
import com.pcm.model.User;

@WebServlet("/updatePCStatus")
public class UpdatePCStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"TECHNICIAN".equals(user.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        int pcId = Integer.parseInt(request.getParameter("pcId"));
        String status = request.getParameter("status");
        
        PCDAO pcDAO = new PCDAO();
        pcDAO.updatePCStatus(pcId, status);
        
        response.sendRedirect("technician/equipment.jsp");
    }
} 