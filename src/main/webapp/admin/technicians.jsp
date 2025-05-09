<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pcm.model.User" %>
<%@ page import="com.pcm.dao.UserDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Technicians Management - PC Maintenance System</title>
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

    UserDAO userDAO = new UserDAO();
    List<User> technicians = userDAO.getAllTechnicians();
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">PC Maintenance System</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="dashboard.jsp">Dashboard</a></li>
                <li class="nav-item"><a class="nav-link" href="equipment.jsp">Equipment</a></li>
                <li class="nav-item"><a class="nav-link" href="requests.jsp">Requests</a></li>
            </ul>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="../logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Technicians Management</h2>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addTechnicianModal">
            <i class="bi bi-plus-circle"></i> Add Technician
        </button>
    </div>

    <div class="table-responsive">
        <table class="table table-striped align-middle">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <% for (User technician : technicians) { %>
                <tr>
                    <td><%= technician.getId() %></td>
                    <td><%= technician.getUsername() %></td>
                    <td>
                        <span class="badge bg-<%= 
                            "AVAILABLE".equals(technician.getStatus()) ? "success" : 
                            "NOT_AVAILABLE".equals(technician.getStatus()) ? "danger" : "warning" 
                        %>">
                            <%= technician.getStatus() %>
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-info text-white" onclick="editTechnician(<%= technician.getId() %>)">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteTechnician(<%= technician.getId() %>)">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
<div class="container mt-4">
    <% 
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null) { 
    %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= success %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } else if (error != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

<!-- Add Technician Modal -->
<div class="modal fade" id="addTechnicianModal" tabindex="-1">
    <div class="modal-dialog">
           <form class="modal-content" action="/pc-maintenance/admin/dashboard/addTechnician" method="post">

            <div class="modal-header">
                <h5 class="modal-title">Add Technician</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" name="username" id="username" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="status" class="form-label">Status</label>
                    <select name="status" id="status" class="form-select" required>
                        <option value="AVAILABLE">Available</option>
                        <option value="NOT_AVAILABLE">Not Available</option>
                        <option value="BUSY">Busy</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button class="btn btn-primary" type="submit">Add Technician</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Technician Modal -->
<div class="modal fade" id="editTechnicianModal" tabindex="-1">
    <div class="modal-dialog">
        <form class="modal-content" action="/pc-maintenance/admin/updateTechnician" method="post">
            <div class="modal-header">
                <h5 class="modal-title">Edit Technician</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="editId" name="id">
                <div class="mb-3">
                    <label for="editUsername" class="form-label">Username</label>
                    <input type="text" name="username" id="editUsername" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="editStatus" class="form-label">Status</label>
                    <select name="status" id="editStatus" class="form-select" required>
                        <option value="AVAILABLE">Available</option>
                        <option value="NOT_AVAILABLE">Not Available</option>
                        <option value="BUSY">Busy</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button class="btn btn-success" type="submit">Update Technician</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function editTechnician(id) {
        <% for (User tech : technicians) { %>
        if (id === <%= tech.getId() %>) {
            document.getElementById("editId").value = "<%= tech.getId() %>";
            document.getElementById("editUsername").value = "<%= tech.getUsername() %>";
            document.getElementById("editStatus").value = "<%= tech.getStatus() %>";
            new bootstrap.Modal(document.getElementById("editTechnicianModal")).show();
        }
        <% } %>
    }

    function deleteTechnician(id) {
        if (confirm("Are you sure you want to delete this technician?")) {
            window.location.href = "<%= request.getContextPath() %>/deleteTechnician?id=" + id;
        }
    }
</script>

</body>
</html>
