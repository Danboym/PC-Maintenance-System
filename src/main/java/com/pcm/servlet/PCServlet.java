package com.pcm.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.pcm.dao.AccessoryDAO;
import com.pcm.dao.NetworkDeviceDAO;
import com.pcm.dao.PCDAO;
import com.pcm.model.Accessory;
import com.pcm.model.NetworkDevice;
import com.pcm.model.PC;
import com.pcm.model.User;

@WebServlet({"/pc",  "/technician/repairEquipment"})
public class PCServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Validate user session and role
        if (user == null || !"TECHNICIAN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=Session%20expired%20or%20access%20denied");
            return;
        }

        // Handle GET requests for repair and replace actions
        switch (path) {
            case "/technician/repairEquipment":
                repairEquipment(request, response, user);
                break;
            case "/technician/replaceEquipment":
                replaceEquipment(request, response, user);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Validate user session and role
        if (user == null || !"TECHNICIAN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=Session%20expired%20or%20access%20denied");
            return;
        }

        // Handle POST requests for registering and replacing equipment
        if ("/technician/registerEquipment".equals(path)) {
            registerEquipment(request, response, user);
        } else if ("/technician/replaceEquipment".equals(path)) {
            replaceEquipment(request, response, user);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Registers new equipment for the technician.
     */
    private void registerEquipment(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String type = validateParameter(request.getParameter("type"), "Equipment type");

            if ("PC".equals(type)) {
                PC pc = new PC();
                pc.setTechnicianId(user.getId());
                pc.setBrand(validateParameter(request.getParameter("brand"), "Brand"));
                pc.setHdd(validateParameter(request.getParameter("hdd"), "HDD"));
                pc.setRam(validateParameter(request.getParameter("ram"), "RAM"));
                pc.setOs(validateParameter(request.getParameter("os"), "Operating System"));
                pc.setRegistrationYear(parseRegistrationYear(request.getParameter("registrationYear")));
                pc.setStatus("WORKING");
                pc.setLocation(validateEnum(request.getParameter("location"), "Location", new String[]{"LAB", "OFFICE"}));

                PCDAO pcDAO = new PCDAO();
                if (pcDAO.addPC(pc)) {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?success=PC%20registered%20successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Failed%20to%20register%20PC");
                }
            } else if ("Accessory".equals(type)) {
                Accessory accessory = new Accessory();
                accessory.setTechnicianId(user.getId());
                String accessoryType = validateEnum(request.getParameter("accessoryType"), "Accessory Type", 
                    new String[]{"MOUSE", "KEYBOARD", "MONITOR", "PROJECTOR"});
                accessory.setType(accessoryType);
                accessory.setBrand(validateParameter(request.getParameter("brand"), "Brand"));
                accessory.setRegistrationYear(parseRegistrationYear(request.getParameter("registrationYear")));
                accessory.setStatus("WORKING");
                accessory.setLocation(validateEnum(request.getParameter("location"), "Location", new String[]{"LAB", "OFFICE"}));

                AccessoryDAO accessoryDAO = new AccessoryDAO();
                if (accessoryDAO.addAccessory(accessory)) {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?success=Accessory%20registered%20successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Failed%20to%20register%20Accessory");
                }
            } else if ("NetworkDevice".equals(type)) {
                NetworkDevice device = new NetworkDevice();
                device.setTechnicianId(user.getId());
                String deviceType = validateEnum(request.getParameter("deviceType"), "Device Type", 
                    new String[]{"ACCESS_POINT", "SWITCH", "ROUTER"});
                device.setType(deviceType);
                device.setBrand(validateParameter(request.getParameter("brand"), "Brand"));
                device.setRegistrationYear(parseRegistrationYear(request.getParameter("registrationYear")));
                device.setStatus("WORKING");
                device.setLocation(validateEnum(request.getParameter("location"), "Location", new String[]{"LAB", "OFFICE"}));

                NetworkDeviceDAO deviceDAO = new NetworkDeviceDAO();
                if (deviceDAO.addNetworkDevice(device)) {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?success=Network%20Device%20registered%20successfully");
                } else {
                    response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Failed%20to%20register%20Network%20Device");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Invalid%20equipment%20type");
            }
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=" + 
                URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=An%20unexpected%20error%20occurred");
        }
    }

    /**
     * Repairs equipment by setting its status to WORKING.
     */
    private void repairEquipment(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String pcIdStr = request.getParameter("id");
            if (pcIdStr == null || pcIdStr.trim().isEmpty()) {
                throw new NumberFormatException("PC ID is required.");
            }
            int pcId = Integer.parseInt(pcIdStr);
            PCDAO pcDAO = new PCDAO();

            if (pcDAO.updatePCStatus(pcId, "WORKING")) {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=repair&success=Equipment%20repaired%20successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=repair&error=Failed%20to%20repair%20equipment");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=repair&error=Invalid%20PC%20ID%20format");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=repair&error=An%20unexpected%20error%20occurred");
        }
    }

    /**
     * Replaces equipment by deleting it (can be extended to archive or mark as replaced).
     */
    private void replaceEquipment(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String pcIdStr = request.getParameter("id");
            if (pcIdStr == null || pcIdStr.trim().isEmpty()) {
                throw new NumberFormatException("PC ID is required.");
            }
            int pcId = Integer.parseInt(pcIdStr);
            PCDAO pcDAO = new PCDAO();

            if (pcDAO.deletePC(pcId)) {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=replace&success=Equipment%20replaced%20successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=replace&error=Failed%20to%20replace%20equipment");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=replace&error=Invalid%20PC%20ID%20format");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?filter=replace&error=An%20unexpected%20error%20occurred");
        }
    }

    private String validateParameter(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value;
    }

    private String validateEnum(String value, String fieldName, String[] validValues) {
        String upperValue = validateParameter(value, fieldName).toUpperCase();
        for (String valid : validValues) {
            if (valid.equals(upperValue)) {
                return valid;
            }
        }
        throw new IllegalArgumentException(fieldName + " must be one of: " + String.join(", ", validValues));
    }

    private int parseRegistrationYear(String registrationYearStr) {
        String value = validateParameter(registrationYearStr, "Registration year");
        try {
            int year = Integer.parseInt(value);
            if (year < 2000 || year > java.time.Year.now().getValue()) {
                throw new IllegalArgumentException("Registration year must be between 2000 and current year");
            }
            return year;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid registration year format");
        }
    }
}