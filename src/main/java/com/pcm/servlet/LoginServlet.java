package com.pcm.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pcm.dao.UserDAO;
import com.pcm.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get username and password from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Input validation
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Validate user credentials
        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(username.trim(), password.trim());

        if (user != null) {
            // Credentials are valid, set user in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect to index.jsp to show welcome message
            response.sendRedirect("index.jsp");
        } else {
            // Invalid credentials, show error message
            request.setAttribute("error", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}