package com.pcm.dao;

import com.pcm.model.PC;
import com.pcm.util.DatabaseUtil;

import java.sql.*;
import java.util.*;

public class PCDAO {

    /**
     * Adds a new PC to the database.
     */
    public boolean addPC(PC pc) {
        String sql = "INSERT INTO PC (brand, hdd, ram, os, registration_year, status, location, technician_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pc.getBrand());
            pstmt.setString(2, pc.getHdd());
            pstmt.setString(3, pc.getRam());
            pstmt.setString(4, pc.getOs());
            pstmt.setInt(5, pc.getRegistrationYear());
            pstmt.setString(6, pc.getStatus());
            pstmt.setString(7, pc.getLocation());
            pstmt.setInt(8, pc.getTechnicianId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding PC: brand=" + pc.getBrand() + 
                ", hdd=" + pc.getHdd() + ", ram=" + pc.getRam() + 
                ", os=" + pc.getOs() + ", registration_year=" + pc.getRegistrationYear() + 
                ", status=" + pc.getStatus() + ", location=" + pc.getLocation() + 
                ", technician_id=" + pc.getTechnicianId());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the total count of PCs in the database.
     */
    public int getTotalEquipmentCount() {
        String sql = "SELECT COUNT(*) FROM PC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching total equipment count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves the count of PCs with status WORKING.
     */
    public int getWorkingEquipmentCount() {
        String sql = "SELECT COUNT(*) FROM PC WHERE status = 'WORKING'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching working equipment count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves all PCs from the database.
     */
    public List<PC> getAllPCs() {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM PC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pcs.add(mapPC(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all PCs: " + e.getMessage());
            e.printStackTrace();
        }
        return pcs;
    }

    /**
     * Retrieves a PC by its ID.
     */
    public PC getPCById(int id) {
        String sql = "SELECT * FROM PC WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapPC(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PC by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all PCs assigned to a specific technician.
     */
    public List<PC> getEquipmentByTechnician(int technicianId) {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM PC WHERE technician_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, technicianId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pcs.add(mapPC(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PCs for technician ID " + technicianId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return pcs;
    }

    /**
     * Retrieves PCs assigned to a specific technician with the given statuses.
     */
    public List<PC> getEquipmentByStatusAndTechnician(int technicianId, String... statuses) {
        List<PC> equipmentList = new ArrayList<>();
        String placeholders = String.join(",", new String[statuses.length]).replaceAll("[^,]+", "?");
        String sql = "SELECT * FROM PC WHERE technician_id = ? AND status IN (" + placeholders + ")";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, technicianId);
            for (int i = 0; i < statuses.length; i++) {
                pstmt.setString(i + 2, statuses[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapPC(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PCs for technician ID " + technicianId + " with statuses " + Arrays.toString(statuses) + ": " + e.getMessage());
            e.printStackTrace();
        }
        return equipmentList;
    }

    /**
     * Retrieves PCs by their status.
     */
    public List<PC> getPCsByStatus(String status) {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM PC WHERE status = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pcs.add(mapPC(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PCs with status " + status + ": " + e.getMessage());
            e.printStackTrace();
        }
        return pcs;
    }

    /**
     * Retrieves PCs by their location.
     */
    public List<PC> getPCsByLocation(String location) {
        List<PC> pcs = new ArrayList<>();
        String sql = "SELECT * FROM PC WHERE location = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, location);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pcs.add(mapPC(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PCs at location " + location + ": " + e.getMessage());
            e.printStackTrace();
        }
        return pcs;
    }

    /**
     * Updates an existing PC in the database.
     */
    public boolean updatePC(PC pc) {
        String sql = "UPDATE PC SET brand = ?, hdd = ?, ram = ?, os = ?, registration_year = ?, " +
                     "status = ?, location = ?, technician_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pc.getBrand());
            pstmt.setString(2, pc.getHdd());
            pstmt.setString(3, pc.getRam());
            pstmt.setString(4, pc.getOs());
            pstmt.setInt(5, pc.getRegistrationYear());
            pstmt.setString(6, pc.getStatus());
            pstmt.setString(7, pc.getLocation());
            pstmt.setInt(8, pc.getTechnicianId());
            pstmt.setInt(9, pc.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating PC with ID " + pc.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the status of a PC.
     */
    public boolean updatePCStatus(int pcId, String status) {
        String sql = "UPDATE PC SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, pcId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status for PC ID " + pcId + " to " + status + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a PC from the database.
     */
    public boolean deletePC(int pcId) {
        String sql = "DELETE FROM PC WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pcId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting PC with ID " + pcId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a count of PCs grouped by status.
     */
    public Map<String, Integer> getPCStatusByCategory() {
        Map<String, Integer> statusMap = new HashMap<>();
        String sql = "SELECT status, COUNT(*) AS count FROM PC GROUP BY status";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                statusMap.put(rs.getString("status").toUpperCase(), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching PC status by category: " + e.getMessage());
            e.printStackTrace();
        }
        return statusMap;
    }

    /**
     * Maps a ResultSet row to a PC object.
     */
    private PC mapPC(ResultSet rs) throws SQLException {
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
        return pc;
    }
}