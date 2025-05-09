package com.pcm.controller;

import com.pcm.model.User;
import com.pcm.model.PC;
import com.pcm.model.Request;
import com.pcm.dao.UserDAO;
import com.pcm.dao.PCDAO;
import com.pcm.dao.RequestDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard/*")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            showDashboard(request, response);
            return;
        }

        switch (path) {
            case "/deleteTechnician":
                deleteTechnician(request, response);
                break;
            case "/deletePC":
                deletePC(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch data for the dashboard
        PCDAO pcDAO = new PCDAO();
        RequestDAO requestDAO = new RequestDAO();
        UserDAO userDAO = new UserDAO();

        int totalEquipment = pcDAO.getTotalEquipmentCount();
        int workingEquipment = pcDAO.getWorkingEquipmentCount();
        int pendingRequests = requestDAO.getPendingRequestCount();
        Map<String, Integer> equipmentStatus = pcDAO.getPCStatusByCategory();

        Map<String, Integer> technicianStatus = new HashMap<>();
        List<User> technicians = userDAO.getAllUsers();
        int available = 0, notAvailable = 0, busy = 0;
        for (User tech : technicians) {
            if ("TECHNICIAN".equals(tech.getRole())) {
                switch (tech.getStatus()) {
                    case "AVAILABLE":
                        available++;
                        break;
                    case "NOT_AVAILABLE":
                        notAvailable++;
                        break;
                    case "BUSY":
                        busy++;
                        break;
                }
            }
        }
        technicianStatus.put("AVAILABLE", available);
        technicianStatus.put("NOT_AVAILABLE", notAvailable);
        technicianStatus.put("BUSY", busy);

        // Set attributes in the request
        request.setAttribute("totalEquipment", totalEquipment);
        request.setAttribute("workingEquipment", workingEquipment);
        request.setAttribute("pendingRequests", pendingRequests);
        request.setAttribute("equipmentStatus", equipmentStatus);
        request.setAttribute("technicianStatus", technicianStatus);

        // Forward to dashboard.jsp
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/addTechnician":
                addTechnician(request, response);
                break;
            case "/updateTechnician":
                updateTechnician(request, response);
                break;
            case "/addPC":
                addPC(request, response);
                break;
            case "/updatePC":
                updatePC(request, response);
                break;
            case "/assignTechnician": // New endpoint for assigning a technician
                assignTechnician(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void assignTechnician(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        int technicianId = Integer.parseInt(request.getParameter("technicianId"));

        RequestDAO requestDAO = new RequestDAO();
        Request requestToUpdate = requestDAO.getRequestById(requestId);

        if (requestToUpdate != null) {
            requestToUpdate.setTechnicianId(technicianId);
            requestToUpdate.setStatus("TECHNICIAN_ASSIGNED");

            if (requestDAO.updateRequest(requestToUpdate)) {
                response.sendRedirect("requests.jsp?success=Technician assigned successfully");
            } else {
                response.sendRedirect("requests.jsp?error=Failed to assign technician");
            }
        } else {
            response.sendRedirect("requests.jsp?error=Request not found");
        }
    }

    // ========== Technician Methods ==========

    private void addTechnician(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRole("TECHNICIAN");
        user.setStatus(request.getParameter("status"));

        UserDAO userDAO = new UserDAO();
        if (userDAO.addUser(user)) {
        	response.sendRedirect(request.getContextPath() + "/admin/technicians.jsp?success=Technician%20added%20successfully");
        } else {
            response.sendRedirect("technicians.jsp?error=Failed to add technician");
        }
    }

    private void updateTechnician(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = new User();
        user.setId(Integer.parseInt(request.getParameter("id")));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setStatus(request.getParameter("status"));

        UserDAO userDAO = new UserDAO();
        if (userDAO.updateUser(user)) {
            response.sendRedirect("technicians.jsp?success=Technician updated successfully");
        } else {
            response.sendRedirect("technicians.jsp?error=Failed to update technician");
        }
    }

    private void deleteTechnician(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        UserDAO userDAO = new UserDAO();

        if (userDAO.deleteUser(userId)) {
        	request.setAttribute("success", "Technician deleted successfully.");
        } else {
            response.sendRedirect("technicians.jsp?error=Failed to delete technician");
        }
    }




    private void addPC(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PC pc = new PC();
        pc.setBrand(request.getParameter("brand"));
        pc.setHdd(request.getParameter("hdd"));
        pc.setRam(request.getParameter("ram"));
        pc.setOs(request.getParameter("os"));
        pc.setRegistrationYear(Integer.parseInt(request.getParameter("registrationYear")));
        pc.setStatus(request.getParameter("status"));
        pc.setLocation(request.getParameter("location"));
        pc.setTechnicianId(Integer.parseInt(request.getParameter("technicianId")));

        PCDAO pcDAO = new PCDAO();
        if (pcDAO.addPC(pc)) {
            response.sendRedirect("equipment.jsp?success=PC added successfully");
        } else {
            response.sendRedirect("equipment.jsp?error=Failed to add PC");
        }
    }

    private void updatePC(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PC pc = new PC();
        pc.setId(Integer.parseInt(request.getParameter("id")));
        pc.setBrand(request.getParameter("brand"));
        pc.setHdd(request.getParameter("hdd"));
        pc.setRam(request.getParameter("ram"));
        pc.setOs(request.getParameter("os"));
        pc.setRegistrationYear(Integer.parseInt(request.getParameter("registrationYear")));
        pc.setStatus(request.getParameter("status"));
        pc.setLocation(request.getParameter("location"));
        pc.setTechnicianId(Integer.parseInt(request.getParameter("technicianId")));

        PCDAO pcDAO = new PCDAO();
        if (pcDAO.updatePC(pc)) {
            response.sendRedirect("equipment.jsp?success=PC updated successfully");
        } else {
            response.sendRedirect("equipment.jsp?error=Failed to update PC");
        }
    }

    private void deletePC(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pcId = Integer.parseInt(request.getParameter("id"));
        PCDAO pcDAO = new PCDAO();

        if (pcDAO.deletePC(pcId)) {
            response.sendRedirect("equipment.jsp?success=PC deleted successfully");
        } else {
            response.sendRedirect("equipment.jsp?error=Failed to delete PC");
        }
    }
}
