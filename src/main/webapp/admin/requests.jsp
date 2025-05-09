<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pcm.model.User" %>
<%@ page import="com.pcm.model.Request" %>
<%@ page import="com.pcm.dao.RequestDAO" %>
<%@ page import="com.pcm.dao.UserDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Requests Management - PC Maintenance System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect("../index.jsp");
            return;
        }

        RequestDAO requestDAO = new RequestDAO();
        UserDAO userDAO = new UserDAO();
        List<Request> requests = requestDAO.getAllRequests();
        List<User> technicians = userDAO.getAllTechnicians(); // Assumes this method exists in UserDAO
    %>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">PC Maintenance System</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="dashboard.jsp">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="technicians.jsp">Technicians</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="equipment.jsp">Equipment</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="requests.jsp">Requests</a>
                    </li>
                </ul>
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="../logout">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Requests Management</h2>
        </div>

        <!-- Display Success/Error Messages -->
        <%
            String success = request.getParameter("success");
            String error = request.getParameter("error");
            if (success != null && !success.isEmpty()) {
        %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <%
            }
            if (error != null && !error.isEmpty()) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <%
            }
        %>

        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Requester</th>
                        <th>Contact</th>
                        <th>Unit</th>
                        <th>Request Type</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Technician</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (requests != null && !requests.isEmpty()) {
                            for (Request req : requests) {
                    %>
                        <tr>
                            <td><%= req.getId() %></td>
                            <td><%= req.getFirstName() %> <%= req.getLastName() %></td>
                            <td>
                                <%= req.getEmail() %><br>
                                <%= req.getTelephone() %>
                            </td>
                            <td><%= req.getUnit() %></td>
                            <td><%= req.getRequestType() %></td>
                            <td><%= req.getRequestDate() %></td>
                            <td>
                                <span class="badge bg-<%=
                                    "PENDING".equals(req.getStatus()) ? "warning" :
                                    "TECHNICIAN_ASSIGNED".equals(req.getStatus()) ? "info" :
                                    "FIXED".equals(req.getStatus()) ? "success" : "danger"
                                %>">
                                    <%= req.getStatus() %>
                                </span>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/assignTechnician" method="post" class="d-flex align-items-center">
    <input type="hidden" name="requestId" value="<%= req.getId() %>">
    <select name="technicianId" class="form-select d-inline w-auto me-2" required>
        <option value="">Select Technician</option>
        <% if (technicians != null) {
            for (User tech : technicians) { %>
                <option value="<%= tech.getId() %>" <%= req.getTechnicianId() == tech.getId() ? "selected" : "" %>>
                    <%= tech.getUsername() %> (<%= tech.getStatus() %>)
                </option>
        <% } } %>
    </select>
    <button type="submit" class="btn btn-sm btn-outline-primary"
            <%= ("FIXED".equals(req.getStatus()) || "NOT_FIXED".equals(req.getStatus())) ? "disabled" : "" %>>
        Assign
    </button>
</form>
                                
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="9" class="text-center">No requests found.</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateStatus(requestId, status) {
            if (confirm(`Are you sure you want to mark this request as ${status}?`)) {
                window.location.href = 'dashboard/updateRequestStatus?id=' + requestId + '&status=' + status;
            }
        }
    </script>
</body>
</html>