package com.pcm.dao;

import com.pcm.model.PC;
import com.pcm.model.Accessory;
import com.pcm.model.NetworkDevice;
import com.pcm.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
public class EquipmentDAO {
	private static final Logger LOGGER = Logger.getLogger(EquipmentDAO.class.getName());

    // Fetch all PCs from the database
    public List<PC> getAllPCs() {
        String sql = "SELECT * FROM PC";
        List<PC> pcs = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                PC pc = new PC();
                pc.setId(rs.getInt("id"));
                pc.setBrand(rs.getString("brand"));
                pc.setHdd(rs.getString("hdd"));
                pc.setRam(rs.getString("ram"));
                pc.setOs(rs.getString("os"));
                pc.setRegistrationYear(rs.getInt("registration_year"));
                pc.setStatus(rs.getString("status"));
                pc.setLocation(rs.getString("location"));
                pc.setTechnicianId(rs.getInt("technician_id"));
                pcs.add(pc);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return pcs;
    }
    public class DatabaseUtilTest {
        public static void main(String[] args) {
            try (Connection conn = DatabaseUtil.getConnection()) {
                System.out.println("Connection successful: " + conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean addPC(String brand, String hdd, String ram, String os, int registrationYear, String location, int technicianId) {
        String sql = "INSERT INTO PC (brand, hdd, ram, os, registration_year, status, location, technician_id) VALUES (?, ?, ?, ?, ?, 'WORKING', ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, hdd);
            pstmt.setString(3, ram);
            pstmt.setString(4, os);
            pstmt.setInt(5, registrationYear);
            pstmt.setString(6, location);
            pstmt.setInt(7, technicianId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addAccessory(String accessoryType, String brand, int registrationYear, String location, int technicianId) {
        String sql = "INSERT INTO Accessories (type, brand, registration_year, location, status, technician_id) VALUES (?, ?, ?, ?, 'WORKING', ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessoryType);
            pstmt.setString(2, brand);
            pstmt.setInt(3, registrationYear);
            pstmt.setString(4, location);
            pstmt.setInt(5, technicianId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addNetworkDevice(String deviceType, String brand, int registrationYear, String location, int technicianId) {
        String sql = "INSERT INTO NetworkDevices (type, brand, registration_year, location, status, technician_id) VALUES (?, ?, ?, ?, 'WORKING', ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deviceType);
            pstmt.setString(2, brand);
            pstmt.setInt(3, registrationYear);
            pstmt.setString(4, location);
            pstmt.setInt(5, technicianId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean replacePC(int oldId, String brand, String hdd, String ram, String os, int registrationYear, String location, int technicianId) {
        // Check if oldId exists
        String checkSql = "SELECT COUNT(*) FROM PC WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, oldId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return false; // oldId doesn't exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "UPDATE PC SET brand = ?, hdd = ?, ram = ?, os = ?, registration_year = ?, status = 'WORKING', location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, hdd);
            pstmt.setString(3, ram);
            pstmt.setString(4, os);
            pstmt.setInt(5, registrationYear);
            pstmt.setString(6, location);
            pstmt.setInt(7, technicianId);
            pstmt.setInt(8, oldId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean replaceAccessory(int oldId, String accessoryType, String brand, int registrationYear, String location, int technicianId) {
        // Check if oldId exists
        String checkSql = "SELECT COUNT(*) FROM Accessories WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, oldId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                LOGGER.warning("Accessory with ID " + oldId + " does not exist");
                return false; // oldId doesn't exist
            }
        } catch (SQLException e) {
            LOGGER.severe("Error checking existence of Accessory ID " + oldId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        String sql = "UPDATE Accessories SET type = ?, brand = ?, registration_year = ?, status = 'WORKING', location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessoryType);
            pstmt.setString(2, brand);
            pstmt.setInt(3, registrationYear);
            pstmt.setString(4, location);
            pstmt.setInt(5, technicianId);
            pstmt.setInt(6, oldId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Accessory with ID " + oldId + " replaced successfully");
            } else {
                LOGGER.warning("No rows updated for Accessory ID " + oldId);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error replacing Accessory ID " + oldId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean replaceNetworkDevice(int oldId, String deviceType, String brand, int registrationYear, String location, int technicianId) {
        // Check if oldId exists
        String checkSql = "SELECT COUNT(*) FROM NetworkDevices WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, oldId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                LOGGER.warning("Network Device with ID " + oldId + " does not exist");
                return false; // oldId doesn't exist
            }
        } catch (SQLException e) {
            LOGGER.severe("Error checking existence of Network Device ID " + oldId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        String sql = "UPDATE NetworkDevices SET type = ?, brand = ?, registration_year = ?, status = 'WORKING', location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deviceType);
            pstmt.setString(2, brand);
            pstmt.setInt(3, registrationYear);
            pstmt.setString(4, location);
            pstmt.setInt(5, technicianId);
            pstmt.setInt(6, oldId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Network Device with ID " + oldId + " replaced successfully");
            } else {
                LOGGER.warning("No rows updated for Network Device ID " + oldId);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error replacing Network Device ID " + oldId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    // Fetch all accessories from the database
    public List<Accessory> getAllAccessories() {
        String sql = "SELECT * FROM Accessories";
        List<Accessory> accessories = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Accessory accessory = new Accessory();
                accessory.setId(rs.getInt("id"));
                accessory.setType(rs.getString("type"));
                accessory.setBrand(rs.getString("brand"));
                accessory.setStatus(rs.getString("status"));
                accessory.setLocation(rs.getString("location"));
                accessory.setTechnicianId(rs.getInt("technician_id"));
                try {
                    accessory.setRegistrationYear(rs.getInt("registration_year"));
                } catch (SQLException e) {
                    accessory.setRegistrationYear(0); // Fallback if column is missing
                }
                accessories.add(accessory);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return accessories;
    }
    public boolean addAccessory(String accessoryType, String brand, int registrationYear, String location) {
        String sql = "INSERT INTO Accessories (type, brand, registration_year, location, status) VALUES (?, ?, ?, ?, 'WORKING')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessoryType);
            pstmt.setString(2, brand);
            pstmt.setInt(3, registrationYear);
            pstmt.setString(4, location);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addNetworkDevice(String deviceType, String brand, int registrationYear, String location) {
        String sql = "INSERT INTO NetworkDevices (type, brand, status, location, registration_year) VALUES (?, ?, 'WORKING', ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deviceType);
            pstmt.setString(2, brand);
            pstmt.setString(3, location);
            pstmt.setInt(4, registrationYear);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addPC(String brand, String hdd, String ram, String os, int registrationYear, String location) {
        String sql = "INSERT INTO PC (brand, hdd, ram, os, registration_year, status, location) VALUES (?, ?, ?, ?, ?, 'WORKING', ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, hdd);
            pstmt.setString(3, ram);
            pstmt.setString(4, os);
            pstmt.setInt(5, registrationYear);
            pstmt.setString(6, location);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}



    // Fetch all network devices from the database
    public List<NetworkDevice> getAllNetworkDevices() {
        String sql = "SELECT * FROM NetworkDevices";
        List<NetworkDevice> devices = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                NetworkDevice device = new NetworkDevice();
                device.setId(rs.getInt("id"));
                device.setType(rs.getString("type"));
                device.setBrand(rs.getString("brand"));
                device.setStatus(rs.getString("status"));
                device.setLocation(rs.getString("location"));
                device.setTechnicianId(rs.getInt("technician_id"));
                try {
                    device.setRegistrationYear(rs.getInt("registration_year"));
                } catch (SQLException e) {
                    device.setRegistrationYear(0); // Fallback if column is missing
                }
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return devices;
    }

    // Fetch PCs by technician ID and status
    public List<PC> getPCsByStatusAndTechnician(int technicianId, String[] statuses) {
        List<PC> pcs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM PC WHERE technician_id = ? AND status IN (");
        for (int i = 0; i < statuses.length; i++) {
            sql.append("?");
            if (i < statuses.length - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, technicianId);
            for (int i = 0; i < statuses.length; i++) {
                pstmt.setString(i + 2, statuses[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PC pc = new PC();
                    pc.setId(rs.getInt("id"));
                    pc.setBrand(rs.getString("brand"));
                    pc.setHdd(rs.getString("hdd"));
                    pc.setRam(rs.getString("ram"));
                    pc.setOs(rs.getString("os"));
                    pc.setRegistrationYear(rs.getInt("registration_year"));
                    pc.setStatus(rs.getString("status"));
                    pc.setLocation(rs.getString("location"));
                    pc.setTechnicianId(rs.getInt("technician_id"));
                    pcs.add(pc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return pcs;
    }

    // Fetch accessories by technician ID and status
    public List<Accessory> getAccessoriesByStatusAndTechnician(int technicianId, String[] statuses) {
        List<Accessory> accessories = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Accessories WHERE technician_id = ? AND status IN (");
        for (int i = 0; i < statuses.length; i++) {
            sql.append("?");
            if (i < statuses.length - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, technicianId);
            for (int i = 0; i < statuses.length; i++) {
                pstmt.setString(i + 2, statuses[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Accessory accessory = new Accessory();
                    accessory.setId(rs.getInt("id"));
                    accessory.setType(rs.getString("type"));
                    accessory.setBrand(rs.getString("brand"));
                    try {
                        accessory.setRegistrationYear(rs.getInt("registration_year"));
                    } catch (SQLException e) {
                        accessory.setRegistrationYear(0); // Fallback if column is missing
                    }
                    accessory.setStatus(rs.getString("status"));
                    accessory.setLocation(rs.getString("location"));
                    accessory.setTechnicianId(rs.getInt("technician_id"));
                    accessories.add(accessory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return accessories;
    }

    // Fetch network devices by technician ID and status
    public List<NetworkDevice> getNetworkDevicesByStatusAndTechnician(int technicianId, String[] statuses) {
        List<NetworkDevice> devices = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NetworkDevices WHERE technician_id = ? AND status IN (");
        for (int i = 0; i < statuses.length; i++) {
            sql.append("?");
            if (i < statuses.length - 1) sql.append(",");
        }
        sql.append(")");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, technicianId);
            for (int i = 0; i < statuses.length; i++) {
                pstmt.setString(i + 2, statuses[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NetworkDevice device = new NetworkDevice();
                    device.setId(rs.getInt("id"));
                    device.setType(rs.getString("type"));
                    device.setBrand(rs.getString("brand"));
                    try {
                        device.setRegistrationYear(rs.getInt("registration_year"));
                    } catch (SQLException e) {
                        device.setRegistrationYear(0); // Fallback if column is missing
                    }
                    device.setStatus(rs.getString("status"));
                    device.setLocation(rs.getString("location"));
                    device.setTechnicianId(rs.getInt("technician_id"));
                    devices.add(device);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
        return devices;
    }

    // Delete a PC by ID
    public boolean deletePC(int id) throws SQLException {
        String sql = "DELETE FROM PC WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Delete an accessory by ID
    public boolean deleteAccessory(int id) throws SQLException {
        String sql = "DELETE FROM Accessories WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Delete a network device by ID
    public boolean deleteNetworkDevice(int id) throws SQLException {
        String sql = "DELETE FROM NetworkDevices WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}