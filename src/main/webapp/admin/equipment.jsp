<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pcm.model.User" %>
<%@ page import="com.pcm.model.PC" %>
<%@ page import="com.pcm.model.Accessory" %>
<%@ page import="com.pcm.model.NetworkDevice" %>
<%@ page import="com.pcm.dao.PCDAO" %>
<%@ page import="com.pcm.dao.AccessoryDAO" %>
<%@ page import="com.pcm.dao.NetworkDeviceDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Equipment Management - PC Maintenance System</title>
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
        
        PCDAO pcDAO = new PCDAO();
        List<PC> pcs = pcDAO.getAllPCs();

        AccessoryDAO accessoryDAO = new AccessoryDAO();
        List<Accessory> accessories = accessoryDAO.getAllAccessories();

        NetworkDeviceDAO networkDeviceDAO = new NetworkDeviceDAO();
        List<NetworkDevice> networkDevices = networkDeviceDAO.getAllNetworkDevices();
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
                        <a class="nav-link active" href="equipment.jsp">Equipment</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="requests.jsp">Requests</a>
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
            <h2>Equipment Management</h2>
        </div>

        <ul class="nav nav-tabs" id="equipmentTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="pcs-tab" data-bs-toggle="tab" data-bs-target="#pcs" type="button">
                    PCs
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="accessories-tab" data-bs-toggle="tab" data-bs-target="#accessories" type="button">
                    Accessories
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="network-devices-tab" data-bs-toggle="tab" data-bs-target="#network-devices" type="button">
                    Network Devices
                </button>
            </li>
        </ul>

        <div class="tab-content mt-3" id="equipmentTabsContent">
            <!-- PCs Tab -->
            <div class="tab-pane fade show active" id="pcs" role="tabpanel">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Brand</th>
                                <th>Specifications</th>
                                <th>Status</th>
                                <th>Location</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (pcs != null && !pcs.isEmpty()) { %>
                                <% for (PC pc : pcs) { %>
                                    <tr>
                                        <td><%= pc.getId() %></td>
                                        <td><%= pc.getBrand() %></td>
                                        <td>
                                            HDD: <%= pc.getHdd() %><br>
                                            RAM: <%= pc.getRam() %><br>
                                            OS: <%= pc.getOs() %><br>
                                            Year: <%= pc.getRegistrationYear() %>
                                        </td>
                                        <td>
                                            <span class="badge bg-<%= 
                                                "WORKING".equals(pc.getStatus()) ? "success" : 
                                                "NOT_WORKING".equals(pc.getStatus()) ? "danger" : 
                                                "DAMAGED".equals(pc.getStatus()) ? "warning" : "secondary" 
                                            %>">
                                                <%= pc.getStatus() %>
                                            </span>
                                        </td>
                                        <td><%= pc.getLocation() %></td>
                                    </tr>
                                <% } %>
                            <% } else { %>
                                <tr>
                                    <td colspan="5" class="text-center">No PCs found.</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Accessories Tab -->
            <div class="tab-pane fade" id="accessories" role="tabpanel">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>Brand</th>
                                <th>Status</th>
                                <th>Location</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (accessories != null && !accessories.isEmpty()) { %>
                                <% for (Accessory accessory : accessories) { %>
                                    <tr>
                                        <td><%= accessory.getId() %></td>
                                        <td><%= accessory.getType() %></td>
                                        <td><%= accessory.getBrand() %></td>
                                        <td>
                                            <span class="badge bg-<%= 
                                                "WORKING".equals(accessory.getStatus()) ? "success" : 
                                                "NOT_WORKING".equals(accessory.getStatus()) ? "danger" : 
                                                "DAMAGED".equals(accessory.getStatus()) ? "warning" : "secondary" 
                                            %>">
                                                <%= accessory.getStatus() %>
                                            </span>
                                        </td>
                                        <td><%= accessory.getLocation() %></td>
                                    </tr>
                                <% } %>
                            <% } else { %>
                                <tr>
                                    <td colspan="5" class="text-center">No accessories found.</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Network Devices Tab -->
            <div class="tab-pane fade" id="network-devices" role="tabpanel">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>Brand</th>
                                <th>Status</th>
                                <th>Location</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (networkDevices != null && !networkDevices.isEmpty()) { %>
                                <% for (NetworkDevice device : networkDevices) { %>
                                    <tr>
                                        <td><%= device.getId() %></td>
                                        <td><%= device.getType() %></td>
                                        <td><%= device.getBrand() %></td>
                                        <td>
                                            <span class="badge bg-<%= 
                                                "WORKING".equals(device.getStatus()) ? "success" : 
                                                "NOT_WORKING".equals(device.getStatus()) ? "danger" : 
                                                "DAMAGED".equals(device.getStatus()) ? "warning" : "secondary" 
                                            %>">
                                                <%= device.getStatus() %>
                                            </span>
                                        </td>
                                        <td><%= device.getLocation() %></td>
                                    </tr>
                                <% } %>
                            <% } else { %>
                                <tr>
                                    <td colspan="5" class="text-center">No network devices found.</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
