<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.pcm.model.PC, com.pcm.model.Accessory, com.pcm.model.NetworkDevice, com.pcm.model.User, com.pcm.dao.EquipmentDAO" %>
<% 
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ page import="java.time.Year" %>
<% 
    int currentYear = Year.now().getValue();
    int startYear = 2000;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Equipment Management - PC Maintenance System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">PC Maintenance</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="dashboard.jsp">Dashboard</a></li>
        <li class="nav-item"><a class="nav-link" href="equipment.jsp">Equipment</a></li>
        <li class="nav-item"><a class="nav-link" href="requests.jsp">Requests</a></li>
      </ul>
      <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link" href="../logout">Logout</a></li>
      </ul>
    </div>
  </div>
</nav>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"TECHNICIAN".equals(user.getRole())) {
        response.sendRedirect("../index.jsp");
        return;
    }

    EquipmentDAO equipmentDAO = new EquipmentDAO();
    String filter = request.getParameter("filter");
    String equipmentType = request.getParameter("type");
    List<PC> pcs = null;
    List<Accessory> accessories = null;
    List<NetworkDevice> networkDevices = null;

    // Fetch equipment based on filter and type
    if ("repair".equals(filter)) {
        String[] repairStatuses = {"NOT_WORKING", "DAMAGED"};
        if ("accessory".equals(equipmentType)) {
            accessories = equipmentDAO.getAccessoriesByStatusAndTechnician(user.getId(), repairStatuses);
        } else if ("network".equals(equipmentType)) {
            networkDevices = equipmentDAO.getNetworkDevicesByStatusAndTechnician(user.getId(), repairStatuses);
        } else {
            pcs = equipmentDAO.getPCsByStatusAndTechnician(user.getId(), repairStatuses);
        }
    } else if ("replace".equals(filter)) {
        String[] replaceStatuses = {"OLD"};
        if ("accessory".equals(equipmentType)) {
            accessories = equipmentDAO.getAccessoriesByStatusAndTechnician(user.getId(), replaceStatuses);
        } else if ("network".equals(equipmentType)) {
            networkDevices = equipmentDAO.getNetworkDevicesByStatusAndTechnician(user.getId(), replaceStatuses);
        } else {
            pcs = equipmentDAO.getPCsByStatusAndTechnician(user.getId(), replaceStatuses);
        }
    } else {
        if ("accessory".equals(equipmentType)) {
            accessories = equipmentDAO.getAllAccessories();
        } else if ("network".equals(equipmentType)) {
            networkDevices = equipmentDAO.getAllNetworkDevices();
        } else {
            pcs = equipmentDAO.getAllPCs();
        }
    }
%>

<div class="container mt-4">
    <h2 class="mb-4">Equipment Management</h2>

    <!-- Success/Error Messages -->
    <% 
        // Prioritize request attributes (set by servlet)
        String successAttr = (String) request.getAttribute("success");
        String errorAttr = (String) request.getAttribute("error");
        String typeAttr = (String) request.getAttribute("type");

        // Fallback to request parameters (for URL-based access)
        String successParam = request.getParameter("success");
        String errorParam = request.getParameter("error");

        // Use attributes if available, otherwise use parameters
        String success = (successAttr != null) ? successAttr : (successParam != null && !successParam.trim().isEmpty() ? successParam : null);
        String error = (errorAttr != null) ? errorAttr : (errorParam != null && !errorParam.trim().isEmpty() ? errorParam : null);

        // Display success message
        if (success != null) {
    %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= success %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% 
        } 
        // Display error message
        if (error != null) { 
    %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% 
        } 
    %>
    <!-- Tabs for Equipment Types -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link <%= (equipmentType == null || "pc".equals(equipmentType)) ? "active" : "" %>" href="?type=pc&filter=<%= filter != null ? filter : "" %>">PCs</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "accessory".equals(equipmentType) ? "active" : "" %>" href="?type=accessory&filter=<%= filter != null ? filter : "" %>">Accessories</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "network".equals(equipmentType) ? "active" : "" %>" href="?type=network&filter=<%= filter != null ? filter : "" %>">Network Devices</a>
        </li>
    </ul>

    <!-- Filter Options -->
    <div class="mb-4">
        <a href="?type=<%= equipmentType != null ? equipmentType : "pc" %>" class="btn btn-outline-primary <%= filter == null ? "active" : "" %>">All Equipment</a>
        <a href="?type=<%= equipmentType != null ? equipmentType : "pc" %>&filter=repair" class="btn btn-outline-warning <%= "repair".equals(filter) ? "active" : "" %>">Needs Repair</a>
        <a href="?type=<%= equipmentType != null ? equipmentType : "pc" %>&filter=replace" class="btn btn-outline-danger <%= "replace".equals(filter) ? "active" : "" %>">Needs Replacement</a>
    </div>

    <!-- Register New Equipment -->
    <div class="card mb-4">
        <div class="card-header bg-primary text-white">Register New Equipment</div>
        <div class="card-body">
    <form action="<%= request.getContextPath() %>/registerEquipment" method="post">
        <div class="mb-3">
            <label for="equipmentTypeSelect" class="form-label">Equipment Type</label>
            <select class="form-select" id="equipmentTypeSelect" name="equipmentType" onchange="toggleFields()">
                <option value="PC">PC</option>
                <option value="Accessory">Accessory</option>
                <option value="NetworkDevice">Network Device</option>
            </select>
        </div>

        <!-- PC Fields -->
        <div id="pcFields" style="display: block;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="pcBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="pcBrand" name="pcBrand" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="hdd" class="form-label">HDD</label>
                    <input type="text" class="form-control" id="hdd" name="hdd" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="ram" class="form-label">RAM</label>
                    <input type="text" class="form-control" id="ram" name="ram" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="os" class="form-label">OS</label>
                    <input type="text" class="form-control" id="os" name="os" required>
                </div>
            </div>
        </div>

        <!-- Accessory Fields -->
        <div id="accessoryFields" style="display: none;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="accessoryType" class="form-label">Type</label>
                    <select class="form-select" id="accessoryType" name="accessoryType">
                        <option value="MOUSE">Mouse</option>
                        <option value="KEYBOARD">Keyboard</option>
                        <option value="MONITOR">Monitor</option>
                        <option value="PROJECTOR">Projector</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="accessoryBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="accessoryBrand" name="accessoryBrand">
                </div>
            </div>
        </div>

        <!-- Network Device Fields -->
        <div id="networkFields" style="display: none;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="networkType" class="form-label">Type</label>
                    <select class="form-select" id="networkType" name="networkType">
                        <option value="ACCESS_POINT">Access Point</option>
                        <option value="SWITCH">Switch</option>
                        <option value="ROUTER">Router</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="networkBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="networkBrand" name="networkBrand">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="registrationYear" class="form-label">Registration Year</label>
                <select class="form-select" id="registrationYear" name="registrationYear" required>
                    <% for (int year = currentYear; year >= startYear; year--) { %>
                        <option value="<%= year %>"><%= year %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-md-6 mb-3">
                <label for="location" class="form-label">Location</label>
                <select class="form-select" id="location" name="location" required>
                    <option value="LAB">Lab</option>
                    <option value="OFFICE">Office</option>
                </select>
            </div>
        </div>
        <input type="hidden" name="technicianId" value="<%= user.getId() %>">
        <button type="submit" class="btn btn-primary">Register Equipment</button>
    </form>
</div>
    </div>

    <!-- Equipment Table -->
    <div class="card">
        <div class="card-header bg-secondary text-white">
            <%= "repair".equals(filter) ? "Equipment Needing Repair" : "replace".equals(filter) ? "Equipment Needing Replacement" : "All Equipment" %>
        </div>
        <div class="card-body">
            <%
                if ("accessory".equals(equipmentType)) {
            %>
                <h3>Accessories</h3>
                <table class="table table-bordered table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>Brand</th>
                            <th>Year</th>
                            <th>Status</th>
                            <th>Location</th>
                            <th>Technician ID</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            if (accessories != null && !accessories.isEmpty()) {
                                for (Accessory accessory : accessories) {
                        %>
                            <tr>
                                <td><%= accessory.getId() %></td>
                                <td><%= accessory.getType() %></td>
                                <td><%= accessory.getBrand() %></td>
                                <td><%= accessory.getRegistrationYear() %></td>
                                <td>
                                    <span class="badge bg-<%= 
                                        "WORKING".equals(accessory.getStatus()) ? "success" :
                                        "NOT_WORKING".equals(accessory.getStatus()) ? "warning" :
                                        "DAMAGED".equals(accessory.getStatus()) ? "danger" :
                                        "OLD".equals(accessory.getStatus()) ? "secondary" : "info" 
                                    %>">
                                        <%= accessory.getStatus() %>
                                    </span>
                                </td>
                                <td><%= accessory.getLocation() %></td>
                                <td><%= accessory.getTechnicianId() %></td>
                                <td>
                                    <% if (accessory.getTechnicianId() == user.getId()) { %>
                                        <% if ("NOT_WORKING".equals(accessory.getStatus()) || "DAMAGED".equals(accessory.getStatus())) { %>
                                            <a href="<%= request.getContextPath() %>/technician/repairEquipment?type=Accessory&id=<%= accessory.getId() %>" 
                                               class="btn btn-sm btn-success me-1" 
                                               onclick="return confirm('Are you sure you want to repair this accessory?')">
                                                <i class="bi bi-tools"></i> Repair
                                            </a>
                                        <% } %>
                                        <% if ("OLD".equals(accessory.getStatus())) { %>
                                            <button class="btn btn-sm btn-danger me-1" 
                                                    onclick="replaceEquipment('Accessory', <%= accessory.getId() %>)">
                                                <i class="bi bi-arrow-repeat"></i> Replace
                                            </button>
                                        <% } %>
                                        <!-- Update Status -->
                                        <form action="<%= request.getContextPath() %>/updateEquipmentStatus" method="post" class="d-inline">
                                            <input type="hidden" name="type" value="Accessory">
                                            <input type="hidden" name="id" value="<%= accessory.getId() %>">
                                            <select name="status" class="form-select d-inline w-auto">
                                                <option value="WORKING" <%= "WORKING".equals(accessory.getStatus()) ? "selected" : "" %>>WORKING</option>
                                                <option value="NOT_WORKING" <%= "NOT_WORKING".equals(accessory.getStatus()) ? "selected" : "" %>>NOT_WORKING</option>
                                                <option value="DAMAGED" <%= "DAMAGED".equals(accessory.getStatus()) ? "selected" : "" %>>DAMAGED</option>
                                                <option value="OLD" <%= "OLD".equals(accessory.getStatus()) ? "selected" : "" %>>OLD</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-primary">Update</button>
                                        </form>
                                        
                                    <% } else { %>
                                        <span class="text-muted">Assigned to another technician</span>
                                    <% } %>
                                </td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="8" class="text-center">No accessories found.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <%
                } else if ("network".equals(equipmentType)) {
            %>
                <h3>Network Devices</h3>
                <table class="table table-bordered table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>Brand</th>
                            <th>Year</th>
                            <th>Status</th>
                            <th>Location</th>
                            <th>Technician ID</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            if (networkDevices != null && !networkDevices.isEmpty()) {
                                for (NetworkDevice device : networkDevices) {
                        %>
                            <tr>
                                <td><%= device.getId() %></td>
                                <td><%= device.getType() %></td>
                                <td><%= device.getBrand() %></td>
                                <td><%= device.getRegistrationYear() %></td>
                                <td>
                                    <span class="badge bg-<%= 
                                        "WORKING".equals(device.getStatus()) ? "success" :
                                        "NOT_WORKING".equals(device.getStatus()) ? "warning" :
                                        "DAMAGED".equals(device.getStatus()) ? "danger" :
                                        "OLD".equals(device.getStatus()) ? "secondary" : "info" 
                                    %>">
                                        <%= device.getStatus() %>
                                    </span>
                                </td>
                                <td><%= device.getLocation() %></td>
                                <td><%= device.getTechnicianId() %></td>
                                <td>
                                    <% if (device.getTechnicianId() == user.getId()) { %>
                                        <% if ("NOT_WORKING".equals(device.getStatus()) || "DAMAGED".equals(device.getStatus())) { %>
                                            <a href="<%= request.getContextPath() %>/technician/repairEquipment?type=NetworkDevice&id=<%= device.getId() %>" 
                                               class="btn btn-sm btn-success me-1" 
                                               onclick="return confirm('Are you sure you want to repair this network device?')">
                                                <i class="bi bi-tools"></i> Repair
                                            </a>
                                        <% } %>
                                        <% if ("OLD".equals(device.getStatus())) { %>
                                            <button class="btn btn-sm btn-danger me-1" 
                                                    onclick="replaceEquipment('NetworkDevice', <%= device.getId() %>)">
                                                <i class="bi bi-arrow-repeat"></i>Replace
                                            </button>
                                        <% } %>
                                        <!-- Update Status -->
                                        <form action="<%= request.getContextPath() %>/updateEquipmentStatus" method="post" class="d-inline">
                                            <input type="hidden" name="type" value="NetworkDevice">
                                            <input type="hidden" name="id" value="<%= device.getId() %>">
                                            <select name="status" class="form-select d-inline w-auto">
                                                <option value="WORKING" <%= "WORKING".equals(device.getStatus()) ? "selected" : "" %>>WORKING</option>
                                                <option value="NOT_WORKING" <%= "NOT_WORKING".equals(device.getStatus()) ? "selected" : "" %>>NOT_WORKING</option>
                                                <option value="DAMAGED" <%= "DAMAGED".equals(device.getStatus()) ? "selected" : "" %>>DAMAGED</option>
                                                <option value="OLD" <%= "OLD".equals(device.getStatus()) ? "selected" : "" %>>OLD</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-primary">Update</button>
                                        </form>
                                        
                                    <% } else { %>
                                        <span class="text-muted">Assigned to another technician</span>
                                    <% } %>
                                </td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="8" class="text-center">No network devices found.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <%
                } else {
            %>
                <h3>PCs</h3>
                <table class="table table-bordered table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Brand</th>
                            <th>HDD</th>
                            <th>RAM</th>
                            <th>OS</th>
                            <th>Year</th>
                            <th>Status</th>
                            <th>Location</th>
                            <th>Technician ID</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            if (pcs != null && !pcs.isEmpty()) {
                                for (PC pc : pcs) {
                        %>
                            <tr>
                                <td><%= pc.getId() %></td>
                                <td><%= pc.getBrand() %></td>
                                <td><%= pc.getHdd() %></td>
                                <td><%= pc.getRam() %></td>
                                <td><%= pc.getOs() %></td>
                                <td><%= pc.getRegistrationYear() %></td>
                                <td>
                                    <span class="badge bg-<%= 
                                        "WORKING".equals(pc.getStatus()) ? "success" :
                                        "NOT_WORKING".equals(pc.getStatus()) ? "warning" :
                                        "DAMAGED".equals(pc.getStatus()) ? "danger" :
                                        "OLD".equals(pc.getStatus()) ? "secondary" : "info" 
                                    %>">
                                        <%= pc.getStatus() %>
                                    </span>
                                </td>
                                <td><%= pc.getLocation() %></td>
                                <td><%= pc.getTechnicianId() %></td>
                                <td>
                                    <% if (pc.getTechnicianId() == user.getId()) { %>
                                        <% if ("NOT_WORKING".equals(pc.getStatus()) || "DAMAGED".equals(pc.getStatus())) { %>
                                            <a href="<%= request.getContextPath() %>/technician/repairEquipment?type=PC&id=<%= pc.getId() %>" 
                                               class="btn btn-sm btn-success me-1" 
                                               onclick="return confirm('Are you sure you want to repair this PC?')">
                                                <i class="bi bi-tools"></i> Repair
                                            </a>
                                        <% } %>
                                        <% if ("OLD".equals(pc.getStatus())) { %>
                                            <button class="btn btn-sm btn-danger me-1" 
                                                    onclick="replaceEquipment('PC', <%= pc.getId() %>)">
                                                <i class="bi bi-arrow-repeat"></i> Replace
                                            </button>
                                        <% } %>
                                        <!-- Update Status -->
                                        <form action="<%= request.getContextPath() %>/updateEquipmentStatus" method="post" class="d-inline">
                                            <input type="hidden" name="type" value="PC">
                                            <input type="hidden" name="id" value="<%= pc.getId() %>">
                                            <select name="status" class="form-select d-inline w-auto">
                                                <option value="WORKING" <%= "WORKING".equals(pc.getStatus()) ? "selected" : "" %>>WORKING</option>
                                                <option value="NOT_WORKING" <%= "NOT_WORKING".equals(pc.getStatus()) ? "selected" : "" %>>NOT_WORKING</option>
                                                <option value="DAMAGED" <%= "DAMAGED".equals(pc.getStatus()) ? "selected" : "" %>>DAMAGED</option>
                                                <option value="OLD" <%= "OLD".equals(pc.getStatus()) ? "selected" : "" %>>OLD</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-primary">Update</button>
                                        </form>
                                        <!-- Delete Equipment -->
                                    <% } else { %>
                                        <span class="text-muted">Assigned to another technician</span>
                                    <% } %>
                                </td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="10" class="text-center">No PCs found.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </div>
</div>

<!-- Replace Equipment Modal -->
<div class="modal fade" id="replaceEquipmentModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Replace Equipment</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
    <form id="replaceEquipmentForm" action="<%= request.getContextPath() %>/replaceEquipment" method="post">
        <input type="hidden" name="oldId" id="oldId">
        <input type="hidden" name="equipmentType" id="replaceEquipmentType">
        <div class="mb-3">
            <label for="replaceEquipmentTypeSelect" class="form-label">Equipment Type</label>
            <select class="form-select" id="replaceEquipmentTypeSelect" disabled>
                <option value="PC">PC</option>
                <option value="Accessory">Accessory</option>
                <option value="NetworkDevice">Network Device</option>
            </select>
        </div>

        <!-- PC Fields -->
        <div id="replacePcFields" style="display: none;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="replacePcBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="replacePcBrand" name="pcBrand">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="replaceHdd" class="form-label">HDD</label>
                    <input type="text" class="form-control" id="replaceHdd" name="hdd">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="replaceRam" class="form-label">RAM</label>
                    <input type="text" class="form-control" id="replaceRam" name="ram">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="replaceOs" class="form-label">OS</label>
                    <input type="text" class="form-control" id="replaceOs" name="os">
                </div>
            </div>
        </div>

        <!-- Accessory Fields -->
        <div id="replaceAccessoryFields" style="display: none;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="replaceAccessoryType" class="form-label">Type</label>
                    <select class="form-select" id="replaceAccessoryType" name="accessoryType">
                        <option value="MOUSE">Mouse</option>
                        <option value="KEYBOARD">Keyboard</option>
                        <option value="MONITOR">Monitor</option>
                        <option value="PROJECTOR">Projector</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="replaceAccessoryBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="replaceAccessoryBrand" name="accessoryBrand">
                </div>
            </div>
        </div>

        <!-- Network Device Fields -->
        <div id="replaceNetworkFields" style="display: none;">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="replaceNetworkType" class="form-label">Type</label>
                    <select class="form-select" id="replaceNetworkType" name="networkType">
                        <option value="ACCESS_POINT">Access Point</option>
                        <option value="SWITCH">Switch</option>
                        <option value="ROUTER">Router</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="replaceNetworkBrand" class="form-label">Brand</label>
                    <input type="text" class="form-control" id="replaceNetworkBrand" name="networkBrand">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="replaceRegistrationYear" class="form-label">Registration Year</label>
                <select class="form-select" id="replaceRegistrationYear" name="registrationYear" required>
                    <% for (int year = currentYear; year >= startYear; year--) { %>
                        <option value="<%= year %>"><%= year %></option>
                    <% } %>
                </select>
            </div>
            <div class="col-md-6 mb-3">
                <label for="replaceLocation" class="form-label">Location</label>
                <select class="form-select" id="replaceLocation" name="location" required>
                    <option value="LAB">Lab</option>
                    <option value="OFFICE">Office</option>
                </select>
            </div>
        </div>
        <input type="hidden" name="technicianId" value="<%= user.getId() %>">
        <button type="submit" class="btn btn-primary">Replace Equipment</button>
    </form>
</div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
function toggleFields() {
    const type = document.getElementById('equipmentTypeSelect').value;
    const pcFields = document.getElementById('pcFields');
    const accessoryFields = document.getElementById('accessoryFields');
    const networkFields = document.getElementById('networkFields');

    pcFields.style.display = type === 'PC' ? 'block' : 'none';
    accessoryFields.style.display = type === 'Accessory' ? 'block' : 'none';
    networkFields.style.display = type === 'NetworkDevice' ? 'block' : 'none';

    toggleRequiredFields(pcFields, type === 'PC');
    toggleRequiredFields(accessoryFields, type === 'Accessory');
    toggleRequiredFields(networkFields, type === 'NetworkDevice');
}

function toggleRequiredFields(container, isVisible) {
    const inputs = container.querySelectorAll('input, select');
    inputs.forEach(input => {
        if (isVisible) {
            input.setAttribute('required', 'required');
        } else {
            input.removeAttribute('required');
            input.value = ''; // Clear hidden fields
        }
    });
}

document.querySelector('form[action*="registerEquipment"]').addEventListener('submit', function(event) {
    try {
        const type = document.getElementById('equipmentTypeSelect').value;
        const activeContainer = type === 'PC' ? document.getElementById('pcFields') :
                               type === 'Accessory' ? document.getElementById('accessoryFields') :
                               document.getElementById('networkFields');
        const requiredFields = activeContainer.querySelectorAll('[required]');
        const locationField = document.querySelector('select[name="location"]');
        let valid = true;

        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                valid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
            }
        });

        if (!locationField.value.trim()) {
            valid = false;
            locationField.classList.add('is-invalid');
        } else {
            locationField.classList.remove('is-invalid');
        }

        if (!valid) {
            event.preventDefault();
            alert('Please fill out all required fields.');
        }
    } catch (e) {
        console.error('Form validation error:', e);
        event.preventDefault();
        alert('An error occurred during form validation. Check console for details.');
    }
    
});
function replaceEquipment(type, id) {
    document.getElementById('oldId').value = id;
    document.getElementById('replaceEquipmentType').value = type;
    document.getElementById('replaceEquipmentTypeSelect').value = type;

    const pcFields = document.getElementById('replacePcFields');
    const accessoryFields = document.getElementById('replaceAccessoryFields');
    const networkFields = document.getElementById('replaceNetworkFields');

    pcFields.style.display = type === 'PC' ? 'block' : 'none';
    accessoryFields.style.display = type === 'Accessory' ? 'block' : 'none';
    networkFields.style.display = type === 'NetworkDevice' ? 'block' : 'none';

    toggleRequiredFields(pcFields, type === 'PC');
    toggleRequiredFields(accessoryFields, type === 'Accessory');
    toggleRequiredFields(networkFields, type === 'NetworkDevice');

    const modal = new bootstrap.Modal(document.getElementById('replaceEquipmentModal'));
    modal.show();
}

function deleteEquipment(type, id) {
    if (confirm('Are you sure you want to delete this equipment?')) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = '<%= request.getContextPath() %>/deleteEquipment';

        const typeInput = document.createElement('input');
        typeInput.type = 'hidden';
        typeInput.name = 'type';
        typeInput.value = type;
        form.appendChild(typeInput);

        const idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'id';
        idInput.value = id;
        form.appendChild(idInput);

        document.body.appendChild(form);
        form.submit();
    }
}
</script>
<%@ include file="/footer.jsp" %>
</body>
</html>