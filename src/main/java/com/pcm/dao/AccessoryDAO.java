package com.pcm.dao;

import com.pcm.model.Accessory;
import com.pcm.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccessoryDAO {

    public List<Accessory> getAllAccessories() {
        List<Accessory> accessories = new ArrayList<>();
        String sql = "SELECT * FROM Accessories";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                accessories.add(mapAccessory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessories;
    }

    public boolean addAccessory(Accessory accessory) {
        String sql = "INSERT INTO Accessories (type, brand, registration_year, status, location, technician_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessory.getType());
            pstmt.setString(2, accessory.getBrand());
            pstmt.setInt(3, accessory.getRegistrationYear());
            pstmt.setString(4, accessory.getStatus());
            pstmt.setString(5, accessory.getLocation());
            pstmt.setInt(6, accessory.getTechnicianId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding Accessory: type=" + accessory.getType() + 
                ", brand=" + accessory.getBrand() + ", registration_year=" + accessory.getRegistrationYear() + 
                ", status=" + accessory.getStatus() + ", location=" + accessory.getLocation() + 
                ", technician_id=" + accessory.getTechnicianId());
            e.printStackTrace();
            return false;
        }
    }

    public List<Accessory> getAccessoriesByTechnician(int technicianId) {
        List<Accessory> accessories = new ArrayList<>();
        String sql = "SELECT * FROM Accessories WHERE technician_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, technicianId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accessories.add(mapAccessory(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessories;
    }

    public boolean updateAccessory(Accessory accessory) {
        String sql = "UPDATE Accessories SET type = ?, brand = ?, registration_year = ?, status = ?, location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessory.getType());
            pstmt.setString(2, accessory.getBrand());
            pstmt.setInt(3, accessory.getRegistrationYear());
            pstmt.setString(4, accessory.getStatus());
            pstmt.setString(5, accessory.getLocation());
            pstmt.setInt(6, accessory.getTechnicianId());
            pstmt.setInt(7, accessory.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccessory(int id) {
        String sql = "DELETE FROM Accessories WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Accessory mapAccessory(ResultSet rs) throws SQLException {
        Accessory accessory = new Accessory();
        accessory.setId(rs.getInt("id"));
        accessory.setType(rs.getString("type"));
        accessory.setBrand(rs.getString("brand"));
        accessory.setRegistrationYear(rs.getInt("registration_year"));
        accessory.setStatus(rs.getString("status"));
        accessory.setLocation(rs.getString("location"));
        accessory.setTechnicianId(rs.getInt("technician_id"));
        return accessory;
    }
}