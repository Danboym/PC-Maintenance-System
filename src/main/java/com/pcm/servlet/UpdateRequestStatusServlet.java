package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pcm.dao.RequestDAO;
import com.pcm.model.User;

@WebServlet("/updateRequestStatus")
public class UpdateRequestStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"TECHNICIAN".equals(user.getRole())) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        String idParam = request.getParameter("requestId");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("technician/requests.jsp?error=Invalid request ID");
            return;
        }

        try {
            int requestId = Integer.parseInt(idParam);
            String status = request.getParameter("status");

            RequestDAO requestDAO = new RequestDAO();
            requestDAO.updateRequestStatus(requestId, status);

            response.sendRedirect("technician/requests.jsp?success=Request status updated successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect("technician/requests.jsp?error=Invalid request ID format");
        }
    }
}
