package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Apartment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ApartmentDao {
    private final Connection con;

    public ApartmentDao(Connection con) { this.con = con; }

    public long create(long buildingId, int floor, String aptNumber, BigDecimal areaM2, Long ownerPersonId) throws SQLException {
        String sql = "INSERT INTO apartments(building_id, floor, apt_number, area_m2, owner_person_id) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, buildingId);
            ps.setInt(2, floor);
            ps.setString(3, aptNumber);
            ps.setBigDecimal(4, areaM2);
            if (ownerPersonId == null) ps.setNull(5, Types.BIGINT); else ps.setLong(5, ownerPersonId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void update(long id, int floor, String aptNumber, BigDecimal areaM2, Long ownerPersonId) throws SQLException {
        String sql = "UPDATE apartments SET floor=?, apt_number=?, area_m2=?, owner_person_id=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, floor);
            ps.setString(2, aptNumber);
            ps.setBigDecimal(3, areaM2);
            if (ownerPersonId == null) ps.setNull(4, Types.BIGINT); else ps.setLong(4, ownerPersonId);
            ps.setLong(5, id);
            ps.executeUpdate();
        }
    }

    public Apartment get(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM apartments WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public List<Apartment> listByBuilding(long buildingId) throws SQLException {
        String sql = "SELECT * FROM apartments WHERE building_id=? ORDER BY floor, apt_number";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Apartment> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM apartments WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Apartment map(ResultSet rs) throws SQLException {
        Long owner = rs.getObject("owner_person_id") == null ? null : rs.getLong("owner_person_id");
        return new Apartment(
                rs.getLong("id"),
                rs.getLong("building_id"),
                rs.getInt("floor"),
                rs.getString("apt_number"),
                rs.getBigDecimal("area_m2"),
                owner
        );
    }
}
