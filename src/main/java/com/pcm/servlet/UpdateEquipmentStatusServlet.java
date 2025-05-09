package com.pcm.servlet;

import com.pcm.util.DatabaseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/updateEquipmentStatus")
public class UpdateEquipmentStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L; // Added to fix warning

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String idStr = request.getParameter("id");
        String status = request.getParameter("status");

        // Validate parameters
        if (type == null || idStr == null || status == null || 
            !type.matches("PC|Accessory|NetworkDevice") || 
            !status.matches("WORKING|NOT_WORKING|DAMAGED|OLD")) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Invalid%20input%20parameters");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
            if (id <= 0) {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Invalid%20equipment%20ID%20format");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Invalid%20equipment%20ID%20format");
            return;
        }

        // Update the database
        String tableName;
        switch (type) {
            case "PC":
                tableName = "PC";
                break;
            case "Accessory":
                tableName = "Accessories";
                break;
            case "NetworkDevice":
                tableName = "NetworkDevices";
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Invalid%20equipment%20type");
                return;
        }

        String sql = "UPDATE " + tableName + " SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?success=Status%20updated%20successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Equipment%20not%20found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/technician/equipment.jsp?error=Database%20error:%20" + e.getMessage());
        }
    }
}