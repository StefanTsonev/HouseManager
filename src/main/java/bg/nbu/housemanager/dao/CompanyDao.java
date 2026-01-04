package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class CompanyDao {
    private final Connection con;

    public CompanyDao(Connection con) { this.con = con; }

    public long create(String name, String vat, String phone, String email) throws SQLException {
        String sql = "INSERT INTO companies(name, vat_number, phone, email) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, vat);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public void update(long id, String name, String vat, String phone, String email) throws SQLException {
        String sql = "UPDATE companies SET name=?, vat_number=?, phone=?, email=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, vat);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setLong(5, id);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM companies WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Company get(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM companies WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public List<Company> list() throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM companies ORDER BY name")) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Company> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Object[]> listCompaniesByRevenue() throws SQLException {
        String sql =
                "SELECT c.id, c.name, COALESCE(SUM(p.amount), 0) AS revenue " +
                "FROM companies c " +
                "LEFT JOIN payments p ON p.company_id = c.id " +
                "GROUP BY c.id, c.name " +
                "ORDER BY revenue DESC, c.name";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Object[]> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new Object[]{rs.getLong("id"), rs.getString("name"), rs.getBigDecimal("revenue")});
            }
            return out;
        }
    }

    private Company map(ResultSet rs) throws SQLException {
        return new Company(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("vat_number"),
                rs.getString("phone"),
                rs.getString("email")
        );
    }
}
