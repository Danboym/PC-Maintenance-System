<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pcm.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PC Maintenance System - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white text-center">
                        <h3>PC Maintenance System</h3>
                    </div>
                    <div class="card-body">
                        <%
                            // Check if the user is already logged in
                            User user = (User) session.getAttribute("user");
                            if (user != null) {
                                // User is logged in, display welcome message
                                String role = user.getRole();
                                String username = user.getUsername(); // Adjust to getName() if your User class has a getName() method
                        %>
                            <div class="alert alert-success text-center" role="alert">
                                Welcome <%= role %> <%= username %>!
                            </div>
                            <div class="text-center">
                                <a href="<%= role.equals("TECHNICIAN") ? "technician/dashboard.jsp" : "admin/dashboard.jsp" %>" class="btn btn-primary me-2">
                                    Go to Dashboard
                                </a>
                                <a href="logout" class="btn btn-secondary">Logout</a>
                            </div>
                        <%
                            } else {
                                // User is not logged in, show login form
                        %>
                            <% if (request.getAttribute("error") != null) { %>
                                <div class="alert alert-danger mt-3">
                                    <%= request.getAttribute("error") %>
                                </div>
                            <% } %>

                            <form action="login" method="post">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="username" name="username" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Login</button>
                                </div>
                            </form>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>