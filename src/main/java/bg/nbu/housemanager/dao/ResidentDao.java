package bg.nbu.housemanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ResidentDao {
    private final Connection con;
    public ResidentDao(Connection con) { this.con = con; }

    public void addResident(long apartmentId, long personId, boolean usesElevator) throws SQLException {
        String sql = "INSERT INTO apartment_residents(apartment_id, person_id, uses_elevator) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, apartmentId);
            ps.setLong(2, personId);
            ps.setBoolean(3, usesElevator);
            ps.executeUpdate();
        }
    }

    public void updateResident(long apartmentId, long personId, boolean usesElevator) throws SQLException {
        String sql = "UPDATE apartment_residents SET uses_elevator=? WHERE apartment_id=? AND person_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, usesElevator);
            ps.setLong(2, apartmentId);
            ps.setLong(3, personId);
            ps.executeUpdate();
        }
    }

    public void removeResident(long apartmentId, long personId) throws SQLException {
        String sql = "DELETE FROM apartment_residents WHERE apartment_id=? AND person_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, apartmentId);
            ps.setLong(2, personId);
            ps.executeUpdate();
        }
    }

    public List<Object[]> listByApartment(long apartmentId) throws SQLException {
        String sql =
                "SELECT p.id, p.first_name, p.last_name, p.birth_date, ar.uses_elevator " +
                "FROM apartment_residents ar " +
                "JOIN persons p ON p.id = ar.person_id " +
                "WHERE ar.apartment_id=? " +
                "ORDER BY p.last_name, p.first_name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, apartmentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Object[]{
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date").toLocalDate(),
                            rs.getBoolean("uses_elevator")
                    });
                }
                return out;
            }
        }
    }
}
