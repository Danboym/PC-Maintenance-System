package com.pcm.dao;

import com.pcm.model.NetworkDevice;
import com.pcm.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkDeviceDAO {

    public List<NetworkDevice> getAllNetworkDevices() {
        List<NetworkDevice> devices = new ArrayList<>();
        String sql = "SELECT * FROM NetworkDevices";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                devices.add(mapNetworkDevice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public boolean addNetworkDevice(NetworkDevice device) {
        String sql = "INSERT INTO NetworkDevices (type, brand, registration_year, status, location, technician_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, device.getType());
            pstmt.setString(2, device.getBrand());
            pstmt.setInt(3, device.getRegistrationYear());
            pstmt.setString(4, device.getStatus());
            pstmt.setString(5, device.getLocation());
            pstmt.setInt(6, device.getTechnicianId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding NetworkDevice: type=" + device.getType() + 
                ", brand=" + device.getBrand() + ", registration_year=" + device.getRegistrationYear() + 
                ", status=" + device.getStatus() + ", location=" + device.getLocation() + 
                ", technician_id=" + device.getTechnicianId());
            e.printStackTrace();
            return false;
        }
    }

    public List<NetworkDevice> getNetworkDevicesByTechnician(int technicianId) {
        List<NetworkDevice> devices = new ArrayList<>();
        String sql = "SELECT * FROM NetworkDevices WHERE technician_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, technicianId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    devices.add(mapNetworkDevice(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public boolean updateNetworkDevice(NetworkDevice device) {
        String sql = "UPDATE NetworkDevices SET type = ?, brand = ?, registration_year = ?, status = ?, location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, device.getType());
            pstmt.setString(2, device.getBrand());
            pstmt.setInt(3, device.getRegistrationYear());
            pstmt.setString(4, device.getStatus());
            pstmt.setString(5, device.getLocation());
            pstmt.setInt(6, device.getTechnicianId());
            pstmt.setInt(7, device.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNetworkDevice(int id) {
        String sql = "DELETE FROM NetworkDevices WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private NetworkDevice mapNetworkDevice(ResultSet rs) throws SQLException {
        NetworkDevice device = new NetworkDevice();
        device.setId(rs.getInt("id"));
        device.setType(rs.getString("type"));
        device.setBrand(rs.getString("brand"));
        device.setRegistrationYear(rs.getInt("registration_year"));
        device.setStatus(rs.getString("status"));
        device.setLocation(rs.getString("location"));
        device.setTechnicianId(rs.getInt("technician_id"));
        return device;
    }
}