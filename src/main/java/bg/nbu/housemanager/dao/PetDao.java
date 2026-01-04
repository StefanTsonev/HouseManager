package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class PetDao {
    private final Connection con;
    public PetDao(Connection con) { this.con = con; }

    public long create(long apartmentId, String name, String type, boolean usesCommonParts) throws SQLException {
        String sql = "INSERT INTO pets(apartment_id, name, type, uses_common_parts) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, apartmentId);
            ps.setString(2, name);
            ps.setString(3, type);
            ps.setBoolean(4, usesCommonParts);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void update(long id, String name, String type, boolean usesCommonParts) throws SQLException {
        String sql = "UPDATE pets SET name=?, type=?, uses_common_parts=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setBoolean(3, usesCommonParts);
            ps.setLong(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM pets WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public List<Pet> listByApartment(long apartmentId) throws SQLException {
        String sql = "SELECT * FROM pets WHERE apartment_id=? ORDER BY name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, apartmentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Pet> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private Pet map(ResultSet rs) throws SQLException {
        return new Pet(
                rs.getLong("id"),
                rs.getLong("apartment_id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getBoolean("uses_common_parts")
        );
    }
}
