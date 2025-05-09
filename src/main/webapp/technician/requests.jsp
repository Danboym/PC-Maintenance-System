<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pcm.model.User" %>
<%@ page import="com.pcm.model.Request" %>
<%@ page import="com.pcm.dao.RequestDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Request Management - PC Maintenance System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null || !"TECHNICIAN".equals(user.getRole())) {
            response.sendRedirect("../index.jsp");
            return;
        }
        
        RequestDAO requestDAO = new RequestDAO();
        List<Request> requests = requestDAO.getRequestsByTechnician(user.getId());
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
        <h2>Request Management</h2>

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
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Request req : requests) { %>
<tr>
    <td><%= req.getId() %></td>
    <td><%= req.getFirstName() %> <%= req.getLastName() %></td>
    <td>
        Email: <%= req.getEmail() %><br>
        Tel: <%= req.getTelephone() %>
    </td>
    <td><%= req.getUnit() %></td>
    <td><%= req.getRequestType() %></td>
    <td><%= req.getRequestDate() %></td>
    <td>
        <span class="badge bg-<%= 
            "PENDING".equals(req.getStatus()) ? "warning" : 
            "IN_PROGRESS".equals(req.getStatus()) ? "info" : 
            "COMPLETED".equals(req.getStatus()) ? "success" : "secondary" 
        %>">
            <%= req.getStatus() %>
        </span>
    </td>
    <td>
        <button class="btn btn-sm btn-primary" onclick="updateStatus(<%= req.getId() %>)">
            <i class="bi bi-check-circle"></i>
        </button>
    </td>
</tr>
<% } %>

                </tbody>
            </table>
        </div>
    </div>

    <!-- Update Status Modal -->
    <div class="modal fade" id="updateStatusModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Request Status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                      <form action="<%= request.getContextPath() %>/updateRequestStatus" method="post">
       <input type="hidden" id="requestId" name="requestId">
       <div class="modal-body">
           <label for="newStatus" class="form-label">New Status</label>
           <select class="form-select" id="newStatus" name="status" required>
               <option value="FIXED">FIXED</option>
               <option value="NOT_FIXED">NOT_FIXED</option>
               <option value="PENDING">PENDING</option>
               <option value="TECHNICIAN_ASSIGNED">TECHNICIAN_ASSIGNED</option>
           </select>
       </div>
       <div class="modal-footer">
           <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
           <button type="submit" class="btn btn-primary">Update Status</button>
       </div>
   </form>
                   
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateStatus(id) {
            document.getElementById('requestId').value = id;
            new bootstrap.Modal(document.getElementById('updateStatusModal')).show();
        }
    </script>
</body>
</html> 