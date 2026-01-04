package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Person;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class PersonDao {
    private final Connection con;

    public PersonDao(Connection con) { this.con = con; }

    public long create(String first, String last, LocalDate birthDate) throws SQLException {
        String sql = "INSERT INTO persons(first_name, last_name, birth_date) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setDate(3, Date.valueOf(birthDate));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void update(long id, String first, String last, LocalDate birthDate) throws SQLException {
        String sql = "UPDATE persons SET first_name=?, last_name=?, birth_date=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setDate(3, Date.valueOf(birthDate));
            ps.setLong(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM persons WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Person get(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM persons WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public List<Person> listSorted(String sortMode) throws SQLException {
        String order = "first_name, last_name";
        if ("age".equalsIgnoreCase(sortMode)) order = "birth_date";
        String sql = "SELECT * FROM persons ORDER BY " + order;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Person> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    public List<Object[]> listResidentsByBuilding(long buildingId, String sortMode) throws SQLException {
        String order = "p.first_name, p.last_name";
        if ("age".equalsIgnoreCase(sortMode)) order = "p.birth_date";
        String sql =
                "SELECT DISTINCT p.id, p.first_name, p.last_name, p.birth_date " +
                "FROM persons p " +
                "JOIN apartment_residents ar ON ar.person_id = p.id " +
                "JOIN apartments a ON a.id = ar.apartment_id " +
                "WHERE a.building_id=? " +
                "ORDER BY " + order;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Object[]{rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birth_date").toLocalDate()});
                }
                return out;
            }
        }
    }

    private Person map(ResultSet rs) throws SQLException {
        return new Person(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date").toLocalDate()
        );
    }
}
