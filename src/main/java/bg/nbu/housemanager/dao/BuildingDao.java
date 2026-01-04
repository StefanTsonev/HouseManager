package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Building;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class BuildingDao {
    private final Connection con;

    public BuildingDao(Connection con) { this.con = con; }

    public long create(long companyId, long employeeId, String address, int floors, int apts, BigDecimal built, BigDecimal common) throws SQLException {
        String sql = "INSERT INTO buildings(company_id, employee_id, address, floors, apartments_count, built_area_m2, common_parts_m2) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, companyId);
            ps.setLong(2, employeeId);
            ps.setString(3, address);
            ps.setInt(4, floors);
            ps.setInt(5, apts);
            ps.setBigDecimal(6, built);
            ps.setBigDecimal(7, common);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void update(long id, long employeeId, String address, int floors, int apts, BigDecimal built, BigDecimal common) throws SQLException {
        String sql = "UPDATE buildings SET employee_id=?, address=?, floors=?, apartments_count=?, built_area_m2=?, common_parts_m2=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, employeeId);
            ps.setString(2, address);
            ps.setInt(3, floors);
            ps.setInt(4, apts);
            ps.setBigDecimal(5, built);
            ps.setBigDecimal(6, common);
            ps.setLong(7, id);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM buildings WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Building get(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM buildings WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public List<Building> listByCompany(long companyId) throws SQLException {
        String sql = "SELECT * FROM buildings WHERE company_id=? ORDER BY address";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Building> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Building> listByEmployee(long employeeId) throws SQLException {
        String sql = "SELECT * FROM buildings WHERE employee_id=? ORDER BY address";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Building> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public void reassignBuilding(long buildingId, long newEmployeeId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE buildings SET employee_id=? WHERE id=?")) {
            ps.setLong(1, newEmployeeId);
            ps.setLong(2, buildingId);
            ps.executeUpdate();
        }
    }

    private Building map(ResultSet rs) throws SQLException {
        return new Building(
                rs.getLong("id"),
                rs.getLong("company_id"),
                rs.getLong("employee_id"),
                rs.getString("address"),
                rs.getInt("floors"),
                rs.getInt("apartments_count"),
                rs.getBigDecimal("built_area_m2"),
                rs.getBigDecimal("common_parts_m2")
        );
    }
}
