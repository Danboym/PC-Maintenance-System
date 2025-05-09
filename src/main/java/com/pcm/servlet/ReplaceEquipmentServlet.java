package com.pcm.servlet;

import com.pcm.dao.EquipmentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/replaceEquipment")
public class ReplaceEquipmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int oldId = Integer.parseInt(request.getParameter("oldId"));
        String equipmentType = request.getParameter("equipmentType");
        String brand = null;
        int registrationYear = Integer.parseInt(request.getParameter("registrationYear"));
        String location = request.getParameter("location");
        int technicianId = Integer.parseInt(request.getParameter("technicianId"));
        EquipmentDAO equipmentDAO = new EquipmentDAO();
        boolean success = false;

        try {
            if ("PC".equals(equipmentType)) {
                String hdd = request.getParameter("hdd");
                String ram = request.getParameter("ram");
                String os = request.getParameter("os");
                brand = request.getParameter("pcBrand");
                success = equipmentDAO.replacePC(oldId, brand, hdd, ram, os, registrationYear, location, technicianId);
            } else if ("Accessory".equals(equipmentType)) {
                String accessoryType = request.getParameter("accessoryType");
                brand = request.getParameter("accessoryBrand");
                success = equipmentDAO.replaceAccessory(oldId, accessoryType, brand, registrationYear, location, technicianId);
            } else if ("NetworkDevice".equals(equipmentType)) {
                String networkType = request.getParameter("networkType");
                brand = request.getParameter("networkBrand");
                success = equipmentDAO.replaceNetworkDevice(oldId, networkType, brand, registrationYear, location, technicianId);
            }

            // Set request attributes instead of redirecting
            request.setAttribute("type", equipmentType.toLowerCase());
            if (success) {
                request.setAttribute("success", "Equipment replaced successfully");
            } else {
                request.setAttribute("error", "Failed to replace equipment");
            }

            // Forward to the JSP instead of redirecting
            RequestDispatcher dispatcher = request.getRequestDispatcher("/technician/equipment.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Set error attribute and forward to the JSP
            request.setAttribute("type", equipmentType.toLowerCase());
            request.setAttribute("error", "Error replacing equipment");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/technician/equipment.jsp");
            dispatcher.forward(request, response);
        }
    }
}