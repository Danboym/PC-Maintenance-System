package com.pcm.servlet;

import com.pcm.dao.EquipmentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registerEquipment")
public class RegisterEquipmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                success = equipmentDAO.addPC(brand, hdd, ram, os, registrationYear, location, technicianId);
            } else if ("Accessory".equals(equipmentType)) {
                String accessoryType = request.getParameter("accessoryType");
                brand = request.getParameter("accessoryBrand");
                success = equipmentDAO.addAccessory(accessoryType, brand, registrationYear, location, technicianId);
            } else if ("NetworkDevice".equals(equipmentType)) {
                String networkType = request.getParameter("networkType");
                brand = request.getParameter("networkBrand");
                success = equipmentDAO.addNetworkDevice(networkType, brand, registrationYear, location, technicianId);
            }

            String redirectUrl = request.getContextPath() + "/technician/equipment.jsp?type=" + equipmentType.toLowerCase();
            if (success) {
                redirectUrl += "&success=Equipment registered successfully";
            } else {
                redirectUrl += "&error=Failed to register equipment";
            }
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?type=" + equipmentType.toLowerCase() + "&error=Error registering equipment");
        }
    }
}