package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get the session, if it exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Invalidate the session to log out the user
            session.invalidate();
        }
        // Redirect to index.jsp with a logout confirmation message
        response.sendRedirect("index.jsp?logout=true");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Support POST requests for logout (e.g., from a form)
        doGet(request, response);
    }
}